package com.app.tao.juheweather.bean;

/**
 * Created by Tao on 2016/4/19.
 */
public class CityList {

    /**
     * id : 1
     * province : 北京
     * city : 北京
     * district : 北京
     */

    private String id;
    private String province;
    private String city;
    private String district;

    public void setId(String id) {
        this.id = id;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getId() {
        return id;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

//    @Override
//    public String toString() {
//        return "CityList[id = "+id+",province = "+province+",city = "+city+",district = "+district+"] ";
//    }
}