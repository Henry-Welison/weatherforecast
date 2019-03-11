package com.example.administrator.weatherforecast.db;

import org.litepal.crud.DataSupport;

public class Province extends DataSupport {

    private int id;              //id为每个实体类应该有的字段

    private String provinceName; //provinceName：省的名字

    private int provinceCode;    //provinceCode：记录省得带好

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
