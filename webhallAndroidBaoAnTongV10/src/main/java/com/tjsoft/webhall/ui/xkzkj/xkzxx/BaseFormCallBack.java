package com.tjsoft.webhall.ui.xkzkj.xkzxx;

import org.json.JSONArray;

/**
 * Created by lenovo on 2017/4/24.
 */

public interface BaseFormCallBack {
    void onResponse(String formsXml, JSONArray dataidArray);
    void onError(Throwable e);
}
