package com.app.tao.juheweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tao on 2016/4/19.
 */
public class JuHeWeatherOpenHelper extends SQLiteOpenHelper {

    /**
     * CityList建表语句
     */

    public static final String CREATE_CITY = "create table City(" +
            "id integer primary key autoincrement," + "city_id text," + "province_name text," + "city_name text,"+"district_name text)";

    public JuHeWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
