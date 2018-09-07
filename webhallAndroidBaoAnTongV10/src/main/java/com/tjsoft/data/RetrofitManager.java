package com.tjsoft.data;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.socks.library.KLog;
import com.tjsoft.webhall.constants.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lenovo on 2017/4/8.
 */

public class RetrofitManager {
    private static Converter.Factory         gsonConverterFactory     = GsonConverterFactory.create();
    private static OkHttpClient              okHttpClient             = new OkHttpClient();
    private static RxJava2CallAdapterFactory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();

    private static final int DEFAULT_TIMEOUT = 30;//默认超时时间（30s）


    private static DownloadApi downloadApi;
    private static UploadApi uploadApi;
    private static UserDataSource userDataSource;
    /**
     * 下载api【DOMAIN】
     * @param progressListener
     * @return
     */
    public static DownloadApi getDownloadApi(final ProgressResponseBody.ProgressListener progressListener) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(getDownloadOkHttpClient(progressListener))
                .baseUrl(Constants.DOMAIN)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
        downloadApi = retrofit.create(DownloadApi.class);
        return downloadApi;
    }

    /**
     * 上传api【DOMAIN】
     * @return
     */
    public static UploadApi getUploadApi() {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(getOkHttpClient())
                    .baseUrl(Constants.DOMAIN)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
        uploadApi = retrofit.create(UploadApi.class);
        return uploadApi;
    }
    public static UserDataSource getUserDataSource() {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(getOkHttpClient())
                    .baseUrl(Constants.DOMAIN)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
        userDataSource = retrofit.create(UserDataSource.class);
        return userDataSource;
    }


    private static OkHttpClient getOkHttpClient() {

        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
                KLog.e("RetrofitLog","OkHttp = "+message);
            }
        });

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //定制OkHttp
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();
        return client;
    }

    private static OkHttpClient getDownloadOkHttpClient(final ProgressResponseBody.ProgressListener progressListener) {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();
            }
        };

        OkHttpClient downloadOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
        return downloadOkHttpClient;
    }
}
