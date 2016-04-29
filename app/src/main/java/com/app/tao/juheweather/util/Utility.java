package com.app.tao.juheweather.util;

import android.text.TextUtils;

import com.app.tao.juheweather.bean.CityList;
import com.app.tao.juheweather.bean.CityStatus;
import com.app.tao.juheweather.db.JuHeWeatherDB;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Tao on 2016/4/19.
 */
public class Utility {


    public synchronized static boolean handleCityJson(JuHeWeatherDB juHeWeatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            Gson gson = new Gson();
            CityStatus state = gson.fromJson(response, CityStatus.class);
            List<CityList> result = state.getResult();
            for (CityList c : result
                    ) {
                juHeWeatherDB.savedCityList(c);
            }
            return true;
        }
        return false;
    }
}