package com.tjsoft.webhall.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


/**
 * Created by Administrator on 2017/2/10.
 */
public abstract class BaseAlertDialog {
    protected AlertDialog mDialog;
    protected Window mWindow;
    protected Context mContext;

    public BaseAlertDialog(Context context) {
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
        p.height = (int) (d.getHeight() * 0.8); // 改变的是dialog框在屏幕中的位置而不是大小
//        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65
        mWindow.setAttributes(p);
//        mWindow.setBackgroundDrawableResource(R.drawable.transpant);
        mWindow.setContentView(getLayoutId());
        initView();
        initListener();
    }

//    private void startAnimator() {
//        final View container = mWindow.findViewById(R.id.container);
//        if (container==null){
//            return;
//        }
//        ValueAnimator animator = ValueAnimator.ofFloat(0.5f,1f);
//        animator.setInterpolator(new OvershootInterpolator());
//        animator.setDuration(500);
//        animator.start();
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float animatedValue = (Float) animation.getAnimatedValue();
//                container.setScaleX(animatedValue);
//                container.setScaleY(animatedValue);
//            }
//        });
//    }

    public void setCancelable(boolean isCancelable){
        mDialog.setCancelable(isCancelable);
    }

    public void setCanceledOnTouchOutside(boolean isCancelable){
        mDialog.setCanceledOnTouchOutside(isCancelable);
    }

    public void dismiss(){
        if (mDialog!=null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    protected View findViewById(int id){
       return mWindow.findViewById(id);
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initListener();
}
