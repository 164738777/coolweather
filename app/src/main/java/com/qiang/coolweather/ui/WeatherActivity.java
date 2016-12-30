package com.qiang.coolweather.ui;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.qiang.coolweather.R;
import com.qiang.coolweather.base.BaseActivity;
import com.qiang.coolweather.base.Constant;
import com.qiang.coolweather.gson.AQI;
import com.qiang.coolweather.gson.Basic;
import com.qiang.coolweather.gson.Forecast;
import com.qiang.coolweather.gson.Now;
import com.qiang.coolweather.gson.Suggestion;
import com.qiang.coolweather.gson.Weather;
import com.qiang.coolweather.util.HttpUtil;
import com.qiang.coolweather.util.Utility;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends BaseActivity {

    @BindView(R.id.tv_cityTitle)
    TextView tvCityTitle;
    @BindView(R.id.tv_updateTime)
    TextView tvUpdateTime;
    @BindView(R.id.tv_degree)
    TextView tvDegree;
    @BindView(R.id.tv_weatherInfo)
    TextView tvWeatherInfo;
    @BindView(R.id.ll_forecast)
    LinearLayout llForecast;
    @BindView(R.id.tv_aqi)
    TextView tvAqi;
    @BindView(R.id.tv_pm25)
    TextView tvPm25;
    @BindView(R.id.tv_comfort)
    TextView tvComfort;
    @BindView(R.id.tv_wash)
    TextView tvWash;
    @BindView(R.id.tv_sport)
    TextView tvSport;
    @BindView(R.id.sv_weather)
    ScrollView svWeather;
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.btn_nav)
    Button btnNav;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;


    private String weatherId;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_weather;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String weatherString = prefs.getString(Constant.KEY_WEATHER, null);
        if (weatherString != null) {
            //有缓存直接解析
            Weather weather = Utility.handleWeatherResponse(weatherString);
            weatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            //没有缓存就上网
            weatherId = getIntent().getStringExtra(Constant.KEY_WEATHER_ID);
            svWeather.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }

        String pic = prefs.getString(Constant.KEY_PIC, null);
        if (pic != null) {
            Glide.with(this).load(pic).into(ivPic);
        } else {
            loadPic();
        }

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(() -> {
            requestWeather(weatherId);
        });
    }

    private void loadPic() {
        HttpUtil.sendOkHttpRequest(Constant.BASE_PATH_PIC, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString(Constant.KEY_PIC, string);
                editor.apply();
                runOnUiThread(() -> {
                    Glide.with(WeatherActivity.this).load(string).into(ivPic);
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void showWeatherInfo(Weather weather) {
        //显示UI
        Basic basic = weather.basic;
        Now now = weather.now;
        AQI aqi = weather.aqi;
        Suggestion suggestion = weather.suggestion;

        String cityName = basic.cityName;
        String updateTime = basic.update.updateTime;
        String temperature = now.temperature + getResources().getString(R.string.temp_text);
        String weatherInfo = now.more.info;
        tvCityTitle.setText(cityName);
        tvUpdateTime.setText(updateTime);
        tvDegree.setText(temperature);
        tvWeatherInfo.setText(weatherInfo);
        llForecast.removeAllViews();

        for (Forecast forecast : weather.forecastList) {
            View inflate = LayoutInflater.from(this).inflate(R.layout.forecast_item, llForecast, false);
            TextView tvmin = (TextView) inflate.findViewById(R.id.tv_min);
            TextView tvmax = (TextView) inflate.findViewById(R.id.tv_max);
            TextView tvinfo = (TextView) inflate.findViewById(R.id.tv_info);
            TextView tvdate = (TextView) inflate.findViewById(R.id.tv_date);
            tvdate.setText(forecast.date);
            tvmax.setText(forecast.temperature.max);
            tvmin.setText(forecast.temperature.min);
            tvinfo.setText(forecast.more.info);
            llForecast.addView(inflate);
        }

        if (aqi != null) {
            tvAqi.setText(aqi.city.aqi);
            tvPm25.setText(aqi.city.pm25);
        }

        String comfort = getResources().getString(R.string.comfortable) + "\n" + suggestion.comfort.info;
        String carWash = getResources().getString(R.string.carWash) + "\n" + suggestion.carWash.info;
        String sport = getResources().getString(R.string.sport) + "\n" + suggestion.sport.info;
        tvComfort.setText(comfort);
        tvWash.setText(carWash);
        tvSport.setText(sport);
        svWeather.setVisibility(View.VISIBLE);
    }

    public void requestWeather(String weatherId) {
        String weatherUrl = Constant.BASE_PATH_WEATHER + Constant.REQUEST_KEY_CITY + weatherId + Constant.BASE_KEY;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Weather weather = Utility.handleWeatherResponse(string);
                runOnUiThread(() -> {
                    if (weather != null && "ok".equals(weather.status)) {
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                        editor.putString(Constant.KEY_WEATHER, string);
                        editor.apply();
                        showWeatherInfo(weather);
                    } else {
                        Toast.makeText(WeatherActivity.this, getResources().getString(R.string.failed_load_weather), Toast.LENGTH_SHORT).show();
                    }
                    swipeRefresh.setRefreshing(false);
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    Toast.makeText(WeatherActivity.this, getResources().getString(R.string.failed_load_weather), Toast.LENGTH_SHORT).show();
                    swipeRefresh.setRefreshing(false);
                });
            }
        });
        loadPic();
    }

    @OnClick(R.id.btn_nav)
    public void onClick() {
        drawerLayout.openDrawer(GravityCompat.START);
    }
}
