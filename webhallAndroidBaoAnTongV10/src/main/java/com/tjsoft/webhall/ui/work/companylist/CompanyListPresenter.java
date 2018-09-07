package com.tjsoft.webhall.ui.work.companylist;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bean.BaseResponseReturnValue;
import com.tjsoft.data.ZhengwuRepository;
import com.tjsoft.data.local.LocalZhengwuDataSource;
import com.tjsoft.data.remote.RemoteZhengwuDataSource;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.webhall.entity.AddCompanyResponseBean;
import com.tjsoft.webhall.entity.AddCompanySendBean;
import com.tjsoft.webhall.entity.CompanyList;
import com.tjsoft.webhall.entity.GetCompanySendBean;

import net.liang.appbaselibrary.base.mvp.BasePresenter;
import net.liang.appbaselibrary.bean.ResponseCode;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by lenovo on 2017/2/22.
 */

public class CompanyListPresenter extends BasePresenter implements CompanyListContract.Presenter {
    @NonNull
    private CompanyListContract.View mView;
    @NonNull
    private ZhengwuRepository repository;

    public CompanyListPresenter(@NonNull CompanyListContract.View mView) {
        this.mView = checkNotNull(mView);
        repository = ZhengwuRepository.getInstance(RemoteZhengwuDataSource.getInstance(), LocalZhengwuDataSource.getInstance());
    }


    @Override
    public Observable<BaseResponseReturnValue<CompanyList>> getCompanyList(String token, String userId, Context context) {
        GetCompanySendBean sendBean = new GetCompanySendBean();
        sendBean.setToken(token);
        sendBean.setUSERID(userId);

        return repository.getCompanyList(sendBean, context);
    }

    @Override
    public void addCompany(AddCompanySendBean addCompanySendBean, final Context context) {
        mView.showProgressDialog("正在添加企业...");


        repository.addCompany(addCompanySendBean,context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BaseResponseReturnValue<AddCompanyResponseBean>>() {
                    @Override
                    public void onNext(BaseResponseReturnValue<AddCompanyResponseBean> value) {
                        if (value != null&&value.getCode()==200) {
                            if (value.getCode() == ResponseCode.success) {
                                DialogUtil.showToast(context,"添加企业成功！");
                                mView.dismissProgressDialog();
                                mView.addCompanySuccess();
                            } else {
                                DialogUtil.showToast(context,value.getError());
                                mView.dismissProgressDialog();

                            }
                        } else {
                            DialogUtil.showToast(context,value.getError());
                            mView.dismissProgressDialog();

                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        DialogUtil.showToast(context,"添加企业失败失败，请检查网络！");
                        mView.dismissProgressDialog();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    @Override
    public void deleteCompany(AddCompanySendBean addCompanySendBean, final Context context) {
        mView.showProgressDialog("正在删除企业...");

        repository.deleteCompany(addCompanySendBean,context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BaseResponseReturnValue<AddCompanyResponseBean>>() {
                    @Override
                    public void onNext(BaseResponseReturnValue<AddCompanyResponseBean> value) {
                        if (value != null&&value.getCode()==200) {
                            if (value.getCode() == ResponseCode.success) {
                                DialogUtil.showToast(context,"删除企业成功！");
                                mView.dismissProgressDialog();
                                mView.addCompanySuccess();
                            } else {
                                DialogUtil.showToast(context,value.getError());
                                mView.dismissProgressDialog();

                            }
                        } else {
                            DialogUtil.showToast(context,value.getError());
                            mView.dismissProgressDialog();

                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        DialogUtil.showToast(context,"删除企业失败，请检查网络！");
                        mView.dismissProgressDialog();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    @Override
    public void editCompany(AddCompanySendBean addCompanySendBean, final Context context) {
        mView.showProgressDialog("正在编辑企业信息...");

        repository.editCompany(addCompanySendBean,context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BaseResponseReturnValue<AddCompanyResponseBean>>() {
                    @Override
                    public void onNext(BaseResponseReturnValue<AddCompanyResponseBean> value) {
                        if (value != null&&value.getCode()==200) {
                            if (value.getCode() == ResponseCode.success) {
                                DialogUtil.showToast(context,value.getReturnValue().getMsg());
                                mView.dismissProgressDialog();
                                mView.addCompanySuccess();
                            } else {
                                DialogUtil.showToast(context,value.getError());
                                mView.dismissProgressDialog();

                            }
                        } else {
                            DialogUtil.showToast(context,value.getError());
                            mView.dismissProgressDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        DialogUtil.showToast(context,"删除企业失败，请检查网络！");
                        mView.dismissProgressDialog();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
