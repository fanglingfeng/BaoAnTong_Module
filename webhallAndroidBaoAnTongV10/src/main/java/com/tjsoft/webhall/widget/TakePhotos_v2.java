package com.tjsoft.webhall.widget;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.tjsoft.camera.TakePhotoActivity;
import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.util.BitmapUtil;
import com.tjsoft.webhall.constants.AppKey;
import com.tjsoft.webhall.constants.PermissionCode;
import com.tjsoft.webhall.entity.ATTBean;
import com.tjsoft.webhall.entity.ApplyBean;
import com.tjsoft.webhall.entity.UploadStatus;
import com.tjsoft.webhall.util.ImageLoader;

import net.liang.appbaselibrary.base.BaseAppCompatActivity;
import net.liang.appbaselibrary.base.mvp.MvpPresenter;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 拍照上传页面
 *
 * @author Administrator
 */
public class TakePhotos_v2 extends BaseAppCompatActivity implements View.OnClickListener {
    TextView rightTv;//确定
    RelativeLayout add;
    RelativeLayout takePhotos;
    //	@BindView(R.id.upload)Button upload;
    Button leftRotate;
    RelativeLayout rightRotate;
    PhotoView imageView;
    RelativeLayout rotateBar;

    private String PERMID;
    private String idString = "";
    private String startTimeString = "";
    private String endTimeString = "";
    private ApplyBean FILE;
    private String localTempImgDir = "tjsoft";
    private String localTempImgFileName = "";
    private File photo = null;
    private int mark;
    private int type;
    private String TYPE;
    private int position = -1;
    private Bitmap myBitmap;
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_ADD_CAMERA = 2;// 拼接
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private int intentKey;
    String file;
    String flag;
    boolean isMuilt;
    ArrayList<String> files;

    @Override
    protected int getLayoutId() {
        return R.layout.take_photos;
    }

    @Override
    protected MvpPresenter getPresenter() {
        return null;
    }

    @Override
    public void init() {

        setToolbarCentel_tv(true, "图片编辑", "确定");
        intentKey = getIntent().getIntExtra(AppKey.INTENT_KEY, -1);
        mark = getIntent().getIntExtra("mark", 0);
        type = getIntent().getIntExtra("type", 0);
        idString = getIntent().getStringExtra("idString");
        startTimeString = getIntent().getStringExtra("startTimeString");
        endTimeString = getIntent().getStringExtra("endTimeString");
        PERMID = getIntent().getStringExtra("PERMID");
        FILE = (ApplyBean) getIntent().getSerializableExtra("FILE");
        TYPE = getIntent().getStringExtra("TYPE");
        position = getIntent().getIntExtra("position", -1);

        takePhoto(PHOTO_REQUEST_CAMERA);// 启动相机
//视图绑定
        rightTv = (TextView) getDelegate().findViewById(R.id.right_tv);
        add = (RelativeLayout) getDelegate().findViewById(R.id.add);
        takePhotos = (RelativeLayout) getDelegate().findViewById(R.id.takePhotos);
        leftRotate = (Button) getDelegate().findViewById(R.id.leftRotate);
        rightRotate = (RelativeLayout) getDelegate().findViewById(R.id.rightRotate);
        imageView = (PhotoView) getDelegate().findViewById(R.id.imageView);
        rotateBar = (RelativeLayout) getDelegate().findViewById(R.id.rotateBar);
        rightTv.setOnClickListener(this);
        takePhotos.setOnClickListener(this);
        rightRotate.setOnClickListener(this);
        leftRotate.setOnClickListener(this);
        add.setOnClickListener(this);
        imageView.enable();

    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.add) {
            takePhoto(PHOTO_ADD_CAMERA);

        } else if (i == R.id.leftRotate) {
            myBitmap = rotateBitmap(-90, myBitmap);
            imageView.setImageBitmap(myBitmap);

        } else if (i == R.id.rightRotate) {
            myBitmap = rotateBitmap(90, myBitmap);
            imageView.setImageBitmap(myBitmap);

        } else if (i == R.id.takePhotos) {
            takePhoto(PHOTO_REQUEST_CAMERA);

        } else if (i == R.id.right_tv) {
            if (null == imageView.getDrawable()) {
                showToast("您还没有拍照，请点击开始拍照进行拍照，然后上传照片！");
            } else {
                photo = saveBitmapFile(myBitmap, photo); // 创建上传文件
                new AlertDialog.Builder(TakePhotos_v2.this).setMessage("是否确定上传？").setTitle(getString(R.string.notify)).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ATTBean attBean = new ATTBean(photo.getAbsolutePath());
                        if (intentKey == AppKey.FROM_FILE_UPLOAD) {
                            attBean.setUpStatus(UploadStatus.ADD_ATT_FOR_FILE_UPLOAD);
                        }
                        if (intentKey == AppKey.FROM_MATERIAL_MANAGE) {
                            attBean.setUpStatus(UploadStatus.ADD_ATT_FOR_MATERIAL_MANAGE);
                        }
                        attBean.setTYPE("1");
                        EventBus.getDefault().post(attBean);
                        finish();

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).show();
            }

        }
    }

    @Override
    public void onResume() {
        if (null != imageView.getDrawable()) {
            rotateBar.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    /**
     * 将图片纠正到正确方向
     *
     * @param degree ： 图片被系统旋转的角度
     * @param bitmap ： 需纠正方向的图片
     * @return 纠向后的图片
     */
    public static Bitmap rotateBitmap(int degree, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bm;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            flag = data.getStringExtra("flag");
            if (TextUtils.equals(flag, "single")) {
                isMuilt = false;
                file = data.getStringExtra("file");
            } else {
                isMuilt = true;
                files = data.getStringArrayListExtra("PATH");
            }
            switch (requestCode) {
                case PHOTO_REQUEST_CAMERA:
//                    try {
//                        photo = new File(data.getAction());
////						photo = new File(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/" + localTempImgFileName);
//                        myBitmap = ImageLoader.decodeFile(photo);
////						photo = saveBitmapFile(myBitmap, photo);
//                        // crop(Uri.fromFile(photo));
//                        imageView.setImageBitmap(myBitmap);// 将图片显示在ImageView里
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

                    if (!isMuilt) {
                        preview();
                    } else {
//                        multiPreview();
                    }
                    break;
                case PHOTO_ADD_CAMERA:
                    try {
                        file = data.getStringExtra("file");
                        photo = new File(file);
//						photo = new File(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/" + localTempImgFileName);
                        Bitmap temp = ImageLoader.decodeFile(photo);
                        if (temp != null && myBitmap != null) {
                            myBitmap = BitmapUtil.addBitmap(myBitmap, temp);
                        } else {
                            myBitmap = temp;
                        }
                        photo = saveBitmapFile(myBitmap, photo);
                        // crop(Uri.fromFile(photo));
                        imageView.setImageBitmap(myBitmap);// 将图片显示在ImageView里
                        imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//关闭硬件加速，避免拼接太多图片的时候显示不出来
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case PHOTO_REQUEST_CUT:
                    try {
                        // myBitmap = data.getParcelableExtra("data");

                        // tempFile.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
        } else {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void preview() {
        try {
            photo = new File(file);
//						photo = new File(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/" + localTempImgFileName);
            myBitmap = ImageLoader.decodeFile(photo);
//						photo = saveBitmapFile(myBitmap, photo);
            // crop(Uri.fromFile(photo));
            imageView.setImageBitmap(myBitmap);// 将图片显示在ImageView里
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File saveBitmapFile(Bitmap bitmap, File file) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private int currentType;

    public void takePhoto(int type) {
        // if (null != myBitmap) {// 回收bitmap 防止oom
        // myBitmap.recycle();
        // }
        // 先验证手机是否有sdcard
        currentType = type;
        String status = Environment.getExternalStorageState();
        PackageManager pm = getPackageManager();
        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
                && pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            if (status.equals(Environment.MEDIA_MOUNTED)) {

                if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                    startActivityForResult(new Intent(TakePhotos_v2.this, TakePhotoActivity.class), type);
                } else {
//do not have permission
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PermissionCode.MY_PERMISSIONS_CAMERA);
                }

            } else {
                Toast.makeText(TakePhotos_v2.this, "没有储存卡", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(TakePhotos_v2.this, "没有相机设备", Toast.LENGTH_LONG).show();
        }
    }

    ;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionCode.MY_PERMISSIONS_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    startActivityForResult(new Intent(TakePhotos_v2.this, TakePhotoActivity.class), currentType);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}