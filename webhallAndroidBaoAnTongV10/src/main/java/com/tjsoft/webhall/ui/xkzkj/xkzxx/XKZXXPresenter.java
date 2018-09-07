package com.tjsoft.webhall.ui.xkzkj.xkzxx;

/*
 *  @项目名：  baoan
 *  @包名：    com.tjsoft.webhall.ui.dzzzk
 *  @文件名:   DZZZKPresenter
 *  @创建者:   lingfeng
 *  @创建时间:  2018/6/6 16:08
 *  @描述：    TODO
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;

import com.bean.BaseResponseReturnValue;
import com.tjsoft.data.DownloadRepository;
import com.tjsoft.data.ZhengwuRepository;
import com.tjsoft.data.local.LocalDownloadDataSource;
import com.tjsoft.data.local.LocalZhengwuDataSource;
import com.tjsoft.data.remote.RemoteDownloadDataSource;
import com.tjsoft.data.remote.RemoteZhengwuDataSource;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.ATTBean;
import com.tjsoft.webhall.entity.CertInfoBean;
import com.tjsoft.webhall.entity.SubmintXKZBean;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.entity.XKZKJBean;
import com.tjsoft.webhall.entity.XKZKJSendbean;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.ui.xkzkj.xkzxx.zzxx.ZZXXContract;

import net.liang.appbaselibrary.base.mvp.BasePresenter;
import net.liang.appbaselibrary.bean.ResponseCode;

import java.io.UnsupportedEncodingException;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.google.common.base.Preconditions.checkNotNull;

public class XKZXXPresenter
        extends BasePresenter
        implements XKZXXContract.Presenter {
    private int mProgress = 0;
    private ZhengwuRepository zhengwuRepository;
    public  volatile boolean isEdit;

    @NonNull
    protected XKZXXContract.View mView;
    @NonNull
    protected ZhengwuRepository repository;
    @NonNull
    private DownloadRepository downloadRepository;
    private Context context;
    private List<ATTBean> atts;// 文件集合

    public XKZXXPresenter(XKZXXContract.View mView) {
        this.mView = checkNotNull(mView);
        repository = ZhengwuRepository.getInstance(RemoteZhengwuDataSource.getInstance(),
                LocalZhengwuDataSource.getInstance());
        downloadRepository = DownloadRepository.getInstance(RemoteDownloadDataSource.getInstance(),
                LocalDownloadDataSource.getInstance());
        zhengwuRepository = new ZhengwuRepository(RemoteZhengwuDataSource.getInstance(),
                LocalZhengwuDataSource.getInstance());


    }
    public void setEdit(boolean edit) {
        isEdit = edit;
        mView.showIsEdit(edit);
    }

    @Override
    public void getDZZZKList(String token, String usercode, Context context) {
        this.context = context;
        mView.showProgressDialog("正在加载...");
        XKZKJSendbean xkzkjSendbean = new XKZKJSendbean();
        xkzkjSendbean.setToken(token);
        xkzkjSendbean.setUSERCODE(usercode);


        Disposable disposable = repository.getXKZKJList(xkzkjSendbean, context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BaseResponseReturnValue<List<XKZKJBean>>>() {
                    @Override
                    public void onNext(BaseResponseReturnValue<List<XKZKJBean>> value) {
                        if (value != null) {
                            if (value.getCode() == ResponseCode.success) {
                                if (value.getReturnValue() != null) {
                                    mView.dismissProgressDialog();

//                                                              mView.showDZZZKList(value.getReturnValue());
                                } else {

                                    mView.showToast("未获取到许可证列表！");
                                    mView.dismissProgressDialog();

                                }
                            } else {
                                mView.dismissProgressDialog();

                                mView.showToast(value.getError());
                            }
                        } else {

                            mView.showToast("未获取到许可证列表！");
                            mView.dismissProgressDialog();

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.dismissProgressDialog();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(disposable);


    }

    public void loadBaseFormData(String certcode, String status, Context context) {
//        if (TextUtils.equals("1", status)) {//已上传

            GloabDelegete gloabDelegete = Constants.getInstance()
                    .getGloabDelegete();
            TransportEntity transportEntity = gloabDelegete.getUserInfo();
            getOneCertInfo(transportEntity.getToken(), transportEntity.getINC_ZZJGDM(), certcode, context);


//        } else {
//
//
//        }


    }

    public void getOneCertInfo(String token, String usercode, String certcode, Context context) {
        this.context = context;
        mView.showProgressDialog("正在加载...");
        XKZKJSendbean xkzkjSendbean = new XKZKJSendbean();
        xkzkjSendbean.setToken(token);
        xkzkjSendbean.setUSERCODE(usercode);
        xkzkjSendbean.setCERTCODE(certcode);


        Disposable disposable = repository.getOneCertInfo(xkzkjSendbean, context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BaseResponseReturnValue<CertInfoBean>>() {
                    @Override
                    public void onNext(BaseResponseReturnValue<CertInfoBean> value) {
                        if (value != null) {
                            if (value.getCode() == ResponseCode.success) {
                                if (value.getReturnValue() != null) {
                                    mView.dismissProgressDialog();

                                    mView.showOneCertInfo(value.getReturnValue());


                                } else {

                                    mView.showToast("未获取到许可证列表！");
                                    mView.dismissProgressDialog();

                                }
                            } else {
                                mView.dismissProgressDialog();

                                mView.showToast(value.getError());
                            }
                        } else {

                            mView.showToast("未获取到许可证列表！");
                            mView.dismissProgressDialog();

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.dismissProgressDialog();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(disposable);


    }

    public void applySubmit(String usercode, String certcode, String certname, String formsXML, String materialsXML,String token,Context context) {

        this.context = context;
        mView.showProgressDialog("正在加载...");
        XKZKJSendbean xkzkjSendbean = new XKZKJSendbean();
        xkzkjSendbean.setToken(token);
        xkzkjSendbean.setUSERCODE(usercode);
        xkzkjSendbean.setCERTNAME(certname);
        xkzkjSendbean.setCERTCODE(certcode);
        try {
            xkzkjSendbean.setDATAXML(new String(Base64.encode(formsXML.getBytes(), 0), "UTF-8"));
            xkzkjSendbean.setCERTFILES(new String(Base64.encode(materialsXML.getBytes(), 0), "UTF-8"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        Disposable disposable = repository.submit(xkzkjSendbean, context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BaseResponseReturnValue<String>>() {
                    @Override
                    public void onNext(BaseResponseReturnValue<String> value) {
                        if (value != null) {
                            if (value.getCode() == ResponseCode.success) {
                                if (value.getReturnValue() != null) {
                                    mView.dismissProgressDialog();
                                    if (TextUtils.equals("0", value.getReturnValue())) {
                                        mView.showToast("保存失败！");
                                    } else {
                                        mView.submitSuccess();
                                    }


                                } else {

                                    mView.showToast("保存失败！");
                                    mView.dismissProgressDialog();

                                }
                            } else {
                                mView.dismissProgressDialog();

                                mView.showToast(value.getError());
                            }
                        } else {

                            mView.showToast("未获取到许可证列表！");
                            mView.dismissProgressDialog();

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.dismissProgressDialog();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        disposables.add(disposable);






    }

    public boolean isEdit() {
        return isEdit;
    }
}
