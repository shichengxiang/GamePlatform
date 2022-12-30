package com.vce.baselib;

import android.app.Application;

import com.vce.baselib.utils.Util;

/**
 * 描述：BaseApplication
 * 创建者: shichengxiang
 * 创建时间：2022/3/24
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Util.init(this);
    }
}
