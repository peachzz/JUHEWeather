package com.app.tao.juheweather.bean;

import java.util.List;

/**
 * Created by Tao on 2016/4/19.
 */
public class State {

    /**
     * resultcode : 200
     * reason : successed
     * result : [{"id":"1","province":"北京","city":"北京","district":"北京"},{"id":"2","province":"北京","city":"北京","district":"海淀"},{"id":"3","province":"北京","city":"北京","district":"朝阳"},{"id":"4","province":"北京","city":"北京","district":"顺义"},{"id":"5","province":"北京","city":"北京","district":"怀柔"},{"id":"6","province":"北京","city":"北京","district":"通州"},{"id":"7","province":"北京","city":"北京","district":"昌平"},{"id":"8","province":"北京","city":"北京","district":"延庆"},{"id":"9","province":"北京","city":"北京","district":"丰台"},{"id":"10","province":"北京","city":"北京","district":"石景山"},{"id":"11","province":"北京","city":"北京","district":"大兴"},{"id":"12","province":"北京","city":"北京","district":"房山"},{"id":"13","province":"北京","city":"北京","district":"密云"},{"id":"14","province":"北京","city":"北京","district":"门头沟"},{"id":"15","province":"北京","city":"北京","district":"平谷"},{"id":"16","province":"上海","city":"上海","district":"上海"},{"id":"17","province":"上海","city":"上海","district":"闵行"},{"id":"18","province":"上海","city":"上海","district":"宝山"},{"id":"19","province":"上海","city":"上海","district":"嘉定"},{"id":"20","province":"上海","city":"上海","district":"南汇"},{"id":"21","province":"上海","city":"上海","district":"金山"},{"id":"22","province":"上海","city":"上海","district":"青浦"},{"id":"23","province":"上海","city":"上海","district":"松江"},{"id":"24","province":"上海","city":"上海","district":"奉贤"},{"id":"25","province":"上海","city":"上海","district":"崇明"},{"id":"26","province":"上海","city":"上海","district":"徐家汇"},{"id":"27","province":"上海","city":"上海","district":"浦东"}]
     * error_code : 0
     */

    private String resultcode;
    private String reason;
    private int error_code;
    private List<CityList> result;

    public void setResult(List<CityList> result) {
        this.result = result;
    }

    public List<CityList> getResult() {
        return result;
    }
    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getResultcode() {
        return resultcode;
    }

    public String getReason() {
        return reason;
    }

    public int getError_code() {
        return error_code;
    }

//    @Override
//    public String toString() {
//        return "State[resultcode = "+getResultcode()+",reason = "+reason+",error_code = "+error_code+"]";
//    }
}
