package com.cypyc.mini_weather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.cypyc.mini_weather.R;
import com.cypyc.mini_weather.Views.ClearEditText;
import com.cypyc.mini_weather.adapter.CityAdapter;
import com.cypyc.mini_weather.app.MyApplication;
import com.cypyc.mini_weather.beans.City;

import java.util.List;

/**
 * Created by yuncity on 2017/11/1.
 */

public class SelectCityActivity extends Activity {

    private ImageView backBtn;
    private ClearEditText clearEditText;
    private ListView citylistView;
    private List<City> cityList;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_city);
        setButtonListener();
        initViews();
    }


    /**
     * methodName: setButton
     * description: 对各项按钮设置监听
     * parameters: void
     * return: void
     */
    private void setButtonListener() {
        /* 设置返回按钮 start */
        backBtn = (ImageView) findViewById(R.id.title_back);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.title_back) {
                    finish();
                }
            }
        });
        /* 设置返回按钮 end */
    }


    private void initViews() {

//        clearEditText = (ClearEditText) findViewById(R.id.search_city);

        MyApplication myApplication = (MyApplication) getApplication();
        cityList = myApplication.getCityList();

        ArrayAdapter<String> adapter = new CityAdapter(SelectCityActivity.this, R.layout.city_item, cityList);
        citylistView = (ListView) findViewById(R.id.city_list);
        citylistView.setAdapter(adapter);

        citylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(SelectCityActivity.this, "你单击了：" + position, Toast.LENGTH_SHORT).show();
                City city = cityList.get(position);
                Intent intent = new Intent();
                intent.putExtra("cityCode", city.getNumber());
                finish();
            }
        });


/*        for (City city : cityList) {
            filterDateList.add(city);
        }
*/

    }
}
