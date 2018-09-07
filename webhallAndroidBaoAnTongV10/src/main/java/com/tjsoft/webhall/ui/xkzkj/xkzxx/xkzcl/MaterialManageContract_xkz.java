package com.tjsoft.webhall.ui.xkzkj.xkzxx.xkzcl;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.tjsoft.webhall.entity.ATTBean;
import com.tjsoft.webhall.entity.CertInfoBean;

import net.liang.appbaselibrary.base.mvp.MvpPresenter;
import net.liang.appbaselibrary.base.mvp.MvpView;

import java.util.List;

/**
 * Created by xieguangwei on 2017/1/23.
 * 多附件管理界面presenter、view接口存放
 */

public interface MaterialManageContract_xkz {
    interface View extends MvpView {

        //刷新页面
        void reView();

        //是否编辑状态
        void showIsEdit(boolean isEdit);

        //是否显示toolbar右侧按钮
        void showToolBarTv(boolean isShow);

        void showPageNo(int num, int total);

        void showIsNext(boolean isAble);

        void showIsPrevious(boolean isAble);

        void showMaterialName(String name);

        void showIsDelete(ATTBean attBean);

        void removeListAtt(ATTBean attBean);

        void dismissDialog();

        void attsListNotify();

        void dissMissEdit(boolean isMissEdit);

        void isEnableAutoDelete(boolean isAble);

        void showAutoDelete(boolean isShow);

        void showOneCertInfo(CertInfoBean returnValue);
    }

    interface Presenter extends MvpPresenter {

        void loadData(Activity activity);

        boolean isEdit();

        void setEdit(boolean isAble);

        void onNext(Activity activity);

        void onPrevious(Activity activity);

        void uploadFile(ATTBean attBean);

        ATTBean getAtt(int position);

        String getAttName(String path);

        void addAtt(String path);

        List getAtts();

        //void Delete(ATTBean attBean);

        void delete(ATTBean attBean);

        boolean getIsHideEditView();

        boolean isAllUpload();

        boolean isHasUpload();

        void setAttsEdit(boolean Edit);

        //判断是否是当前材料页面
        boolean isManagerAtt(int managerPosition);

        void autoDelete();

        void getOneCertInfo(String token, String inc_zzjgdm, String certcode, Context activity);
    }
}
