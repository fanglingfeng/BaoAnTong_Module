package com.tjsoft.data.remote;


import com.tjsoft.data.DownloadDataSource;
import com.tjsoft.data.ProgressResponseBody;
import com.tjsoft.data.RetrofitManager;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by lenovo on 2017/4/8.
 */

public class RemoteDownloadDataSource
        implements DownloadDataSource
{
    private static RemoteDownloadDataSource INSTANCE;

    public static RemoteDownloadDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteDownloadDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Observable<ResponseBody> downloadAttach(String url, ProgressResponseBody.ProgressListener progressListener) {
        return RetrofitManager.getDownloadApi(progressListener).downloadAttach(url);
    }
}
