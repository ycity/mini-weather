package com.cypyc.mini_weather.app;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.cypyc.mini_weather.beans.City;
import com.cypyc.mini_weather.db.CityDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuncity on 2017/10/30.
 */

public class MyApplication extends Application {

    private static final String TAG = "City";
    private static MyApplication myApp;
    private CityDB cityDB;
    private List<City> cityList;

    public void onCreate() {
        super.onCreate();
        myApp = this;
        cityDB = openCityDB();
        initCityList();
    }

    private void initCityList() {
        cityList = new ArrayList<City>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareCityList();
            }
        }).start();
    }

    private boolean prepareCityList() {
        cityList = cityDB.getAllCity();
        int i = 0;
        for (City city : cityList) {
            i++;
            String cityName = city.getName();
            String cityCode = city.getNumber();
            Log.d(TAG, cityCode + ":" + cityName);
        }
        Log.d(TAG, "i=" + i);
        return true;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public static MyApplication getInstance() {
        return myApp;
    }

    private CityDB openCityDB() {
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + getPackageName()
                + File.separator + "databases1"
                + File.separator
                + CityDB.CITY_DB_NAME;
        File db = new File(path);
        Log.d(TAG, path);
        if (!db.exists()) {
            String pathfolder = "/data"
                    + Environment.getDataDirectory().getAbsolutePath()
                    + File.separator + getPackageName()
                    + File.separator + "databases1"
                    + File.separator;
            File dirFirstFolder = new File(pathfolder);
            if (!dirFirstFolder.exists()) {
                dirFirstFolder.mkdirs();
                Log.i("MyApp", "mkdirs");
            }
            Log.i("MyApp", "db is not exists");
            try {
                InputStream is = getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(this, path);
    }
}
