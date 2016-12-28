package com.qiang.coolweather;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * 作者:  qiang on 2016/12/28 21:51
 * 邮箱:  anworkmail_q@126.com
 * 作用:
 */

public class MyApp extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        context = getApplicationContext();
        LitePal.initialize(context);
    }
}
