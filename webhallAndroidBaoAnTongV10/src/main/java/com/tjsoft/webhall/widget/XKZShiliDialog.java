package com.tjsoft.webhall.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.webhall.ui.xkzkj.xkzxx.ShiliActivity;


/**
 * Created by gersy on 2017/6/21.
 */

public class XKZShiliDialog {

    public Confirm confirm;
    TextView tvYangli1;
    TextView tvYangli2;
    protected AlertDialog mDialog;
    protected Window mWindow;
    protected Context mContext;

    public XKZShiliDialog(Context context, String name, String pid, Confirm confirm) {
        this.confirm = confirm;
        mContext = context;
        mDialog = new AlertDialog.Builder(context).create();
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog .setView(((Activity) mContext).getLayoutInflater().inflate(getLayoutId(), null));
        mDialog.show();
        mWindow = mDialog.getWindow();
        WindowManager m = ((Activity)mContext).getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = mWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.4); // 改变的是dialog框在屏幕中的位置而不是大小
//        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65
        mWindow.setAttributes(p);
//        mWindow.setBackgroundDrawableResource(R.drawable.transpant);
        mWindow.setContentView(getLayoutId());
        initView();
        initListener();

    }

    protected int getLayoutId() {
        return R.layout.dialog_xkz_shili;
    }

    protected void initView() {
        tvYangli1 = (TextView) mWindow.findViewById(R.id.tv_shili1);
        tvYangli2 = (TextView) mWindow.findViewById(R.id.tv_shili2);


    }

    public void setContent(String content) {

    }

    protected void initListener() {
        tvYangli1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShiliActivity.class);
                intent.putExtra("shili",1);
                mContext.startActivity(intent);


            }
        });
        tvYangli2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShiliActivity.class);
                intent.putExtra("shili",2);
                mContext.startActivity(intent);
            }
        });



    }

    public interface Confirm {
        void onConfirm();
    }
}
