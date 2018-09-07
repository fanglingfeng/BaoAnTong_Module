package com.tjsoft.webhall.ui.xkzkj.xkzxx;

import android.content.Context;

import com.tjsoft.webhall.entity.CertInfoBean;

import net.liang.appbaselibrary.base.mvp.MvpPresenter;
import net.liang.appbaselibrary.base.mvp.MvpView;

/**
 * Created by lenovo on 2017/5/2.
 */

public interface XKZXXContract {
    interface View extends MvpView {


        void showProgressDialog(String msg);

        /**
         * 隐藏进度条
         */
        void dismissProgressDialog();

        void showOneCertInfo(CertInfoBean returnValue);

        void submitSuccess();

        void showIsEdit(boolean edit);
    }

    interface Presenter extends MvpPresenter {
        void getDZZZKList(String token, String usercode, Context context);

    }
}
