package com.tjsoft.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.ImageButton;
import android.widget.Toast;


import com.tjsoft.camera.utils.ImageUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 相机工具类
 *
 * @author yifan
 */
public class MQCameraInterfaceUtil {
    private static final String TAG = MQCameraInterfaceUtil.class.getSimpleName();
    private Camera mCamera;
    private Camera.Parameters mParams;
    private boolean isPreviewing = false;
    private float mPreviwRate = -1f;
    private static MQCameraInterfaceUtil mCameraInterface;
    private static Context mContext;
    private Size adapterSize = null;
    private Size previewSize = null;
    private long start;
    // 保存成功
    private boolean saveSuccess;

    private String localTempImgDir = "tjsoft";
    private String localTempImgFileName = "";
    /**
     * 最小预览界面的分辨率
     */
    private static final int MIN_PREVIEW_PIXELS = 480 * 320;
    /**
     * 最大宽高比差
     */
    private static final double MAX_ASPECT_DISTORTION = 0.15;
//    private static final double MAX_ASPECT_DISTORTION = 0.57;

    public void setImagePathCallBack(ImagePathCallBack imagePathCallBack) {
        this.imagePathCallBack = imagePathCallBack;
    }

    private ImagePathCallBack imagePathCallBack;

    public interface CamOpenOverCallback {
        public void cameraHasOpened();
    }

    private MQCameraInterfaceUtil() {

    }

    public boolean isPreviewing() {
        return isPreviewing;
    }

    public static synchronized MQCameraInterfaceUtil getInstance(Context context) {
        mContext = context;
        if (mCameraInterface == null) {
            mCameraInterface = new MQCameraInterfaceUtil();
        }
        return mCameraInterface;
    }

    /**
     * 打开Camera
     *
     * @param callback
     */
    public void doOpenCamera(CamOpenOverCallback callback) {
        Log.d(TAG, "---------onPreviewFrame---------" + Thread.currentThread().toString());
        try {

            mCamera = new CameraHelper(mContext).openCamera(cameraType);
//            mCamera = Camera.open(cameraType);
            Log.i(TAG, "Camera open over....");
            callback.cameraHasOpened();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Camera open failed....");
            openCameraFailCallback();
        }
//        EventBus.getDefault().post(CommonConstant.INFO_COMMANDS.OPEN_CAMERA_CALLBACK);
    }


    /**
     * 使用Surfaceview开启预览
     *
     * @param holder
     * @param previewRate
     */
    public void doStartPreview(SurfaceHolder holder, float previewRate) {
        Log.d(TAG, "---------doStartPreview---------" + Thread.currentThread().toString());
        Log.i(TAG, "doStartPreview...---------------------=" + isPreviewing);
        if (isPreviewing) {
            mCamera.stopPreview();
            return;
        }
        if (mCamera != null) {
            try {
                holder.addCallback(new SurfaceCallback());
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "doStartPreview: -------------------------message=" + e.getMessage());
            }
//			initCamera(previewRate);
            initCamera();

        }


    }

    /*SurfaceCallback*/
    private final class SurfaceCallback implements SurfaceHolder.Callback {

        public void surfaceDestroyed(SurfaceHolder holder) {
            try {
                if (mCamera != null) {
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                }
            } catch (Exception e) {
                //相机已经关了
            }

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (null == mCamera) {
                try {
//                    mCamera = Camera.open();
                    mCamera = new CameraHelper(mContext).openCamera(cameraType);
                    mCamera.setPreviewDisplay(holder);
                    initCamera();
                    mCamera.startPreview();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            autoFocus();
        }
    }

    //实现自动对焦
    private void autoFocus() {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mCamera == null) {
                    return;
                }
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (success) {
                            initCamera();//实现相机的参数初始化
                        }
                    }
                });
            }
        };
    }

    /**
     * 使用TextureView预览Camera
     *
     * @param surface
     * @param previewRate
     */
    public void doStartPreview(SurfaceTexture surface, float previewRate) {
        Log.i(TAG, "doStartPreview...");
        if (isPreviewing) {
            mCamera.stopPreview();
            return;
        }
        if (mCamera != null) {
            try {
                mCamera.setPreviewTexture(surface);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//			initCamera(previewRate);
            initCamera();
        }

    }

    /**
     * 停止预览，释放Camera
     */
    public void doStopCamera() {
        if (null != mCamera) {
            Log.i(TAG, "-----------------------------doStopCamera-----------...");
            mCamera.setPreviewCallback(null);
            mCamera.setOneShotPreviewCallback(null);
//            mCamera.setPreviewCallbackWithBuffer(null);
            mCamera.stopPreview();
            isPreviewing = false;
            CommonConstant.isCameraUsing = false;
            mPreviwRate = -1f;
            mCamera.release();
            mCamera = null;
            adapterSize = null;
            previewSize = null;
            System.gc();
        }
    }

    /**
     * 闪光灯
     *
     * @param flashIcon
     */
    private boolean isFlashOn = false;

    public void cameraFlash(ImageButton flashIcon) {
//        freeCameraResource();
//        try {
//            if (mCamera != null) {
//                if (cameraType == 1) return;
//                isFlashOn = !isFlashOn;
////        //闪光灯
//                Camera.Parameters parameters = mCamera.getParameters();
//                if (isFlashOn) {
//                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//                } else {
//                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//                }
//                mCamera.setParameters(parameters);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        initCamera();
        try {
            if (mCamera == null || mCamera.getParameters() == null
                    || mCamera.getParameters().getSupportedFlashModes() == null) {
                return;
            }
            Camera.Parameters parameters = mCamera.getParameters();
            String flashMode = mCamera.getParameters().getFlashMode();
            List<String> supportedModes = mCamera.getParameters().getSupportedFlashModes();
            if (Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)
                    && supportedModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {//关闭状态
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
                flashIcon.setSelected(false);
            } else if (Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode)) {//开启状态
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                flashIcon.setSelected(true);
                mCamera.setParameters(parameters);
            }
        } catch (Exception e) {

        }
    }

    /**
     * 切换摄像头
     */
    public int cameraType = 0; // 摄像机类型:默认后置


    public void switchCamera(CamOpenOverCallback camOpenOverCallback) {
        cameraType = (cameraType + 1) % new CameraHelper(mContext).getNumberOfCameras();
        doStopCamera();
        doOpenCamera(camOpenOverCallback);
        Log.d("cameraType", "---------cameraType---------" + cameraType);
    }


    /**
     * 拍照
     */
    public void doTakePicture() {
        if (isPreviewing && (mCamera != null)) {
            try {
                mCamera.takePicture(new ShutterCallback() {
                    @Override
                    public void onShutter() {
                        Log.i(TAG, "myShutterCallback:onShutter...");
                    }
                }, null, mJpegPictureCallback);
            } catch (Throwable t) {
                t.printStackTrace();
                Toast.makeText(mContext, "拍照失败，请重试！", Toast.LENGTH_SHORT).show();
                try {
                    mCamera.startPreview();
                } catch (Throwable e) {

                }
            }
        }
    }

    int DST_RECT_WIDTH, DST_RECT_HEIGHT;

    public void doTakePicture(int w, int h) {
//        if (isPreviewing && (mCamera != null)) {
        if (mCamera != null) {
            Log.i(TAG, "矩形拍照尺寸:width = " + w + " h = " + h);
            DST_RECT_WIDTH = w;
            DST_RECT_HEIGHT = h;
            // 三个参数分别为拍照声音设置方法，raw个人理解为拍摄有声照片时的回调方法，jpeg参数即为获取图像的回调
//            mCamera.takePicture(mShutterCallback, null, mRectJpegPictureCallback);
//            isTrue = true;

            Log.d("time1", "---------time1---------" + System.currentTimeMillis());
            mCamera.setOneShotPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(final byte[] data, Camera camera) {
                    Log.d("VideoTestActivity", "---------onPreviewFrame---------");
                    Log.d("time2", "---------time2---------" + System.currentTimeMillis());
                    final Size size = camera.getParameters().getPreviewSize();
                    //在拍照的时候    预览静止   回调之后在开启
                    mCamera.stopPreview();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //保存Bitmap到sdcard
                            String path = Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/" + localTempImgFileName;
//                    long dataTake = System.currentTimeMillis();
                            String fileShortName = "";
                            SimpleDateFormat format = new SimpleDateFormat("HH-mm-ss");
                            String fileName = format.format(new Date());
                            fileShortName = new StringBuilder().append(fileName).append(".jpg").toString();
//                    String fileShortName = new StringBuilder().append("/").append(dataTake).append(".jpg").toString();
                            String jpegName = path + fileShortName;
                            Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
                            Bitmap bmp = null;
                            Bitmap rectBitmap = null;
                            try {
                                File file = new File(jpegName);
                                if (!file.getParentFile().exists()) {
                                    file.mkdirs();

                                }
                                file.createNewFile();
                                FileOutputStream fout = new FileOutputStream(file);
                                BufferedOutputStream bos = new BufferedOutputStream(fout);

                                Log.d("time3", "---------time3---------" + System.currentTimeMillis());

                                YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                                if (image != null) {
                                    Log.d("VideoTestActivity", "---------image != null---------");
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    image.compressToJpeg(new Rect(0, 0, size.width, size.height), 100, stream);
                                    bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
//                            int x = bmp.getWidth() / 2 - DST_RECT_WIDTH / 2;
//                            int y = bmp.getHeight() / 2 - DST_RECT_HEIGHT / 2;
                                    Log.i(TAG, "rotaBitmap.getWidth() = " + bmp.getWidth()
                                            + " rotaBitmap.getHeight() = " + bmp.getHeight());
//                            rectBitmap = Bitmap.createBitmap(bmp, x, y, DST_RECT_WIDTH, DST_RECT_HEIGHT);
                                    Log.d("time4", "---------time4---------" + System.currentTimeMillis());
                                    if(cameraType == 0) {
                                        bmp = ImageUtils.getRotateBitmap(bmp, ImageUtils.getRotateDegree(jpegName));
                                    }else {
                                        bmp = ImageUtils.getRotateBitmap(bmp, ImageUtils.getRotateDegree(jpegName)+180);
                                    }
                                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                    stream.close();
                                    bmp.recycle();
                                    bmp = null;
                                }

                                Log.d("time5", "---------time5---------" + System.currentTimeMillis());

                                bos.flush();
                                bos.close();
                                Log.i(TAG, "saveBitmap成功");
                                saveSuccess = true;
                                long end = System.currentTimeMillis();
                                Log.d(TAG, "-----------------------time=" + (end - start));
                                imagePathCallBack.returnImagePath(jpegName);
                            } catch (IOException e) {
                                Log.i(TAG, "saveBitmap:失败");
                                e.printStackTrace();
                                //再次进入预览
//                                mCamera.startPreview();
                                isPreviewing = true;

                                openCameraFailCallback();
                            }

                            //再次进入预览
//                            mCamera.startPreview();

                            if (bmp != null) {
                                if (bmp.isRecycled()) {
                                    bmp.recycle();
                                    bmp = null;
                                }
                            }
                            if (rectBitmap != null) {
                                if (rectBitmap.isRecycled()) {
                                    rectBitmap.recycle();
                                    rectBitmap = null;
                                }
                            }
                        }
                    }).start();

                }


            });
        }
    }


    public Point doGetPrictureSize() {
        Size s = mCamera.getParameters().getPictureSize();
        return new Point(s.width, s.height);
    }

    private void initCamera() {

        try {
            mParams = mCamera.getParameters();
            mParams.setPictureFormat(ImageFormat.JPEG);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
            } else {
                mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
            if (adapterSize == null) {
                setUpPicSize(mParams);
                setUpPreviewSize(mParams);
            }
            if (adapterSize != null) {
                mParams.setPictureSize(adapterSize.width, adapterSize.height);
            }
            if (previewSize != null) {
                mParams.setPreviewSize(previewSize.width, previewSize.height);
            }

//            if (isFlashOn) {
//                mParams.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//            } else {
//                mParams.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//            }

            mCamera.setParameters(mParams);
            setDispaly(mParams, mCamera);

            //解决Camra 100问题
            mCamera.setErrorCallback(new Camera.ErrorCallback() {
                @Override
                public void onError(int error, Camera camera) {
                    Log.e(TAG, "error = " + error);
                    openCameraFailCallback();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "camera init failed");
            //有些相机setParameters failed    设置系统相机默认的
            if (e instanceof RuntimeException) {
                setDispaly(mCamera.getParameters(), mCamera);
            } else {
                openCameraFailCallback();
            }
        }


        mCamera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                Log.d(TAG, "---------dismissDialog---------");
                imagePathCallBack.dismissDialog();
                mCamera.setPreviewCallback(null);
            }
        });

        mCamera.startPreview();
        mCamera.cancelAutoFocus();
        isPreviewing = true;
    }


    //控制图像的正确显示方向
    private void setDispaly(Camera.Parameters parameters, Camera camera) {
        if (Build.VERSION.SDK_INT >= 8) {
            setDisplayOrientation(camera, 90);
        } else {
            parameters.setRotation(90);
        }
    }

    //实现的图像的正确显示
    private void setDisplayOrientation(Camera camera, int i) {
        Method downPolymorphic;
        try {
            downPolymorphic = camera.getClass().getMethod("setDisplayOrientation",
                    new Class[]{int.class});
            if (downPolymorphic != null) {
                downPolymorphic.invoke(camera, new Object[]{i});
            }
        } catch (Exception e) {
            Log.e("Came_e", "图像出错");
        }
    }

    /**
     * 打开摄像头失败回调
     */
    private void openCameraFailCallback() {
        Log.d(TAG, "openCameraFailCallback: -------------------openCameraFailCallback---------");
        // 复位
//        ZA_usbled za_usbled = new ZA_usbled();
//        za_usbled.USB_Hub1_rst();
//        za_usbled.USB_Hub2_rst();

        imagePathCallBack.resetCamera();

//        try {
//            mCamera.startPreview();
//        } catch (Throwable e) {
//
//        }
    }

    public void resstCamera(CamOpenOverCallback camOpenOverCallback) {
        doStopCamera();
        doOpenCamera(camOpenOverCallback);
    }

    private void setUpPicSize(Camera.Parameters parameters) {

//		if (adapterSize != null) {
//			return;
//		} else {
        adapterSize = findBestPictureResolution();
//			return;
//		}
    }

    private void setUpPreviewSize(Camera.Parameters parameters) {

//		if (previewSize != null) {
//			return;
//		} else {
        previewSize = findBestPreviewResolution();
//		}
    }

    /**
     * 找出最适合的预览界面分辨率
     *
     * @return
     */
    private Size findBestPreviewResolution() {
        Camera.Parameters cameraParameters = mCamera.getParameters();
        Size defaultPreviewResolution = cameraParameters.getPreviewSize();

        List<Size> rawSupportedSizes = cameraParameters.getSupportedPreviewSizes();
        if (rawSupportedSizes == null) {
            return defaultPreviewResolution;
        }

        // 按照分辨率从大到小排序
        List<Size> supportedPreviewResolutions = new ArrayList<Size>(rawSupportedSizes);
        Collections.sort(supportedPreviewResolutions, new Comparator<Size>() {
            @Override
            public int compare(Size a, Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {
                    return -1;
                }
                if (bPixels > aPixels) {
                    return 1;
                }
                return 0;
            }
        });

        StringBuilder previewResolutionSb = new StringBuilder();
        for (Size supportedPreviewResolution : supportedPreviewResolutions) {
            previewResolutionSb.append(supportedPreviewResolution.width).append('x').append(supportedPreviewResolution.height)
                    .append(' ');
        }
        Log.v(TAG, "Supported preview resolutions: " + previewResolutionSb);


        // 移除不符合条件的分辨率
        double screenAspectRatio = (double) ScreenUtils.getScreenWidth(mContext)
                / (double) ScreenUtils.getScreenHeight(mContext);
        Iterator<Size> it = supportedPreviewResolutions.iterator();
        while (it.hasNext()) {
            Size supportedPreviewResolution = it.next();
            int width = supportedPreviewResolution.width;
            int height = supportedPreviewResolution.height;

            // 移除低于下限的分辨率，尽可能取高分辨率
            if (width * height < MIN_PREVIEW_PIXELS) {
                it.remove();
                continue;
            }

            // 在camera分辨率与屏幕分辨率宽高比不相等的情况下，找出差距最小的一组分辨率
            // 由于camera的分辨率是width>height，我们设置的portrait模式中，width<height
            // 因此这里要先交换然preview宽高比后在比较
            boolean isCandidatePortrait = width > height;
            int maybeFlippedWidth = isCandidatePortrait ? height : width;
            int maybeFlippedHeight = isCandidatePortrait ? width : height;
            double aspectRatio = (double) maybeFlippedWidth / (double) maybeFlippedHeight;
            double distortion = Math.abs(aspectRatio - screenAspectRatio);
            double aspect = ScreenUtils.getScreenMetrics(mContext).x / ScreenUtils.getScreenMetrics(mContext).y;
            if (distortion > MAX_ASPECT_DISTORTION) {
//			if (distortion > aspect) {
                it.remove();
                continue;
            }

            // 找到与屏幕分辨率完全匹配的预览界面分辨率直接返回
            if (width == ScreenUtils.getScreenWidth(mContext)
                    && height == ScreenUtils.getScreenHeight(mContext)) {
                return supportedPreviewResolution;
            }
        }

//         如果没有找到合适的，并且还有候选的像素，则设置其中最大比例的，对于配置比较低的机器不太合适
        if (!supportedPreviewResolutions.isEmpty()) {
            Size largestPreview = supportedPreviewResolutions.get(0);
            return largestPreview;
        }

        // 没有找到合适的，就返回默认的

        return defaultPreviewResolution;
    }

    private Size findBestPictureResolution() {
        Camera.Parameters cameraParameters = mCamera.getParameters();
        List<Size> supportedPicResolutions = cameraParameters.getSupportedPictureSizes(); // 至少会返回一个值

        StringBuilder picResolutionSb = new StringBuilder();
        for (Size supportedPicResolution : supportedPicResolutions) {
            picResolutionSb.append(supportedPicResolution.width).append('x')
                    .append(supportedPicResolution.height).append(" ");
        }
        Log.d(TAG, "Supported picture resolutions: " + picResolutionSb);

        Size defaultPictureResolution = cameraParameters.getPictureSize();
        Log.d(TAG, "default picture resolution " + defaultPictureResolution.width + "x"
                + defaultPictureResolution.height);

        // 排序
        List<Size> sortedSupportedPicResolutions = new ArrayList<Size>(
                supportedPicResolutions);
        Collections.sort(sortedSupportedPicResolutions, new Comparator<Size>() {
            @Override
            public int compare(Size a, Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {
                    return -1;
                }
                if (bPixels > aPixels) {
                    return 1;
                }
                return 0;
            }
        });

        // 移除不符合条件的分辨率
        double screenAspectRatio = (double) ScreenUtils.getScreenWidth(mContext)
                / (double) ScreenUtils.getScreenHeight(mContext);
        Iterator<Size> it = sortedSupportedPicResolutions.iterator();
        while (it.hasNext()) {
            Size supportedPreviewResolution = it.next();
            int width = supportedPreviewResolution.width;
            int height = supportedPreviewResolution.height;

            // 在camera分辨率与屏幕分辨率宽高比不相等的情况下，找出差距最小的一组分辨率
            // 由于camera的分辨率是width>height，我们设置的portrait模式中，width<height
            // 因此这里要先交换然后在比较宽高比
            boolean isCandidatePortrait = width > height;
            int maybeFlippedWidth = isCandidatePortrait ? height : width;
            int maybeFlippedHeight = isCandidatePortrait ? width : height;
            double aspectRatio = (double) maybeFlippedWidth / (double) maybeFlippedHeight;
            double distortion = Math.abs(aspectRatio - screenAspectRatio);
            double aspect = ScreenUtils.getScreenMetrics(mContext).x / ScreenUtils.getScreenMetrics(mContext).y;
            if (distortion > MAX_ASPECT_DISTORTION) {
//			if (distortion > aspect) {
                it.remove();
                continue;
//            }
            }

        }

//         如果没有找到合适的，并且还有候选的像素，对于照片，则取其中最大比例的，而不是选择与屏幕分辨率相同的
        if (!sortedSupportedPicResolutions.isEmpty()) {
            return sortedSupportedPicResolutions.get(0);
        }

        // 没有找到合适的，就返回默认的
        return defaultPictureResolution;
    }


    /*为了实现拍照的快门声音及拍照保存照片需要下面三个回调变量*/
    ShutterCallback mShutterCallback = new ShutterCallback()
            //快门按下的回调，在这里我们可以设置类似播放“咔嚓”声之类的操作。默认的就是咔嚓。
    {
        public void onShutter() {
            // TODO Auto-generated method stub
            Log.i(TAG, "myShutterCallback:onShutter...");
        }
    };
    PictureCallback mRawCallback = new PictureCallback()
            // 拍摄的未压缩原数据的回调,可以为null
    {

        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Log.i(TAG, "myRawCallback:onPictureTaken...");

        }
    };
    /**
     * 常规拍照
     */
    PictureCallback mJpegPictureCallback = new PictureCallback()
            //对jpeg图像数据的回调,最重要的一个回调
    {
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            Log.i(TAG, "myJpegCallback:onPictureTaken...");
            Bitmap b = null;
            if (null != data) {
                b = BitmapFactory.decodeByteArray(data, 0, data.length);//data是字节数据，将其解析成位图
                mCamera.stopPreview();
                isPreviewing = false;
            }
            //保存图片到sdcard
            if (null != b) {
                //设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。
                //图片竟然不能旋转了，故这里要旋转下
//                Bitmap rotaBitmap = ImageUtils.getRotateBitmap(b, 90.0f);
                saveBitmap(b);
            }
            //再次进入预览
            mCamera.startPreview();
//            isPreviewing = true;
        }
    };

    /**
     * 保存Bitmap到sdcard
     *
     * @param b
     */
    public void saveBitmap(Bitmap b) {

//		String path = initPath();
        String path = Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/" + localTempImgFileName;
        long dataTake = System.currentTimeMillis();
        String jpegName = path + "/" + dataTake + ".jpg";
        Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
        try {
            File file = new File(jpegName);
            if (!file.getParentFile().exists()) {
                file.mkdirs();

            }
            file.createNewFile();

            FileOutputStream fout = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
//            b = ImageUtils.compressByQuality(b,1024*400,true);
            b.compress(Bitmap.CompressFormat.JPEG, 50, bos);
//            compressBitmap(jpegName).compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            Log.i(TAG, "saveBitmap成功");
            imagePathCallBack.returnImagePath(jpegName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.i(TAG, "saveBitmap:失败");
            e.printStackTrace();
        }

    }


    public interface ImagePathCallBack {
        // 跳转到预览界面
        public void returnImagePath(String imagePath);

        // 修改进度条
        public void changeProgressBar();

        // 隐藏进度条
        public void dismissDialog();

        // 错误100  重启相机
        public void resetCamera();
    }

    public void setSaveSuccess(boolean saveSuccess) {
        this.saveSuccess = saveSuccess;
    }
}
