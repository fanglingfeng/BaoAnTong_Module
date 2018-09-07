package com.tjsoft.camera;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;


/**
 * 中间透明预览框
 */
public class MaskView extends ImageView {
	private static final String TAG = MaskView.class.getSimpleName();
	private Paint mLinePaint;
	private Paint mAreaPaint;
	private Paint mCenterAreaPaint;
	private Rect mCenterRect = null;
	private Context mContext;


	public MaskView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initPaint();
		mContext = context;
		Point p	= ScreenUtils.getScreenMetrics(mContext);
		widthScreen = p.x;
		heightScreen = p.y;
	}

	private void initPaint(){
		//绘制中间透明区域矩形边界的Paint
		mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mLinePaint.setColor(Color.BLUE);
		mLinePaint.setStyle(Style.STROKE);
		mLinePaint.setStrokeWidth(5f);
		mLinePaint.setAlpha(30);

		//绘制四周阴影区域
		mAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mAreaPaint.setColor(Color.BLACK);
		mAreaPaint.setStyle(Style.FILL);
		mAreaPaint.setAlpha(255);

		// 中间区域paint
		mCenterAreaPaint = new Paint();
		mAreaPaint.setColor(Color.BLACK);
		mCenterAreaPaint.setAntiAlias(true);
		mCenterAreaPaint.setStyle(Style.FILL);
		mCenterAreaPaint.setAlpha(0);

	}
	public void setCenterRect(Rect r){
		Log.i(TAG, "setCenterRect...");
		this.mCenterRect = r;
		postInvalidate();
	}
	public void clearCenterRect(Rect r){
		this.mCenterRect = null;
	}

	int widthScreen, heightScreen;
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
//		Log.i(TAG, "start-onDraw...");
		if(mCenterRect == null)
			return;
		//绘制四周阴影区域
		canvas.drawRect(0, 0, widthScreen, mCenterRect.top, mAreaPaint);
		canvas.drawRect(0, mCenterRect.bottom + 1, widthScreen, heightScreen, mAreaPaint);
		canvas.drawRect(0, mCenterRect.top, mCenterRect.left - 1, mCenterRect.bottom  + 1, mAreaPaint);
		canvas.drawRect(mCenterRect.right + 1, mCenterRect.top, widthScreen, mCenterRect.bottom + 1, mAreaPaint);

		//绘制目标透明区域
		canvas.drawRect(mCenterRect, mCenterAreaPaint);
		super.onDraw(canvas);
//		Log.i(TAG, "stop-onDraw...");
	}



}
