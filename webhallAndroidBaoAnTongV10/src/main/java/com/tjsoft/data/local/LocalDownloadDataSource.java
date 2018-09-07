package com.tjsoft.data.local;

import android.support.annotation.Nullable;

import com.tjsoft.data.DownloadDataSource;
import com.tjsoft.data.ProgressResponseBody;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by lenovo on 2017/4/8.
 */

public class LocalDownloadDataSource
        implements DownloadDataSource
{
    @Nullable
    private static LocalDownloadDataSource INSTANCE;



    public static LocalDownloadDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LocalDownloadDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Observable<ResponseBody> downloadAttach(String url, ProgressResponseBody.ProgressListener progressListener) {
        return null;
    }
}
