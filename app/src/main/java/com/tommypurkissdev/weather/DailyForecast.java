package com.tommypurkissdev.weather;

import android.util.Log;

class DailyForecast {
    private static final String TAG = "DailyForecast";


    public String day;
    public String tempMin;
    public String tempMax;


    public DailyForecast(String day, String tempMin, String tempMax) {
        this.day = day;
        Log.d(TAG, "DailyForecast: " + day);
        this.tempMin = tempMin;
        this.tempMax = tempMax;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }
}
