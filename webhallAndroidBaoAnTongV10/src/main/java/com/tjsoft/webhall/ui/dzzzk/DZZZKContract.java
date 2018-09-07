package com.tjsoft.webhall.ui.dzzzk;

import android.content.Context;

import com.tjsoft.webhall.entity.ApplyBean;
import com.tjsoft.webhall.entity.ChakanResponsebean;
import com.tjsoft.webhall.entity.ZZDATABean;

import net.liang.appbaselibrary.base.mvp.MvpPresenter;
import net.liang.appbaselibrary.base.mvp.MvpView;

import java.util.List;

/**
 * Created by lenovo on 2017/5/2.
 */

public interface DZZZKContract {
    interface View extends MvpView {

        void showDZZZKList(List<ZZDATABean> value);
        void showProgressDialog(String msg);

        /**
         * 隐藏进度条
         */
        void dismissProgressDialog();
        void showUrl(String oneoffurl);

        void showChakan(ChakanResponsebean returnValue,ApplyBean applyBean,int position,String authcode);

        void showSetPermissionDialog(String permission);

        void showPreviewLayout(String absolutePath,ApplyBean applyBean);

        void refreshList();

    }

    interface Presenter extends MvpPresenter {
        void getDZZZKList(String token, String ZJHM, String SXBM, String SXMC, Context context);
        void downloadUrl(ChakanResponsebean chakanResponsebean,ApplyBean applyBean,int position,String filename);
        void getUrl(String token, ZZDATABean zzdataBean, Context context);
        void chakan(String token, String AUTHCODE,String filename, Context context, ApplyBean applyBean,int position);
    }
}
