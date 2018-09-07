package com.tjsoft.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class TextSizeUtils {

	public static int getFontSize(Context context, int textSize) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        // screenWidth = screenWidth > screenHeight ? screenWidth :
        // screenHeight;
        int rate = (int) (textSize * (float) screenHeight / 1280);
        return rate;
    }
	
}
