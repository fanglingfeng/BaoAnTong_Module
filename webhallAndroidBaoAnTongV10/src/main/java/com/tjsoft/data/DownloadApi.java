package com.tjsoft.data;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by lenovo on 2017/4/8.
 */

public interface DownloadApi {
    @Streaming
    @GET
    Observable<ResponseBody> downloadAttach(@Url String url);
}
