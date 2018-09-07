package com.tjsoft.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by lenovo on 2017/4/8.
 */

public class DownloadRepository
        implements DownloadDataSource {
    @Nullable
    private static DownloadRepository INSTANCE = null;

    @NonNull
    private final DownloadDataSource mRemoteDataSource;

    @NonNull
    private final DownloadDataSource mLocalDataSource;



    private DownloadRepository(@NonNull DownloadDataSource remoteDataSource,
                               @NonNull DownloadDataSource localDataSource) {
        mRemoteDataSource = checkNotNull(remoteDataSource);
        mLocalDataSource = checkNotNull(localDataSource);
    }

    public static DownloadRepository getInstance(@NonNull DownloadDataSource remoteFoodsDataSource,
                                             @NonNull DownloadDataSource localFoodsDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new DownloadRepository(remoteFoodsDataSource, localFoodsDataSource);
        }
        return INSTANCE;
    }
    @Override
    public Observable<ResponseBody> downloadAttach(String url, ProgressResponseBody.ProgressListener progressListener) {
        return mRemoteDataSource.downloadAttach(url,progressListener);
    }
}
