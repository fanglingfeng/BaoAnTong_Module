package com.tjsoft.webhall.ui.work;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.intsig.scanner.CommonUtil;
import com.intsig.scanner.ScannerSDK;
import com.intsig.view.ImageEditView;
import com.intsig.view.ImageEditView.OnCornorChangeListener;
import com.intsig.view.RotateBitmap;
import com.tjsoft.msfw.guangdongshenzhenbaoan.R;

import net.liang.appbaselibrary.base.BaseAppCompatActivity;
import net.liang.appbaselibrary.base.mvp.MvpPresenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * 该界面主要展示图片切边与增强 This view is used to show image's crop and enhance process
 */
public class ImageScannerActivity extends BaseAppCompatActivity implements
        OnItemSelectedListener, OnClickListener,
        CameraSurfaceView.OnCameraStatusListener {
    private static final String TAG = ImageScannerActivity.class
            .getSimpleName();
    private static final int REQ_CODE_GALLERY_IMPORT = 0;
    /**
     * 正常的切边颜色 Normal crop color
     */
    private final static int mNormalColor = 0xff19BC9C;
    /**
     * 异常的切边颜色 Abnormal crop color
     */
    private final static int mErrorColor = 0xffff9500;
    /**
     * 用于显示增强结果 This view is used to show image enhance result
     */
    private ImageView mIVEnhance;
    /**
     * 显示切边图 This view is used to show image crop result
     */
//    private ImageEditView mIvEditView;
    /**
     * 切边视图层 Crop layout
     */
    private View mTrimView;
    /**
     * 增强视图层 Enhance layout
     */
    private View mEnhanceView;
    /**
     * 添加图片的视图层 The layout used to add image
     */
    private ImageView mBtnNext;
    private String mRootPath;
    private String mOriTrimImagePath;
    /**
     * 用于显示增强模式 used to show the enhance model set
     */
    private Spinner mSpinner;
    private Spinner typemSpinner;

    private static SimpleDateFormat sPdfTime = new SimpleDateFormat(
            "yyyy-MM-dd_HH-mm-ss");

    private ScannerSDK mScannerSDK;
    /**
     * 压缩图/原图 Compress image/source image
     */
    private float mScale = 1.0f;
    /**
     * 增强处理前的图片 Original enhance image
     */
    private Bitmap mOriginalEnhanceBitmap;
    /**
     * 当前增强处理的图片 Enhancing image
     */
    private Bitmap mEnhanceBitmap;
    /**
     * 记录当前导入图片的路径 Current input image path
     */
    private String mCurrentInputImagePath;
    /**
     * 记录上一次引检测图片切边结果 Last detected border by engine
     */
    private int[] mLastDetectBorder;
    private static final int TRIM_IMAGE_MAXSIDE = 1024;
    /**
     * your app key+
     */
    private static final String APPKEY = "K2XWPEQABP11PafD1JRQQr8D";
    /**
     * 用于引擎指针，用于辅助检测切边区域是否合法
     */
    private int mEngineContext;
    private String mOriTrimImagePathCamera;// fox--update--20171106
    private ArrayList<String> mOriTrimImagePathCameras;// fox--update--20171106
    private ArrayList<String> mOriTrimImagePaths;// fox--update--20171106
    public String fileAddress;//上个页面传过来的图片地址

    private ViewPager viewPager;
    private PagerAdapter adapter;
    private TextView imagesNum;
    private List<ImageEditView> imageEditViews;
    private int pos = 0;

    private View delcut_rl;
    private View btns;
    private boolean isFull = false;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_scanner;
    }

    @Override
    protected MvpPresenter getPresenter() {
        return null;
    }

    @Override
    public void init() {
        super.init();
        setToolbarCentel(true, "新文档");
        ImageView back = (ImageView) findViewById(net.liang.appbaselibrary.R.id.toolbar_back);
        back.setImageResource(R.drawable.tj_back);
        toolbar.setBackgroundColor(getResources().getColor(R.color.qiebian));
        mRootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        mRootPath += File.separator + "intsig" + File.separator
                + "demo_imagescanner";
        mOriTrimImagePath = mRootPath + File.separator + "oriTrim.jpg";
//        mOriTrimImagePathCamera = mRootPath + File.separator
//                + "oriTrimcamera.jpg";// fox--update--20171106
        mOriTrimImagePaths = new ArrayList<>();
        File dir = new File(mRootPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        mScannerSDK = new ScannerSDK();

        new Thread(new Runnable() {

            @Override
            public void run() {
                int code = mScannerSDK.initSDK(ImageScannerActivity.this,
                        APPKEY);
                mEngineContext = mScannerSDK.initThreadContext();
                mHandler.sendEmptyMessage(code);
                Log.d(TAG, "code=" + code);
            }
        }).start();
        initView();

        mOriTrimImagePathCamera = getIntent().getStringExtra("path");
        mOriTrimImagePathCameras = getIntent().getStringArrayListExtra("paths");
        if (!TextUtils.isEmpty(mOriTrimImagePathCamera)) {
//            loadTrimImageFile(mOriTrimImagePathCamera,mIvEditView);
            mOriTrimImagePathCameras = new ArrayList<>();
            mOriTrimImagePathCameras.add(mOriTrimImagePathCamera);
            delcut_rl.setVisibility(View.GONE);
            btns.setVisibility(View.VISIBLE);
        } else if (mOriTrimImagePathCameras != null) {
            delcut_rl.setVisibility(View.VISIBLE);
            btns.setVisibility(View.GONE);
        }
        if (mOriTrimImagePathCameras != null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            imageEditViews = new ArrayList<>();
            for (int i = 0; i < mOriTrimImagePathCameras.size(); i++) {
                ImageEditView imageEditView = (ImageEditView) inflater.inflate(R.layout.image_edit, null);
                imageEditView.setDrapPoint(R.drawable.dragpoint);
                imageEditView.setRegionVisibility(true);
                imageEditView.setOnCornorChangeListener(new MyCornorChangeListener(imageEditView));
                imageEditView.setOffset(getResources().getDimension(
                        R.dimen.highlight_point_diameter));
                if (!TextUtils.isEmpty(mOriTrimImagePathCamera)) {
                    imageEditView.showDrawPoints(true);
                    imageEditView.enableMovePoints(true);
                } else {
                    imageEditView.showDrawPoints(false);
                    imageEditView.enableMovePoints(false);
                }
                loadTrimImageFile(mOriTrimImagePathCameras.get(i), imageEditView);
                if (TextUtils.isEmpty(mOriTrimImagePathCamera)) {

                    startTtrim(mOriTrimImagePathCameras.get(i));
                    imageEditViews.add(imageEditView);

                } else {

                    imageEditViews.add(imageEditView);

                    startTtrim(mOriTrimImagePathCamera);

                }
            }
            viewPager.setOffscreenPageLimit(imageEditViews.size());
            viewPager.setAdapter(adapter = new PagerAdapter() {
                @Override
                public int getCount() {
                    return imageEditViews.size();
                }

                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view == object;
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
//                    container.removeView(imageEditViews.get(position));
                    container.removeView((View) object);
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    View view = imageEditViews.get(position);
                    container.addView(view);
                    return view;
                }

                @Override
                public int getItemPosition(Object object) {
                    return POSITION_NONE;
                }
            });
            imagesNum.setText("1/" + mOriTrimImagePathCameras.size());
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    pos = position;
                    imagesNum.setText((position + 1) + "/" + mOriTrimImagePathCameras.size());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
//暂时屏蔽
            if (msg.what != 0) {
                errorcode = msg.what;
                Toast.makeText(
                        ImageScannerActivity.this,
                        "授权失败" + "-->" + errorcode + "\nmsg:"
                                + CommonUtil.commentMsg(errorcode),
                        Toast.LENGTH_LONG).show();
                boolClick = false;
            }

            super.handleMessage(msg);
        }

    };
    boolean boolClick = true;
    int errorcode = 0;
    /**
     * 拍照
     */
    private final int REQUEST_CAPTURE_PIC = 100;

    private void startCapture() {
        // if (mOriTrimImagePath == null) {
        // mTakePicPath = DIR_ICR + "card.jpg";
        // }
        File dstFile = new File(mOriTrimImagePathCamera);// fox--update--20171106
        Uri uri = Uri.fromFile(dstFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_CAPTURE_PIC);

    }

    int rotateValue = 0;

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.bt_rotate) {// fox--update--20171106---旋转切边图片

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = inSampleSizeAll;

            rotateValue = rotateValue >= 360 ? 90 : rotateValue + 90;

            Bitmap bitmap = BitmapFactory.decodeFile(mOriTrimImagePathCameras.get(pos),
                    options);
            RotateBitmap bitmapRotate = new RotateBitmap(bitmap, rotateValue);

//            mIvEditView.rotate(bitmapRotate, true);
            imageEditViews.get(pos).rotate(bitmapRotate, true);

        } else if (viewId == R.id.bt_rotate_reserve) {

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = inSampleSizeAll;

            rotateValue = rotateValue >= 360 ? 90 : rotateValue - 90;

            Bitmap bitmap = BitmapFactory.decodeFile(mOriTrimImagePathCameras.get(pos),
                    options);
            RotateBitmap bitmapRotate = new RotateBitmap(bitmap, rotateValue);

//            mIvEditView.rotate(bitmapRotate, true);
            imageEditViews.get(pos).rotate(bitmapRotate, true);
        } else if (viewId == R.id.bt_quanping) {


//            imageEditViews.get(pos).setRawImageBounds(new int[]{100, 100});
            if (!isFull) {
                int[] wh = (int[]) imageEditViews.get(pos).getTag();
                int w = wh[0];
                int h = wh[1];
                imageEditViews.get(pos).setRegion(new float[]{0, 0, w, 0, w, h, 0, h}, mScale);
                isFull = true;
            } else {
                float[] region = (float[]) imageEditViews.get(pos).getTag(R.id.add_btn);
                imageEditViews.get(pos).setRegion(region, mScale);
                isFull = false;
            }

        } else {
            if (viewId == R.id.bt_enhance) {
                if (!boolClick) {
                    Toast.makeText(
                            ImageScannerActivity.this,
                            "授权失败" + "-->" + errorcode + "\nmsg:"
                                    + CommonUtil.commentMsg(errorcode),
                            Toast.LENGTH_LONG).show();
                    return;
                }
                //切边的地方
                imageEditViews.get(pos).showDrawPoints(false);
                imageEditViews.get(pos).enableMovePoints(false);
                // click 'next' button, and start trim
                if (imageEditViews.get(pos).isCanTrim(mEngineContext)) {
                    startTtrim(mOriTrimImagePathCameras.get(pos));
                } else {
                    Toast.makeText(ImageScannerActivity.this,
                            R.string.bound_trim_error, Toast.LENGTH_SHORT).show();
                }
                String outputPath0 = saveBitmap2Filev2(mEnhanceBitmap, mOriTrimImagePathCameras.get(pos));
                final String outputPath1 = outputPath0;
                File f = new File(outputPath0);
                if (f.exists() && f.isFile()) {
                    long filesize = f.length() / (1024);
                    outputPath0 = outputPath0 + ",图像大小：" + filesize + "kb";
                }

                final String outputPath = outputPath0;
            } else if (viewId == R.id.bt_back_trim) {
                enterTrimLayout();
            } else if (viewId == R.id.bt_save) {
                String outputPath0 = saveBitmap2File(mEnhanceBitmap, mOriTrimImagePathCameras.get(pos));
                final String outputPath1 = outputPath0;
                File f = new File(outputPath0);
                if (f.exists() && f.isFile()) {
                    long filesize = f.length() / (1024);
                    outputPath0 = outputPath0 + ",图像大小：" + filesize + "kb";
                }

                final String outputPath = outputPath0;

                new AlertDialog.Builder(this)
                        .setTitle(R.string.a_label_save)
                        .setMessage(outputPath)
                        .setNegativeButton(R.string.a_label_cancel, null)
                        .setPositiveButton(R.string.a_label_open,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
//                                    Uri uri = Uri
//                                            .fromFile(new File(outputPath1));
//                                    Intent intent = new Intent(
//                                            Intent.ACTION_VIEW);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    intent.putExtra(Intent.EXTRA_STREAM, uri);
//                                    intent.setDataAndType(uri, "image/*");
//                                    try {
//                                        startActivity(intent);
//                                    } catch (Exception e) {
//                                        Toast.makeText(
//                                                ImageScannerActivity.this,
//                                                R.string.a_msg_not_available_application,
//                                                Toast.LENGTH_SHORT).show();
//                                    }
                                        setResult(RESULT_OK);

                                    }
                                }).create().show();
            } else if (viewId == R.id.trash_iv) {
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("确认删除？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确认",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        if (mOriTrimImagePathCameras != null) {
                                            if (mOriTrimImagePathCameras.size() == 1) {
                                                mOriTrimImagePathCameras.remove(pos);
                                                onBackPressed();
                                            } else {
                                                mOriTrimImagePathCameras.remove(pos);
                                                imageEditViews.remove(pos);
//                                            adapter.destroyItem(viewPager,pos,null);
                                                adapter.notifyDataSetChanged();
                                                imagesNum.setText((pos + 1) + "/" + mOriTrimImagePathCameras.size());
                                            }
                                        }
                                    }
                                }).create().show();
            } else if (viewId == R.id.cut_iv) {
                delcut_rl.setVisibility(View.GONE);
                btns.setVisibility(View.VISIBLE);
                imageEditViews.get(pos).showDrawPoints(true);
                imageEditViews.get(pos).enableMovePoints(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("path", mOriTrimImagePathCameras);
        intent.putExtra("trimpath", new ArrayList(new HashSet(mOriTrimImagePaths)));
        setResult(RESULT_OK, intent);
        super.onBackPressed();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        Log.d(TAG, "position=" + position);
        if (mOriginalEnhanceBitmap != null) {
            EnhanceTask enhanceTask = new EnhanceTask(getEnhanceMode(position));
            enhanceTask.execute();
        } else {
            Log.d(TAG, "mOriginalEnhanceBitmap=" + mOriginalEnhanceBitmap);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScannerSDK.destroyContext(mEngineContext);
        if (mOriginalEnhanceBitmap != null
                && !mOriginalEnhanceBitmap.isRecycled()) {
            mOriginalEnhanceBitmap.recycle();
        }
        if (mEnhanceBitmap != null && !mEnhanceBitmap.isRecycled()) {
            mEnhanceBitmap.recycle();
        }
    }


    private void initView() {

        mTrimView = getDelegate().findViewById(R.id.rl_trim);
        mEnhanceView = getDelegate().findViewById(R.id.ll_enhance);
//        mIvEditView = (ImageEditView) getDelegate().findViewById(R.id.iv_trim);
//        mIvEditView.setDrapPoint(R.drawable.dragpoint);
//        mIvEditView.setRegionVisibility(true);
//        mIvEditView.setOnCornorChangeListener(new MyCornorChangeListener());
//        mIvEditView.setOffset(getResources().getDimension(
//                R.dimen.highlight_point_diameter));

        getDelegate().findViewById(R.id.bt_rotate).setOnClickListener(this);
        getDelegate().findViewById(R.id.bt_rotate_reserve).setOnClickListener(this);
        getDelegate().findViewById(R.id.bt_quanping).setOnClickListener(this);// fox--update--20171106

        mBtnNext = (ImageView) getDelegate().findViewById(R.id.bt_enhance);
        mBtnNext.setOnClickListener(this);

        getDelegate().findViewById(R.id.bt_back_trim).setOnClickListener(this);
        getDelegate().findViewById(R.id.bt_save).setOnClickListener(this);
        getDelegate().findViewById(R.id.trash_iv).setOnClickListener(this);
        getDelegate().findViewById(R.id.cut_iv).setOnClickListener(this);

        mIVEnhance = (ImageView) getDelegate().findViewById(R.id.iv_enhance);
        imagesNum = (TextView) getDelegate().findViewById(R.id.imagenum);
        viewPager = (ViewPager) getDelegate().findViewById(R.id.viewpager);
        delcut_rl = getDelegate().findViewById(R.id.delcut_rl);
        btns = getDelegate().findViewById(R.id.btns);


    }

    /**
     * 进入添加图片视图 Enter the add image layout
     */
    public void enterAddImageLayout() {
        mEnhanceView.setVisibility(View.GONE);
        mTrimView.setVisibility(View.GONE);
    }

    /**
     * 进入切边视图界面 Enter the crop image layout
     */
    public void enterTrimLayout() {
        mEnhanceView.setVisibility(View.GONE);
        mTrimView.setVisibility(View.VISIBLE);
    }

    /**
     * 进入增强视图界面 Enter the enhance image layout
     */
    Integer selectSaveImgType = 0;

    public void enterEnhanceLayout() {
        if (mSpinner == null) {
            mSpinner = (Spinner) getDelegate().findViewById(R.id.sp_enhance_mode);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter
                    .createFromResource(ImageScannerActivity.this,
                            R.array.arrays_enhance,
                            R.layout.spinner_checked_text);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(adapter);
            mSpinner.setOnItemSelectedListener(ImageScannerActivity.this);
        }

        if (typemSpinner == null) {
            typemSpinner = (Spinner) getDelegate().findViewById(R.id.sp_img_type_mode);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter
                    .createFromResource(ImageScannerActivity.this,
                            R.array.arrays_img_type,
                            R.layout.spinner_checked_text);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            typemSpinner.setAdapter(adapter);
            typemSpinner
                    .setOnItemSelectedListener(new OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int position, long arg3) {
                            // TODO Auto-generated method stub
                            selectSaveImgType = position;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub

                        }
                    });
        }

        mIVEnhance.setImageBitmap(mOriginalEnhanceBitmap);
        mEnhanceView.setVisibility(View.VISIBLE);
        mTrimView.setVisibility(View.GONE);
    }

    @Override
    public void onCameraStopped(String data) {
        // mCameraPreview.closeCamera();
//        loadTrimImageFile(data);
    }

    /**
     * detect image border
     */
    class DetectBorderTask extends AsyncTask<Void, Void, Boolean> {
        private long mStartTime;
        /**
         * 待检测图片的绝对路径 it's a absolute path of the source image
         */
        private ImageEditView imageEditView;
        private String mPath;
        private float[] mOrginBounds = null;

        public DetectBorderTask(String path, ImageEditView imageEditView) {
            mPath = path;
            this.imageEditView = imageEditView;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean succed = false;
            long tempTime = 0;
            mStartTime = System.currentTimeMillis();
            int threadContext = mScannerSDK.initThreadContext();
            System.out
                    .println("DetectBorderTask, initThreadContext, cost time:"
                            + (System.currentTimeMillis() - mStartTime));
            tempTime = System.currentTimeMillis();
            int imageStruct = mScannerSDK.decodeImageS(mPath);
            System.out.println("DetectBorderTask, decodeImageS, cost time:"
                    + (System.currentTimeMillis() - tempTime));

            mLastDetectBorder = null;
            int[] imgBound = getImageSizeBound(mPath);
            if (imageStruct != 0) {
                // 检测边缘
                // Detect the border of the image
                tempTime = System.currentTimeMillis();
                mLastDetectBorder = mScannerSDK.detectBorder(threadContext,
                        imageStruct);
                System.out.println("DetectBorderTask, detectBorder, cost time:"
                        + (System.currentTimeMillis() - tempTime));
                Log.d(TAG,
                        "detectAndTrimImageBorder, borders="
                                + Arrays.toString(mLastDetectBorder));
                tempTime = System.currentTimeMillis();
                mOrginBounds = getScanBoundF(imgBound, mLastDetectBorder);

                System.out.println("DetectBorderTask, fix border, cost time:"
                        + (System.currentTimeMillis() - tempTime));
                tempTime = System.currentTimeMillis();
                mScannerSDK.releaseImage(imageStruct);
                System.out.println("DetectBorderTask, releaseImage, cost time:"
                        + (System.currentTimeMillis() - tempTime));
                succed = true;
            } else {
                mOrginBounds = getScanBoundF(imgBound, null);
            }
            mScannerSDK.destroyContext(threadContext);
            System.out.println("DetectBorderTask, cost time:"
                    + (System.currentTimeMillis() - mStartTime));
            return succed;
        }

        protected void onPostExecute(Boolean result) {
            dismissProgressDialog();
            Log.d(TAG, "result=" + result);
            if (result) {
                // 标识切边区域
                // remark the cropped area
                if (mScale < 0.001 && mScale > -0.001) {
                    mScale = 1.0f;
                }
                if (mOrginBounds != null) {
                    // 加载在原图的切边区载
                    // load the source image crop area
                    imageEditView.setRegion(mOrginBounds, mScale);
                    imageEditView.setTag(R.id.add_btn, mOrginBounds);
                    // 显示切边区域
                    // set the crop are to be shown
                    imageEditView.setRegionVisibility(true);
                    // 设置显示切边区域，但不显示锚点和不能手动更改区域
                    // mIvEditView.showDrawPoints(false);
                    // mIvEditView.enableMovePoints(false);
                }
                mBtnNext.setVisibility(View.VISIBLE);
            } else {
                Log.d(TAG, "result=" + result);
            }
        }

    }

    /**
     * 异步处理切边 deal with the cropping asynchronous
     */
    class TrimTask extends AsyncTask<Void, Void, Boolean> {
        private long mStartTime;
        /**
         * 待切边图片的绝对路径 The absolute path of the image going to be cropped
         */
        private String mPath;

        public TrimTask(String path) {
            mPath = path;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d(TAG, "TrimTask, doInBackground");
            long tempTime = 0;
            boolean succeed = false;
            mStartTime = System.currentTimeMillis();
            // 用于多线程执行图像处理 该函数返回一块内存指针，在该线程上执行图像操作时，作为第一个参数传入
            int threadContext = mScannerSDK.initThreadContext();
            System.out.println("TrimTask, initThreadContext, cost time:"
                    + (System.currentTimeMillis() - mStartTime));

            tempTime = System.currentTimeMillis();
            int imageStruct = mScannerSDK.decodeImageS(mPath);
            System.out.println("TrimTask, decodeImageS, cost time:"
                    + (System.currentTimeMillis() - tempTime));

            if (imageStruct != 0) {
                // 检测边缘，并切边处理
                // Detect the border and crop it
                tempTime = System.currentTimeMillis();
                // 从控件中，获取对应图片上的边缘
                // Get the edge of the image on the control
                int[] bound = imageEditViews.get(pos).getRegion(false);
                Log.d(TAG, "bound=" + Arrays.toString(bound));
                mScannerSDK.trimImage(threadContext, imageStruct, bound,
                        TRIM_IMAGE_MAXSIDE);
                System.out.println("TrimTask, trimImage, cost time:"
                        + (System.currentTimeMillis() - tempTime));

                tempTime = System.currentTimeMillis();
                mScannerSDK.saveImage(imageStruct, mOriTrimImagePath, 80);
                System.out.println("TrimTask, saveImage, cost time:"
                        + (System.currentTimeMillis() - tempTime));

                tempTime = System.currentTimeMillis();
                mScannerSDK.releaseImage(imageStruct);
                System.out.println("TrimTask, releaseImage, cost time:"
                        + (System.currentTimeMillis() - tempTime));

                File file = new File(mOriTrimImagePath);
                if (file.exists()) {
                    tempTime = System.currentTimeMillis();
                    Bitmap trimBitmap = null;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(mOriTrimImagePath, options);
                    int bitmapWidth = options.outWidth;
                    int bitmapHeight = options.outHeight;
                    Log.d(TAG, "bitmapWidth=" + bitmapWidth + " bitmapHeight="
                            + bitmapHeight);
                    if (bitmapWidth > 0 && bitmapHeight > 0) {
                        int viewWidth = imageEditViews.get(pos).getWidth();
                        int viewHeight = imageEditViews.get(pos).getHeight();
                        float scaleX = 1.0f * viewWidth / bitmapWidth;
                        float scaleY = 1.0f * viewHeight / bitmapHeight;
                        float scale = scaleX > scaleY ? scaleY : scaleX;
                        int inSampleSize = (int) (1 / scale);
                        if (inSampleSize == 0) {
                            inSampleSize = 1;
                        }
                        options.inSampleSize = inSampleSize;
                        options.inJustDecodeBounds = false;
                        trimBitmap = BitmapFactory.decodeFile(
                                mOriTrimImagePath, options);

                        // fox--update--20171106----旋转切边增强的图片--start
                        Matrix m = null;


                        if (rotateValue != 0) {
                            if (m == null) {
                                m = new Matrix();
                            }
                            m.postRotate(rotateValue);
                            trimBitmap = Bitmap.createBitmap(trimBitmap, 0, 0,
                                    trimBitmap.getWidth(),
                                    trimBitmap.getHeight(), m, true);
                        }

                        // fox--update--20171106----旋转切边增强的图片---end

                    }
                    // 回收之前生成增强图片
                    if (mOriginalEnhanceBitmap != null
                            && !mOriginalEnhanceBitmap.isRecycled()) {
                        mOriginalEnhanceBitmap.recycle();
                    }
                    mOriginalEnhanceBitmap = trimBitmap;
                    try {
                        file.delete();
                    } catch (Exception e) {
                        Log.e(TAG, "Exception", e);
                    }
                    System.out
                            .println("TrimTask, BitmapFactory.decodeFile, cost time:"
                                    + (System.currentTimeMillis() - tempTime));
                } else {
                    Log.d(TAG, "file is not exist");
                }
                succeed = true;
            }
            mScannerSDK.destroyContext(threadContext);
            System.out.println("TrimTask, cost time:"
                    + (System.currentTimeMillis() - mStartTime));
            return succeed;
        }

        protected void onPostExecute(Boolean result) {
            dismissProgressDialog();
            Log.d(TAG, "result=" + result);
            if (result) {
//                enterEnhanceLayout();
                mEnhanceBitmap = mOriginalEnhanceBitmap.copy(
                        mOriginalEnhanceBitmap.getConfig(), true);

                saveBitmap2File(mEnhanceBitmap, mPath);
            } else {
                Log.d(TAG, "result=" + result);
            }
        }
    }

    /**
     * 开始对图像进行切边 Start to crop the image
     */
    private void startTtrim(String path) {
        if (TextUtils.isEmpty(mOriTrimImagePath)) {
            return;
        }
        if (!TextUtils.isEmpty(path)) {
            TrimTask trimTask = new TrimTask(path);
            trimTask.execute();
        }
//        if (!TextUtils.isEmpty(mCurrentInputImagePath)) {
//            TrimTask trimTask = new TrimTask(mCurrentInputImagePath);
//            trimTask.execute();
//        }
    }

    private int mEnhanceMode = ScannerSDK.ENHANCE_MODE_AUTO;

    /**
     * 图像增强处理 Enhance the image asynchronous
     */
    class EnhanceTask extends AsyncTask<Void, Void, Void> {

        public EnhanceTask(int mode) {
            mEnhanceMode = mode;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {
            int threadContext = mScannerSDK.initThreadContext();
            // 回收之前生成增强图片
            // Recycle the previous enhanced image
            if (mEnhanceBitmap != null && !mEnhanceBitmap.isRecycled()) {
                mEnhanceBitmap.recycle();
            }
            // 先拷贝一份
            // Copy a piece
            mEnhanceBitmap = mOriginalEnhanceBitmap.copy(
                    mOriginalEnhanceBitmap.getConfig(), true);
            Log.d(TAG, "mEnhanceBitmap");
            mScannerSDK.enhanceImage(threadContext, mEnhanceBitmap,
                    mEnhanceMode);
            Log.d(TAG, "enhanceImage");
            mScannerSDK.destroyContext(threadContext);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dismissProgressDialog();
            mIVEnhance.setImageBitmap(mEnhanceBitmap);
            Log.d(TAG, "finish, EnhanceTask");
        }
    }

    /**
     * 获取图片大小
     *
     * @param pathName
     * @param pathName
     * @return image size
     */
    private static int[] getImageSizeBound(String pathName) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        BitmapFactory.decodeFile(pathName, options);
        int[] wh = null;
        if (options.mCancel || options.outWidth == -1
                || options.outHeight == -1) {
            Log.d(TAG, "getImageBound error " + pathName);
        } else {
            wh = new int[2];
            wh[0] = options.outWidth;
            wh[1] = options.outHeight;
        }
        return wh;
    }

    /**
     * 对切边区域进行调整，保证切边区域在图片内部
     *
     * @param size    原图宽高
     * @param borders 探测边界
     * @param size    : source image wide and high
     * @param borders : it's an int array of the 4 corner points
     * @return the new crop image area after the adjustment
     */
    private static float[] getScanBoundF(int[] size, int[] borders) {
        float[] bound = null;
        if (size != null) {
            if ((borders == null)) {
                Log.d(TAG, "did not found bound");
                bound = new float[]{0, 0, size[0], 0, size[0], size[1], 0,
                        size[1]};
            } else {
                bound = new float[8];
                for (int j = 0; j < bound.length; j++) {
                    bound[j] = borders[j];
                }
                for (int i = 0; i < 4; i++) {
                    if (bound[i * 2] < 0)// x
                        bound[i * 2] = 0;
                    if (bound[i * 2 + 1] < 0)// y
                        bound[i * 2 + 1] = 0;
                    if (bound[i * 2] > size[0])// x
                        bound[i * 2] = size[0];
                    if (bound[i * 2 + 1] > size[1])// y
                        bound[i * 2 + 1] = size[1];
                }
            }
        }
        return bound;
    }

    /**
     * get enhance mode
     *
     * @param which <pre>
     *                                                                                                                                                                                                                                                                                                                         0:自动
     *                                                                                                                                                                                                                                                                                                                         1：原图
     *                                                                                                                                                                                                                                                                                                                         2：增强
     *                                                                                                                                                                                                                                                                                                                         3：增强并锐化
     *                                                                                                                                                                                                                                                                                                                         4：灰度模式
     *                                                                                                                                                                                                                                                                                                                         5：黑白模式
     *                                                                                                                                                                                                                                                                                                                         其它：自动
     *                                                                                                                                                                                                                                                                                                            </pre>
     *              <p>
     *              get enhance mode
     *              <p>
     *              <pre>
     *                                                                                                                                                                                                                                                                                                                         0: Auto
     *                                                                                                                                                                                                                                                                                                                         1：No enhance
     *                                                                                                                                                                                                                                                                                                                         2：Enhance
     *                                                                                                                                                                                                                                                                                                                         3：Enhance and Magic
     *                                                                                                                                                                                                                                                                                                                         4：Gray
     *                                                                                                                                                                                                                                                                                                                         5：Black-and-White
     *                                                                                                                                                                                                                                                                                                            </pre>
     */
    public int getEnhanceMode(int which) {
        int mode = ScannerSDK.ENHANCE_MODE_AUTO;
        switch (which) {
            case 0:
                // 自动
                // Auto
                mode = ScannerSDK.ENHANCE_MODE_AUTO;
                break;
            case 1:
                // 原图
                // No Enhance
                mode = ScannerSDK.ENHANCE_MODE_NO_ENHANCE;
                break;
            case 2:
                // 增强
                // Enhance
                mode = ScannerSDK.ENHANCE_MODE_LINEAR;
                break;
            case 3:
                // 增强并锐化
                // Enhance and Magic
                mode = ScannerSDK.ENHANCE_MODE_MAGIC;
                break;
            case 4:
                // 灰度模式
                // Gray
                mode = ScannerSDK.ENHANCE_MODE_GRAY;
                break;
            case 5:
                // 黑白模式
                // Black-and-White
                mode = ScannerSDK.ENHANCE_MODE_BLACK_WHITE;
                break;
            default:
                mode = ScannerSDK.ENHANCE_MODE_AUTO;
        }
        return mode;
    }

    private ProgressDialog mProgressDialog;

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(ImageScannerActivity.this);
            mProgressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage(getResources().getString(
                    R.string.a_msg_working));
        }
        mProgressDialog.show();
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 保存图片
     *
     * @param src Save the result image--cropped and enhanced
     */
    public String saveBitmap2File(Bitmap src, String path) {

        String imgTypeString = ".jpg";
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        switch (selectSaveImgType) {
            case 0:
                imgTypeString = ".jpg";
                format = Bitmap.CompressFormat.JPEG;
                break;
            case 1:
                imgTypeString = ".png";
                format = Bitmap.CompressFormat.PNG;
                break;
            case 2:
                imgTypeString = ".jpeg";
                format = Bitmap.CompressFormat.JPEG;

                break;

            default:
                break;
        }

//        String outPutFilePath = mRootPath + File.separator
//                + sPdfTime.format(new Date()) + imgTypeString;
        File file = new File(path);
        String outPutFilePath = mRootPath + File.separator + file.getName();
        mOriTrimImagePaths.add(outPutFilePath);
        FileOutputStream outPutStream = null;
        try {
            outPutStream = new FileOutputStream(outPutFilePath);
            src.compress(format, 100, outPutStream);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException", e);
        } finally {
            if (outPutStream != null) {
                try {
                    outPutStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException", e);
                }
            }
        }
        if (TextUtils.isEmpty(mOriTrimImagePathCamera)) {
            delcut_rl.setVisibility(View.VISIBLE);
            btns.setVisibility(View.GONE);
        } else {
//            Bundle bundle = new Bundle();
//            bundle.putString("path", outPutFilePath);
//            nextActivity(ImageEnhanceActivity.class, bundle);
//            onBackPressed();
        }
        Log.d(TAG, "saveBitmap2File, outPutFilePath=" + outPutFilePath);
        return outPutFilePath;
    }

    public String saveBitmap2Filev2(Bitmap src, String path) {

        String imgTypeString = ".jpg";
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        switch (selectSaveImgType) {
            case 0:
                imgTypeString = ".jpg";
                format = Bitmap.CompressFormat.JPEG;
                break;
            case 1:
                imgTypeString = ".png";
                format = Bitmap.CompressFormat.PNG;
                break;
            case 2:
                imgTypeString = ".jpeg";
                format = Bitmap.CompressFormat.JPEG;

                break;

            default:
                break;
        }

//        String outPutFilePath = mRootPath + File.separator
//                + sPdfTime.format(new Date()) + imgTypeString;
        File file = new File(path);
        String outPutFilePath = mRootPath + File.separator + file.getName();
        mOriTrimImagePaths.add(outPutFilePath);
        FileOutputStream outPutStream = null;
        try {
            outPutStream = new FileOutputStream(outPutFilePath);
            src.compress(format, 100, outPutStream);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException", e);
        } finally {
            if (outPutStream != null) {
                try {
                    outPutStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException", e);
                }
            }
        }
        if (TextUtils.isEmpty(mOriTrimImagePathCamera)) {
            delcut_rl.setVisibility(View.VISIBLE);
            btns.setVisibility(View.GONE);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("path", outPutFilePath);
            nextActivity(ImageEnhanceActivity.class, bundle);
            onBackPressed();
        }
        Log.d(TAG, "saveBitmap2File, outPutFilePath=" + outPutFilePath);
        return outPutFilePath;
    }

    /**
     * 判断图片是否合法，png或jpg为合法图片
     *
     * @param path 图片路径
     * @return true image is valid
     * <p>
     * Whether the image is valid or not, only support .png and .jpg
     */
    public boolean isValidImage(String path) {
        return !TextUtils.isEmpty(path)
                && (path.endsWith("png") || path.endsWith("jpg")
                || path.endsWith("jpeg") || path.endsWith("PNG")
                || path.endsWith("JPG") || path.endsWith("JPEG"));
    }

    /**
     * 处理导入的图片
     *
     * @param data Process the input image
     */
    private void progressExportImage(Intent data) {
        if (data != null) {
            Uri u = data.getData();
            Log.d(TAG, "data.getData()=" + u);
            if (u != null) {
                String path = DocumentUtil.getInstance().getPath(this, u);
                if (isValidImage(path)) {
//                    loadTrimImageFile(path);
                } else {
                    Toast.makeText(this, R.string.a_msg_illegal,
                            Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Log.d(TAG, "data==null");
        }
    }

    /**
     * 对图片进行切边处理，并显示
     *
     * @param imageFilePath
     * Crop the image
     */
    int inSampleSizeAll = 1;

    private void loadTrimImageFile(final String imageFilePath, final ImageEditView view) {
        if (TextUtils.isEmpty(imageFilePath)) {
            Log.d(TAG, "imageFilePath is empty");
            return;
        }
        File file = new File(imageFilePath);
        if (!file.exists()) {
            Log.d(TAG, "imageFilePath is not exist");
            return;
        }

        Log.d(TAG, "loadTrimImageFile, imageFilePath=" + imageFilePath);
        enterTrimLayout();
        view.post(new Runnable() {

            @Override
            public void run() {
                // 为了防止图片过大出现内存溢出，使用压缩后的图进行显示
                // to prevent out of memory error when the image is too large,
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imageFilePath, options);
                int bitmapWidth = options.outWidth;
                int bitmapHeight = options.outHeight;
                Log.d(TAG, "bitmapWidth=" + bitmapWidth + " bitmapHeight="
                        + bitmapHeight);
                if (bitmapWidth > 0 && bitmapHeight > 0) {
                    int viewWidth = view.getWidth();
                    int viewHeight = view.getHeight();
                    if (viewWidth > 0 && viewHeight > 0) {
                        float scaleX = 1.0f * viewWidth / bitmapWidth;
                        float scaleY = 1.0f * viewHeight / bitmapHeight;
                        float scale = scaleX > scaleY ? scaleY : scaleX;
                        int inSampleSize = (int) (1 / scale);
                        if (inSampleSize == 0) {
                            inSampleSize = 1;
                        }
                        options.inSampleSize = inSampleSize;
                        inSampleSizeAll = inSampleSize;
                        options.inJustDecodeBounds = false;
                        Bitmap testBitmap = BitmapFactory.decodeFile(
                                imageFilePath, options);
                        mScale = 1.0f * testBitmap.getWidth() / bitmapWidth;
                        int[] imgBound = new int[]{bitmapWidth, bitmapHeight};
                        // 设置加载原图的宽高
                        // Set the source image wide and high
                        view.setRawImageBounds(imgBound);
                        view.setTag(imgBound);
                        // 加载
                        // Load the image
                        view.loadDrawBitmap(testBitmap);
                        mCurrentInputImagePath = imageFilePath;
                        DetectBorderTask detectTask = new DetectBorderTask(
                                mCurrentInputImagePath, view);
                        detectTask.execute();
                    }

                } else {
                    Log.d(TAG, "bitmapWidth=" + bitmapWidth + " bitmapHeight="
                            + bitmapHeight);
                }

            }
        });
    }

    /**
     * 监听切边区域的调整事件
     *
     * @author zhongze_wu listen the event of crop area adjustment
     */
    private class MyCornorChangeListener implements OnCornorChangeListener {
        private ImageEditView imageEditView;

        /**
         * 开始移动点或边 Start to move the frame or the point
         */
        public MyCornorChangeListener(ImageEditView imageEditView) {
            this.imageEditView = imageEditView;
        }

        @Override
        public void onPreMove() {
        }

        /**
         * 移动结束 Moving finished
         */
        @Override
        public void onPostMove() {
        }

        @Override
        public void onCornorChanged() {
            if (imageEditView != null) {
                if (imageEditView.isCanTrim(mEngineContext)) {
                    imageEditView.setLinePaintColor(mNormalColor);
                } else {
                    imageEditView.setLinePaintColor(mErrorColor);
                }
                imageEditView.invalidate();
            }
        }
    }
}
