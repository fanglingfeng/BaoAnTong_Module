package com.tjsoft.webhall.entity;

import android.databinding.ObservableBoolean;

/**
 * Created by lenovo on 2017/4/1.
 * 加载失败、成功观察bean
 */

public class LoadObservableBean {
    public final ObservableBoolean showReloadLayout = new ObservableBoolean();//默认不显示重新加载布局
}
