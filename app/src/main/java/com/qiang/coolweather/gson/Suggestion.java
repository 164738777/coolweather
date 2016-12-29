package com.qiang.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 作者:  qiang on 2016/12/29 12:17
 * 邮箱:  anworkmail_q@126.com
 * 作用:
 */

public class Suggestion {


    @SerializedName("comf")
    public Comfort comfort;
    @SerializedName("cw")
    public CwBean carWash;
    public Sport sport;

    public static class Comfort {
        @SerializedName("txt")
        public String info;
    }

    public static class CwBean {
        @SerializedName("txt")
        public String info;
    }

    public static class Sport {
        @SerializedName("txt")
        public String info;
    }
}
