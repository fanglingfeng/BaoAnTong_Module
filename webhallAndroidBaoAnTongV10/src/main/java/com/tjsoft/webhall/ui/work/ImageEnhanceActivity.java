package com.tjsoft.webhall.ui.work;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.intsig.scanner.CommonUtil;
import com.intsig.scanner.ScannerSDK;
import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.webhall.entity.EnhenceMode;
import com.tjsoft.webhall.entity.PictureEvent;

import net.liang.appbaselibrary.base.BaseAppCompatActivity;
import net.liang.appbaselibrary.base.BindingViewHolder;
import net.liang.appbaselibrary.base.RecyclerView.BaseRecyclerAdapter;
import net.liang.appbaselibrary.base.mvp.MvpPresenter;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 该界面主要展示图片切边与增强 This view is used to show image's crop and enhance process
 */
public class ImageEnhanceActivity extends BaseAppCompatActivity implements OnClickListener {
    private static final String TAG = ImageEnhanceActivity.class
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
     * 用于显示增强模式 used to show the enhance model set
     */
    private Spinner mSpinner;
    private Spinner typemSpinner;


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
    private static final int TRIM_IMAGE_MAXSIDE = 1024;
    /**
     * your app key
     */
    private static final String APPKEY = "K2XWPEQABP11PafD1JRQQr8D";
    /**
     * 用于引擎指针，用于辅助检测切边区域是否合法
     */
    private int mEngineContext;
    private String mOriTrimImagePathCamera;// fox--update--20171106
    private ArrayList<Bitmap> enhancedBitmaps = new ArrayList<>();// fox--update--20171106
    List<EnhenceMode> modes;

    private RecyclerView recyclerView;
    private MyRecyclerAdapter adapter;
    ;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_enhance;
    }

    @Override
    protected MvpPresenter getPresenter() {
        return null;
    }

    @Override
    public void init() {
        super.init();
        mOriTrimImagePathCamera = getIntent().getStringExtra("path");
        mOriginalEnhanceBitmap = BitmapFactory.decodeFile(mOriTrimImagePathCamera);
        mScannerSDK = new ScannerSDK();

        new Thread(new Runnable() {

            @Override
            public void run() {
                int code = mScannerSDK.initSDK(ImageEnhanceActivity.this,
                        APPKEY);
                mEngineContext = mScannerSDK.initThreadContext();
                mHandler.sendEmptyMessage(code);
                Log.d(TAG, "code=" + code);
            }
        }).start();
        modes = new ArrayList<>();
        modes.add(new EnhenceMode("自动", 0));
        modes.add(new EnhenceMode("原图", 1));
        modes.add(new EnhenceMode("增亮", 2));
        modes.add(new EnhenceMode("增强并锐化", 3));
        modes.add(new EnhenceMode("灰度并锐化", 4));
        modes.add(new EnhenceMode("黑白", 5));

        setToolbarCentel(true, "图片增强");
        ImageView back = (ImageView) findViewById(net.liang.appbaselibrary.R.id.toolbar_back);
        back.setImageResource(R.drawable.tj_back);
        toolbar.setBackgroundColor(getResources().getColor(R.color.qiebian));
        for (int i = 0; i < 6; i++) {
            EnhanceTask enhanceTask = new EnhanceTask(getEnhanceMode(i), mIVEnhance, i);
            enhanceTask.execute();
        }
        initView();
    }


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            if (msg.what != 0) {
                errorcode = msg.what;
                Toast.makeText(
                        ImageEnhanceActivity.this,
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

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.bt_back_trim) {
//            int rotateValue = 0;
//            BitmapFactory.Options options = new BitmapFactory.Options();
//
//            options.inSampleSize = 1;
//
//            rotateValue = rotateValue >= 360 ? 90 : rotateValue + 90;
//
//            Bitmap bitmap = BitmapFactory.decodeFile(mOriTrimImagePathCamera,
//                    options);
//            RotateBitmap bitmapRotate = new RotateBitmap(bitmap, rotateValue);
            mEnhanceBitmap = com.tjsoft.util.BitmapFactory.rotateBitmap(mEnhanceBitmap, 90);
//            mIvEditView.rotate(bitmapRotate, true);
            mIVEnhance = (ImageView) getDelegate().findViewById(R.id.iv_enhance);
            mIVEnhance.setImageBitmap(mEnhanceBitmap);

        } else if (viewId == R.id.bt_save) {
            String outputPath0 = saveBitmap2File(mEnhanceBitmap);
            final String outputPath1 = outputPath0;
            File f = new File(outputPath0);
            if (f.exists() && f.isFile()) {
                long filesize = f.length() / (1024);
                outputPath0 = outputPath0 + ",图像大小：" + filesize + "kb";
            }

            final String outputPath = outputPath0;
            //最终图像
            PictureEvent pictureEvent = new PictureEvent();
            pictureEvent.setSingle(true);
            pictureEvent.setFileUri(outputPath1);
            EventBus.getDefault().post(pictureEvent);
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScannerSDK.destroyContext(mEngineContext);
        try {
            if (mOriginalEnhanceBitmap != null
                    && !mOriginalEnhanceBitmap.isRecycled()) {
                mOriginalEnhanceBitmap.recycle();
            }
            if (mEnhanceBitmap != null && !mEnhanceBitmap.isRecycled()) {
                mEnhanceBitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initView() {

        getDelegate().findViewById(R.id.bt_back_trim).setOnClickListener(this);
        getDelegate().findViewById(R.id.bt_save).setOnClickListener(this);

        mIVEnhance = (ImageView) getDelegate().findViewById(R.id.iv_enhance);
        mIVEnhance.setImageBitmap(mOriginalEnhanceBitmap);
        mEnhanceBitmap=mOriginalEnhanceBitmap;
        recyclerView = (RecyclerView) getDelegate().findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);


    }


    Integer selectSaveImgType = 0;


    /**
     * 图像增强处理 Enhance the image asynchronous
     */
    class EnhanceTask extends AsyncTask<Void, Void, Bitmap> {
        private int mEnhanceMode = ScannerSDK.ENHANCE_MODE_AUTO;
        private ImageView imageView;
        private int position;

        public EnhanceTask(int mode, ImageView imageView, int positon) {
            mEnhanceMode = mode;
            this.imageView = imageView;
            this.position = positon;
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            int threadContext = mScannerSDK.initThreadContext();
            // 回收之前生成增强图片
            // Recycle the previous enhanced image
//            if (mEnhanceBitmap != null && !mEnhanceBitmap.isRecycled()) {
//                mEnhanceBitmap.recycle();
//            }
            // 先拷贝一份
            // Copy a piece
            Bitmap copy = mOriginalEnhanceBitmap.copy(
                    mOriginalEnhanceBitmap.getConfig(), true);
            Log.d(TAG, "mEnhanceBitmap");
            mScannerSDK.enhanceImage(threadContext, copy,
                    mEnhanceMode);
            Log.d(TAG, "enhanceImage");
            mScannerSDK.destroyContext(threadContext);
            return copy;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            dismissProgressDialog();
//            imageView.setImageBitmap(mEnhanceBitmap);
            enhancedBitmaps.add(result);
//            enhancedBitmaps.add(mEnhanceBitmap);
            Log.d(TAG, "finish, EnhanceTask");
            if (position == 5) {
                adapter = new MyRecyclerAdapter(ImageEnhanceActivity.this, recyclerView, R.layout.item_enhance, modes);
                recyclerView.setAdapter(adapter);
                recyclerView.addOnItemTouchListener(new OnItemClickListener() {

                    @Override
                    public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//                        ImageView iv = (ImageView) view.findViewById(R.id.iv_enhance);
                        mEnhanceBitmap = enhancedBitmaps.get(position);
                        mIVEnhance.setImageBitmap(mEnhanceBitmap);
//                iv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                EnhanceTask enhanceTask = new EnhanceTask(getEnhanceMode(position),mIVEnhance);
//                enhanceTask.execute();
                    }
                });
            }
        }
    }


    /**
     * get enhance mode
     *
     * @param which <pre>
     *                                                                                                                      0:自动
     *                                                                                                                      1：原图
     *                                                                                                                      2：增强
     *                                                                                                                      3：增强并锐化
     *                                                                                                                      4：灰度模式
     *                                                                                                                      5：黑白模式
     *                                                                                                                      其它：自动
     *                                                                                                         </pre>
     *              <p>
     *              get enhance mode
     *              <p>
     *              <pre>
     *                                                                                                                      0: Auto
     *                                                                                                                      1：No enhance
     *                                                                                                                      2：Enhance
     *                                                                                                                      3：Enhance and Magic
     *                                                                                                                      4：Gray
     *                                                                                                                      5：Black-and-White
     *                                                                                                         </pre>
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
            mProgressDialog = new ProgressDialog(ImageEnhanceActivity.this);
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
    public String saveBitmap2File(Bitmap src) {

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
//        String outPutFilePath =  mRootPath + File.separator + System.currentTimeMillis()+"oriTrim.jpg";
        String outPutFilePath = mOriTrimImagePathCamera;
//        mOriTrimImagePaths.add(outPutFilePath);
        FileOutputStream outPutStream = null;
        try {
            outPutStream = new FileOutputStream(outPutFilePath);
            if (src != null) {
                src.compress(format, 100, outPutStream);
            }
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
        Log.d(TAG, "saveBitmap2File, outPutFilePath=" + outPutFilePath);
        return outPutFilePath;
    }


    public class MyRecyclerAdapter<T> extends BaseRecyclerAdapter<T> {

        public MyRecyclerAdapter(Context context, RecyclerView recyclerView, int layoutIdRes, List<T> data) {
            super(context, recyclerView, layoutIdRes, data);
        }


        @Override
        protected void convert(BindingViewHolder helper, T item) {
            helper.setText(R.id.mode_tv, ((EnhenceMode) item).getModeName());
            int position = ((EnhenceMode) item).getPosition();
            Log.d(TAG, "position=" + position);
            for (Bitmap enhancedBitmap : enhancedBitmaps) {
                Log.d(TAG, "enhancedBitmap=" + enhancedBitmap);
            }
            helper.setImageBitmap(R.id.iv_mode, enhancedBitmaps.get(position));

//            EnhanceTask enhanceTask = new EnhanceTask(getEnhanceMode(helper.getLayoutPosition()),(ImageView) helper.getView(R.id.iv_enhance));
//            enhanceTask.execute();
        }
    }

}
