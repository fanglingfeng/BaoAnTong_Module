package com.tjsoft.webhall.ui.xkzkj;

import android.content.Context;

import com.bean.BaseResponseReturnValue;
import com.tjsoft.webhall.entity.ApplyBean;
import com.tjsoft.webhall.entity.ChakanResponsebean;
import com.tjsoft.webhall.entity.XKZKJBean;
import com.tjsoft.webhall.entity.ZZDATABean;

import net.liang.appbaselibrary.base.mvp.MvpPresenter;
import net.liang.appbaselibrary.base.mvp.MvpView;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by lenovo on 2017/5/2.
 */

public interface XKZKJContract {
    interface View extends MvpView {

        void showDZZZKList(List<XKZKJBean> value);
        void showDZZZKList(List<XKZKJBean> value,int pageno);
        void showProgressDialog(String msg);

        /**
         * 隐藏进度条
         */
        void dismissProgressDialog();
        void showUrl(String oneoffurl);

        void showChakan(ChakanResponsebean returnValue, ApplyBean applyBean, int position, String authcode);

        void showSetPermissionDialog(String permission);

        void showPreviewLayout(String absolutePath, ApplyBean applyBean);

        void refreshList();

    }

    interface Presenter extends MvpPresenter {
        Observable<BaseResponseReturnValue<List<XKZKJBean>>> getDZZZKList(String token, String usercode, Context context, String search,int pageno);
        void getDZZZKList_v2(String token, String usercode, Context context, String search,int pageno);
        void downloadUrl(ChakanResponsebean chakanResponsebean, ApplyBean applyBean, int position, String filename);
        void getUrl(String token, ZZDATABean zzdataBean, Context context);
        void chakan(String token, String AUTHCODE, String filename, Context context, ApplyBean applyBean, int position);
    }
}
