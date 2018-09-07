package com.tjsoft.camera;


import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;



public class MQCameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	private static final String TAG = MQCameraSurfaceView.class.getSimpleName();
	MQCameraInterfaceUtil mCameraInterface;
	Context mContext;
	SurfaceHolder mSurfaceHolder;
	public MQCameraSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		mSurfaceHolder = getHolder();
		mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);//translucent��͸�� transparent͸��
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceHolder.addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.i(TAG, "surfaceCreated...");
		try {
			ThreadPoolManager.getInstance().addAsyncTask(new Runnable() {
				@Override
				public void run() {
//					if (CommonConstant.CAMERA_TYPE == 0) {
						MQCameraInterfaceUtil.getInstance(mContext).doOpenCamera((TakePhotoActivity) mContext);
//					}
//					else {
//						MQCameraInterfaceUtil.getInstance(mContext).doOpenA4Camera();
//					}
				}
			});
		} catch (Exception e) {
			Log.i(TAG,"e:"+e.getMessage());
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
		// TODO Auto-generated method stub
		Log.i(TAG, "surfaceChanged...");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.i(TAG, "surfaceDestroyed...");
		MQCameraInterfaceUtil.getInstance(mContext).doStopCamera();
	}
	public SurfaceHolder getSurfaceHolder(){
		return mSurfaceHolder;
	}
	
}
