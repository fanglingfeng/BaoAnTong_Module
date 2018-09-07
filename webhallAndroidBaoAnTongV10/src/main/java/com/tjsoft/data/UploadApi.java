package com.tjsoft.data;

import com.bean.BaseResponseReturnValue;
import com.bean.FileUploadResponseBean;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * Created by lenovo on 2017/4/11.
 */

public interface UploadApi {
    /*附件上传下载服务 start*/
    @Multipart
    @POST("servlet/mobileUploadServlet")
    Observable<BaseResponseReturnValue<FileUploadResponseBean>> uploadFile(@PartMap Map<String, RequestBody> params);
}
