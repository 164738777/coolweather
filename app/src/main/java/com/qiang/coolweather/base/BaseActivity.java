package com.qiang.coolweather.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * 作者:  qiang on 2016/12/28 22:17
 * 邮箱:  anworkmail_q@126.com
 * 作用:
 */

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        ButterKnife.bind(this);
        initAll();
    }

    protected abstract void initAll();
    protected abstract int getLayoutRes();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onRelease();
    }

    private void onRelease() {}
}
