package com.qiang.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 作者:  qiang on 2016/12/29 12:29
 * 邮箱:  anworkmail_q@126.com
 * 作用:
 */

public class Weather {
    public String status;
    public Basic basic;
    public AQI aqi;
    public Now now;
    public Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
