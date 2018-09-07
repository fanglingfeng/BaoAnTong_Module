package com.tjsoft.data.local;

import android.content.Context;
import android.support.annotation.Nullable;

import com.bean.BaseResponseReturnValue;
import com.bean.GetUploadFileBean;
import com.google.gson.Gson;
import com.tjsoft.network.ZhengWuApi;
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
import retrofit2.http.PartMap;


/**
 * Created by Dino on 10/25 0025.
 */

public class LocalZhengwuDataSource
        implements ZhengWuApi
{
    @Nullable
    private static LocalZhengwuDataSource INSTANCE;

    private Gson gson = new Gson();

    public static LocalZhengwuDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LocalZhengwuDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Observable<BaseResponseReturnValue<String>> deleteAttach(ATTBean attBean) {
        return null;
    }

    @Override
    public Observable<BaseResponseReturnValue<CompanyList>> getCompanyList(GetCompanySendBean sendBean,
                                                                           Context context)
    {
        return null;
    }

    @Override
    public Observable<BaseResponseReturnValue<AddCompanyResponseBean>> addCompany(AddCompanySendBean sendBean,
                                                                                  Context context)
    {
        return null;
    }

    @Override
    public Observable<BaseResponseReturnValue<AddCompanyResponseBean>> deleteCompany(
            AddCompanySendBean sendBean,
            Context context)
    {
        return null;
    }

    @Override
    public Observable<BaseResponseReturnValue<AddCompanyResponseBean>> editCompany(
            AddCompanySendBean sendBean,
            Context context)
    {
        return null;
    }

    @Override
    public Observable<BaseResponseReturnValue<DZZZKBean>> getDZZZKList(DZZZKSendbean sendBean,
                                                                       Context context)
    {
        return null;
    }
    @Override
    public Observable<BaseResponseReturnValue<List<XKZKJBean>>> getXKZKJList(XKZKJSendbean sendBean,
                                                                             Context context)
    {
        return null;
    }
    @Override
    public Observable<BaseResponseReturnValue<CertInfoBean>> getOneCertInfo(XKZKJSendbean sendBean,
                                                                            Context context)
    {
        return null;
    } @Override
    public Observable<BaseResponseReturnValue<UserInfoBean>> getUserInfo(XKZKJSendbean sendBean,
                                                                         Context context)
    {
        return null;
    }
    @Override
    public Observable<BaseResponseReturnValue<String>> submit(XKZKJSendbean sendBean,
                                                                      Context context)
    {
        return null;
    }
    @Override
    public Observable<BaseResponseReturnValue<ChakanResponsebean>> chakan(ChakanSendbean sendBean,
                                                                          Context context)
    {
        return null;
    }

    @Override
    public Observable<BaseResponseReturnValue<DZZZKUrlbean>> getUrl(DZZZKUrlSendbean sendBean,
                                                                    Context context)
    {
        return null;
    }

    @Override
    public Observable<BaseResponseReturnValue<GetUploadFileBean>> uploadFile(@PartMap Map<String, RequestBody> params) {
        return null;
    }

}
