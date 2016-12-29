package com.qiang.coolweather.gson;

/**
 * 作者:  qiang on 2016/12/29 12:14
 * 邮箱:  anworkmail_q@126.com
 * 作用:
 */

public class AQI {

    public AQICity city;

    public static class AQICity {
        /**
         * aqi : 50
         * pm25 : 33
         */

        public String aqi;
        public String pm25;
    }
}
