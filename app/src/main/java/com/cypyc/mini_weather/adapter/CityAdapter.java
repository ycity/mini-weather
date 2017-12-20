package com.cypyc.mini_weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cypyc.mini_weather.R;
import com.cypyc.mini_weather.beans.City;

import java.util.List;

/**
 * Created by yuncity on 2017/12/20.
 */

public class CityAdapter extends ArrayAdapter {

    private int resourceId;

    public CityAdapter(Context context, int textViewResourceId, List<City> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int positon, View convertView, ViewGroup parent) {
        City city = (City) getItem(positon);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView cityName = (TextView) view.findViewById(R.id.city_name);
        cityName.setText(city.getName());
        return view;
    }

}
