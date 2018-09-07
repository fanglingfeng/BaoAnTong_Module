package com.tjsoft.webhall.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

import com.tjsoft.msfw.guangdongshenzhenbaoan.R;


/**
 * Created by gersy on 2017/6/21.
 */

public class ShiliDialog extends BaseAlertDialog {


    public ShiliDialog(Context context) {
        super(context);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_shili;
    }

    @Override
    protected void initView() {

    }

    public void setContent(String content){

    }

    @Override
    protected void initListener() {

    }
}
