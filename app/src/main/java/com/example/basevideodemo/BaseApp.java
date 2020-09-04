package com.example.basevideodemo;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.danikula.videocache.HttpProxyCacheServer;
import com.example.basevideodemo.until.CommonUtils;

/**
 * @author puyantao
 * @description :
 * @date 2020/9/4
 */
public class BaseApp extends Application {
    private HttpProxyCacheServer proxy;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public static HttpProxyCacheServer getProxy(Context context) {
        BaseApp app = (BaseApp) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }


}












