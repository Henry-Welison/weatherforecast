package com.example.administrator.weatherforecast.db;

import org.litepal.crud.DataSupport;

public class City extends DataSupport {

    private int id;          //id为每个实体类应该有的字段

    private int cityCode;    //城市的代号

    private String cityName; //城市的名字

    private int provinceId;  //当前市所属的省的id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
