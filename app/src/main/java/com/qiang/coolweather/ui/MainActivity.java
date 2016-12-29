package com.qiang.coolweather.ui;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.qiang.coolweather.R;
import com.qiang.coolweather.base.BaseActivity;
import com.qiang.coolweather.base.Constant;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref.getString(Constant.KEY_WEATHER, null) != null) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            ChooseAreaFragment fragment = new ChooseAreaFragment();
            transaction.add(R.id.ll, fragment).show(fragment).commit();
        }
    }
}
