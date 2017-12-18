package com.cypyc.mini_weather.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.cypyc.mini_weather.R;

/**
 * Created by yuncity on 2017/11/1.
 */

public class SelectCityActivity extends Activity {

    private ImageView mBackBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_city);

        mBackBtn = (ImageView) findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.title_back) {
                    finish();
                }
            }
        });
    }

}
