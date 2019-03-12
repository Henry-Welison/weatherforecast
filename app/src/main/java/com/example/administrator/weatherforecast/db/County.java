package com.example.administrator.weatherforecast.db;

import org.litepal.crud.DataSupport;

public class County extends DataSupport {

    private int id;

    private String countyName;   //县的名字

    private int countyId;        //县的id

    private int cityId;          //县所在的市的id

    private int weatherId;       //县对应的天气的id

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getCountyId() {
        return countyId;
    }

    public void setCountyId(int countyId) {
        this.countyId = countyId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
