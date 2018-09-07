package com.tjsoft.data;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by lenovo on 2017/4/8.
 */

public interface DownloadDataSource {
    /**
     * 下载附件
     * @return
     */
    Observable<ResponseBody> downloadAttach(String url,
                                            ProgressResponseBody.ProgressListener progressListener);
}
