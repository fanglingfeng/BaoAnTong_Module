package com.tjsoft.webhall.ui.work;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.util.Background;
import com.tjsoft.util.BitmapUtil;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.ImageLoader;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.NetworkUtils;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.entity.ApplyBean;
import com.tjsoft.webhall.entity.PictureEvent;

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
public class TakePhotos extends AutoDialogActivity {
    private static final String PHOTO_PATH = "photo_path";
    private static final String REQUEST_CODE = "requestcode";
    private static final String LAST_PATH = "lastPath";
    private static final String NOT_ADD = "not_add";
    public static final String TO_QIEBIAN = "to_qiebian";
    private Button takePhotos, upload, back, leftRotate, rightRotate, add, qiebian;
    private GridView gvMuiltFile;
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
    private ImageView imageView;
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_ADD_CAMERA = 2;// 拼接
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    public static final int PHOTO_REQUEST_QIEBIAN = 4;// 切边
    private RelativeLayout rotateBar;
    private String lastPath;
    private int requestCode;
    private boolean notAdd;
    String file;
    String flag;
    boolean isMuilt;
    ArrayList<String> files;
    String addfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_take_photos"));
        mark = getIntent().getIntExtra("mark", 0);
        type = getIntent().getIntExtra("type", 0);
        idString = getIntent().getStringExtra("idString");
        startTimeString = getIntent().getStringExtra("startTimeString");
        endTimeString = getIntent().getStringExtra("endTimeString");
        PERMID = getIntent().getStringExtra("PERMID");
        FILE = (ApplyBean) getIntent().getSerializableExtra("FILE");
        TYPE = getIntent().getStringExtra("TYPE");
        position = getIntent().getIntExtra("position", -1);
        add = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "add"));
        takePhotos = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "takePhotos"));
        upload = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "upload"));
        back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
        leftRotate = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "leftRotate"));
        rightRotate = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "rightRotate"));
        imageView = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "imageView"));
        rotateBar = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "rotateBar"));
//        qiebian = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "qiebian"));
        gvMuiltFile = (GridView) findViewById(MSFWResource.getResourseIdByName(this, "id", "gv_muilt_file"));
        System.out.println("decodeFile" + System.currentTimeMillis());
        initSetOnListener();
        if (savedInstanceState == null) {
            takePhoto(PHOTO_REQUEST_CAMERA);// 启动相机
        } else {
            localTempImgFileName = savedInstanceState.getString(PHOTO_PATH);
            System.out.println("decodeFile  localTempImgFileName ==" + localTempImgFileName);
            if (savedInstanceState.getInt(REQUEST_CODE) == PHOTO_REQUEST_CAMERA) {
                preview();
            } else {
                lastPath = savedInstanceState.getString(LAST_PATH);
                notAdd = savedInstanceState.getBoolean(NOT_ADD, false);
                previewAdd();
            }
        }

    }

    private void initSetOnListener() {
        add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                notAdd = true;
                takePhoto(PHOTO_ADD_CAMERA);

            }
        });
        leftRotate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (myBitmap == null) {
                    myBitmap = ImageLoader.decodeFile(photo, 1920);
                }
                myBitmap = rotateBitmap(-90, myBitmap);
                imageView.setImageBitmap(myBitmap);
            }
        });
        rightRotate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (myBitmap == null) {
                    myBitmap = ImageLoader.decodeFile(photo, 1920);
                }
                myBitmap = rotateBitmap(90, myBitmap);
                imageView.setImageBitmap(myBitmap);
            }
        });
        takePhotos.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                takePhoto(PHOTO_REQUEST_CAMERA);
//                Intent intent = new Intent(TakePhotos.this,ImageScannerActivity.class);
//                intent.putExtra(TO_QIEBIAN,Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/" + localTempImgFileName);
//                startActivityForResult(intent,PHOTO_REQUEST_QIEBIAN);

            }
        });
//        qiebian.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(TakePhotos.this, ImageScannerActivity.class);
//                intent.putExtra(TO_QIEBIAN, photo);
//                intent.putExtra("flag", "qiebian");
//                startActivityForResult(intent, PHOTO_REQUEST_QIEBIAN);
//            }
//        });
        upload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null == imageView.getDrawable()&&!isMuilt) {
                    DialogUtil.showUIToast(TakePhotos.this, "您还没有拍照，请点击开始拍照进行拍照，然后上传照片！");
                } else {
                    if (NetworkUtils.isAvailable(TakePhotos.this)) {
                        if (NetworkUtils.isWifiConnected(TakePhotos.this)) {
                            new AlertDialog.Builder(TakePhotos.this).setMessage("是否确定上传？")
                                    .setTitle(getString(
                                            MSFWResource.getResourseIdByName(TakePhotos.this, "string", "tj_notify")))
                                    .setCancelable(false)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog = Background.Process(TakePhotos.this, new Runnable() {
                                                public void run() {

                                                    if (!isMuilt) {
                                                        uploadFile(photo);
                                                    } else {
                                                        uploadFiles(files);
                                                    }

                                                }
                                            }, "正在上传...");

                                        }
                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).show();
                        } else {
                            new AlertDialog.Builder(TakePhotos.this).setMessage("您当前不是Wifi环境，是否确定上传？")
                                    .setTitle(getString(
                                            MSFWResource.getResourseIdByName(TakePhotos.this, "string", "tj_notify")))
                                    .setCancelable(false)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog = Background.Process(TakePhotos.this, new Runnable() {
                                                public void run() {
                                                    if (!isMuilt) {
                                                        uploadFile(photo);
                                                    } else {
                                                        uploadFiles(files);
                                                    }
                                                }
                                            }, "正在上传...");
                                        }
                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).show();
                        }
                    } else {
                        DialogUtil.showUIToast(TakePhotos.this, "当前无可用网络！");
                    }

                }
            }
        });
        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                TakePhotos.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
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
            this.requestCode = requestCode;
            switch (requestCode) {
                case PHOTO_REQUEST_CAMERA:
                    //这里先跳转到切边的那个界面那里，然后切完之后再在这里展示
//                    Intent intent = new Intent(this,ImageScannerActivity.class);
//                    intent.putExtra(TO_QIEBIAN,Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/" + localTempImgFileName);
//                    startActivityForResult(intent,PHOTO_REQUEST_QIEBIAN);
//                    flag = data.getStringExtra("flag");
//                    if (TextUtils.equals(flag, "single")) {
//                        isMuilt = false;
//                        file = data.getStringExtra("file");
//                    } else {
//                        isMuilt = true;
//                        files = data.getStringArrayListExtra("PATH");
//                    }
//                    if (!isMuilt) {
                        preview();
//                    } else {
//                        multiPreview();
//                    }
                    break;
                case PHOTO_ADD_CAMERA:
                    previewAdd();
                    break;
                case PHOTO_REQUEST_CUT:
                    try {
                        // myBitmap = data.getParcelableExtra("data");

                        // tempFile.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case PHOTO_REQUEST_QIEBIAN:

                    preview();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //展示多图片
    private void multiPreview() {
        MultiFileAdapter muiltFileAdapter = new MultiFileAdapter(files,TakePhotos.this);
        gvMuiltFile.setAdapter(muiltFileAdapter);

        imageView.setVisibility(View.GONE);
        rightRotate.setVisibility(View.GONE);
        add.setVisibility(View.INVISIBLE);
//        qiebian.setVisibility(View.INVISIBLE);
        gvMuiltFile.setVisibility(View.VISIBLE);
    }

    class MultiFileAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public MultiFileAdapter(ArrayList<String> files, Context context) {
            super();
            inflater = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            if (null != files) {
                return files.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return files.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(MSFWResource.getResourseIdByName(TakePhotos.this, "layout", "picture_item"), null);
                viewHolder = new ViewHolder();
                viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String muiltfile = files.get(position);
            File muiltphoto = new File(muiltfile);
            try {
                ImageLoader.compressAndGenImage(muiltphoto, 1024);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // crop(Uri.fromFile(photo));

            // TODO Auto-generated method stub
            com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                    .displayImage("file://" + muiltphoto.getAbsolutePath(), viewHolder.image);

            viewHolder.title.setText(muiltphoto.getName());
//            viewHolder.image.setImageResource(pictures.get(position).getImageId());
            return convertView;
        }

    }

    class ViewHolder {
        public TextView title;
        public ImageView image;
    }

    private void previewAdd() {

        try {
            photo = new File(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/"
                    + localTempImgFileName);
            Bitmap temp = ImageLoader.decodeFileAdd(photo, 1920);
            myBitmap = BitmapFactory.decodeFile(lastPath);
            lastPath = photo.getAbsolutePath();
            System.out.println("previewAdd lastPath=="+lastPath);
            if (temp != null && myBitmap != null) {
                if (notAdd) {
                    myBitmap = BitmapUtil.addBitmap(myBitmap, temp);
                }
            } else {
                myBitmap = temp;
            }
            photo = saveBitmapFile(myBitmap, photo);
            notAdd = false;
            ImageLoader.compressAndGenImage(photo, 1024);
            myBitmap.recycle();
            temp.recycle();
            myBitmap = null;
            temp = null;
            System.out.println("filesize保存后=" + photo.length());
            // crop(Uri.fromFile(photo));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                            .displayImage("file://" + photo.getAbsolutePath(), imageView);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void preview() {
        photo = new File(
                Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/" + localTempImgFileName);
        imageView.setVisibility(View.VISIBLE);
        rightRotate.setVisibility(View.VISIBLE);
        add.setVisibility(View.VISIBLE);
//        qiebian.setVisibility(View.VISIBLE);
        gvMuiltFile.setVisibility(View.GONE);
        System.out.println("decodeFile " + localTempImgFileName + "+filesize原始=" + photo.length());
        // myBitmap = ImageLoader.decodeFile(photo);
        // System.out.println("filesize压缩后="+photo.length()+"---bitmapsize="+myBitmap.getByteCount());
        // photo = saveBitmapFile(myBitmap, photo);
        // imageView.setImageBitmap(ImageLoader.decodeFileSmall(photo));//
        // 将图片显示在ImageView里you
        try {
            ImageLoader.compressAndGenImage(photo, 1024);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("filesize保存后=" + photo.length());
        // crop(Uri.fromFile(photo));

        // TODO Auto-generated method stub
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
        com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                .displayImage("file://" + photo.getAbsolutePath(), imageView);
        lastPath = photo.getAbsolutePath();

    }

    public File saveBitmapFile(Bitmap bitmap, File file) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
    public void uploadFiles(ArrayList<String> files){

        Intent intent = new Intent();
        intent.setClass(TakePhotos.this, MaterialManage.class);
        intent.putExtra("PERMID", PERMID);
        intent.putExtra("FILEID", FILE.getFILEID());
        intent.putExtra("CLMC", FILE.getCLMC());
        intent.putExtra("FILE", FILE);
        intent.putExtra("applyBean", FILE);
        intent.putExtra("position", position);
        intent.putExtra("TYPE", TYPE + "");
        intent.putExtra("FROM", "2");//跳转到编辑

        if (myBitmap != null) {
            photo = saveBitmapFile(myBitmap, photo);
            try {
                ImageLoader.compressAndGenImage(photo, 1024);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                myBitmap.recycle();
                myBitmap = null;
            } // 创建上传文件
            myBitmap.recycle();
            myBitmap = null;
        }
        intent.putExtra("files", files);
        startActivity(intent);
        finish();

    }
    public void uploadFile(File file) {
//        Intent intent = new Intent();
//        intent.setClass(TakePhotos.this, MaterialManage.class);
//        intent.putExtra("PERMID", PERMID);
//        intent.putExtra("FILEID", FILE.getFILEID());
//        intent.putExtra("CLMC", FILE.getCLMC());
//        intent.putExtra("FILE", FILE);
//        intent.putExtra("applyBean", FILE);
//        intent.putExtra("position", position);
//        intent.putExtra("TYPE", TYPE + "");
//        intent.putExtra("FROM", "1");//跳转到编辑

        if (myBitmap != null) {
            photo = saveBitmapFile(myBitmap, photo);
            try {
                ImageLoader.compressAndGenImage(photo, 1024);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                myBitmap.recycle();
                myBitmap = null;
            } // 创建上传文件
            myBitmap.recycle();
            myBitmap = null;
        }
//        intent.putExtra("file", file);
//        startActivity(intent);
        ArrayList<String> paths = new ArrayList<>();
        paths.add(file.getAbsolutePath());
        PictureEvent pictureEvent = new PictureEvent();
        pictureEvent.setSingle(false);
        pictureEvent.setFileUris(paths);
        EventBus.getDefault().post(pictureEvent);
        finish();
//
//        System.out.println("uploadFileSize=" + file.length());
//        try {
//            String requestUrl = Constants.DOMAIN + "servlet/uploadMobileFileServlet";
//            Map<String, String> params = new HashMap<String, String>();
//            params.put("USERCODE", Constants.user.getCODE());
//            params.put("TYPE", TYPE + "");//
//            params.put("ATTACHCODE", FILE.getCLBH());
//            params.put("FILENAME", file.getName());
//
//            // 上传文件
//
//            FormFile formfile = new FormFile(file.getName(), file, "image", "application/octet-stream");
//            String json = SocketHttpRequester.post(requestUrl, params, formfile);
//            String fileId = new JSONObject(new JSONObject(json).getString("ReturnValue")).getString("FILEID");
//            String filePath = new JSONObject(new JSONObject(json).getString("ReturnValue")).getString("FILEPATH");
//            if (null != fileId && !fileId.equals("") && null != filePath && !filePath.equals("")) {
//
//                DialogUtil.showUIToast(this, "上传成功！");
//                ATT att = new ATT(fileId, file.getName(), filePath, "");
//                if (null == Constants.material.get(FILE.getCLBH())) {
//                    Constants.material.put(FILE.getCLBH(), new ArrayList<ATT>());
//                }
//                Constants.material.get(FILE.getCLBH()).add(att);// 将新增文件添加到map
//
//                if (position != -1 && TYPE.equals("1")) {// 证照信息数据状态改变
//                    // HistoreShare.licenseDate.get(position).setSTATUS("1");
//
//                } else if (position != -1 && TYPE.equals("2")) {// 大附件数据状态改变
//                    HistoreShare_v2.bigFileDate.get(position).setSTATUS("1");
//                }
//                Intent intent = getIntent();
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("apply", FILE);
//                intent.putExtras(bundle);
//                setResult(RESULT_OK, intent);
//                // setResult(RESULT_OK);
//                finish();
//            } else {
//                DialogUtil.showUIToast(this, "上传文件" + file.getName() + "失败，请确保网络环境良好重试，或者到电脑上传！");
//            }
//        } catch (Exception e) {
//            DialogUtil.showUIToast(this, "上传文件" + file.getName() + "失败，请确保网络环境良好重试，或者到电脑上传！");
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onDestroy() {
        System.out.println("decodeFile onDestroy  ");
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(PHOTO_PATH, localTempImgFileName);
        outState.putInt(REQUEST_CODE, requestCode);
        outState.putString(LAST_PATH, lastPath);
        outState.putBoolean(NOT_ADD, notAdd);
        System.out.println("decodeFile onSaveInstanceState lastPath=" + lastPath);
        System.out.println("decodeFile onSaveInstanceState" + localTempImgFileName);
        super.onSaveInstanceState(outState);
    }

    public void takePhoto(int type) {
        if (null != myBitmap) {// 回收bitmap 防止oom
            myBitmap.recycle();
        }
        // 先验证手机是否有sdcard
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                File dir = new File(Environment.getExternalStorageDirectory() + "/" + localTempImgDir);
                if (!dir.exists())
                    dir.mkdirs();

                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                localTempImgFileName = System.currentTimeMillis() + ".jpg";
                File f = new File(dir, localTempImgFileName);// localTempImgDir和localTempImageFileName是自己定义的名字
                System.out.println("decodeFile   take" + localTempImgFileName);
                Uri u = Uri.fromFile(f);
                if (Build.VERSION.SDK_INT >= 24) {
                    String packageName = this.getPackageName();
                    u  = FileProvider.getUriForFile(this.getApplicationContext(),packageName +".fileProvider", f);
                } else {
                    u  = Uri.fromFile(f);
                }

                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                startActivityForResult(intent, type);

//            startActivityForResult(new Intent(TakePhotos.this, TakePhotoActivity.class), type);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(TakePhotos.this, "没有找到储存目录", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(TakePhotos.this, "没有储存卡", Toast.LENGTH_LONG).show();
        }
    }

    ;

}