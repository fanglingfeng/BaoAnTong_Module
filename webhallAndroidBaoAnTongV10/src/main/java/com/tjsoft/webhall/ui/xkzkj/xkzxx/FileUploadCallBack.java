package com.tjsoft.webhall.ui.xkzkj.xkzxx;

/**
 * Created by lenovo on 2017/4/24.
 */

public interface FileUploadCallBack {
    void onResponse(String materialsXml);
    void onError(Throwable e);
}
