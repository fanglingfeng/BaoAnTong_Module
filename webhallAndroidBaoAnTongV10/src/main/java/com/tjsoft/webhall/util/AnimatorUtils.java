package com.tjsoft.webhall.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * Created by Dino on 2017/8/24.
 */

public class AnimatorUtils {

    public static void showView(View view) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            int cx = (view.getLeft() + view.getRight()) / 2;
            int cy = (view.getTop() + view.getBottom()) / 2;
            // get the final radius for the clipping circle
            int finalRadius = Math.max(view.getWidth(), view.getHeight());
            // create the animator for this view (the start radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
            anim.setDuration(2000);
            // make the view visible and start the animation
            view.setVisibility(View.VISIBLE);
            anim.start();
        }else{
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void hideView(final View view, final int f) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            // previously visible view
            // get the center for the clipping circle
            int cx = (view.getLeft() + view.getRight()) / 2;
            int cy = (view.getTop() + view.getBottom()) / 2;
            // get the initial radius for the clipping circle
            int initialRadius = view.getWidth();
            // create the animation (the final radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);
            anim.setDuration(2000);
            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(f);
                }
            });
            // start the animation
            anim.start();
        }else{
            view.setVisibility(f);
        }
    }
}
