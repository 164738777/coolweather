package com.qiang.coolweather.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qiang.coolweather.R;
import com.qiang.coolweather.base.BaseFragment;
import com.qiang.coolweather.base.Constant;
import com.qiang.coolweather.db.City;
import com.qiang.coolweather.db.County;
import com.qiang.coolweather.db.Province;
import com.qiang.coolweather.util.HttpUtil;
import com.qiang.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * 作者:  qiang on 2016/12/28 22:26
 * 邮箱:  anworkmail_q@126.com
 * 作用:
 */

public class ChooseAreaFragment extends BaseFragment {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.listView)
    ListView lv;

    private ProgressDialog progressDialog;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    private List<Province> provinces;
    private List<City> cities;
    private List<County> counties;
    private Province selectedProvince;
    private City selectedCity;
    private County selectedCounty;
    private int currentLevel;


    @Override
    protected void initAll() {
        lv.setOnItemClickListener((parent, view, position, id) -> {
            switch (currentLevel) {
                case Constant.LEVEL_PROVINCE:
                    selectedProvince = provinces.get(position);
                    queryCities();
                    break;
                case Constant.LEVEL_CITY:
                    selectedCity = cities.get(position);
                    queryCounties();
                    break;
                case Constant.LEVEL_COUNTY:
                    String weatherId = counties.get(position).getWeatherId();
                    Intent intent = new Intent(getActivity(), WeatherActivity.class);
                    intent.putExtra(Constant.KEY_WEATHER_ID, weatherId);
                    startActivity(intent);
                    getActivity().finish();
                    break;
            }
        });

        queryProvinces();
    }


    @Override
    protected void initOnCreateView() {
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        lv.setAdapter(adapter);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.frg_choose_area;
    }

    @OnClick(R.id.btn_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                if (currentLevel == Constant.LEVEL_COUNTY) {
                    queryCities();
                } else if (currentLevel == Constant.LEVEL_CITY) {
                    queryProvinces();
                }
                break;
        }
    }

    private void queryProvinces() {
        //查询省,优先从数据库查
        tvTitle.setText(getActivity().getResources().getString(R.string.china));
        btnBack.setVisibility(View.GONE);
        provinces = DataSupport.findAll(Province.class);
        if (provinces.size() > 0) {
            dataList.clear();
            for (Province province : provinces) {
                dataList.add(province.getProvinceName());
            }
            updateListView(Constant.LEVEL_PROVINCE);
        } else {
            queryFromServer(Constant.BASE_PATH_CITY, "province");
        }
    }

    private void queryCities() {
        tvTitle.setText(selectedProvince.getProvinceName());
        btnBack.setVisibility(View.VISIBLE);
        cities = DataSupport.where("provinceId = ?", String.valueOf(selectedProvince.getId())).find(City.class);
        if (cities.size() > 0) {
            dataList.clear();
            for (City city : cities) {
                dataList.add(city.getCityName());
            }
            updateListView(Constant.LEVEL_CITY);
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            queryFromServer(Constant.BASE_PATH_CITY + provinceCode, "city");
        }
    }

    private void queryCounties() {
        tvTitle.setText(selectedCity.getCityName());
        btnBack.setVisibility(View.VISIBLE);
        counties = DataSupport.where("cityId = ?", String.valueOf(selectedCity.getId())).find(County.class);
        if (counties.size() > 0) {
            dataList.clear();
            for (County county : counties) {
                dataList.add(county.getCountyName());
            }
            updateListView(Constant.LEVEL_COUNTY);
        } else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            queryFromServer(Constant.BASE_PATH_CITY + provinceCode + "/" + cityCode, "county");
        }
    }

    private void updateListView(int lever) {
        adapter.notifyDataSetChanged();
        lv.setSelection(0);
        currentLevel = lever;
    }


    private void queryFromServer(String basePath, final String type) {
        ShowProgressDialog();
        HttpUtil.sendOkHttpRequest(basePath, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                switch (type) {
                    case "province":
                        result = Utility.handleProvinceResponse(responseText);
                        break;
                    case "city":
                        result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                        break;
                    case "county":
                        result = Utility.handleCountyResponse(responseText, selectedCity.getId());
                        break;
                }
                if (result) {
                    getActivity().runOnUiThread(() -> {
                        closeProgressDialog();
                        switch (type) {
                            case "province":
                                queryProvinces();
                                break;
                            case "city":
                                queryCities();
                                break;
                            case "county":
                                queryCounties();
                                break;
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(() -> {
                    closeProgressDialog();
                    Toast.makeText(getContext(), getActivity().getResources().getString(R.string.failed_load), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void closeProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void ShowProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getActivity().getResources().getString(R.string.loading));
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
}
