package com.tjsoft.data.remote;

import android.content.Context;

import com.bean.BaseResponseReturnValue;
import com.bean.GetUploadFileBean;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hxsoft.bat.openapi.entity.UserInfo;
import com.tjsoft.network.ZhengWuApi;
import com.tjsoft.network.post.NetWork;
import com.tjsoft.network.soap.SoapNetwork;
import com.tjsoft.util.HTTP;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.ATTBean;
import com.tjsoft.webhall.entity.AddCompanyResponseBean;
import com.tjsoft.webhall.entity.AddCompanySendBean;
import com.tjsoft.webhall.entity.CertInfoBean;
import com.tjsoft.webhall.entity.ChakanResponsebean;
import com.tjsoft.webhall.entity.ChakanSendbean;
import com.tjsoft.webhall.entity.CompanyList;
import com.tjsoft.webhall.entity.DZZZKBean;
import com.tjsoft.webhall.entity.DZZZKSendbean;
import com.tjsoft.webhall.entity.DZZZKUrlSendbean;
import com.tjsoft.webhall.entity.DZZZKUrlbean;
import com.tjsoft.webhall.entity.GetCompanySendBean;
import com.tjsoft.webhall.entity.SubmintXKZBean;
import com.tjsoft.webhall.entity.UserInfoBean;
import com.tjsoft.webhall.entity.XKZKJBean;
import com.tjsoft.webhall.entity.XKZKJSendbean;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.RequestBody;
import retrofit2.http.PartMap;


/**
 * Created by Dino on 10/25 0025.
 */

public class RemoteZhengwuDataSource implements ZhengWuApi {

    private static RemoteZhengwuDataSource INSTANCE;
    private MyCson gson = new MyCson();

    public static RemoteZhengwuDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteZhengwuDataSource();
        }
        return INSTANCE;
    }

    class MyCson {

        Gson mGson = new Gson();

        public <T> T fromJson(String json, Type typeOfT) {
            try {
                return mGson.fromJson(json, typeOfT);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                return null;
            }
        }

        public String toJson(Object src) {
            return mGson.toJson(src);
        }
    }

    @Override
    public Observable<BaseResponseReturnValue<String>> deleteAttach(final ATTBean attBean) {
        return Observable.create(new ObservableOnSubscribe<BaseResponseReturnValue<String>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponseReturnValue<String>> e) throws Exception {
                String response = SoapNetwork.excute("attachDelete", "SpaceAttachInfoService", gson.toJson(attBean));
                BaseResponseReturnValue<String> returnValue = gson.fromJson(response, new TypeToken<BaseResponseReturnValue<String>>() {
                }.getType());
                if (!e.isDisposed()) {
                    e.onNext(returnValue);
                }
            }
        });
    }

    @Override
    public Observable<BaseResponseReturnValue<CompanyList>> getCompanyList(final GetCompanySendBean sendBean, final Context context) {
        return Observable.create(new ObservableOnSubscribe<BaseResponseReturnValue<CompanyList>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponseReturnValue<CompanyList>> e) throws Exception {
                JSONObject param = new JSONObject();
                param.put("token", sendBean.getToken());
                param.put("USERID", sendBean.getUSERID());
                String response = HTTP.excuteAndCache("getINCAuthenticationByUserID", "RestUserService", param.toString(),
                        context);
                BaseResponseReturnValue<CompanyList> returnValue = gson.fromJson(response, new TypeToken<BaseResponseReturnValue<CompanyList>>() {
                }.getType());
                if (!e.isDisposed()) {
                    e.onNext(returnValue);
                }
            }
        });
    }

    @Override
    public Observable<BaseResponseReturnValue<AddCompanyResponseBean>> addCompany(final AddCompanySendBean sendBean, final Context context) {
        return Observable.create(new ObservableOnSubscribe<BaseResponseReturnValue<AddCompanyResponseBean>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponseReturnValue<AddCompanyResponseBean>> e) throws Exception {
                JSONObject param = new JSONObject();
                param.put("token", sendBean.getToken());
                param.put("USERID", sendBean.getUSERID());
                param.put("INC_NAME", sendBean.getINC_NAME());
                param.put("INC_TYSHXYDM", sendBean.getINC_TYSHXYDM());
                param.put("INC_DEPUTY", sendBean.getINC_DEPUTY());
                param.put("INC_PID", sendBean.getINC_PID());
                param.put("DEPUTY_ISREALNAME", sendBean.getDEPUTY_ISREALNAME());
                String response = HTTP.excuteAndCache("saveINCAuthentication", "RestUserService", param.toString(),
                        context);
                BaseResponseReturnValue<AddCompanyResponseBean> returnValue = gson.fromJson(response, new TypeToken<BaseResponseReturnValue<AddCompanyResponseBean>>() {
                }.getType());
                if (!e.isDisposed()) {
                    e.onNext(returnValue);
                }
            }
        });
    }

    @Override
    public Observable<BaseResponseReturnValue<AddCompanyResponseBean>> editCompany(final AddCompanySendBean sendBean, final Context context) {
        return Observable.create(new ObservableOnSubscribe<BaseResponseReturnValue<AddCompanyResponseBean>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponseReturnValue<AddCompanyResponseBean>> e) throws Exception {
                JSONObject param = new JSONObject();
                param.put("token", sendBean.getToken());
                param.put("USERID", sendBean.getUSERID());
                param.put("INC_NAME", sendBean.getINC_NAME());
                param.put("INC_TYSHXYDM", sendBean.getINC_TYSHXYDM());
                param.put("INC_DEPUTY", sendBean.getINC_DEPUTY());
                param.put("INC_PID", sendBean.getINC_PID());
                param.put("INC_PID", sendBean.getINC_PID());
                param.put("INC_EXT_ID", sendBean.getINC_EXT_ID());
                param.put("DEPUTY_ISREALNAME", sendBean.getDEPUTY_ISREALNAME());
                String response = HTTP.excuteAndCache("editINCAuthentication", "RestUserService", param.toString(),
                        context);
                BaseResponseReturnValue<AddCompanyResponseBean> returnValue = gson.fromJson(response, new TypeToken<BaseResponseReturnValue<AddCompanyResponseBean>>() {
                }.getType());
                if (!e.isDisposed()) {
                    e.onNext(returnValue);
                }
            }
        });
    }

    @Override
    public Observable<BaseResponseReturnValue<AddCompanyResponseBean>> deleteCompany(final AddCompanySendBean sendBean, final Context context) {
        return Observable.create(new ObservableOnSubscribe<BaseResponseReturnValue<AddCompanyResponseBean>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponseReturnValue<AddCompanyResponseBean>> e) throws Exception {
                JSONObject param = new JSONObject();
                param.put("token", sendBean.getToken());
                param.put("INC_EXT_ID", sendBean.getINC_EXT_ID());

                String response = HTTP.excuteAndCache("deleteINCAuthentication", "RestUserService", param.toString(),
                        context);
                BaseResponseReturnValue<AddCompanyResponseBean> returnValue = gson.fromJson(response, new TypeToken<BaseResponseReturnValue<AddCompanyResponseBean>>() {
                }.getType());
                if (!e.isDisposed()) {
                    e.onNext(returnValue);
                }
            }
        });
    }

    @Override
    public Observable<BaseResponseReturnValue<DZZZKBean>> getDZZZKList(final DZZZKSendbean sendBean, final Context context) {
        return Observable.create(new ObservableOnSubscribe<BaseResponseReturnValue<DZZZKBean>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponseReturnValue<DZZZKBean>> e) throws Exception {
                JSONObject param = new JSONObject();
                param.put("token", sendBean.getToken());
                param.put("SFZJHM", sendBean.getSFZJHM());
                param.put("SXBM", sendBean.getSXBM());
                param.put("SXMC", sendBean.getSXMC());

                String response = HTTP.excuteAndCache("getZzInfo", "RestZzgxService", param.toString(),
                        context);
                BaseResponseReturnValue<DZZZKBean> returnValue = gson.fromJson(response, new TypeToken<BaseResponseReturnValue<DZZZKBean>>() {
                }.getType());
                if (!e.isDisposed()) {
                    e.onNext(returnValue);
                }
            }
        });
    }

    @Override
    public Observable<BaseResponseReturnValue<List<XKZKJBean>>> getXKZKJList(final XKZKJSendbean sendBean, final Context context) {
        return Observable.create(new ObservableOnSubscribe<BaseResponseReturnValue<List<XKZKJBean>>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponseReturnValue<List<XKZKJBean>>> e) throws Exception {
                JSONObject param = new JSONObject();
                param.put("token", sendBean.getToken());
                param.put("USERCODE", sendBean.getUSERCODE());
                param.put("PAGENO", sendBean.getPAGENO());
                param.put("PAGESIZE", sendBean.getPAGESIZE());
                param.put("NAME", sendBean.getNAME());


                String response = HTTP.excuteAndCache("getCertInfo", "SpaceCertInfoService", param.toString(),
                        context);
                BaseResponseReturnValue<List<XKZKJBean>> returnValue = gson.fromJson(response, new TypeToken<BaseResponseReturnValue<List<XKZKJBean>>>() {
                }.getType());
                if (!e.isDisposed()) {
                    e.onNext(returnValue);
                }
            }
        });
    }

    @Override
    public Observable<BaseResponseReturnValue<CertInfoBean>> getOneCertInfo(final XKZKJSendbean sendBean, final Context context) {
        return Observable.create(new ObservableOnSubscribe<BaseResponseReturnValue<CertInfoBean>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponseReturnValue<CertInfoBean>> e) throws Exception {
                JSONObject param = new JSONObject();
                param.put("token", sendBean.getToken());
                param.put("USERCODE", sendBean.getUSERCODE());
                param.put("CERTCODE", sendBean.getCERTCODE());


                String response = HTTP.excuteAndCache("getOneCertInfo", "SpaceCertInfoService", param.toString(),
                        context);
                BaseResponseReturnValue<CertInfoBean> returnValue = gson.fromJson(response, new TypeToken<BaseResponseReturnValue<CertInfoBean>>() {
                }.getType());
                if (!e.isDisposed()) {
                    e.onNext(returnValue);
                }
            }
        });
    }

    @Override
    public Observable<BaseResponseReturnValue<UserInfoBean>> getUserInfo(final XKZKJSendbean sendBean, final Context context) {
        return Observable.create(new ObservableOnSubscribe<BaseResponseReturnValue<UserInfoBean>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponseReturnValue<UserInfoBean>> e) throws Exception {
                JSONObject param = new JSONObject();
                param.put("token", Constants.user.getTOKEN());
                param.put("ID", Constants.user.getUSER_ID());

//                {"token":"50CA204E-89B4-4F00-9118-6A6E4F50CFF5","ID":"328573"}
                String response = HTTP.excuteAndCache("getInfoByUserid", "RestUserService", param.toString(),
                        context);
                BaseResponseReturnValue<UserInfoBean> returnValue = gson.fromJson(response, new TypeToken<BaseResponseReturnValue<UserInfoBean>>() {
                }.getType());
                if (!e.isDisposed()) {
                    e.onNext(returnValue);
                }
            }
        });
    }

    @Override
    public Observable<BaseResponseReturnValue<String>> submit(final XKZKJSendbean sendBean, final Context context) {
        return Observable.create(new ObservableOnSubscribe<BaseResponseReturnValue<String>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponseReturnValue<String>> e) throws Exception {
                JSONObject param = new JSONObject();
                param.put("token", sendBean.getToken());
                param.put("USERCODE", sendBean.getUSERCODE());
                param.put("CERTCODE", sendBean.getCERTCODE());
                param.put("CERTNAME", sendBean.getCERTNAME());
                param.put("DATAXML", sendBean.getDATAXML());
                param.put("CERTFILES", sendBean.getCERTFILES());


                String response = HTTP.excuteAndCache("submit", "SpaceCertInfoService", param.toString(),
                        context);
                BaseResponseReturnValue<String> returnValue = gson.fromJson(response, new TypeToken<BaseResponseReturnValue<String>>() {
                }.getType());
                if (!e.isDisposed()) {
                    e.onNext(returnValue);
                }
            }
        });
    }

    @Override
    public Observable<BaseResponseReturnValue<ChakanResponsebean>> chakan(final ChakanSendbean sendBean, final Context context) {
        return Observable.create(new ObservableOnSubscribe<BaseResponseReturnValue<ChakanResponsebean>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponseReturnValue<ChakanResponsebean>> e) throws Exception {
                JSONObject param = new JSONObject();
                param.put("token", sendBean.getToken());
                param.put("AUTHCODE", sendBean.getAUTHCODE());

                String response = HTTP.excuteAndCache("savePdfToMongo", "RestZzgxService", param.toString(),
                        context);
                BaseResponseReturnValue<ChakanResponsebean> returnValue = gson.fromJson(response, new TypeToken<BaseResponseReturnValue<ChakanResponsebean>>() {
                }.getType());
                if (!e.isDisposed()) {
                    e.onNext(returnValue);
                }
            }
        });
    }

    @Override
    public Observable<BaseResponseReturnValue<DZZZKUrlbean>> getUrl(final DZZZKUrlSendbean sendBean, final Context context) {
        return Observable.create(new ObservableOnSubscribe<BaseResponseReturnValue<DZZZKUrlbean>>() {
            @Override
            public void subscribe(ObservableEmitter<BaseResponseReturnValue<DZZZKUrlbean>> e) throws Exception {
                JSONObject param = new JSONObject();
                param.put("token", sendBean.getToken());
                param.put("AUTHCODE", sendBean.getAUTHCODE());

                String response = HTTP.excuteAndCache("getOneOffUrl", "RestZzgxService", param.toString(),
                        context);
                BaseResponseReturnValue<DZZZKUrlbean> returnValue = gson.fromJson(response, new TypeToken<BaseResponseReturnValue<DZZZKUrlbean>>() {
                }.getType());
                if (!e.isDisposed()) {
                    e.onNext(returnValue);
                }
            }
        });
    }

    @Override
    public Observable<BaseResponseReturnValue<GetUploadFileBean>> uploadFile(@PartMap Map<String, RequestBody> params) {
        return NetWork.getZhengWuDOMAINApi().uploadFile(params);
    }
}
