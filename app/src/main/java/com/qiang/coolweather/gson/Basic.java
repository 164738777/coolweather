package com.qiang.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 作者:  qiang on 2016/12/29 12:08
 * 邮箱:  anworkmail_q@126.com
 * 作用:
 */

public class Basic {

    /**
     * city : 广州
     * id : CN101280101
     * update : {"loc":"2016-12-29 11:52","utc":"2016-12-29 03:52"}
     */

    @SerializedName("city")
    public String cityName;
    @SerializedName("id")
    public String weatherId;
    public Update update;

    public static class Update {
        /**
         * loc : 2016-12-29 11:52
         */
        @SerializedName("loc")
        public String updateTime;
    }
}
