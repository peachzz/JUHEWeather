package com.app.tao.juheweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tao.juheweather.R;
import com.app.tao.juheweather.bean.CityList;
import com.app.tao.juheweather.db.JuHeWeatherDB;
import com.app.tao.juheweather.util.Utility;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import java.util.ArrayList;
import java.util.List;

public class ChooseCity extends AppCompatActivity {

    private CityList mCityList;
    private JuHeWeatherDB juHeWeatherDB;
    private List<CityList> result = new ArrayList<>();

    private ListView mListView;
    private TextView titleText;
    private ArrayAdapter<String> mAdapter;

    public static final int LEVEL_PROVINCE = 1;
    public static final int LEVEL_CITY = 2;
    public static final int LEVEL_DISTRICT = 3;
    private int currentLevel;//当前级别

    private String selectedProvince;
    private String selectedCity;
//    private String selectProvince;

    private List<String> provinceList;
    private List<String> cityList;
    private List<String> districtList;
    private List<String> dataList = new ArrayList<>();//加载数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("city_selected",false)){
            Intent intent = new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.choose_city);
        mListView = (ListView) findViewById(R.id.ls_view);
        titleText = (TextView) findViewById(R.id.title_tv);

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        mListView.setAdapter(mAdapter);
        juHeWeatherDB = JuHeWeatherDB.getInstance(this);
        mCityList = new CityList();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCitise();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryDistricts();
                }else if (currentLevel == LEVEL_DISTRICT){
                    String selectedDistrict = districtList.get(position);
                    Intent i = new Intent(ChooseCity.this,WeatherActivity.class);
                    i.putExtra("district_name",selectedDistrict);
                   startActivity(i);
                }
            }
        });
        queryProvinces();
    }

    private void queryCityListFromServer(final String type) {
        Parameters params = new Parameters();
        params.add("dtype", "json");
        JuheData.executeWithAPI(getApplicationContext(), 39, "http://v.juhe.cn/weather/citys", JuheData.GET, params, new DataCallBack() {
            @Override
            public void onSuccess(int i, final String jsonData) {
                boolean result = false;
                if (i == 200 && ("province".equals(type) || "city".equals(type) || "district".equals(type))) {
                    result = Utility.handleCityJson(JuHeWeatherDB.getInstance(getApplicationContext()), jsonData);
                }
                if (result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCitise();
                            } else if ("district".equals(type)) {
                                queryDistricts();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFinish() {
//                Toast.makeText(getApplicationContext(), "finish",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {
//                mTextView.append(throwable.getMessage() + "\n");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "数据加载失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 查询全国省份
     */
    private void queryProvinces() {
        provinceList = juHeWeatherDB.loadProvinces();
        if (provinceList.size() > 0) {
            dataList.clear();
            for (String p : provinceList
                    ) {
                dataList.add(p);
            }
            mAdapter.notifyDataSetChanged();//刷新ListView
            mListView.setSelection(0);//定位到第一个Item
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        } else {
            queryCityListFromServer("province");
        }
    }

    /**
     * 根据选中的省份查询城市
     */
    private void queryCitise() {
        cityList = juHeWeatherDB.loadCities(selectedProvince);
        if (cityList.size() > 0) {
            dataList.clear();
            for (String c : cityList
                    ) {
                dataList.add(c);
            }
            mAdapter.notifyDataSetChanged();//刷新ListView
            mListView.setSelection(0);//定位到第一个Item
            titleText.setText(selectedProvince);
            currentLevel = LEVEL_CITY;
        } else {
            queryCityListFromServer("city");
        }
    }

    /**
     * 根据选中的城市查询地区
     */
    private void queryDistricts() {
        districtList = juHeWeatherDB.loadDistricts(selectedCity);
        if (districtList.size() > 0) {
            dataList.clear();
            for (String d : districtList
                    ) {
                dataList.add(d);
            }
            mAdapter.notifyDataSetChanged();//刷新ListView
            mListView.setSelection(0);//定位到第一个Item
            titleText.setText(selectedCity);
            currentLevel = LEVEL_DISTRICT;
        } else {
            queryCityListFromServer("district");
        }
    }

    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_DISTRICT) {
            queryCitise();
        } else if (currentLevel == LEVEL_CITY) {
            queryProvinces();
        } else {
            finish();
        }
    }
}