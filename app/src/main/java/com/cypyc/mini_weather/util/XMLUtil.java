package com.cypyc.mini_weather.util;

import android.util.Log;

import com.cypyc.mini_weather.beans.Weather;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by yuncity on 2017/12/20.
 */

public class XMLUtil {

    /**
     * methodName: parseXML
     * description: 对所读取的xml数据进行解析
     * parameters: @xmldata xml数据（字符串存储）
     * return: Weather
     */
    public static ArrayList<Weather> parseXML(String xmldata) {

        ArrayList<Weather> weatherList = new ArrayList<Weather>();
        Weather weather = null;

        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT: // 判断当前事件是否为文档开始事件
                        break;
                    case XmlPullParser.START_TAG: // 判断当前事件是否为标签元素开始事件
                        if (xmlPullParser.getName().equals("resp") || xmlPullParser.getName().equals("weather")) {
                            Log.d("TAG", "1");
                            if (weather != null) {
                                weatherList.add(weather);
                                Log.d("TAG", "2");
                            }
                            weather = new Weather();
                        }
                        /* 将标签里的数据对应地存入todayWeather中 START */
                        if (weather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                weather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                weather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                weather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                weather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                weather.setPm25(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                weather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang")) {
                                eventType = xmlPullParser.next();
                                weather.setFengxiang(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengli")) {
                                eventType = xmlPullParser.next();
                                weather.setFengli(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("date")) {
                                eventType = xmlPullParser.next();
                                weather.setDate(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("high")) {
                                eventType = xmlPullParser.next();
                                weather.setHigh(xmlPullParser.getText().substring(2).trim());
                            } else if (xmlPullParser.getName().equals("low")) {
                                eventType = xmlPullParser.next();
                                weather.setLow(xmlPullParser.getText().substring(2).trim());
                            } else if (xmlPullParser.getName().equals("type")) {
                                eventType = xmlPullParser.next();
                                weather.setType(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("high_1")) {
                                eventType = xmlPullParser.next();
                                weather.setHigh(xmlPullParser.getText().substring(2).trim());
                            } else if (xmlPullParser.getName().equals("low_1")) {
                                eventType = xmlPullParser.next();
                                weather.setLow(xmlPullParser.getText().substring(2).trim());
                            } else if (xmlPullParser.getName().equals("type_1")) {
                                eventType = xmlPullParser.next();
                                weather.setType(xmlPullParser.getText());
                            }
                            /* 将标签里的数据对应地存入todayWeather中 END */
                        }
                        break;
                    case XmlPullParser.END_TAG: // 判断当前事件是否为标签元素结束事件
                        break;
                }
                eventType = xmlPullParser.next(); // 进入下一个元素并触发相应事件
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weatherList;
    }

}
