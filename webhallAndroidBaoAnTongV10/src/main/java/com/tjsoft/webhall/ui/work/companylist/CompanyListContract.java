package com.tjsoft.webhall.ui.work.companylist;

import android.content.Context;

import com.bean.BaseResponseReturnValue;
import com.tjsoft.webhall.entity.AddCompanySendBean;
import com.tjsoft.webhall.entity.CompanyBean;
import com.tjsoft.webhall.entity.CompanyList;

import net.liang.appbaselibrary.base.mvp.MvpPresenter;
import net.liang.appbaselibrary.base.mvp.MvpView;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by xieguangwei on 2017/2/22.
 */

public interface CompanyListContract {
    interface View extends MvpView {


        void addCompanySuccess();

        void showProgressDialog(String msg);

        void dismissProgressDialog();
    }
    interface Presenter extends MvpPresenter {

        Observable<BaseResponseReturnValue<CompanyList>> getCompanyList(String token, String userId, Context context);
        void addCompany(AddCompanySendBean addCompanySendBean, Context context);
        void deleteCompany(AddCompanySendBean addCompanySendBean, Context context);
        void editCompany(AddCompanySendBean addCompanySendBean, Context context);


    }
}
