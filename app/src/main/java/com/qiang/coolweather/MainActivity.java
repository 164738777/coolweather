package com.qiang.coolweather;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.qiang.coolweather.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void initAll() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        ChooseAreaFragment fragment = new ChooseAreaFragment();
        transaction.add(R.id.ll, fragment).show(fragment).commit();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }
}
