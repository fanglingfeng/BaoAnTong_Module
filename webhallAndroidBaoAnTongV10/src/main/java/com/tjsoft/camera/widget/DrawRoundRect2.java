package com.tjsoft.camera.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import org.jetbrains.annotations.Nullable;

/**
 * Created by Dino on 2017/12/26.
 */

public class DrawRoundRect2 extends View {
    private boolean isSelect = false;
    public DrawRoundRect2(Context context) {
        super(context);
    }

    public DrawRoundRect2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawRoundRect2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int screenWidth = canvas.getWidth();
        int screenHeight = canvas.getHeight();
        int gap = screenHeight * 1/ 8;
        int gap2 = screenHeight * 3 / 8;
        int round = 10;
        Paint paint = new Paint();
        if (isSelect) {
            paint.setColor(Color.parseColor("#1ABC9E"));
        } else {
            paint.setColor(Color.parseColor("#FFFFFF"));
        }
        canvas.drawRoundRect(new RectF(gap, 0, screenWidth, screenHeight - gap - gap2), round, round, paint);
        paint.setStrokeWidth(3);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(3, gap, 3, screenHeight - gap2 - 3, paint);
        canvas.drawLine(3, screenHeight - gap2 - 3, screenWidth - gap, screenHeight - gap2 - 3, paint);

        if (isSelect) {
            paint.setStrokeWidth(10);
            canvas.drawPoint(screenWidth / 2, screenHeight - 10, paint);
        }
    }

    public void setSelect(boolean isSelect){
        this.isSelect = isSelect;
        invalidate();
    }
}
