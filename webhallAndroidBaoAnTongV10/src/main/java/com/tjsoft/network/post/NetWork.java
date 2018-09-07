package com.tjsoft.network.post;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.socks.library.KLog;
import com.tjsoft.msfw.guangdongshenzhenbaoan.BuildConfig;
import com.tjsoft.network.ZhengWuApi;
import com.tjsoft.util.AuthorizationGenUtils;
import com.tjsoft.webhall.constants.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lenovo on 2017/1/6.
 */

public class NetWork {
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();

    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static RxJava2CallAdapterFactory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();
    private static ZhengWuApi zhengWuDOMAINApi;

    private static final int DEFAULT_TIMEOUT = 30;

    /**
     * 政务模块api【DOMAIN】
     *
     * @return
     */
    public static ZhengWuApi getZhengWuDOMAINApi() {
        if (zhengWuDOMAINApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(getOkHttpClient())
                    .baseUrl(Constants.DOMAIN)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            zhengWuDOMAINApi = retrofit.create(ZhengWuApi.class);
        }
        return zhengWuDOMAINApi;
    }
    private static OkHttpClient getOkHttpClient() {
//  //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //打印retrofit日志
                KLog.e("RetrofitLog", "OkHttp = " + message);
            }
        });

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());  //如果401了，会先执行TokenAuthenticator
                if (response.code() == 401) {
//                    LoginUtils.startLoginActivity();
                }
                return response;
            }
        };

        //定制OkHttp
        OkHttpClient client = null;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(interceptor);
        if (BuildConfig.DEBUG) {
            //上传文件时，拦截器setLevel(HttpLoggingInterceptor.Level.BODY)会多读一次body，使进度多写一次
            client = builder.addInterceptor(loggingInterceptor)
                    .build();
        } else {
            client = builder.build();
        }
        return client;
    }
    public static OkHttpClient getSenseTimeHttpClient() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder builder1 = request.newBuilder();
                Request build = builder1.addHeader("Authorization", AuthorizationGenUtils.getAuthoriztionStr()).build();
                return chain.proceed(build);
            }
        };
        //定制OkHttp
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
        return client;
    }
}
