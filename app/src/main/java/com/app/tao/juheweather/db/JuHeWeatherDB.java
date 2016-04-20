package com.app.tao.juheweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.tao.juheweather.bean.CityList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Tao on 2016/4/19.
 */
public class JuHeWeatherDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "juhe_weather";
    public static final int VERSION = 1;

    private static JuHeWeatherDB mJuHeWeatherDB;
    private SQLiteDatabase db;

    public JuHeWeatherDB(Context context) {
        JuHeWeatherOpenHelper dbHelper = new JuHeWeatherOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static JuHeWeatherDB getInstance(Context context) {
        if (mJuHeWeatherDB == null) {
            mJuHeWeatherDB = new JuHeWeatherDB(context);
        }
        return mJuHeWeatherDB;
    }

    /**
     * 保存CityList对象数据到数据库
     *
     * @param cityList
     */
    public void savedCityList(CityList cityList) {
        if (cityList != null) {
            ContentValues values = new ContentValues();
            values.put("city_id", cityList.getId());
            values.put("province_name", cityList.getProvince());
            values.put("city_name", cityList.getCity());
            values.put("district_name", cityList.getDistrict());
            db.insert("City", null, values);
            values.clear();
        }
    }

    /**
     * 加载Province数据,返回省份List<String>
     */
    public List<String> loadProvinces() {
        List<String> mList = new ArrayList<>();
        Set<String> setList = new HashSet<>();
        Cursor cursor = db.query("City", new String[]{"province_name"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String s = cursor.getString(cursor.getColumnIndex("province_name"));
                setList.add(s);
            } while (cursor.moveToNext());
        }
        mList.addAll(setList);
//        for (String c : mList
//                ) {
//            Log.v("TAC", "" + c);
//        }
        if (cursor != null) {
            cursor.close();
        }
        return mList;
    }

    /**
     * 根据传入的省份加载City数据,返回城市List<String>
     */
    public List<String> loadCities(String selectProvince) {
        List<String> mList = new ArrayList<>();
        if (!selectProvince.isEmpty()) {
            Set<String> setList = new HashSet<>();
            Cursor cursor = db.query("City", new String[]{"city_name"}, "province_name = ?", new String[]{String.valueOf(selectProvince)}, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    String c = cursor.getString(cursor.getColumnIndex("city_name"));
                    setList.add(c);
                } while (cursor.moveToNext());
            }
            mList.addAll(setList);
            if (cursor != null) {
                cursor.close();
            }
        }
        return mList;
    }

    /**
     * 根据传入的城市加载District数据，返回地方List<String>
     */

    public List<String> loadDistricts(String selectedDistrict) {
        List<String> mList = new ArrayList<>();
        if (!selectedDistrict.isEmpty()) {
            Cursor cursor = db.query("City", new String[]{"district_name"}, "city_name = ?", new String[]{String.valueOf(selectedDistrict)}, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    String d = cursor.getString(cursor.getColumnIndex("district_name"));
                    mList.add(d);
                } while (cursor.moveToNext());
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return mList;
    }
}
