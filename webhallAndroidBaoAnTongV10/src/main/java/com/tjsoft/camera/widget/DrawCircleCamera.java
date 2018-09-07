package com.tjsoft.camera.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.tjsoft.msfw.guangdongshenzhenbaoan.R;

import org.jetbrains.annotations.Nullable;

/**
 * Created by Dino on 2017/12/26.
 */

public class DrawCircleCamera extends View {
    public DrawCircleCamera(Context context) {
        super(context);
    }

    public DrawCircleCamera(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawCircleCamera(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int screenWidth =canvas.getWidth();
        int screenHeight =canvas.getHeight();
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#19BD9C"));
        canvas.drawCircle(screenWidth/2,screenHeight/2,screenWidth/2,paint);
        Bitmap camera = BitmapFactory.decodeResource(getResources(), R.drawable.camera);
        canvas.drawBitmap(camera,screenWidth/2-camera.getWidth()/2,screenHeight/2-camera.getHeight()/2,paint);
    }
}
