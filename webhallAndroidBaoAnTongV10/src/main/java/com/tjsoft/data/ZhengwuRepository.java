package com.tjsoft.data;

import android.content.Context;
import android.support.annotation.NonNull;
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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by lenovo on 2017/1/6.
 */

public class ZhengwuRepository implements ZhengWuApi {
    private Gson gson = new Gson();

    @Nullable
    private static ZhengwuRepository INSTANCE = null;

    @NonNull
    private final ZhengWuApi mRemoteDataSource;


    @NonNull
    private final ZhengWuApi mLocalDataSource;

    public ZhengwuRepository(@NonNull ZhengWuApi remoteDataSource,
                             @NonNull ZhengWuApi localDataSource) {
        mRemoteDataSource = checkNotNull(remoteDataSource);
        mLocalDataSource = checkNotNull(localDataSource);
    }

    public static ZhengwuRepository getInstance(@NonNull ZhengWuApi remoteFoodsDataSource,
                                                @NonNull ZhengWuApi localFoodsDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ZhengwuRepository(remoteFoodsDataSource, localFoodsDataSource);
        }
        return INSTANCE;
    }
    @Override
    public Observable<BaseResponseReturnValue<String>> deleteAttach(ATTBean attBean) {
        return mRemoteDataSource.deleteAttach(attBean);
    }

    @Override
    public Observable<BaseResponseReturnValue<GetUploadFileBean>> uploadFile(@PartMap Map<String, RequestBody> params) {
        return mRemoteDataSource.uploadFile(params);
    }
    @Override
    public Observable<BaseResponseReturnValue<CompanyList>> getCompanyList(GetCompanySendBean sendBean, Context context) {
        return mRemoteDataSource.getCompanyList(sendBean,context);
    }
    @Override
    public Observable<BaseResponseReturnValue<AddCompanyResponseBean>> addCompany(AddCompanySendBean sendBean, Context context) {
        return mRemoteDataSource.addCompany(sendBean,context);
    } @Override
    public Observable<BaseResponseReturnValue<AddCompanyResponseBean>> deleteCompany(AddCompanySendBean sendBean, Context context) {
        return mRemoteDataSource.deleteCompany(sendBean,context);
    }

    @Override
    public Observable<BaseResponseReturnValue<AddCompanyResponseBean>> editCompany(AddCompanySendBean sendBean, Context context) {
        return mRemoteDataSource.editCompany(sendBean,context);
    }

    @Override
    public Observable<BaseResponseReturnValue<DZZZKBean>> getDZZZKList(DZZZKSendbean sendBean, Context context) {
        return mRemoteDataSource.getDZZZKList(sendBean,context);
    }
    @Override
    public Observable<BaseResponseReturnValue<List<XKZKJBean>>> getXKZKJList(XKZKJSendbean sendBean, Context context) {
        return mRemoteDataSource.getXKZKJList(sendBean,context);
    }
    @Override
    public Observable<BaseResponseReturnValue<CertInfoBean>> getOneCertInfo(XKZKJSendbean sendBean, Context context) {
        return mRemoteDataSource.getOneCertInfo(sendBean,context);
    }
    @Override
    public Observable<BaseResponseReturnValue<UserInfoBean>> getUserInfo(XKZKJSendbean sendBean, Context context) {
        return mRemoteDataSource.getUserInfo(sendBean,context);
    }
    @Override
    public Observable<BaseResponseReturnValue<String>> submit(XKZKJSendbean sendBean, Context context) {
        return mRemoteDataSource.submit(sendBean,context);
    }

    @Override
    public Observable<BaseResponseReturnValue<ChakanResponsebean>> chakan(ChakanSendbean sendBean, Context context) {
        return mRemoteDataSource.chakan(sendBean,context);
    }
    @Override
    public Observable<BaseResponseReturnValue<DZZZKUrlbean>> getUrl(DZZZKUrlSendbean sendBean, Context context) {
        return mRemoteDataSource.getUrl(sendBean,context);
    }

}
