
package com.google.zxing;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.camera.CameraManager;
import com.google.zxing.decoding.CaptureActivityHandler;
import com.google.zxing.decoding.InactivityTimer;
import com.google.zxing.view.ViewfinderView;
import com.tjsoft.util.Background;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.StatisticsTools;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.ui.search.LoadingActivity;
import com.tjsoft.webhall.ui.search.SearchSchedule;

import net.liang.appbaselibrary.utils.ToastUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Vector;

/**
 * Initial the camera
 *
 * @author yeungeek
 * @ClassName: CaptureActivity
 * @Description: TODO
 * @date 2013-4-28 下午12:59:44
 */
public class CaptureActivity extends Activity implements Callback {

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;

    private TextView mTitle;
    private ImageView mGoHome;
    private int flag;

    private String ID;
    private String BSNUM;
    private String SXID;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatisticsTools.start();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_qr_code_scan"));

        flag = getIntent().getIntExtra("flag", 0);
        SXID = getIntent().getStringExtra("SXID");
        BSNUM = getIntent().getStringExtra("BSNUM");
        CameraManager.init(getApplication());
        initControl();


        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }


    private void initControl() {
        viewfinderView = (ViewfinderView) findViewById(
                MSFWResource.getResourseIdByName(this, "id", "viewfinder_view"));
        mTitle = (TextView) findViewById(
                MSFWResource.getResourseIdByName(this, "id", "details_textview_title"));
        mTitle.setText("请将扫描框对准业务回执二维码");
        if (flag == 1) {
            findViewById(MSFWResource.getResourseIdByName(this, "id", "details_fl"))
                    .setVisibility(View.GONE);
        }
        mGoHome = (ImageView) findViewById(
                MSFWResource.getResourseIdByName(this, "id", "details_imageview_gohome"));
        mGoHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(
                MSFWResource.getResourseIdByName(this, "id", "preview_view"));
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    public void onDestroy() {
        inactivityTimer.shutdown();
        StatisticsTools.end("业务检索", "二维码扫描", null);
        super.onDestroy();
    }

    /**
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();

        final String resultString = result.getText();
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("result", resultString);
        resultIntent.putExtras(bundle);
        this.setResult(RESULT_OK, resultIntent);
        if (flag == 1) {
            Uri uri = Uri.parse(resultString);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (flag == 2) {
            ID = resultString;
            Intent intent = new Intent();
            intent.setClass(this, LoadingActivity.class);
            intent.putExtra("ID", ID);
            intent.putExtra("BSNUM", BSNUM);
            intent.putExtra("SXID", SXID);
            startActivity(intent);
        } else if (flag==3) {
            ID=resultString;
            //扫描复杂表单填写
            Background.Process(CaptureActivity.this, fzbdtx,
                    getString(MSFWResource.getResourseIdByName(CaptureActivity.this, "string", "tj_loding")));
        } else {
            // 二维码扫描结果处理
//            DialogUtil.showUIToast(this, resultString);
            Intent intent = new Intent();
            intent.setClass(this, SearchSchedule.class);
            intent.putExtra("BSNUM", resultString);
            intent.putExtra("APPNAME", "erweima");
            startActivity(intent);

        }

        finish();
    }
    final Runnable fzbdtx = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject object = new JSONObject();
                object.put("ID", ID);
                object.put("USERID", Constants.user.getUSER_ID());
                object.put("SXID", SXID);
                object.put("BSNUM", BSNUM);

                String str = HTTP.excuteAndCache("mobileScanQRCode", "RestOnlineDeclareService", object.toString(),
                        CaptureActivity.this);
                JSONObject jb = new JSONObject(str);
                String cd = jb.getString("code");
                if (cd.equals("200")) {
                    ToastUtils.showToast("获取暂存表单成功");
                } else {
                    ToastUtils.showToast("获取暂存表单失败");
                }

            } catch (Exception e) {
                // DialogUtil.showUIToast(HistoreShare_v2.this,
                // getString(MSFWResource.getResourseIdByName(HistoreShare_v2.this,
                // "string", "tj_occurs_error_network")));
                e.printStackTrace();

            }
        }

    };
    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    /**
     * 扫描正确后的震动声音,如果感觉apk大了,可以删除
     */
    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources()
                    .openRawResourceFd(MSFWResource.getResourseIdByName(this, "raw", "tj_beep"));
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


}
