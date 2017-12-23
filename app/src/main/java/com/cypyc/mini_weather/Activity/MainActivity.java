package com.cypyc.mini_weather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cypyc.mini_weather.R;
import com.cypyc.mini_weather.beans.TodayWeather;
import com.cypyc.mini_weather.util.NetUtil;
import com.cypyc.mini_weather.util.XMLUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yuncity on 2017/10/26.
 */
public class MainActivity extends AppCompatActivity {

    private static final int UPDATE_TODAY_WEATHER = 1;
    private ImageView updateBtn, selectCityBtn;
    private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv, pmQualityTv, temperatureTv, climateTv, windTv, city_name_Tv;
    private ImageView weatherImg, pmImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.weather_info); // 设置layout
        setButtonListener(); // 设置各个按钮的监听
        checkNetStat(); // 检查网络状况
        initView(); // 对界面进行初始化
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String cityCode = data.getStringExtra("cityCode");
                    Log.d("cityCode", cityCode);
                    queryWeatherByCityCode(cityCode);
                }
                break;
            default:
        }
        Intent intent = getIntent();
        String cityCode = intent.getStringExtra("cityCode");
        // Log.d("cityCode", cityCode);
        // queryWeatherByCityCode(cityCode);

    }


    /* 创建一个Handler实例并重写其handleMessage函数 START */
    private Handler handler = new Handler() {
        /**
         * methodName: handleMessage
         * description: 通过message中保存的数值来进行处理
         * parameters: @msg 一个Message
         * return: void
         */
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };
    /* 创建一个Handler实例并重写其handleMessage函数 END */


    /**
     * methodName: setButton
     * description: 对各项按钮设置监听
     * parameters: void
     * return: void
     */
    private void setButtonListener() {
        /* 设置更新按钮 start */
        updateBtn = (ImageView) findViewById(R.id.title_update_btn);
        updateBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.title_update_btn) {
                    Log.d("setUptBtn", "Yes");
                    SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE); // 设置sharedPreferences的文件名为config.xml，操作模式为私有数据
                    String cityCode = sharedPreferences.getString("main_city_code", "101010200"); // 获取sharedPreferences中的默认城市代码，若找不到该项则默认返回北京市海淀区的代码
                    queryWeatherByCityCode(cityCode); // 用获取的城市代码查询当地的天气
                }
            }
        });
        /* 设置更新按钮 end */

        /* 设置选择城市按钮 start */
        selectCityBtn = (ImageView) findViewById(R.id.title_city_manager);
        selectCityBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.title_city_manager) {
                    Intent i = new Intent(MainActivity.this, SelectCityActivity.class); // 创建一个意图
                    startActivityForResult(i, 1); // 打开选择城市Activity
                }
            }
        });
        /* 设置选择城市按钮 end */
    }


    /**
     * methodName: checkNetStat
     * description: 检查当前网络状态，并弹出提示
     * parameters: void
     * return: void
     */
    public void checkNetStat() {
        /* 检查网络状态 start */
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Toast.makeText(MainActivity.this, "网络OK！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
        }
        /* 检查网络状态 end */
    }


    /**
     * methodName: queryWeatherByCityCode
     * description: 通过城市代码查询当地天气
     * parameters: @cityCode 城市代码
     * return: void
     */
    private void queryWeatherByCityCode(String cityCode) {

        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("url", address);
        /* 创建一个线程查询城市数据 START */
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                TodayWeather todayWeather = null;
                try {
                    /* 设置连接参数 START */
                    URL url = new URL(address);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    /* 设置连接参数 END */

                    /* 创建输入流，并逐行读取站点中的信息，最终保存在content字符串中 START */
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder buffer = new StringBuilder();
                    String line, content;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    content = buffer.toString();
                    /* 创建输入流，并逐行读取站点中的信息，最终保存在content字符串中 END */

                    /* 对读取的天气信息进行解析 START */
                    todayWeather = XMLUtil.parseXML(content);
                    if (todayWeather != null) {
                        Message msg = new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj = todayWeather;
                        handler.sendMessage(msg);
                        handler.handleMessage(msg);
                    }
                    /* 对读取的天气信息进行解析 END */
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();
        /* 创建一个线程查询城市数据 END */

    }


    /**
     * methodName: initView
     * description: 对界面进行初始化
     * parameters: void
     * return: void
     */
    void initView() {
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);
        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
    }


    /**
     * methodName: updateTodayWeather
     * description: 将todayWeather中的各项信息更新到界面中
     * parameters: @todayWeather 一个表示今日天气的类对象
     * return: updateTodayWeather
     */
    void updateTodayWeather(TodayWeather todayWeather) {
        city_name_Tv.setText(todayWeather.getCity() + "天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime() + "发布");
        humidityTv.setText("湿度：" + todayWeather.getShidu());
        if (todayWeather.getPm25() == null) {
            todayWeather.setPm25("-");
        }
        pmDataTv.setText(todayWeather.getPm25());
        if (todayWeather.getQuality() == null) {
            todayWeather.setQuality("-");
        }
        pmQualityTv.setText(todayWeather.getQuality());
        // updatePMImg(todayWeather.getPm25());
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getHigh() + "~" + todayWeather.getLow());
        climateTv.setText(todayWeather.getType());
        updateWeatherImg(todayWeather.getType());
        windTv.setText("风力:" + todayWeather.getFengli());
        Toast.makeText(MainActivity.this, "更新成功！", Toast.LENGTH_SHORT).show();
    }


    /**
     * methodName: updateWeatherImg
     * description: 根据传入的信息更新对应的天气图片
     * parameters: @weather 天气
     * return: void
     */
    private void updateWeatherImg(String weather) {
        switch (weather) {
            case "暴雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);
                break;
            case "暴雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);
                break;
            case "大暴雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
                break;
            case "大雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);
                break;
            case "大雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);
                break;
            case "多云":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);
                break;
            case "雷阵雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
                break;
            case "雷阵雨冰雹":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
                break;
            case "沙尘暴":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
                break;
            case "晴":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);
                break;
            case "雾":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);
                break;
            case "小雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
                break;
            case "小雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
                break;
            case "阴":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);
                break;
            case "雨夹雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
                break;
            case "阵雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
                break;
            case "阵雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
                break;
            case "中雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
                break;
            case "中雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
                break;
        }
    }


    private void updatePMImg(String pm) {
        int pmVal = Integer.parseInt(pm);
        if (pmVal <= 50) {
            pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
        } else if (pmVal <= 100) {
            pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);
        } else if (pmVal <= 150) {
            pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);
        } else if (pmVal <= 200) {
            pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
        } else if (pmVal <= 300) {
            pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
        } else {
            pmImg.setImageResource(R.drawable.biz_plugin_weather_greater_300);
        }
    }

}