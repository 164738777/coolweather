package com.qiang.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 作者:  qiang on 2016/12/29 12:15
 * 邮箱:  anworkmail_q@126.com
 * 作用:
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public static class More {

        @SerializedName("txt")
        public String info;
    }
}
