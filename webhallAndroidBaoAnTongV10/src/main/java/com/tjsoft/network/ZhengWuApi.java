package com.tjsoft.network;


import android.content.Context;

import com.bean.BaseResponseReturnValue;
import com.bean.GetUploadFileBean;
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

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * Created by lenovo on 2017/1/6.
 */

public interface ZhengWuApi {
    Observable<BaseResponseReturnValue<String>> deleteAttach(ATTBean attBean);
    Observable<BaseResponseReturnValue<CompanyList>> getCompanyList(GetCompanySendBean sendBean, Context context);
    Observable<BaseResponseReturnValue<AddCompanyResponseBean>> addCompany(AddCompanySendBean sendBean, Context context);
    Observable<BaseResponseReturnValue<AddCompanyResponseBean>> deleteCompany(AddCompanySendBean sendBean, Context context);
    Observable<BaseResponseReturnValue<AddCompanyResponseBean>> editCompany(AddCompanySendBean sendBean, Context context);
    /*附件上传下载服务 start*/
    @Multipart
    @POST("servlet/uploadMobileFileServlet")
    Observable<BaseResponseReturnValue<GetUploadFileBean>> uploadFile(@PartMap Map<String, RequestBody> params);

    Observable<BaseResponseReturnValue<DZZZKBean>> getDZZZKList(DZZZKSendbean sendbean, Context context);
    Observable<BaseResponseReturnValue<List<XKZKJBean>>> getXKZKJList(XKZKJSendbean sendbean, Context context);
    Observable<BaseResponseReturnValue<CertInfoBean>> getOneCertInfo(XKZKJSendbean sendbean, Context context);
    Observable<BaseResponseReturnValue<UserInfoBean>> getUserInfo(XKZKJSendbean sendbean, Context context);
    Observable<BaseResponseReturnValue<String>> submit(XKZKJSendbean sendbean, Context context);
    Observable<BaseResponseReturnValue<ChakanResponsebean>> chakan(ChakanSendbean sendbean, Context context);
    Observable<BaseResponseReturnValue<DZZZKUrlbean>> getUrl(DZZZKUrlSendbean sendbean, Context context);
}
