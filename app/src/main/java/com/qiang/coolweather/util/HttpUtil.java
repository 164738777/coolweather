package com.qiang.coolweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 作者:  qiang on 2016/12/28 22:00
 * 邮箱:  anworkmail_q@126.com
 * 作用:
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

}
