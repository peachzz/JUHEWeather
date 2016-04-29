package com.app.tao.juheweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tao.juheweather.R;
import com.app.tao.juheweather.bean.FutureWeatherBean;
import com.app.tao.juheweather.bean.HoursWeatherBean;
import com.app.tao.juheweather.bean.PMBean;
import com.app.tao.juheweather.bean.WeatherBean;
import com.thinkland.sdk.android.DataCallBack;
import com.thinkland.sdk.android.JuheData;
import com.thinkland.sdk.android.Parameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Tao on 2016/4/20.
 */
public class WeatherActivity extends Activity {

    private RelativeLayout rlCity;

    private TextView tvCity;// 城市
    private TextView tvRelease;// 发布时间
    private TextView tvNowWeather;// 天气
    private TextView tvTodayTemp;// 温度
    private TextView tvNowTemp;// 当前温度
    private TextView tvAqi;// 空气质量指数
    private TextView tvQuality;// 空气质量
    private TextView tvNextThree;// 3小时
    private TextView tvNextSix;// 6小时
    private TextView tvNextNine;//9小时
    private TextView tvNextTwelve;// 12小时
    private TextView tvNextFifteen;// 15小时
    private TextView tvNextThreeTemp;// 3小时温度
    private TextView tvNextSixTemp;// 6小时温度
    private TextView tvNextNineTemp;// 9小时温度
    private TextView tvNextTwelveTemp;// 12小时温度
    private TextView tvNextFifteenTemp;// 15小时温度
    private TextView tvTodayTempA;// 今天温度a
    private TextView tvTodayTempB;// 今天温度b
    private TextView tvTommorrow;// 明天
    private TextView tvTommorrowTempA;// 明天温度a
    private TextView tvTommorrowTempB;// 明天温度b
    private TextView tvThirdday;// 第三天
    private TextView tvThirddayTempA;// 第三天温度a
    private TextView tvThirddayTempB;// 第三天温度b
    private TextView tvFourthday;// 第四天
    private TextView tvFourthdayTempA;// 第四天温度a
    private TextView tvFourthdayTempB;// 第四天温度b
    private TextView tvFeltAirTemp;
    private TextView tvHumidity;// 湿度
    private TextView tvWind;//风力
    private TextView tvUvIndex;// 紫外线指数
    private TextView tvDressingIndex;// 穿衣指数
    private TextView tvDressingAdvice;// 穿衣指数

    private ImageView ivNowWeather;// 现在
    private ImageView ivNextThree;// 3小时
    private ImageView ivNextSix;// 6小时
    private ImageView ivNextNine;// 9小时
    private ImageView ivNextTwelve;//12小时
    private ImageView ivNextFifteen;// 15小时
    private ImageView ivTodayWeather;// 今天
    private ImageView ivTommorrowWeather;// 明天
    private ImageView ivThirddayWeather;// 第三天
    private ImageView ivFourthdayWeather;// 第四天

    private WeatherBean weatherBean;
    private FutureWeatherBean futureWeatherBean;
    private PMBean pmBean;
    private HoursWeatherBean hoursWeatherBean;
    private List<HoursWeatherBean> hoursWeatherBeanList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);
        initView();
        String selectedCityName = getIntent().getStringExtra("district_name");
        Log.i("Tag", selectedCityName);
        if (!TextUtils.isEmpty(selectedCityName)) {
            showWeatherData(selectedCityName);
        } else {
            showWeather(weatherBean);
        }
        rlCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, ChooseCity.class);
                startActivity(intent);
            }
        });

    }

    private void showWeatherData(String selectedCityName) {
        getWeatherData(selectedCityName);
        getAirWeather(selectedCityName);
        getHoursWeather(selectedCityName);
    }

    private void showWeather(WeatherBean bean) {
        tvCity.setText(bean.getCity());
        tvRelease.setText(bean.getRelease());
        tvNowWeather.setText(bean.getWeather_str());
        String[] tempArr = bean.getTemp().split("~");
        String temp_str_a = tempArr[1].substring(0, tempArr[1].indexOf("℃"));
        String temp_str_b = tempArr[0].substring(0, tempArr[0].indexOf("℃"));
        // 温度 8℃~16℃" ↑ ↓ °
        tvTodayTemp.setText("↑ " + temp_str_a + "°   ↓" + temp_str_b + "°");
        tvNowTemp.setText(bean.getNow_temp() + " °");
        ivTodayWeather.setImageResource(getResources().getIdentifier("d" + bean.getWeather_id(), "drawable", "com.app.tao.juheweather"));

        tvTodayTempA.setText(temp_str_a + "°");
        tvTodayTempB.setText(temp_str_b + "°");

        List<FutureWeatherBean> futureList = bean.getFutureList();
        if (futureList != null && futureList.size() == 3) {
            setFutureData(tvTommorrow, ivTommorrowWeather, tvTommorrowTempA, tvTommorrowTempB, futureList.get(0));
            setFutureData(tvThirdday, ivThirddayWeather, tvThirddayTempA, tvThirddayTempB, futureList.get(1));
            setFutureData(tvFourthday, ivFourthdayWeather, tvFourthdayTempA, tvFourthdayTempB, futureList.get(2));
        }

        Calendar c = Calendar.getInstance();
        int time = c.get(Calendar.HOUR_OF_DAY);
        String prefixStr = null;
        if (time >= 6 && time < 18) {
            prefixStr = "d";
        } else {
            prefixStr = "n";
        }
        ivNowWeather.setImageResource(getResources().getIdentifier(prefixStr + bean.getWeather_id(), "drawable", "com.app.tao.juheweather"));

        tvHumidity.setText(bean.getHumidity());
        tvDressingIndex.setText(bean.getDressing_index());
//        tvDressingAdvice.setText(bean.getDressing_advice());
        tvFeltAirTemp.setText(bean.getNow_temp()+"℃");
        tvUvIndex.setText(bean.getUv_index());
        tvWind.setText(bean.getWind());
    }

    private void setHourData(TextView tv_hour, ImageView iv_weather, TextView tv_temp, HoursWeatherBean bean) {

        String prefixStr = null;
        int time = Integer.valueOf(bean.getTime());
        if (time >= 6 && time < 18) {
            prefixStr = "d";
        } else {
            prefixStr = "n";
        }

        tv_hour.setText(bean.getTime() + "时");
        iv_weather.setImageResource(getResources().getIdentifier(prefixStr + bean.getWeather_id(), "drawable", "com.app.tao.juheweather"));
        tv_temp.setText(bean.getTemp() + "°");
    }

    private void setFutureData(TextView tv_week, ImageView iv_weather, TextView tv_temp_a, TextView tv_temp_b, FutureWeatherBean bean) {
        tv_week.setText(bean.getWeek());
        iv_weather.setImageResource(getResources().getIdentifier("d" + bean.getWeather_id(), "drawable", "com.juhe.weather"));
        String[] tempArr = bean.getTemp().split("~");
        String temp_str_a = tempArr[1].substring(0, tempArr[1].indexOf("℃"));
        String temp_str_b = tempArr[0].substring(0, tempArr[0].indexOf("℃"));
        tv_temp_a.setText(temp_str_a + "°");
        tv_temp_b.setText(temp_str_b + "°");
    }


    private void getWeatherData(String city) {

        Parameters params = new Parameters();
        params.add("cityname", city);
        params.add("dtype", "json");
        params.add("format", 2);

        JuheData.executeWithAPI(this, 39, "http://v.juhe.cn/weather/index", JuheData.GET, params, new DataCallBack() {
            @Override
            public void onSuccess(int i, String s) {
                if (i == 200) {

                    WeatherBean bean = handleWeatherJson(s);
                    showWeather(bean);
                }
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "天气更新失败！", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void getAirWeather(String city) {
        Parameters params2 = new Parameters();
        params2.add("city", city);
        JuheData.executeWithAPI(this, 33, "http://web.juhe.cn:8080/environment/air/cityair", JuheData.GET, params2, new DataCallBack() {
            @Override
            public void onSuccess(int i, String s) {
                if (i == 200) {
                    pmBean = handleAirWeatherJson(s);
                    showAirWeather(pmBean);
                }
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "PM2.5信息更新失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAirWeather(PMBean pmBean) {
        tvAqi.setText(pmBean.getAqi());
        tvQuality.setText(pmBean.getQuality());
    }

    private void getHoursWeather(String city) {
        Parameters params1 = new Parameters();
        params1.add("cityname", city);
        params1.add("dtype", "json");
        JuheData.executeWithAPI(this, 39, "http://v.juhe.cn/weather/forecast3h", JuheData.GET, params1, new DataCallBack() {
            @Override
            public void onSuccess(int i, String s) {
                if (i == 200) {
                    hoursWeatherBeanList = handleForecastWeatherJson(s);
                    showHoursWeather(hoursWeatherBeanList);
                }
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int i, String s, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "未来三天天气更新失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showHoursWeather(List<HoursWeatherBean> list) {
        if (list != null && list.size() == 5) {
            setHourData(tvNextThree, ivNextThree, tvNextThreeTemp, list.get(0));
            setHourData(tvNextSix, ivNextSix, tvNextSixTemp, list.get(1));
            setHourData(tvNextNine, ivNextNine, tvNextNineTemp, list.get(2));
            setHourData(tvNextTwelve, ivNextTwelve, tvNextTwelveTemp, list.get(3));
            setHourData(tvNextFifteen, ivNextFifteen, tvNextFifteenTemp, list.get(4));
        }
    }

    private WeatherBean handleWeatherJson(String s) {
        WeatherBean weatherBean = new WeatherBean();
        if (!TextUtils.isEmpty(s)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

                JSONObject jsonData = new JSONObject(s);
                String resultcode = jsonData.getString("resultcode");
                int error_code = jsonData.getInt("error_code");
                JSONObject result = jsonData.getJSONObject("result");
                if (resultcode.equals("200") && error_code == 0) {

                    JSONObject today = result.getJSONObject("today");
                    //today
                    weatherBean.setCity(today.getString("city"));
                    weatherBean.setWeather_id(today.getJSONObject("weather_id").getString("fa"));
                    weatherBean.setWeather_str(today.getString("weather"));
                    weatherBean.setTemp(today.getString("temperature"));
                    weatherBean.setDressing_index(today.getString("dressing_index"));
                    weatherBean.setDressing_advice(today.getString("dressing_advice"));//穿衣建议
                    weatherBean.setUv_index(today.getString("uv_index"));
                    //sk
                    JSONObject sk = result.getJSONObject("sk");
                    weatherBean.setNow_temp(sk.getString("temp"));
                    weatherBean.setRelease(today.getString("date_y") + " " + sk.getString("time"));//2016年04月29日 13:43
                    weatherBean.setHumidity(sk.getString("humidity"));
                    weatherBean.setWind(sk.getString("wind_direction")+ "~"+ sk.getString("wind_strength"));//东北风 2级
                    //futureWeather
                    Date date = new Date(System.currentTimeMillis());
                    JSONArray future = result.getJSONArray("future");
                    List<FutureWeatherBean> fList = new ArrayList<>();
                    for (int i = 0; i < future.length(); i++) {
                        JSONObject futureData = future.getJSONObject(i);
                        FutureWeatherBean futureBean = new FutureWeatherBean();
                        Date datef = sdf.parse(futureData.getString("date"));
                        if (!datef.after(date)) {
                            continue;
                        }
                        futureBean.setWeek(futureData.getString("week"));
                        futureBean.setWeather_id(futureData.getJSONObject("weather_id").getString("fa"));
                        futureBean.setTemp(futureData.getString("temperature"));
                        futureBean.setDate(futureData.getString("date"));
                        fList.add(futureBean);
                        if (fList.size() == 3) {
                            break;
                        }
                    }
                    weatherBean.setFutureList(fList);
                } else {
                    Toast.makeText(this, "天气数据获取失败！", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return weatherBean;
    }


    private List<HoursWeatherBean> handleForecastWeatherJson(String s) {
        hoursWeatherBeanList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        Date currentData = new Date(System.currentTimeMillis());
        if (!TextUtils.isEmpty(s)) {
            try {
                JSONObject jsonData = new JSONObject(s);
                String resultcode = jsonData.getString("resultcode");
                int error_code = jsonData.getInt("error_code");
                if (resultcode.equals("200") && error_code == 0) {
                    JSONArray hourArray = jsonData.getJSONArray("result");
                    for (int i = 0; i < hourArray.length(); i++) {
                        JSONObject hJson = hourArray.getJSONObject(i);
                        Date hDate = sdf.parse(hJson.getString("sfdate"));
                        //假如现在14:00，跳过早于这个时间的循环
                        if (!hDate.after(currentData)) {
                            continue;
                        }
                        HoursWeatherBean hBean = new HoursWeatherBean();
                        hBean.setTemp(hJson.getString("temp1") + "℃~" + hJson.getString("temp2") + "℃");
                        hBean.setWeather_id(hJson.getString("weatherid"));
                        Calendar c = Calendar.getInstance();
                        c.setTime(hDate);
                        hBean.setTime(c.get(Calendar.HOUR_OF_DAY) + "");
                        hoursWeatherBeanList.add(hBean);
                        if (hoursWeatherBeanList.size() == 5) {
                            break;
                        }
                    }
                } else {
                    Toast.makeText(this, "三小时天气数据获取失败！", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return hoursWeatherBeanList;
    }


    private PMBean handleAirWeatherJson(String s) {
        if (!TextUtils.isEmpty(s)) {
            try {
                JSONObject airJson = new JSONObject(s);
                String resultcode = airJson.getString("resultcode");
                int error_code = airJson.getInt("error_code");
                if (resultcode.equals("200") && error_code == 0) {
                    JSONArray result = airJson.getJSONArray("result");
                    JSONObject airData = result.getJSONObject(0);
                    JSONObject cityNow = airData.getJSONObject("citynow");
                    pmBean = new PMBean();
                    pmBean.setAqi(cityNow.getString("AQI"));
                    pmBean.setQuality(cityNow.getString("quality"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return pmBean;
    }

    private void initView() {
        rlCity = (RelativeLayout) findViewById(R.id.rl_city);
        tvCity = (TextView) findViewById(R.id.tv_city);
        tvRelease = (TextView) findViewById(R.id.tv_release);
        ivNowWeather = (ImageView) findViewById(R.id.iv_now_weather);
        tvNowWeather = (TextView) findViewById(R.id.tv_now_weather);
        tvTodayTemp = (TextView) findViewById(R.id.tv_today_temp);
        tvNowTemp = (TextView) findViewById(R.id.tv_now_temp);
        tvAqi = (TextView) findViewById(R.id.tv_aqi);
        tvQuality = (TextView) findViewById(R.id.tv_quality);
        tvNextThree = (TextView) findViewById(R.id.tv_next_three);
        tvNextSix = (TextView) findViewById(R.id.tv_next_six);
        tvNextNine = (TextView) findViewById(R.id.tv_next_nine);
        tvNextTwelve = (TextView) findViewById(R.id.tv_next_twelve);
        tvNextFifteen = (TextView) findViewById(R.id.tv_next_fifteen);
        ivNextThree = (ImageView) findViewById(R.id.iv_next_three);
        ivNextSix = (ImageView) findViewById(R.id.iv_next_six);
        ivNextNine = (ImageView) findViewById(R.id.iv_next_nine);
        ivNextTwelve = (ImageView) findViewById(R.id.iv_next_twelve);
        ivNextFifteen = (ImageView) findViewById(R.id.iv_next_fifteen);
        tvNextThreeTemp = (TextView) findViewById(R.id.tv_next_three_temp);
        tvNextSixTemp = (TextView) findViewById(R.id.tv_next_six_temp);
        tvNextNineTemp = (TextView) findViewById(R.id.tv_next_nine_temp);
        tvNextTwelveTemp = (TextView) findViewById(R.id.tv_next_twelve_temp);
        tvNextFifteenTemp = (TextView) findViewById(R.id.tv_next_fifteen_temp);
        ivTodayWeather = (ImageView) findViewById(R.id.iv_today_weather);
        tvTodayTempA = (TextView) findViewById(R.id.tv_today_temp_a);
        tvTodayTempB = (TextView) findViewById(R.id.tv_today_temp_b);
        tvTommorrow = (TextView) findViewById(R.id.tv_tommorrow);
        ivTommorrowWeather = (ImageView) findViewById(R.id.iv_tommorrow_weather);
        tvTommorrowTempA = (TextView) findViewById(R.id.tv_tommorrow_temp_a);
        tvTommorrowTempB = (TextView) findViewById(R.id.tv_tommorrow_temp_b);
        tvThirdday = (TextView) findViewById(R.id.tv_thirdday);
        ivThirddayWeather = (ImageView) findViewById(R.id.iv_thirdday_weather);
        tvThirddayTempA = (TextView) findViewById(R.id.tv_thirdday_temp_a);
        tvThirddayTempB = (TextView) findViewById(R.id.tv_thirdday_temp_b);
        tvFourthday = (TextView) findViewById(R.id.tv_fourthday);
        ivFourthdayWeather = (ImageView) findViewById(R.id.iv_fourthday_weather);
        tvFourthdayTempA = (TextView) findViewById(R.id.tv_fourthday_temp_a);
        tvFourthdayTempB = (TextView) findViewById(R.id.tv_fourthday_temp_b);
        tvFeltAirTemp = (TextView) findViewById(R.id.tv_felt_air_temp);
        tvHumidity = (TextView) findViewById(R.id.tv_humidity);
        tvWind = (TextView) findViewById(R.id.tv_wind);
        tvUvIndex = (TextView) findViewById(R.id.tv_uv_index);
        tvDressingIndex = (TextView) findViewById(R.id.tv_dressing_index);
//        tvDressingAdvice = (TextView) findViewById(R.id.tv_dressing_advice);
    }
}
