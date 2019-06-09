package com.tommypurkissdev.weather;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class DailyForecastAdapter extends ArrayAdapter<DailyForecast> {

    public static TextView tvTempMinF;
    public static TextView tvTempMaxF;


    public static TextView tvTempF;
    public TextView tvDayF;

    private static final String TAG = "DailyForecastAdapter";
    public Context mContext;
    int mResource;

    public DailyForecastAdapter(Context context, int resource, ArrayList<DailyForecast> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;

    }

    @NotNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String day = getItem(position).getDay();
        Log.d(TAG, "getView: day" + day);

        //String tempMin = getItem(position).getTempMin();
        //String tempMax = getItem(position).getTempMax();

        String temp = getItem(position).getTemp();


        //DailyForecast dailyForecast = new DailyForecast(day, tempMin, tempMax);

        DailyForecast dailyForecast = new DailyForecast(day, temp);


        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);


        TextView tvDayF = convertView.findViewById(R.id.tv_forecast_day);
        tvTempF = convertView.findViewById(R.id.tv_forecast_temp);
        //tvTempMinF = convertView.findViewById(R.id.tv_forecast_temp_min);
        //tvTempMaxF = convertView.findViewById(R.id.tv_forecast_temp_max);

        tvDayF.setText(day);
        tvTempF.setText(temp);
        //tvTempMinF.setText(tempMin);
        //tvTempMaxF.setText(tempMax);

        return convertView;
    }
}
