package com.tjsoft.webhall.util;

import android.content.res.Resources;

/**
 * Created by bruce on 14-11-6.
 */
public class DpUtils {
    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp){
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}
