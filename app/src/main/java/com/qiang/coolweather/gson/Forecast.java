package com.qiang.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 作者:  qiang on 2016/12/29 12:26
 * 邮箱:  anworkmail_q@126.com
 * 作用:
 */

public class Forecast {
    public String date;
    @SerializedName("tmp")
    public Temperature temperature;
    @SerializedName("cond")
    public More more;

    public class Temperature{
        public String max;
        public String min;
    }

    public class More{
        @SerializedName("txt_d")
        public String info;
    }
}
