package com.tjsoft.webhall.ui.xkzkj.xkzxx.zzxx;

import android.content.Context;

import com.tjsoft.webhall.entity.ApplyBean;
import com.tjsoft.webhall.entity.CertInfoBean;
import com.tjsoft.webhall.entity.ChakanResponsebean;
import com.tjsoft.webhall.entity.UserInfoBean;
import com.tjsoft.webhall.entity.XKZKJBean;
import com.tjsoft.webhall.entity.ZZDATABean;

import net.liang.appbaselibrary.base.mvp.MvpPresenter;
import net.liang.appbaselibrary.base.mvp.MvpView;

import java.util.List;

/**
 * Created by lenovo on 2017/5/2.
 */

public interface ZZXXContract {
    interface View extends MvpView {


        void showProgressDialog(String msg);

        /**
         * 隐藏进度条
         */
        void dismissProgressDialog();

        void showOneCertInfo(CertInfoBean returnValue);

        void saveFormInfos();

        void showBaseInfo(UserInfoBean userInfoBean);
    }

    interface Presenter extends MvpPresenter {
        void getDZZZKList(String token, String usercode, Context context);

    }
}
