package com.qiang.coolweather.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者:  qiang on 2016/12/28 22:15
 * 邮箱:  anworkmail_q@126.com
 * 作用:
 */

public abstract class BaseFragment extends Fragment {
    private Unbinder bind;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(),container,false);
        bind = ButterKnife.bind(this, view);
        initOnCreateView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAll();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
    }

    protected abstract void initAll();
    protected void initOnCreateView(){}
    protected abstract int getLayoutRes();
}
