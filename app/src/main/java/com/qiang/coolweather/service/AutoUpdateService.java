package com.qiang.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.qiang.coolweather.base.Constant;
import com.qiang.coolweather.gson.Weather;
import com.qiang.coolweather.util.HttpUtil;
import com.qiang.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updatePic();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String string = prefs.getString(Constant.KEY_WEATHER, null);
        if (string != null) {
            Weather weather = Utility.handleWeatherResponse(string);
            String weatherId = weather.basic.weatherId;
            String weatherUrl = Constant.BASE_PATH_WEATHER + Constant.REQUEST_KEY_CITY + weatherId + Constant.BASE_KEY;
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String string1 = response.body().string();
                    Weather weather1 = Utility.handleWeatherResponse(string1);
                    if (weather1 != null && "ok".equals(weather1.status)) {
                        SharedPreferences.Editor editor =  PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString(Constant.KEY_WEATHER, string1);
                        editor.apply();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void updatePic() {
        HttpUtil.sendOkHttpRequest(Constant.BASE_PATH_PIC, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                SharedPreferences.Editor editor =  PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString(Constant.KEY_PIC, string);
                editor.apply();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
