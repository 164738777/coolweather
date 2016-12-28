package com.qiang.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * 作者:  qiang on 2016/12/28 20:19
 * 邮箱:  anworkmail_q@126.com
 * 作用:
 */

public class Province extends DataSupport{
    private int id ;
    private String provinceName;
    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
