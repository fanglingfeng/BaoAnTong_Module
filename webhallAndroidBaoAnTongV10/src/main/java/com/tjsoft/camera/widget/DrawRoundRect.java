package com.tjsoft.camera.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import org.jetbrains.annotations.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Dino on 2017/12/26.
 */

public class DrawRoundRect extends View {
    private boolean isSelect = false;
    public DrawRoundRect(Context context) {
        super(context);
    }

    public DrawRoundRect(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawRoundRect(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int screenWidth =canvas.getWidth();
        int screenHeight =canvas.getHeight();
        int gap = screenHeight*3/8;
        int round = 10;
        Paint paint = new Paint();
        if(isSelect) {
            paint.setColor(Color.parseColor("#1ABC9E"));
        }else{
            paint.setColor(Color.parseColor("#FFFFFF"));
        }
        canvas.drawRoundRect(new RectF(0,0,screenWidth,screenHeight-gap),round,round,paint);
        if(isSelect){
            paint.setStrokeWidth(10);
            paint.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawPoint(screenWidth/2,screenHeight-10,paint);
        }
    }

    public void setSelect(boolean isSelect){
        this.isSelect = isSelect;
        invalidate();
    }
}
