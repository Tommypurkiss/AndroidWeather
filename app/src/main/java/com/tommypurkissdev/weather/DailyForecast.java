package com.tommypurkissdev.weather;

//import android.util.Log;

class DailyForecast {
    //private static final String TAG = "DailyForecast";

    public String day;
    public String temp;

    public DailyForecast(String day, String temp) {
        this.day = day;
        this.temp = temp;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
}
