package com.tjsoft.camera;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tjsoft.camera.utils.ConvertUtils;
import com.tjsoft.camera.widget.DrawRoundRect;
import com.tjsoft.camera.widget.DrawRoundRect2;
import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.webhall.entity.PictureEvent;
import com.tjsoft.webhall.ui.work.ImageScannerActivity;
import com.tjsoft.webhall.widget.ShiliDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


/**
 * 拍照界面
 */
public class TakePhotoActivity extends Activity implements
        MQCameraInterfaceUtil.CamOpenOverCallback, MQCameraInterfaceUtil.ImagePathCallBack {
    private static final String TAG = TakePhotoActivity.class.getSimpleName();
    MQCameraSurfaceView surfaceView = null;
    View shutterBtn;
    RelativeLayout camera_img_rl;
    ImageView camera_img;
    TextView camera_img_number;
    TextView btn_finish;
    TextView btn_shili;
    DrawRoundRect btn_single;
    DrawRoundRect2 btn_multi;


    TextView tvChooseSingle;
    TextView tvChooseMulti;
    TextView tvSelectedType;


    ImageButton backBtn;
    ImageButton flashBtn;
    ImageButton overBtn;
    MaskView maskView = null;
    float previewRate = -1f;
    Point rectPictureSize = null;
    private FrameLayout framelayout;
    private int wScreen; // 屏幕宽
    private int hScreen; // 屏幕高
    private boolean flag; // 按钮延时标志
    public static final int OPEN_PREVIEW = 1;
    private int mFromType; //1是材料界面跳转 0是其他
    private RelativeLayout progressBar_rl;

    private boolean isSingle = true;

    public ArrayList<String> paths;
    public ArrayList<String> trimpath;


    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
        setContentView(R.layout.activity_take_photo);
        init();
        super.onCreate(savedInstanceState);
        mFromType = getIntent().getIntExtra("fromType", 0);
        Log.d(TAG, "-----------------------onCreate--------mFromType=" + mFromType);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//拍照过程屏幕一直处于高亮
        init();
        initViewParams();

        shutterBtn.setOnClickListener(new BtnListeners());
//        btn_single.setOnClickListener(new BtnListeners());
//        btn_multi.setOnClickListener(new BtnListeners());
        backBtn.setOnClickListener(new BtnListeners());
        flashBtn.setOnClickListener(new BtnListeners());
        btn_finish.setOnClickListener(new BtnListeners());
        camera_img.setOnClickListener(new BtnListeners());
        overBtn.setOnClickListener(new BtnListeners());
        btn_shili.setOnClickListener(new BtnListeners());

        //单张多张的选择
        tvChooseSingle.setOnClickListener(new BtnListeners());
        tvChooseMulti.setOnClickListener(new BtnListeners());


        changeMode();
        toast = Toast.makeText(TakePhotoActivity.this, "", Toast.LENGTH_SHORT);
    }

    private void changeMode() {
        if (isSingle) {
//            btn_single.setSelect(true);
//            btn_multi.setSelect(false);
            tvChooseSingle.setVisibility(View.GONE);
            tvSelectedType.setText("单张模式");
            tvChooseMulti.setVisibility(View.VISIBLE);
        } else {
//            btn_single.setSelect(false);
//            btn_multi.setSelect(true);
            tvChooseSingle.setVisibility(View.VISIBLE);
            tvSelectedType.setText("连续模式");
            tvChooseMulti.setVisibility(View.GONE);
        }
    }


    public void init() {
        wScreen = ScreenUtils.getScreenMetrics(this).x;
        hScreen = ScreenUtils.getScreenMetrics(this).y - ConvertUtils.dp2px(this, 80);
        surfaceView = (MQCameraSurfaceView) findViewById(R.id.camera_surfaceview);
        shutterBtn = findViewById(R.id.btn_shutter);
        camera_img_rl = (RelativeLayout) findViewById(R.id.camera_img_rl);
        camera_img = (ImageView) findViewById(R.id.camera_img);
        btn_finish = (TextView) findViewById(R.id.btn_finish);


        btn_shili = (TextView) findViewById(R.id.btn_shili);


        camera_img_number = (TextView) findViewById(R.id.camera_img_number);
//        btn_single = (DrawRoundRect) findViewById(R.id.btn_single);
//        btn_multi = (DrawRoundRect2) findViewById(R.id.btn_multi);
        backBtn = (ImageButton) findViewById(R.id.camera_btn_back);
        flashBtn = (ImageButton) findViewById(R.id.camera_btn_flash);
        overBtn = (ImageButton) findViewById(R.id.camera_btn_overturn);
        maskView = (MaskView) findViewById(R.id.view_mask);
        framelayout = (FrameLayout) findViewById(R.id.framelayout);

        tvChooseSingle = (TextView) findViewById(R.id.tv_choose_single);
        tvChooseMulti = (TextView) findViewById(R.id.tv_choose_multi);
        tvSelectedType = (TextView) findViewById(R.id.tv_selected_type);

        ImageView mQrLineView = (ImageView) findViewById(R.id.capture_scan_line);
        ImageView captrue = (ImageView) findViewById(R.id.captrue);
        ScaleAnimation animation = new ScaleAnimation(1.0f, 1.0f, 0.0f, 1.0f);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(1200);
        mQrLineView.startAnimation(animation);
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(640, 480);
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(640, 720);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(wScreen, hScreen);

        layoutParams.gravity = Gravity.CENTER;
        mQrLineView.setLayoutParams(layoutParams);
        captrue.setLayoutParams(layoutParams);
    }

    private void initViewParams() {
        LayoutParams params = surfaceView.getLayoutParams();
        Point p = ScreenUtils.getScreenMetrics(this);
        params.width = p.x;
        params.height = p.y - ConvertUtils.dp2px(this, 80);

//        params.width = 640;
//        params.height = 480;
//        params.width = 960;
//        params.height = 720;
        Log.i(TAG, "screen: w = " + p.x + " y = " + p.y);
        previewRate = ScreenUtils.getScreenRate(this); //默认全屏的比例预览
        surfaceView.setLayoutParams(params);

        //手动设置拍照ImageButton的大小为120dip×120dip,原图片大小是64×64
//        LayoutParams p2 = shutterBtn.getLayoutParams();
//        p2.width = DensityUtil.dip2px(this, 80);
//        p2.height = DensityUtil.dip2px(this, 80);
//        shutterBtn.setLayoutParams(p2);
        MQCameraInterfaceUtil.getInstance(TakePhotoActivity.this).setImagePathCallBack(TakePhotoActivity.this);
        progressBar_rl = (RelativeLayout) findViewById(R.id.progressBar_rl);
    }

    @Override
    public void onResume() {
        super.onResume();
        flag = false;
        progressBar_rl.setVisibility(View.GONE);

//        shutterBtn.setVisibility(View.GONE);
//        shutterBtn.setEnabled(false);
    }


    @Override
    public void cameraHasOpened() {
        // TODO Auto-generated method stub
        SurfaceHolder holder = surfaceView.getHolder();
        MQCameraInterfaceUtil.getInstance(this).doStartPreview(holder, previewRate);
        if (maskView != null) {
            // 除了布局文件要修改瞄准框外，这里也要设置大小，和布局文件的保持一致
//            Rect screenCenterRect = createCenterScreenRect(wScreen - DensityUtil.dip2px(this, 250) * 2
//                    , hScreen - DensityUtil.dip2px(this, 10) * 2);
//            Rect screenCenterRect = createCenterScreenRect(640, 480);

//            Rect screenCenterRect = createCenterScreenRect(640, 480);
//            maskView.setCenterRect(screenCenterRect);
        }
    }

    @Override
    public void returnImagePath(final String imagePath) {
        Log.d(TAG, "-----------------returnImagePath--------------------imagePath=" + imagePath);
        if (isSingle) {
            Intent intent = new Intent(TakePhotoActivity.this, ImageScannerActivity.class);
            intent.putExtra("path", imagePath);
            startActivity(intent);
            finish();
        } else {
            if (paths == null) paths = new ArrayList<>();
            paths.add(imagePath);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    camera_img.setImageBitmap(bitmap);
                    camera_img_number.setText("" + paths.size());
                    camera_img_rl.setVisibility(View.VISIBLE);
                    btn_finish.setVisibility(View.VISIBLE);
//                    btn_multi.setVisibility(View.GONE);
//                    btn_single.setVisibility(View.GONE);
                    tvChooseSingle.setVisibility(View.GONE);
                    Animation mAnimation = AnimationUtils.loadAnimation(TakePhotoActivity.this, R.anim.balloonscale);
                    camera_img.setAnimation(mAnimation);
                    mAnimation.start();
                }
            });


            resetCamera();
        }
    }

    @Override
    public void changeProgressBar() {

    }

    @Override
    public void dismissDialog() {
//        shutterBtn.setVisibility(View.VISIBLE);
        shutterBtn.setEnabled(true);
        progressBar_rl.setVisibility(View.GONE);
    }

    private void showDiaolg() {
//        shutterBtn.setVisibility(View.GONE);
        shutterBtn.setEnabled(false);
        progressBar_rl.setVisibility(View.GONE);
    }

    @Override
    public void resetCamera() {
        MQCameraInterfaceUtil.getInstance(TakePhotoActivity.this).resstCamera(TakePhotoActivity.this);
    }

    private class BtnListeners implements OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            if (v == findViewById(R.id.btn_shutter)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 321);
                    } else {
                        takePhoto();
                    }
                } else {
                    takePhoto();
                }
            } else if (v == findViewById(R.id.camera_btn_back)) {
                onBackPressed();
            } else if (v == findViewById(R.id.camera_btn_flash)) {
                MQCameraInterfaceUtil.getInstance(TakePhotoActivity.this).cameraFlash(flashBtn);
            } else if (v == findViewById(R.id.camera_btn_overturn)) {
                MQCameraInterfaceUtil.getInstance(TakePhotoActivity.this).switchCamera(TakePhotoActivity.this);
            } /*else if (v == btn_single) {
                toast.setText("单页模式");
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                isSingle = true;
                changeMode();
            } else if (v == btn_multi) {
                toast.setText("多页模式");
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                isSingle = false;
                changeMode();
            } */ else if (v == tvChooseSingle) {
                toast.setText("单页模式");
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                isSingle = true;
                changeMode();
            } else if (v == tvChooseMulti) {
                toast.setText("多页模式");
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                isSingle = false;
                changeMode();
            } else if (v == btn_finish) {
                //最终图片
                Intent intent = new Intent(TakePhotoActivity.this, ImageScannerActivity.class);
                if (trimpath != null && trimpath.size() > 0) {
                    intent.putStringArrayListExtra("path", trimpath);
                    PictureEvent pictureEvent = new PictureEvent();
                    pictureEvent.setSingle(false);
                    pictureEvent.setFileUris(trimpath);
                    EventBus.getDefault().post(pictureEvent);

                } else {
                    intent.putStringArrayListExtra("path", paths);
                    intent.putStringArrayListExtra("path", trimpath);
                    PictureEvent pictureEvent = new PictureEvent();
                    pictureEvent.setSingle(false);
                    pictureEvent.setFileUris(paths);
                    EventBus.getDefault().post(pictureEvent);
                }
//                startActivity(intent);
                onBackPressed();

            } else if (v == camera_img) {
                Intent intent = new Intent(TakePhotoActivity.this, ImageScannerActivity.class);
                intent.putExtra("paths", paths);
                startActivityForResult(intent, 1000);
            } else if (v == btn_shili) {
//示例
                ShiliDialog dialog = new ShiliDialog(TakePhotoActivity.this);

            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting();
                    } else
                        finish();
                } else {
                    takePhoto();
//                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // 提示用户去应用设置界面手动开启权限

    private void showDialogTipUserGoToAppSettting() {
        new AlertDialog.Builder(this)
                .setTitle("读写权限不可用")
                .setMessage("请在-应用设置-权限中，允许使用读写权限")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivity(intent);
    }


    public void takePhoto() {
        if (!flag) {
            if (rectPictureSize == null) {
                // 除了布局文件要修改瞄准框外，这里也要设置大小，和布局文件的保持一致
//                            rectPictureSize = createCenterPictureRect(wScreen - DensityUtil.dip2px(TakePhotoActivity.this, 250) * 2
//                                    , hScreen - DensityUtil.dip2px(TakePhotoActivity.this, 10) * 2);
//                            rectPictureSize = createCenterPictureRect(640, 480);
//                            Log.d(TAG, "------------------------------rectPictureSize.x=" + rectPictureSize.x);
//                            Log.d(TAG, "------------------------------rectPictureSize.y=" + rectPictureSize.y);
            }
//                        MQCameraInterfaceUtil.getInstance(TakePhotoActivity.this).doTakePicture(wScreen, hScreen);
//                        MQCameraInterfaceUtil.getInstance(TakePhotoActivity.this).doTakePicture();
            showDiaolg();
            MQCameraInterfaceUtil.getInstance(TakePhotoActivity.this).doTakePicture(wScreen, hScreen);
        }
    }


    /**
     * 生成拍照后图片的中间矩形的宽度和高度
     *
     * @param w 屏幕上的矩形宽度，单位px
     * @param h 屏幕上的矩形高度，单位px
     * @return
     */
    private Point createCenterPictureRect(int w, int h) {

        int wSavePicture = MQCameraInterfaceUtil.getInstance(this).doGetPrictureSize().x; //因为图片旋转了，所以此处宽高换位
        int hSavePicture = MQCameraInterfaceUtil.getInstance(this).doGetPrictureSize().y; //因为图片旋转了，所以此处宽高换位
        Log.d(TAG, "-----------------------------wSavePicture=" + wSavePicture);
        Log.d(TAG, "-----------------------------hSavePicture=" + hSavePicture);
        float wRate = (float) (wSavePicture) / (float) (wScreen);
        float hRate = (float) (hSavePicture) / (float) (hScreen);
        int wRectPicture = (int) (w * wRate);
        int hRectPicture = (int) (h * hRate);
        Log.d(TAG, "-----------------------------wRectPicture=" + wRectPicture);
        Log.d(TAG, "-----------------------------hRectPicture=" + hRectPicture);
        return new Point(wRectPicture, hRectPicture);
//        return new Point(wSavePicture, hSavePicture);

    }

    /**
     * 生成屏幕中间的矩形
     *
     * @param w 目标矩形的宽度,单位px
     * @param h 目标矩形的高度,单位px
     * @return
     */
    private Rect createCenterScreenRect(int w, int h) {
        Log.d(TAG, "-----------------------------w=" + w);
        Log.d(TAG, "-----------------------------h=" + h);
        int x1 = ScreenUtils.getScreenMetrics(this).x / 2 - w / 2;
        int y1 = ScreenUtils.getScreenMetrics(this).y / 2 - h / 2;
        int x2 = x1 + w;
        int y2 = y1 + h;
        return new Rect(x1, y1, x2, y2);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "------------------------OPEN_PREVIEW=" + OPEN_PREVIEW);
        Log.d(TAG, "------------------------RESULT_OK=" + resultCode);
        if (resultCode == RESULT_OK && requestCode == OPEN_PREVIEW) {
            if (data != null) {
                String imagePath = data.getStringExtra("imagePath");
                if (imagePath != null) {
//                    sendResult(imagePath);
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == 1000) {
            paths = data.getStringArrayListExtra("path");
            trimpath = data.getStringArrayListExtra("trimpath");
            if (paths != null && paths.size() > 0) {
                Bitmap bitmap = BitmapFactory.decodeFile(paths.get(paths.size() - 1));
                camera_img.setImageBitmap(bitmap);
                camera_img_number.setText("" + paths.size());
                camera_img_rl.setVisibility(View.VISIBLE);
                btn_finish.setVisibility(View.VISIBLE);
//                btn_multi.setVisibility(View.GONE);
//                btn_single.setVisibility(View.GONE);
            } else {
                camera_img_number.setText("");
                camera_img_rl.setVisibility(View.GONE);
                btn_finish.setVisibility(View.GONE);
//                btn_multi.setVisibility(View.VISIBLE);
//                btn_single.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
