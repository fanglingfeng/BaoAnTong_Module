package com.tjsoft.webhall.ui.xkzkj.xkzxx.zzxx;

/*
 *  @项目名：  baoan
 *  @包名：    com.tjsoft.webhall.ui.dzzzk
 *  @文件名:   DZZZKPresenter
 *  @创建者:   lingfeng
 *  @创建时间:  2018/6/6 16:08
 *  @描述：    TODO
 */

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;

import com.bean.BaseResponseReturnValue;
import com.bean.GetUploadFileBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.socks.library.KLog;
import com.tjsoft.data.DownloadRepository;
import com.tjsoft.data.ProgressResponseBody;
import com.tjsoft.data.ZhengwuRepository;
import com.tjsoft.data.local.LocalDownloadDataSource;
import com.tjsoft.data.local.LocalZhengwuDataSource;
import com.tjsoft.data.remote.RemoteDownloadDataSource;
import com.tjsoft.data.remote.RemoteZhengwuDataSource;
import com.tjsoft.util.FileUtil;
import com.tjsoft.util.XMLUtil;
import com.tjsoft.webhall.constants.BusConstant;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.constants.PermissionCode;
import com.tjsoft.webhall.entity.ATTBean;
import com.tjsoft.webhall.entity.ApplyBean;
import com.tjsoft.webhall.entity.CertInfoBean;
import com.tjsoft.webhall.entity.ChakanResponsebean;
import com.tjsoft.webhall.entity.ChakanSendbean;
import com.tjsoft.webhall.entity.DZZZKUrlSendbean;
import com.tjsoft.webhall.entity.DZZZKUrlbean;
import com.tjsoft.webhall.entity.FormItemBean;
import com.tjsoft.webhall.entity.FormItemBean_v2;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.entity.UploadStatus;
import com.tjsoft.webhall.entity.UserInfoBean;
import com.tjsoft.webhall.entity.XKZKJBean;
import com.tjsoft.webhall.entity.XKZKJSendbean;
import com.tjsoft.webhall.entity.ZZDATABean;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.ui.dzzzk.DZZZKActivity;
import com.tjsoft.webhall.ui.materialmanage.CountingRequestBody;
import com.tjsoft.webhall.ui.work.HistoreShare_v2;
import com.tjsoft.webhall.ui.xkzkj.XKZKJContract;

import net.liang.appbaselibrary.base.mvp.BasePresenter;
import net.liang.appbaselibrary.bean.ResponseCode;
import net.liang.appbaselibrary.utils.NetworkUtils;
import net.liang.appbaselibrary.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.tjsoft.webhall.constants.BusConstant.UPDATA_ATTS_STUTAS;

public class ZZXXPresenter
        extends BasePresenter
        implements ZZXXContract.Presenter {
    private int mProgress = 0;
    private ZhengwuRepository zhengwuRepository;

    @NonNull
    protected ZZXXContract.View mView;
    @NonNull
    protected ZhengwuRepository repository;
    @NonNull
    private DownloadRepository downloadRepository;
    private Context context;
    private List<ATTBean> atts;// 文件集合
    protected Gson gson = new Gson();

    public ZZXXPresenter(ZZXXContract.View mView) {
        this.mView = checkNotNull(mView);
        repository = ZhengwuRepository.getInstance(RemoteZhengwuDataSource.getInstance(),
                LocalZhengwuDataSource.getInstance());
        downloadRepository = DownloadRepository.getInstance(RemoteDownloadDataSource.getInstance(),
                LocalDownloadDataSource.getInstance());
        zhengwuRepository = new ZhengwuRepository(RemoteZhengwuDataSource.getInstance(),
                LocalZhengwuDataSource.getInstance());


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
    public Observable<String> getFormsXml(final String certcode, final Context context) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                mView.saveFormInfos();
                Thread.currentThread();
                Thread.sleep(1000);
                if (!e.isDisposed()) {
                    e.onNext(FileUtil.Load(context, Constants.user.getUSER_ID() + "_" + certcode + "XML"));
                }
            }
        });
    }
    public void androidSave(String s, String certcode,Context context) {
        KLog.e("执行js androidSave，回调android save方法");

        String formsXML = "";
        if (TextUtils.isEmpty(s) || s.length() < 10) {
            EventBus.getDefault().post(BusConstant.CHECK_FORM_WRONG);
            return;
        } else {
            EventBus.getDefault().post(BusConstant.CHECK_FORM_RIGHT);
        }
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {

            JSONObject json = new JSONObject(s);
            List<FormItemBean_v2> formItemBeans = gson.fromJson(json.getString("data"), new TypeToken<List<FormItemBean_v2>>() {
            }.getType());// 表单实例list
            serializer.setOutput(writer);
//            serializer.startDocument("utf-8", null);
            serializer.startTag("", "body");


            for (int j = 0; j < formItemBeans.size(); j++) {
                serializer.startTag("", formItemBeans.get(j).getName());

                serializer.startTag("", "value");
                if (TextUtils.isEmpty(formItemBeans.get(j).getValue())) {
                    serializer.cdsect("  ");

                } else {
                    serializer.cdsect(formItemBeans.get(j).getValue());
                }
                serializer.endTag("", "value");

//
                serializer.startTag("", "title");
                serializer.text(formItemBeans.get(j).getTitle());
                serializer.endTag("", "title");

                serializer.endTag("", formItemBeans.get(j).getName());
//                formDataCache.put(formItemBeans.get(j).getName(), formItemBeans.get(j).getValue());
            }
//            FileUtil.Write(context, AccountHelper.getUser().getUSER_ID() + "_" + PERMID, formDataCache.toString());

            serializer.endTag("", "body");
            serializer.endDocument();
//            forms.get(index).setXML(writer.toString());
//            System.out.println("fuchl  " + forms.get(index).getXML());
//            if (index == forms.size() - 1) {// 最后一张表单时将xml组装
//                for (int i = 0; i < forms.size(); i++) {
//                    formsXML += forms.get(i).getXML();
//                    formsXML = formsXML.replaceAll("<\\?xml.*.\\?>", "");
//                }
                formsXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><zznr><head/>" + writer.toString() + "</zznr>";
                FileUtil.Write(context, Constants.user.getUSER_ID() + "_" + certcode +  "XML", formsXML);
//            } else {
//                mView.loadUrlByFormId(forms.get(0).getFILEID(), false);
//            }

        } catch (Exception e) {
            e.printStackTrace();
            KLog.e("存储表单信息到本地失败，异常信息：" + e.getMessage());
//            EventBus.getDefault().post(BusConstant.FAILED_TO_SAVE_BASE_FORM);
        }
    }

    public void loadBaseFormData(String certcode, String status, Context context) {
        if (TextUtils.equals("1", status)) {//已上传

            GloabDelegete gloabDelegete = Constants.getInstance()
                    .getGloabDelegete();
            TransportEntity transportEntity = gloabDelegete.getUserInfo();
            getOneCertInfo(transportEntity.getToken(), transportEntity.getINC_ZZJGDM(), certcode, context);


        } else {
            GloabDelegete gloabDelegete = Constants.getInstance()
                    .getGloabDelegete();
            TransportEntity transportEntity = gloabDelegete.getUserInfo();

           getInfoByUserid( Constants.user.getTOKEN(), Constants.user.getUSER_ID(),"",context);

        }


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
    public void getInfoByUserid(String token, String userid, String certcode, Context context) {
        this.context = context;
        mView.showProgressDialog("正在加载...");
        XKZKJSendbean xkzkjSendbean = new XKZKJSendbean();
        xkzkjSendbean.setToken(token);
        xkzkjSendbean.setID(userid);

//{"token":"50CA204E-89B4-4F00-9118-6A6E4F50CFF5","ID":"328573"}
        Disposable disposable = repository.getUserInfo(xkzkjSendbean, context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BaseResponseReturnValue<UserInfoBean>>() {
                    @Override
                    public void onNext(BaseResponseReturnValue<UserInfoBean> value) {
                        if (value != null) {
                            if (value.getCode() == ResponseCode.success) {
                                if (value.getReturnValue() != null) {
                                    mView.dismissProgressDialog();

                                    mView.showBaseInfo(value.getReturnValue());


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
}
