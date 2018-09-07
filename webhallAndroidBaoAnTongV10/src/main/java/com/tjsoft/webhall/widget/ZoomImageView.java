package com.tjsoft.webhall.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by Dino on 2018/1/11.
 */

public class ZoomImageView extends View {
    private final Path mPath = new Path();
    private final Matrix matrix = new Matrix();
    private Bitmap bitmap;
    // 放大镜的半径

    private static final int RADIUS = 80;
    // 放大倍数

    private static final int FACTOR = 2;
    private int mCurrentX, mCurrentY;

    public ZoomImageView(Context context) {
        super(context);
        mPath.addCircle(RADIUS, RADIUS, RADIUS, Path.Direction.CW);
        matrix.setScale(FACTOR, FACTOR);
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPath.addCircle(RADIUS, RADIUS, RADIUS, Path.Direction.CW);
        matrix.setScale(FACTOR, FACTOR);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        mCurrentX = (int) event.getX();
//        mCurrentY = (int) event.getY();
//
//        invalidate();
//        return true;
//    }

    public void setBitmap(Bitmap bitmap,int mCurrentX,int mCurrentY){
        this.bitmap = bitmap;
        if(bitmap == null){
            setVisibility(INVISIBLE);
            return;
        }else{
            setVisibility(VISIBLE);
        }
        this.mCurrentX = mCurrentX;
        this.mCurrentY = mCurrentY;
        invalidate();
    }
    public void setXY(int mCurrentX,int mCurrentY){
        if(bitmap == null){
            setVisibility(INVISIBLE);
            return;
        }else{
            setVisibility(VISIBLE);
        }
        this.mCurrentX = mCurrentX;
        this.mCurrentY = mCurrentY;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 底图

        if(bitmap == null){
            setVisibility(INVISIBLE);
            return;
        }else{
            setVisibility(VISIBLE);
        }
        canvas.drawBitmap(bitmap, 0, 0, null);
        // 剪切

        canvas.translate(mCurrentX - RADIUS, mCurrentY - RADIUS);
        canvas.clipPath(mPath);
        // 画放大后的图

        canvas.translate(RADIUS - mCurrentX * FACTOR, RADIUS - mCurrentY
                * FACTOR);
        canvas.drawBitmap(bitmap, matrix, null);
    }
}
