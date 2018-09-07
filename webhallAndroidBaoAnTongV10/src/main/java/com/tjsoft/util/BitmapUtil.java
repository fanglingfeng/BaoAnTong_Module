package com.tjsoft.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class BitmapUtil {
    /**
     * 将两张位图拼接成一张(横向拼接)
     *
     * @param first
     * @param second
     * @return
     */
    public static Bitmap add2Bitmap(Bitmap first, Bitmap second) {
        int width = first.getWidth() + second.getWidth();
        int height = Math.max(first.getHeight(), second.getHeight());
        Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        canvas.drawBitmap(second, first.getWidth(), 0, null);
        return result;
    }

    /**
     * 将两张位图拼接成一张(横向拼接)
     *
     * @param first
     * @param second
     * @return
     */
    public static Bitmap addBitmap(Bitmap first, Bitmap second) {
//        int width = Math.max(first.getWidth(), second.getWidth());
        int width = second.getWidth();
        int height;
        if (first.getWidth() == second.getWidth()) {
            height = first.getHeight() + second.getHeight();
            Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            canvas.drawBitmap(first, 0, 0, null);

            canvas.drawBitmap(second, 0, first.getHeight(), null);
            return result;

        } else {
            first = scaleBitmap(first,second.getWidth()/first.getWidth());
            Bitmap middleresult = Bitmap.createBitmap(first.getWidth(),first.getHeight()+second.getHeight(),Config.ARGB_8888);
            Canvas canvas = new Canvas(middleresult);

            canvas.drawBitmap(first, 0, 0, null);

            canvas.drawBitmap(second, 0, first.getHeight(), null);
            float i = second.getHeight()*2f/(first.getHeight()+second.getHeight());

            Bitmap result = scaleBitmap(middleresult,i);





//            height = first.getHeight() + second.getHeight() / 2;
//            Bitmap result = Bitmap.createBitmap(width * 4 / 3, second.getHeight()*2, Config.ARGB_8888);
//            Canvas canvas = new Canvas(result);
//            canvas.drawBitmap(scaleBitmap(first, 1f * 4 / 3), 0, 0, null);
//
//            canvas.drawBitmap(scaleBitmap(second, 0.5f * 4 / 3), 0, first.getHeight()*4/3, null);
            return result;
        }

//		float time = 2/1.5f;

    }

    private static Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }
}
