package com.tjsoft.util;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 显示键盘d工具类
 */
public class InputMethodUtils {
    /** 显示软键盘 */
    public static void showInputMethod(View view) {
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext()
                                                                   .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            //showSoftInput无效原因：
            // http://stackoverflow.com/questions/5520085/android-show-softkeyboard-with-showsoftinput-is-not-working
            //imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /** 显示软键盘 */
    public static void showInputMethod(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                                                                   .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideInputMethodDelay(final View view, long delayMillis) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideInputMethod(view);
            }
        }, delayMillis);
    }

    /** 多少时间后显示软键盘 */
    public static void showInputMethodDelay(final View view, long delayMillis) {
        if (view instanceof EditText) {
            ((EditText) view).requestFocus();
        }
        // 显示输入法
        view.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodUtils.showInputMethod(view);
            }
        }, delayMillis);
    }
}
