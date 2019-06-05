package com.tommypurkissdev.weather;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {


    //VARS
    public static TextView tvTemperature;
    public TextView tvLocation;
    public TextView tvDescription;
    public ImageButton buttonCurrentLocation;
    public EditText mSearchText;
    public TextView tvLastUpdated;
    public static TextView tvTempMin;
    public static TextView tvTempMax;
    public static String dailyForecastTempMin;

    public ListView forecastListView;

    // day forecast vars

    public String dailyForecastDay;
    public static String dailyForecastTempMax;
    public static TextView tvForecastOneTemp;

    // forecast 1
    public TextView tvForecastOneTitle;
    public ImageView ivForecastOne;
    public static TextView tvForecastTwoTemp;

    // forecast 2
    public TextView tvForecastTwoTitle;
    public ImageView ivForecastTwo;
    public static TextView tvForecastThreeTemp;

    // forecast 3
    public TextView tvForecastThreeTitle;
    public ImageView ivForecastThree;
    public static TextView tvForecastFourTemp;

    // forecast 4
    public TextView tvForecastFourTitle;
    public ImageView ivForecastFour;
    public static TextView tvForecastFiveTemp;

    // forecast 5
    public TextView tvForecastFiveTitle;
    public ImageView ivForecastFive;
    public static TextView tvForecastSixTemp;

    // forecast 6
    public TextView tvForecastSixTitle;
    public ImageView ivForecastSix;
    public static TextView tvForecastSevenTemp;

    // forecast 7
    public TextView tvForecastSevenTitle;
    public ImageView ivForecastSeven;
    public static TextView tvForecastEightTemp;

    // forecast 8
    public TextView tvForecastEightTitle;
    public ImageView ivForecastEight;

    public ImageButton ibRefresh;
    public ImageButton ibSettings;
    public ImageView ivWeatherIcon;

    public RequestQueue mRequestQueue;
    public Boolean mLocationPermissionGranted = false;
    public FusedLocationProviderClient mFusedLocationClient;

    public FirebaseAnalytics mFirebaseAnalytics;

    //CONST
    public final static String TAG = "MainActivity";
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String API_KEY = "7b35bff27c44e02a0ed4347c65c2ffa8"; //openweathermap api key
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    //double
    public static double lat;
    public static double lon;

    //weather icons
    public String weatherIcon;
    public String weatherIcon0;
    public String weatherIcon1;
    public String weatherIcon2;
    public String weatherIcon3;
    public String weatherIcon4;
    public String weatherIcon5;
    public String weatherIcon6;
    public String weatherIcon7;

    public TextView humidityValue;
    public TextView pressureValue;
    public TextView windValue;
    public TextView cloudsValue;
    public TextView rainPrecipValue;

    public TextView tvDayF;
    public static TextView tvTempMinF;
    public static TextView tvTempMaxF;
    public String cityName = "";
    public Switch celFahSwitch;

    public String units = "metric"; // default unit?

    private AdView mAdView;



    //public String urlAPICityName = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=" + units + "&appid=" + API_KEY;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        tvTemperature = findViewById(R.id.tv_temp);
        tvLocation = findViewById(R.id.tv_location);
        tvDescription = findViewById(R.id.tv_description);
        buttonCurrentLocation = findViewById(R.id.button_current_location);
        mSearchText = findViewById(R.id.et_search);
        tvLastUpdated = findViewById(R.id.tv_last_updated);
        tvTempMin = findViewById(R.id.tv_temp_min);
        tvTempMax = findViewById(R.id.tv_temp_max);
        //ibSettings = findViewById(R.id.ib_settings);
        ibRefresh = findViewById(R.id.ib_refresh);


        forecastListView = findViewById(R.id.forecast_list_view);

        tvDayF = findViewById(R.id.tv_forecast_day);
        tvTempMinF = findViewById(R.id.tv_forecast_temp_min);
        tvTempMaxF = findViewById(R.id.tv_forecast_temp_max);

        //forecast 1
        tvForecastOneTitle = findViewById(R.id.tv_forecast_one_title);
        ivForecastOne = findViewById(R.id.iv_forecast_one);
        tvForecastOneTemp = findViewById(R.id.tv_forecast_one_temp);


        //forecast 2
        tvForecastTwoTitle = findViewById(R.id.tv_forecast_two_title);
        ivForecastTwo = findViewById(R.id.iv_forecast_two);
        tvForecastTwoTemp = findViewById(R.id.tv_forecast_two_temp);



        //forecast 3
        tvForecastThreeTitle = findViewById(R.id.tv_forecast_three_title);
        ivForecastThree = findViewById(R.id.iv_forecast_three);
        tvForecastThreeTemp = findViewById(R.id.tv_forecast_three_temp);

        //forecast 4
        tvForecastFourTitle = findViewById(R.id.tv_forecast_four_title);
        ivForecastFour = findViewById(R.id.iv_forecast_four);
        tvForecastFourTemp = findViewById(R.id.tv_forecast_four_temp);

        //forecast 5
        tvForecastFiveTitle = findViewById(R.id.tv_forecast_five_title);
        ivForecastFive = findViewById(R.id.iv_forecast_five);
        tvForecastFiveTemp = findViewById(R.id.tv_forecast_five_temp);

        //forecast 6
        tvForecastSixTitle = findViewById(R.id.tv_forecast_six_title);
        ivForecastSix = findViewById(R.id.iv_forecast_six);
        tvForecastSixTemp = findViewById(R.id.tv_forecast_six_temp);

        //forecast 7
        tvForecastSevenTitle = findViewById(R.id.tv_forecast_seven_title);
        ivForecastSeven = findViewById(R.id.iv_forecast_seven);
        tvForecastSevenTemp = findViewById(R.id.tv_forecast_seven_temp);

        //forecast 8
        tvForecastEightTitle = findViewById(R.id.tv_forecast_eight_title);
        ivForecastEight = findViewById(R.id.iv_forecast_eight);
        tvForecastEightTemp = findViewById(R.id.tv_forecast_eight_temp);

        ivWeatherIcon = findViewById(R.id.iv_weather_icon);

        humidityValue = findViewById(R.id.tv_humidity_value);
        pressureValue = findViewById(R.id.tv_pressure_value);
        windValue = findViewById(R.id.tv_wind_value);
        cloudsValue = findViewById(R.id.tv_clouds_value);
        rainPrecipValue = findViewById(R.id.tv_rain_precip_value);

        celFahSwitch = findViewById(R.id.cel_fah_switch);

        mRequestQueue = Volley.newRequestQueue(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        //keeps the keyboard closed on app opening - was previously opening automatically?
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        // app id :: ca-app-pub-9479719882402186~8478090471
        // ad unit :: ca-app-pub-9479719882402186/7174238351


        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        // ad unit :: ca-app-pub-3940256099942544/6300978111
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

 /*       AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
*/

        /* -------------- METHODS CALLED IN ONCREATE -------------- */

        getLocationPermission();
        getDeviceLocation();

        init();

        lastUpdated();


        /* -------------------------------------------------------- */

        final Settings settings = new Settings();



        buttonCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation(); //WORKS - gets device location and weather data
                Log.d(TAG, "Location Button: " + buttonCurrentLocation);
                //Toast.makeText(MainActivity.this, "Current Location WeatherActivity", Toast.LENGTH_SHORT).show();
            }
        });

        ibRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // rotates 360
                float deg = ibRefresh.getRotation() + 360F;
                ibRefresh.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

                mSearchText.setText("");
                refreshActivity();
                lastUpdated();

            }
        });

/*
        ibSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // intent to settings activity

                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);

            }
        });
*/

        celFahSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (celFahSwitch.isChecked()) {

                    units = "imperial";
                    settings.celsiusToFahrenheit();

                    getDailyForecastWeather(); // calling this makes fah cel work for the daily forecast temps
                } else {

                    units = "metric";
                    settings.fahrenheitToCelsius();

                    getDailyForecastWeather(); // calling this makes fah cel work for the daily forecast temps
                }
            }
        });

    }
    // END OF ON CREATE


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        getDeviceLocation();

        //refreshActivity();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //handles the searching for cities
    public void init() {
        Log.d(TAG, "Initialising");

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    // execute searchmethod
                    //geoLocate(); // geo locates works and gets the lat and lon but doesnt return clear results eg barcelona returns nothing new or eixample

                    getWeatherByCityName();

                    lastUpdated();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mSearchText.getWindowToken(), 0);

                    mSearchText.setText("");

                }
                return false;
            }
        });
    }


    private void refreshActivity() {

        //TODO - fix reloading animation
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);



        /* TODO
        for refresh when pressed get the last saved state eg
        if its on device location weather then call the devicelocationweather method

        else user searched through search bar get last city from cityNAme var then call the weather  method


         */


    }


    private void lastUpdated() {

        //get current date and time and print it in the text view

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getDefault());
        String currentDateAndTime = sdf.format(new Date());

        tvLastUpdated.setText("Last Updated: " + currentDateAndTime);

        Log.d(TAG, "refreshData: show date and time" + currentDateAndTime);

    }







    // works
    /* -------------- WEATHER LONDON API KEY SAMPLE -------------- */

    //weather api calls updated every 10 minutes from openweather
    public void getWeather() {

        String url = "https://api.openweathermap.org/data/2.5/find?q=London,uk&units=metric&appid=API_KEY";

        Log.d(TAG, "string url: " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    //list array
                    JSONArray jsonArray = response.getJSONArray("list");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    //main object
                    JSONObject main = jsonObject.getJSONObject("main");

                    //weather array inside list array
                    JSONArray jsaWeather = jsonObject.getJSONArray("weather");
                    JSONObject jsoWeather = jsaWeather.getJSONObject(0);

                    //main get string calls temp
                    String temp = main.getString("temp");
                    //removes the decimal point from the string
                    String tempFormat = String.valueOf(temp).split("\\.")[0];
                    tvTemperature.setText(tempFormat);

                    //json object has name
                    String city = jsonObject.getString("name");
                    tvLocation.setText(city);

                    //json object1 get string calls description from array
                    String desc = jsoWeather.getString("description");
                    tvDescription.setText(desc);

                    //TODO GET MIN MAX FOR THIS


                    getForecastWeather();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }


    /* -------------- WEATHER BY CITY NAME -------------- */

    public void getWeatherByCityName() {

        //TODO - get city name from geo locate and pass it through this url api

        cityName = mSearchText.getText().toString();

        //String urlAPI = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=metric&appid=" + API_KEY;
        String urlAPI = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=" + units + "&appid=" + API_KEY;



        Log.d(TAG, "getWeatherByCityName: cityname searched: " + cityName);
        //Log.d(TAG, "getWeatherByCityName: urlAPI: " + urlAPICityName);

        /*
         if city name in search text field = to urlAPI (it contains city name) then get the relevant data according to city data


         */

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlAPI, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject jsoCoord = response.getJSONObject("coord");

                    String lonCity = jsoCoord.getString("lon");
                    String latCity = jsoCoord.getString("lat");

                    lon = Double.valueOf(lonCity);
                    lat = Double.valueOf(latCity);

                    getForecastWeather();


                    Log.d(TAG, "onResponse: lat lon " + lat + lon);


                    JSONObject jsoMain = response.getJSONObject("main");

                    //temp
                    String temp = jsoMain.getString("temp");
                    String tempFormat = String.valueOf(temp).split("\\.")[0];
                    tvTemperature.setText(tempFormat);

                    String tempMin = jsoMain.getString("temp_min");
                    String tempMinFormat = String.valueOf(tempMin).split("\\.")[0];
                    tvTempMin.setText(tempMinFormat);

                    String tempMax = jsoMain.getString("temp_max");
                    String tempMaxFormat = String.valueOf(tempMax).split("\\.")[0];
                    tvTempMax.setText(tempMaxFormat);

                    // weather
                    JSONArray jsaWeather = response.getJSONArray("weather");
                    JSONObject jsoWeather = jsaWeather.getJSONObject(0);

                    //description
                    String description = jsoWeather.getString("description");
                    tvDescription.setText(description);

                    //icon
                    weatherIcon = jsoWeather.getString("icon");

                    //weather icons
                    weatherIcons();


                    String name = response.getString("name");
                    tvLocation.setText(name);

                    //todo details

                    String humidity = jsoMain.getString("humidity");
                    humidityValue.setText(humidity);

                    String pressure = jsoMain.getString("pressure");
                    pressureValue.setText(pressure);

                    JSONObject jsoWind = response.getJSONObject("wind");
                    String wind = jsoWind.getString("speed");
                    windValue.setText(wind);

                    JSONObject jsoClouds = response.getJSONObject("clouds");
                    String clouds = jsoClouds.getString("all");
                    cloudsValue.setText(clouds);


                    JSONObject jsoRain = response.getJSONObject("rain");
                    String rain = jsoRain.getString("1h");

                    rainPrecipValue.setText(rain);



                    /*

                    time zone test

Long dayTimestamp = Long.valueOf(jso0.getString("dt"));
                    Log.d(TAG, "onResponse: dayTimestamp" + dayTimestamp); // unix timestamp
                    //convert unix timestamp
                    Date date = new Date(dayTimestamp*1000L);

                    Log.d(TAG, "onResponse: date" + date);

                    SimpleDateFormat sdf = new SimpleDateFormat("E HH:mm");
                    // give a timezone reference for formatting (see comment at the bottom)
                    sdf.setTimeZone(java.util.TimeZone.getTimeZone("Locale"));
                    String formattedDate = sdf.format(date);
 Log.d(TAG, "onResponse: format date" + formattedDate);

                     */


                    TimeZone tz = TimeZone.getTimeZone("America/Los_Angeles");
                    Log.d(TAG, "onResponse: tz" + tz);


                    Long dayTimestamp = Long.valueOf(response.getString("dt"));


                    Log.d(TAG, "onResponse: dt " + dayTimestamp);






                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mRequestQueue.add(jsonObjectRequest);
    }



    /* -------------- WEATHER BY DEVICE LOCATION -------------- */

    public void getWeatherByDeviceLocation() {


        String urlAPILatLong = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=" + units + "&appid=" + API_KEY;

        Log.d(TAG, "string url latlon total: " + urlAPILatLong);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlAPILatLong, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    //returns correct latlong location
                    JSONObject jsonObject = response.getJSONObject("coord");
                    String jsoLon = jsonObject.getString("lon");
                    String jsoLat = jsonObject.getString("lat");
                    lat = Double.valueOf(jsoLat);
                    lon = Double.valueOf(jsoLon);


                    //weather details description
                    JSONArray jsaWeather = response.getJSONArray("weather");
                    JSONObject jsoWeather = jsaWeather.getJSONObject(0);

                    //description
                    String description = jsoWeather.getString("description");
                    tvDescription.setText(description);

                    //icon
                    weatherIcon = jsoWeather.getString("icon");


                    weatherIcons();


                    //temp
                    JSONObject main = response.getJSONObject("main");
                    String temperature = main.getString("temp");
                    String tempFormat = String.valueOf(temperature).split("\\.")[0];
                    tvTemperature.setText(tempFormat);

                    String tempMin = main.getString("temp_min");
                    String tempMinFormat = String.valueOf(tempMin).split("\\.")[0];
                    tvTempMin.setText(tempMinFormat);

                    String tempMax = main.getString("temp_max");
                    String tempMaxFormat = String.valueOf(tempMax).split("\\.")[0];
                    tvTempMax.setText(tempMaxFormat);


                    //name of location
                    String name = response.getString("name");
                    tvLocation.setText(name);


                    //details
                    String humidity = main.getString("humidity");
                    humidityValue.setText(humidity);

                    String pressure = main.getString("pressure");
                    pressureValue.setText(pressure);

                    JSONObject jsoWind = response.getJSONObject("wind");
                    String wind = jsoWind.getString("speed");
                    windValue.setText(wind);

                    JSONObject jsoClouds = response.getJSONObject("clouds");
                    String clouds = jsoClouds.getString("all");
                    cloudsValue.setText(clouds);

                    JSONObject jsoRain = response.getJSONObject("rain");
                    String rain = jsoRain.getString("1h");

                    rainPrecipValue.setText(rain);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });
        mRequestQueue.add(jsonObjectRequest);

        getForecastWeather();
        //getDailyForecastWeather();

        lastUpdated();


    }





    /* -------------- 5 DAY WEATHER BY DEVICE LOCATION -------------- */

    public void getForecastWeather() {

        //TODO Get forecast weather from either the device location weather or searched city weather

        String urlAPILatLong = "https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&units=" + units + "&appid=" + API_KEY;

        //Log.d(TAG, "getForecastWeather: " + urlAPILatLong);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlAPILatLong, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //String[] cityForecast = new String[0];
                //ArrayList<String> cityForecast;


                try {
                    JSONArray jsonArray = response.getJSONArray("list");

                    //Log.d(TAG, "onResponse: length " + jsonArray.length()); //length 40
                    //Log.d(TAG, "onResponse: " + jsonArray); // shows json objects


                    // FORECAST ONE

                    JSONObject jso0 = jsonArray.getJSONObject(0);

                    //date/day
                    //String date0 = jso0.getString("dt_txt"); //OLD
                    //Log.d(TAG, "onResponse: day0" + date0);

                    Long dayTimestamp = Long.valueOf(jso0.getString("dt"));
                    // Log.d(TAG, "onResponse: dayTimestamp" + dayTimestamp); // unix timestamp
                    //convert unix timestamp
                    Date date = new Date(dayTimestamp*1000L);

                    //Log.d(TAG, "onResponse: date" + date);

                    SimpleDateFormat sdf = new SimpleDateFormat("E HH:mm", Locale.getDefault());
                    // give a timezone reference for formatting (see comment at the bottom)
                    sdf.setTimeZone(java.util.TimeZone.getTimeZone("Locale"));


                    String formattedDate = sdf.format(date);

                    //Log.d(TAG, "onResponse: format date" + formattedDate);

                    //end convert unix timestamp

                    tvForecastOneTitle.setText(formattedDate);
                    //tvForecastOneTitle.setText(date0); // OLD


                    //weather icon
                    JSONArray jsaWeather0 = jso0.getJSONArray("weather");
                    JSONObject jsoWeather0 = jsaWeather0.getJSONObject(0);

                    weatherIcon0 = jsoWeather0.getString("icon");


                    //temperature
                    JSONObject jsoMain0 = jso0.getJSONObject("main");
                    String temp0 = jsoMain0.getString("temp");
                    String temp0Format = String.valueOf(temp0).split("\\.")[0];
                    tvForecastOneTemp.setText(temp0Format);




                    // FORECAST TWO
                    JSONObject jso1 = jsonArray.getJSONObject(1);

                    //date/day
                    //String date1 = jso1.getString("dt_txt");
                    //Log.d(TAG, "onResponse: day0" + date1);

                    Long dayTimestamp1 = Long.valueOf(jso1.getString("dt"));
                    //Log.d(TAG, "onResponse: dayTimestamp" + dayTimestamp1); // unix timestamp
                    //convert unix timestamp
                    Date date1 = new Date(dayTimestamp1*1000L);

                    //Log.d(TAG, "onResponse: date" + date1);

                    SimpleDateFormat sdf1 = new SimpleDateFormat("E HH:mm");
                    // give a timezone reference for formatting (see comment at the bottom)
                    sdf1.setTimeZone(java.util.TimeZone.getTimeZone("Locale"));
                    String formattedDate1 = sdf1.format(date1);

                    //Log.d(TAG, "onResponse: format date" + formattedDate1);

                    //end convert unix timestamp

                    tvForecastTwoTitle.setText(formattedDate1);

                    //tvForecastTwoTitle.setText(date1);

                    //weather icon
                    JSONArray jsaWeather1 = jso1.getJSONArray("weather");
                    JSONObject jsoWeather1 = jsaWeather1.getJSONObject(0);

                    weatherIcon1 = jsoWeather1.getString("icon");


                    // temp
                    JSONObject jsoMain1 = jso1.getJSONObject("main");
                    String temp1 = jsoMain1.getString("temp");
                    String temp1Format = String.valueOf(temp1).split("\\.")[0];
                    tvForecastTwoTemp.setText(temp1Format);

                    //Log.d(TAG, "onResponse: " + jso1);




                    // FORECAST THREE

                    JSONObject jso2 = jsonArray.getJSONObject(2);

                    //date/day
//                    String date2 = jso2.getString("dt_txt");
//                    //Log.d(TAG, "onResponse: day0" + date2);
//                    tvForecastThreeTitle.setText(date2);

                    Long dayTimestamp2 = Long.valueOf(jso2.getString("dt"));
                    // Log.d(TAG, "onResponse: dayTimestamp" + dayTimestamp2); // unix timestamp
                    //convert unix timestamp
                    Date date2 = new Date(dayTimestamp2*1000L);

                    //Log.d(TAG, "onResponse: date2" + date2);

                    SimpleDateFormat sdf2 = new SimpleDateFormat("E HH:mm");
                    // give a timezone reference for formatting (see comment at the bottom)
                    sdf2.setTimeZone(java.util.TimeZone.getTimeZone("Locale"));
                    String formattedDate2 = sdf2.format(date2);

                    //Log.d(TAG, "onResponse: format date" + formattedDate2);

                    //end convert unix timestamp

                    tvForecastThreeTitle.setText(formattedDate2);


                    //weather icon
                    JSONArray jsaWeather2 = jso2.getJSONArray("weather");
                    JSONObject jsoWeather2 = jsaWeather2.getJSONObject(0);

                    weatherIcon2 = jsoWeather2.getString("icon");


                    // temp
                    JSONObject jsoMain2 = jso2.getJSONObject("main");
                    String temp2 = jsoMain2.getString("temp");
                    String temp2Format = String.valueOf(temp2).split("\\.")[0];
                    tvForecastThreeTemp.setText(temp2Format);


                    // FORECAST FOUR

                    JSONObject jso3 = jsonArray.getJSONObject(3);

                    //date/day
//                    String date3 = jso3.getString("dt_txt");
//                    //Log.d(TAG, "onResponse: day3" + date3);
//                    tvForecastFourTitle.setText(date3);


                    Long dayTimestamp3 = Long.valueOf(jso3.getString("dt"));
                    //Log.d(TAG, "onResponse: dayTimestamp" + dayTimestamp3); // unix timestamp
                    //convert unix timestamp
                    Date date3 = new Date(dayTimestamp3*1000L);

                    // Log.d(TAG, "onResponse: date2" + date3);

                    SimpleDateFormat sdf3 = new SimpleDateFormat("E HH:mm");
                    // give a timezone reference for formatting (see comment at the bottom)
                    sdf3.setTimeZone(java.util.TimeZone.getTimeZone("Locale"));
                    String formattedDate3 = sdf3.format(date3);

                    //Log.d(TAG, "onResponse: format date" + formattedDate3);

                    //end convert unix timestamp

                    tvForecastFourTitle.setText(formattedDate3);

                    //weather icon
                    JSONArray jsaWeather3 = jso3.getJSONArray("weather");
                    JSONObject jsoWeather3 = jsaWeather3.getJSONObject(0);

                    weatherIcon3 = jsoWeather3.getString("icon");


                    // temp
                    JSONObject jsoMain3 = jso3.getJSONObject("main");
                    String temp3 = jsoMain3.getString("temp");
                    String temp3Format = String.valueOf(temp3).split("\\.")[0];
                    tvForecastFourTemp.setText(temp3Format);


                    // FORECAST FIVE

                    JSONObject jso4 = jsonArray.getJSONObject(4);

                    //date/day
//                    String date4 = jso4.getString("dt_txt");
//                    //Log.d(TAG, "onResponse: day4" + date4);
//                    tvForecastFiveTitle.setText(date4);

                    Long dayTimestamp4 = Long.valueOf(jso4.getString("dt"));
                    // Log.d(TAG, "onResponse: dayTimestamp" + dayTimestamp4); // unix timestamp
                    //convert unix timestamp
                    Date date4 = new Date(dayTimestamp4*1000L);

                    //Log.d(TAG, "onResponse: date2" + date4);

                    SimpleDateFormat sdf4 = new SimpleDateFormat("E HH:mm");
                    // give a timezone reference for formatting (see comment at the bottom)
                    sdf4.setTimeZone(java.util.TimeZone.getTimeZone("Locale"));
                    String formattedDate4 = sdf4.format(date4);

                    // Log.d(TAG, "onResponse: format date" + formattedDate4);

                    //end convert unix timestamp

                    tvForecastFiveTitle.setText(formattedDate4);

                    //weather icon
                    JSONArray jsaWeather4 = jso4.getJSONArray("weather");
                    JSONObject jsoWeather4 = jsaWeather4.getJSONObject(0);

                    weatherIcon4 = jsoWeather4.getString("icon");


                    // temp
                    JSONObject jsoMain4 = jso4.getJSONObject("main");
                    String temp4 = jsoMain4.getString("temp");
                    String temp4Format = String.valueOf(temp4).split("\\.")[0];
                    tvForecastFiveTemp.setText(temp4Format);


                    // FORECAST SIX

                    JSONObject jso5 = jsonArray.getJSONObject(5);

                    //date/day
//                    String date5 = jso5.getString("dt_txt");
//                    //Log.d(TAG, "onResponse: day5" + date5);
//                    tvForecastSixTitle.setText(date5);

                    Long dayTimestamp5 = Long.valueOf(jso5.getString("dt"));
                    // Log.d(TAG, "onResponse: dayTimestamp" + dayTimestamp5); // unix timestamp
                    //convert unix timestamp
                    Date date5 = new Date(dayTimestamp5*1000L);

                    // Log.d(TAG, "onResponse: date2" + date5);

                    SimpleDateFormat sdf5 = new SimpleDateFormat("E HH:mm");
                    // give a timezone reference for formatting (see comment at the bottom)
                    sdf5.setTimeZone(java.util.TimeZone.getTimeZone("Locale"));
                    String formattedDate5 = sdf5.format(date5);

                    // Log.d(TAG, "onResponse: format date" + formattedDate5);

                    //end convert unix timestamp

                    tvForecastSixTitle.setText(formattedDate5);

                    //weather icon
                    JSONArray jsaWeather5 = jso5.getJSONArray("weather");
                    JSONObject jsoWeather5 = jsaWeather5.getJSONObject(0);

                    weatherIcon5 = jsoWeather5.getString("icon");


                    // temp
                    JSONObject jsoMain5 = jso5.getJSONObject("main");
                    String temp5 = jsoMain5.getString("temp");
                    String temp5Format = String.valueOf(temp5).split("\\.")[0];
                    tvForecastSixTemp.setText(temp5Format);


                    // FORECAST SEVEN

                    JSONObject jso6 = jsonArray.getJSONObject(6);

                    //date/day
//                    String date6 = jso6.getString("dt_txt");
//                    //Log.d(TAG, "onResponse: day6" + date6);
//                    tvForecastSevenTitle.setText(date6);

                    Long dayTimestamp6 = Long.valueOf(jso6.getString("dt"));
                    // Log.d(TAG, "onResponse: dayTimestamp" + dayTimestamp6); // unix timestamp
                    //convert unix timestamp
                    Date date6 = new Date(dayTimestamp6*1000L);

                    //Log.d(TAG, "onResponse: date2" + date6);

                    SimpleDateFormat sdf6 = new SimpleDateFormat("E HH:mm");
                    // give a timezone reference for formatting (see comment at the bottom)
                    sdf6.setTimeZone(java.util.TimeZone.getTimeZone("Locale"));
                    String formattedDate6 = sdf6.format(date6);

                    //Log.d(TAG, "onResponse: format date" + formattedDate6);

                    //end convert unix timestamp

                    tvForecastSevenTitle.setText(formattedDate6);

                    //weather icon
                    JSONArray jsaWeather6 = jso6.getJSONArray("weather");
                    JSONObject jsoWeather6 = jsaWeather6.getJSONObject(0);

                    weatherIcon6 = jsoWeather6.getString("icon");


                    // temp
                    JSONObject jsoMain6 = jso6.getJSONObject("main");
                    String temp6 = jsoMain6.getString("temp");
                    String temp6Format = String.valueOf(temp6).split("\\.")[0];
                    tvForecastSevenTemp.setText(temp6Format);


                    // FORECAST EIGHT

                    JSONObject jso7 = jsonArray.getJSONObject(7);

                    //date/day
//                    String date7 = jso7.getString("dt_txt");
//                    //Log.d(TAG, "onResponse: day7" + date7);
//                    tvForecastEightTitle.setText(date7);

                    Long dayTimestamp7 = Long.valueOf(jso7.getString("dt"));
                    // Log.d(TAG, "onResponse: dayTimestamp" + dayTimestamp7); // unix timestamp
                    //convert unix timestamp
                    Date date7 = new Date(dayTimestamp7*1000L);

                    // Log.d(TAG, "onResponse: date2" + date7);

                    SimpleDateFormat sdf7 = new SimpleDateFormat("E HH:mm");
                    // give a timezone reference for formatting (see comment at the bottom)
                    sdf7.setTimeZone(java.util.TimeZone.getTimeZone("Locale"));
                    String formattedDate7 = sdf7.format(date7);

                    // Log.d(TAG, "onResponse: format date" + formattedDate7);

                    //end convert unix timestamp

                    tvForecastEightTitle.setText(formattedDate7);

                    //weather icon
                    JSONArray jsaWeather7 = jso7.getJSONArray("weather");
                    JSONObject jsoWeather7 = jsaWeather7.getJSONObject(0);

                    weatherIcon7 = jsoWeather7.getString("icon");

                    //  Log.d(TAG, "onResponse: " + weatherIcon0);

                    //weatherIcons();
                    forecastWeatherIcons();

                    // temp
                    JSONObject jsoMain7 = jso7.getJSONObject("main");
                    String temp7 = jsoMain7.getString("temp");
                    String temp7Format = String.valueOf(temp7).split("\\.")[0];
                    tvForecastEightTemp.setText(temp7Format);




                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mRequestQueue.add(jsonObjectRequest);

        getDailyForecastWeather();
    }


    //MARK:- As to why the min max temps are showing the same or similar it is because it gets the min/max for that current hour shown not for the whole day.
    //TODO:- Fix the min and max to show it for the whole day rather than the hour.
    public void getDailyForecastWeather() {


        String urlAPILatLong = "https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&units=" + units + "&appid=" + API_KEY;

        //Log.d(TAG, "getForecastWeather: " + urlAPILatLong);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlAPILatLong, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("list");

                    //Log.d(TAG, "onResponse: length " + jsonArray.length()); //length 40
                    //Log.d(TAG, "onResponse: " + jsonArray); // shows json


                    ArrayList<DailyForecast> dailyForecast = new ArrayList<>(); // shows all five days when put before the for loop

                    for (int i = 0; i < jsonArray.length(); i += 8) {

                        JSONObject dayForecast = jsonArray.getJSONObject(i);

                        //Log.d(TAG, "onResponse: day forecast??" + dailyForecast);

                        //dailyForecastDay = dayForecast.getString("dt_txt");

                        //forecast days
                        String day = dayForecast.getString("dt_txt");
                        //Log.d(TAG, "onResponse: day " + day); //shows date and time


                        Long dayTimestamp = Long.valueOf(dayForecast.getString("dt"));
                        //Log.d(TAG, "onResponse: dayTimestamp" + dayTimestamp); // unix timestamp

                        //convert unix timestamp

                        Date date = new Date(dayTimestamp*1000L);

                        //Log.d(TAG, "onResponse: date" + date);

                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                        // give a timezone reference for formatting (see comment at the bottom)
                        sdf.setTimeZone(java.util.TimeZone.getTimeZone("Locale"));
                        String formattedDate = sdf.format(date);

                        //Log.d(TAG, "onResponse: format date" + formattedDate);

                        //end convert unix timestamp

                        dailyForecastDay = formattedDate;




                        //temps
                        JSONObject jsoMain0 = dayForecast.getJSONObject("main");
                        String tempMin = jsoMain0.getString("temp_min");
                        dailyForecastTempMin = String.valueOf(tempMin).split("\\.")[0];

                        String tempMax = jsoMain0.getString("temp_max");
                        dailyForecastTempMax = String.valueOf(tempMax).split("\\.")[0];

                        DailyForecast dailyForecastTotal = new DailyForecast(dailyForecastDay, dailyForecastTempMin, dailyForecastTempMax);

                        dailyForecast.add(dailyForecastTotal);

                        DailyForecastAdapter dailyForecastAdapter = new DailyForecastAdapter(getApplicationContext(), R.layout.forecast_custom_layout, dailyForecast);
                        forecastListView.setAdapter(dailyForecastAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mRequestQueue.add(jsonObjectRequest);
    }


    public void weatherIcons() {

        /* DAY WEATHER*/
        // day
        if (weatherIcon.contentEquals("01d")) {
            ivWeatherIcon.setImageResource(R.drawable.weather_01d);
        }
        if (weatherIcon.contentEquals("02d")) {
            ivWeatherIcon.setImageResource(R.drawable.weather_02d);
        }
        if (weatherIcon.contentEquals("03d")) {
            ivWeatherIcon.setImageResource(R.drawable.weather_03d);
        }
        if (weatherIcon.contentEquals("04d")) {
            ivWeatherIcon.setImageResource(R.drawable.weather_04d);
        }
        if (weatherIcon.contentEquals("09d")) {
            ivWeatherIcon.setImageResource(R.drawable.weather_09d);
        }
        if (weatherIcon.contentEquals("10d")) {
            ivWeatherIcon.setImageResource(R.drawable.weather_10d);
        }
        if (weatherIcon.contentEquals("11d")) {
            ivWeatherIcon.setImageResource(R.drawable.weather_11d);
        }
        if (weatherIcon.contentEquals("13d")) {
            ivWeatherIcon.setImageResource(R.drawable.weather_13d);
        }
        if (weatherIcon.contentEquals("50d")) {
            ivWeatherIcon.setImageResource(R.drawable.weather_50d);
        }

        // night
        if (weatherIcon.contentEquals("01n")) {
            ivWeatherIcon.setImageResource(R.drawable.weather_01n);
        }
        if (weatherIcon.contentEquals("02n")) {
            ivWeatherIcon.setImageResource(R.drawable.weather_02n);
        }
        if (weatherIcon.contentEquals("03n")) {
            ivWeatherIcon.setImageResource(R.drawable.weather_03n);
        }
        if (weatherIcon.contentEquals("04n")) {
            ivWeatherIcon.setImageResource(R.drawable.weather_04n);
        }
        if (weatherIcon.contentEquals("09n")) {
            ivWeatherIcon.setImageResource(R.drawable.weather_09n);
        }
        if (weatherIcon.contentEquals("10n")) {
            ivWeatherIcon.setImageResource(R.drawable.weather_10n);
        }
        if (weatherIcon.contentEquals("11n")) {
            ivWeatherIcon.setImageResource(R.drawable.weather_11n);
        }
        if (weatherIcon.contentEquals("13n")) {
            ivWeatherIcon.setImageResource(R.drawable.weather_13n);
        }
        if (weatherIcon.contentEquals("50n")) {
            ivWeatherIcon.setImageResource(R.drawable.weather_50n);
        }
        /* END DAY WEATHER*/


    }

    public void forecastWeatherIcons() {


        /* FORECAST ONE WEATHER*/

        // WEATHER ICON 0

        if (weatherIcon0.contentEquals("01d")) {
            ivForecastOne.setImageResource(R.drawable.weather_01d);
        }
        if (weatherIcon0.contentEquals("02d")) {
            ivForecastOne.setImageResource(R.drawable.weather_02d);
        }
        if (weatherIcon0.contentEquals("03d")) {
            ivForecastOne.setImageResource(R.drawable.weather_03d);
        }
        if (weatherIcon0.contentEquals("04d")) {
            ivForecastOne.setImageResource(R.drawable.weather_04d);
        }
        if (weatherIcon0.contentEquals("09d")) {
            ivForecastOne.setImageResource(R.drawable.weather_09d);
        }
        if (weatherIcon0.contentEquals("10d")) {
            ivForecastOne.setImageResource(R.drawable.weather_10d);
        }
        if (weatherIcon0.contentEquals("11d")) {
            ivForecastOne.setImageResource(R.drawable.weather_11d);
        }
        if (weatherIcon0.contentEquals("13d")) {
            ivForecastOne.setImageResource(R.drawable.weather_13d);
        }
        if (weatherIcon0.contentEquals("50d")) {
            ivForecastOne.setImageResource(R.drawable.weather_50d);
        }

        // night
        if (weatherIcon0.contentEquals("01n")) {
            ivForecastOne.setImageResource(R.drawable.weather_01n);
        }
        if (weatherIcon0.contentEquals("02n")) {
            ivForecastOne.setImageResource(R.drawable.weather_02n);
        }
        if (weatherIcon0.contentEquals("03n")) {
            ivForecastOne.setImageResource(R.drawable.weather_03n);
        }
        if (weatherIcon0.contentEquals("04n")) {
            ivForecastOne.setImageResource(R.drawable.weather_04n);
        }
        if (weatherIcon0.contentEquals("09n")) {
            ivForecastOne.setImageResource(R.drawable.weather_09n);
        }
        if (weatherIcon0.contentEquals("10n")) {
            ivForecastOne.setImageResource(R.drawable.weather_10n);
        }
        if (weatherIcon0.contentEquals("11n")) {
            ivForecastOne.setImageResource(R.drawable.weather_11n);
        }
        if (weatherIcon0.contentEquals("13n")) {
            ivForecastOne.setImageResource(R.drawable.weather_13n);
        }
        if (weatherIcon0.contentEquals("50n")) {
            ivForecastOne.setImageResource(R.drawable.weather_50n);
        }

        /* END FORECAST ONE WEATHER*/


        /* FORECAST TWO WEATHER*/

        // WEATHER ICON 1

        if (weatherIcon1.contentEquals("01d")) {
            ivForecastTwo.setImageResource(R.drawable.weather_01d);
        }
        if (weatherIcon1.contentEquals("02d")) {
            ivForecastTwo.setImageResource(R.drawable.weather_02d);
        }
        if (weatherIcon1.contentEquals("03d")) {
            ivForecastTwo.setImageResource(R.drawable.weather_03d);
        }
        if (weatherIcon1.contentEquals("04d")) {
            ivForecastTwo.setImageResource(R.drawable.weather_04d);
        }
        if (weatherIcon1.contentEquals("09d")) {
            ivForecastTwo.setImageResource(R.drawable.weather_09d);
        }
        if (weatherIcon1.contentEquals("10d")) {
            ivForecastTwo.setImageResource(R.drawable.weather_10d);
        }
        if (weatherIcon1.contentEquals("11d")) {
            ivForecastTwo.setImageResource(R.drawable.weather_11d);
        }
        if (weatherIcon1.contentEquals("13d")) {
            ivForecastTwo.setImageResource(R.drawable.weather_13d);
        }
        if (weatherIcon1.contentEquals("50d")) {
            ivForecastTwo.setImageResource(R.drawable.weather_50d);
        }

        // night
        if (weatherIcon1.contentEquals("01n")) {
            ivForecastTwo.setImageResource(R.drawable.weather_01n);
        }
        if (weatherIcon1.contentEquals("02n")) {
            ivForecastTwo.setImageResource(R.drawable.weather_02n);
        }
        if (weatherIcon1.contentEquals("03n")) {
            ivForecastTwo.setImageResource(R.drawable.weather_03n);
        }
        if (weatherIcon1.contentEquals("04n")) {
            ivForecastTwo.setImageResource(R.drawable.weather_04n);
        }
        if (weatherIcon1.contentEquals("09n")) {
            ivForecastTwo.setImageResource(R.drawable.weather_09n);
        }
        if (weatherIcon1.contentEquals("10n")) {
            ivForecastTwo.setImageResource(R.drawable.weather_10n);
        }
        if (weatherIcon1.contentEquals("11n")) {
            ivForecastTwo.setImageResource(R.drawable.weather_11n);
        }
        if (weatherIcon1.contentEquals("13n")) {
            ivForecastTwo.setImageResource(R.drawable.weather_13n);
        }
        if (weatherIcon1.contentEquals("50n")) {
            ivForecastTwo.setImageResource(R.drawable.weather_50n);
        }

        /* END FORECAST TWO WEATHER*/




        /* FORECAST THREE WEATHER*/

        // WEATHER ICON 2

        if (weatherIcon2.contentEquals("01d")) {
            ivForecastThree.setImageResource(R.drawable.weather_01d);
        }
        if (weatherIcon2.contentEquals("02d")) {
            ivForecastThree.setImageResource(R.drawable.weather_02d);
        }
        if (weatherIcon2.contentEquals("03d")) {
            ivForecastThree.setImageResource(R.drawable.weather_03d);
        }
        if (weatherIcon2.contentEquals("04d")) {
            ivForecastThree.setImageResource(R.drawable.weather_04d);
        }
        if (weatherIcon2.contentEquals("09d")) {
            ivForecastThree.setImageResource(R.drawable.weather_09d);
        }
        if (weatherIcon2.contentEquals("10d")) {
            ivForecastThree.setImageResource(R.drawable.weather_10d);
        }
        if (weatherIcon2.contentEquals("11d")) {
            ivForecastThree.setImageResource(R.drawable.weather_11d);
        }
        if (weatherIcon2.contentEquals("13d")) {
            ivForecastThree.setImageResource(R.drawable.weather_13d);
        }
        if (weatherIcon2.contentEquals("50d")) {
            ivForecastThree.setImageResource(R.drawable.weather_50d);
        }

        // night
        if (weatherIcon2.contentEquals("01n")) {
            ivForecastThree.setImageResource(R.drawable.weather_01n);
        }
        if (weatherIcon2.contentEquals("02n")) {
            ivForecastThree.setImageResource(R.drawable.weather_02n);
        }
        if (weatherIcon2.contentEquals("03n")) {
            ivForecastThree.setImageResource(R.drawable.weather_03n);
        }
        if (weatherIcon2.contentEquals("04n")) {
            ivForecastThree.setImageResource(R.drawable.weather_04n);
        }
        if (weatherIcon2.contentEquals("09n")) {
            ivForecastThree.setImageResource(R.drawable.weather_09n);
        }
        if (weatherIcon2.contentEquals("10n")) {
            ivForecastThree.setImageResource(R.drawable.weather_10n);
        }
        if (weatherIcon2.contentEquals("11n")) {
            ivForecastThree.setImageResource(R.drawable.weather_11n);
        }
        if (weatherIcon2.contentEquals("13n")) {
            ivForecastThree.setImageResource(R.drawable.weather_13n);
        }
        if (weatherIcon2.contentEquals("50n")) {
            ivForecastThree.setImageResource(R.drawable.weather_50n);
        }

        /* END FORECAST THREE WEATHER*/




        /* FORECAST FOUR WEATHER*/

        // WEATHER ICON 3

        if (weatherIcon3.contentEquals("01d")) {
            ivForecastFour.setImageResource(R.drawable.weather_01d);
        }
        if (weatherIcon3.contentEquals("02d")) {
            ivForecastFour.setImageResource(R.drawable.weather_02d);
        }
        if (weatherIcon3.contentEquals("03d")) {
            ivForecastFour.setImageResource(R.drawable.weather_03d);
        }
        if (weatherIcon3.contentEquals("04d")) {
            ivForecastFour.setImageResource(R.drawable.weather_04d);
        }
        if (weatherIcon3.contentEquals("09d")) {
            ivForecastFour.setImageResource(R.drawable.weather_09d);
        }
        if (weatherIcon3.contentEquals("10d")) {
            ivForecastFour.setImageResource(R.drawable.weather_10d);
        }
        if (weatherIcon3.contentEquals("11d")) {
            ivForecastFour.setImageResource(R.drawable.weather_11d);
        }
        if (weatherIcon3.contentEquals("13d")) {
            ivForecastFour.setImageResource(R.drawable.weather_13d);
        }
        if (weatherIcon3.contentEquals("50d")) {
            ivForecastFour.setImageResource(R.drawable.weather_50d);
        }

        // night
        if (weatherIcon3.contentEquals("01n")) {
            ivForecastFour.setImageResource(R.drawable.weather_01n);
        }
        if (weatherIcon3.contentEquals("02n")) {
            ivForecastFour.setImageResource(R.drawable.weather_02n);
        }
        if (weatherIcon3.contentEquals("03n")) {
            ivForecastFour.setImageResource(R.drawable.weather_03n);
        }
        if (weatherIcon3.contentEquals("04n")) {
            ivForecastFour.setImageResource(R.drawable.weather_04n);
        }
        if (weatherIcon3.contentEquals("09n")) {
            ivForecastFour.setImageResource(R.drawable.weather_09n);
        }
        if (weatherIcon3.contentEquals("10n")) {
            ivForecastFour.setImageResource(R.drawable.weather_10n);
        }
        if (weatherIcon3.contentEquals("11n")) {
            ivForecastFour.setImageResource(R.drawable.weather_11n);
        }
        if (weatherIcon3.contentEquals("13n")) {
            ivForecastFour.setImageResource(R.drawable.weather_13n);
        }
        if (weatherIcon3.contentEquals("50n")) {
            ivForecastFour.setImageResource(R.drawable.weather_50n);
        }

        /* END FORECAST FOUR WEATHER*/




        /* FORECAST FIVE WEATHER*/

        // WEATHER ICON 4

        if (weatherIcon4.contentEquals("01d")) {
            ivForecastFive.setImageResource(R.drawable.weather_01d);
        }
        if (weatherIcon4.contentEquals("02d")) {
            ivForecastFive.setImageResource(R.drawable.weather_02d);
        }
        if (weatherIcon4.contentEquals("03d")) {
            ivForecastFive.setImageResource(R.drawable.weather_03d);
        }
        if (weatherIcon4.contentEquals("04d")) {
            ivForecastFive.setImageResource(R.drawable.weather_04d);
        }
        if (weatherIcon4.contentEquals("09d")) {
            ivForecastFive.setImageResource(R.drawable.weather_09d);
        }
        if (weatherIcon4.contentEquals("10d")) {
            ivForecastFive.setImageResource(R.drawable.weather_10d);
        }
        if (weatherIcon4.contentEquals("11d")) {
            ivForecastFive.setImageResource(R.drawable.weather_11d);
        }
        if (weatherIcon4.contentEquals("13d")) {
            ivForecastFive.setImageResource(R.drawable.weather_13d);
        }
        if (weatherIcon4.contentEquals("50d")) {
            ivForecastFive.setImageResource(R.drawable.weather_50d);
        }

        // night
        if (weatherIcon4.contentEquals("01n")) {
            ivForecastFive.setImageResource(R.drawable.weather_01n);
        }
        if (weatherIcon4.contentEquals("02n")) {
            ivForecastFive.setImageResource(R.drawable.weather_02n);
        }
        if (weatherIcon4.contentEquals("03n")) {
            ivForecastFive.setImageResource(R.drawable.weather_03n);
        }
        if (weatherIcon4.contentEquals("04n")) {
            ivForecastFive.setImageResource(R.drawable.weather_04n);
        }
        if (weatherIcon4.contentEquals("09n")) {
            ivForecastFive.setImageResource(R.drawable.weather_09n);
        }
        if (weatherIcon4.contentEquals("10n")) {
            ivForecastFive.setImageResource(R.drawable.weather_10n);
        }
        if (weatherIcon4.contentEquals("11n")) {
            ivForecastFive.setImageResource(R.drawable.weather_11n);
        }
        if (weatherIcon4.contentEquals("13n")) {
            ivForecastFive.setImageResource(R.drawable.weather_13n);
        }
        if (weatherIcon4.contentEquals("50n")) {
            ivForecastFive.setImageResource(R.drawable.weather_50n);
        }

        /* END FORECAST FIVE WEATHER*/




        /* FORECAST SIX WEATHER*/

        // WEATHER ICON 5

        if (weatherIcon5.contentEquals("01d")) {
            ivForecastSix.setImageResource(R.drawable.weather_01d);
        }
        if (weatherIcon5.contentEquals("02d")) {
            ivForecastSix.setImageResource(R.drawable.weather_02d);
        }
        if (weatherIcon5.contentEquals("03d")) {
            ivForecastSix.setImageResource(R.drawable.weather_03d);
        }
        if (weatherIcon5.contentEquals("04d")) {
            ivForecastSix.setImageResource(R.drawable.weather_04d);
        }
        if (weatherIcon5.contentEquals("09d")) {
            ivForecastSix.setImageResource(R.drawable.weather_09d);
        }
        if (weatherIcon5.contentEquals("10d")) {
            ivForecastSix.setImageResource(R.drawable.weather_10d);
        }
        if (weatherIcon5.contentEquals("11d")) {
            ivForecastSix.setImageResource(R.drawable.weather_11d);
        }
        if (weatherIcon5.contentEquals("13d")) {
            ivForecastSix.setImageResource(R.drawable.weather_13d);
        }
        if (weatherIcon5.contentEquals("50d")) {
            ivForecastSix.setImageResource(R.drawable.weather_50d);
        }

        // night
        if (weatherIcon5.contentEquals("01n")) {
            ivForecastSix.setImageResource(R.drawable.weather_01n);
        }
        if (weatherIcon5.contentEquals("02n")) {
            ivForecastSix.setImageResource(R.drawable.weather_02n);
        }
        if (weatherIcon5.contentEquals("03n")) {
            ivForecastSix.setImageResource(R.drawable.weather_03n);
        }
        if (weatherIcon5.contentEquals("04n")) {
            ivForecastSix.setImageResource(R.drawable.weather_04n);
        }
        if (weatherIcon5.contentEquals("09n")) {
            ivForecastSix.setImageResource(R.drawable.weather_09n);
        }
        if (weatherIcon5.contentEquals("10n")) {
            ivForecastSix.setImageResource(R.drawable.weather_10n);
        }
        if (weatherIcon5.contentEquals("11n")) {
            ivForecastSix.setImageResource(R.drawable.weather_11n);
        }
        if (weatherIcon5.contentEquals("13n")) {
            ivForecastSix.setImageResource(R.drawable.weather_13n);
        }
        if (weatherIcon5.contentEquals("50n")) {
            ivForecastSix.setImageResource(R.drawable.weather_50n);
        }

        /* END FORECAST SIX WEATHER*/




        /* FORECAST SEVEN WEATHER*/

        // WEATHER ICON 6

        if (weatherIcon6.contentEquals("01d")) {
            ivForecastSeven.setImageResource(R.drawable.weather_01d);
        }
        if (weatherIcon6.contentEquals("02d")) {
            ivForecastSeven.setImageResource(R.drawable.weather_02d);
        }
        if (weatherIcon6.contentEquals("03d")) {
            ivForecastSeven.setImageResource(R.drawable.weather_03d);
        }
        if (weatherIcon6.contentEquals("04d")) {
            ivForecastSeven.setImageResource(R.drawable.weather_04d);
        }
        if (weatherIcon6.contentEquals("09d")) {
            ivForecastSeven.setImageResource(R.drawable.weather_09d);
        }
        if (weatherIcon6.contentEquals("10d")) {
            ivForecastSeven.setImageResource(R.drawable.weather_10d);
        }
        if (weatherIcon6.contentEquals("11d")) {
            ivForecastSeven.setImageResource(R.drawable.weather_11d);
        }
        if (weatherIcon6.contentEquals("13d")) {
            ivForecastSeven.setImageResource(R.drawable.weather_13d);
        }
        if (weatherIcon6.contentEquals("50d")) {
            ivForecastSeven.setImageResource(R.drawable.weather_50d);
        }

        // night
        if (weatherIcon6.contentEquals("01n")) {
            ivForecastSeven.setImageResource(R.drawable.weather_01n);
        }
        if (weatherIcon6.contentEquals("02n")) {
            ivForecastSeven.setImageResource(R.drawable.weather_02n);
        }
        if (weatherIcon6.contentEquals("03n")) {
            ivForecastSeven.setImageResource(R.drawable.weather_03n);
        }
        if (weatherIcon6.contentEquals("04n")) {
            ivForecastSeven.setImageResource(R.drawable.weather_04n);
        }
        if (weatherIcon6.contentEquals("09n")) {
            ivForecastSeven.setImageResource(R.drawable.weather_09n);
        }
        if (weatherIcon6.contentEquals("10n")) {
            ivForecastSeven.setImageResource(R.drawable.weather_10n);
        }
        if (weatherIcon6.contentEquals("11n")) {
            ivForecastSeven.setImageResource(R.drawable.weather_11n);
        }
        if (weatherIcon6.contentEquals("13n")) {
            ivForecastSeven.setImageResource(R.drawable.weather_13n);
        }
        if (weatherIcon6.contentEquals("50n")) {
            ivForecastSeven.setImageResource(R.drawable.weather_50n);
        }

        /* END FORECAST SEVEN WEATHER*/




        /* FORECAST EIGHT WEATHER*/

        // WEATHER ICON 7

        if (weatherIcon7.contentEquals("01d")) {
            ivForecastEight.setImageResource(R.drawable.weather_01d);
        }
        if (weatherIcon7.contentEquals("02d")) {
            ivForecastEight.setImageResource(R.drawable.weather_02d);
        }
        if (weatherIcon7.contentEquals("03d")) {
            ivForecastEight.setImageResource(R.drawable.weather_03d);
        }
        if (weatherIcon7.contentEquals("04d")) {
            ivForecastEight.setImageResource(R.drawable.weather_04d);
        }
        if (weatherIcon7.contentEquals("09d")) {
            ivForecastEight.setImageResource(R.drawable.weather_09d);
        }
        if (weatherIcon7.contentEquals("10d")) {
            ivForecastEight.setImageResource(R.drawable.weather_10d);
        }
        if (weatherIcon7.contentEquals("11d")) {
            ivForecastEight.setImageResource(R.drawable.weather_11d);
        }
        if (weatherIcon7.contentEquals("13d")) {
            ivForecastEight.setImageResource(R.drawable.weather_13d);
        }
        if (weatherIcon7.contentEquals("50d")) {
            ivForecastEight.setImageResource(R.drawable.weather_50d);
        }

        // night
        if (weatherIcon7.contentEquals("01n")) {
            ivForecastEight.setImageResource(R.drawable.weather_01n);
        }
        if (weatherIcon7.contentEquals("02n")) {
            ivForecastEight.setImageResource(R.drawable.weather_02n);
        }
        if (weatherIcon7.contentEquals("03n")) {
            ivForecastEight.setImageResource(R.drawable.weather_03n);
        }
        if (weatherIcon7.contentEquals("04n")) {
            ivForecastEight.setImageResource(R.drawable.weather_04n);
        }
        if (weatherIcon7.contentEquals("09n")) {
            ivForecastEight.setImageResource(R.drawable.weather_09n);
        }
        if (weatherIcon7.contentEquals("10n")) {
            ivForecastEight.setImageResource(R.drawable.weather_10n);
        }
        if (weatherIcon7.contentEquals("11n")) {
            ivForecastEight.setImageResource(R.drawable.weather_11n);
        }
        if (weatherIcon7.contentEquals("13n")) {
            ivForecastEight.setImageResource(R.drawable.weather_13n);
        }
        if (weatherIcon7.contentEquals("50n")) {
            ivForecastEight.setImageResource(R.drawable.weather_50n);
        }

        /* END FORECAST SEVEN WEATHER*/
    }




    /* -------------- LOCATION -------------- */


    public void geoLocate() {

        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MainActivity.this);
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(searchString, 1);

        } catch (IOException e) {
            Log.e(TAG, "geoLocate: IOException e" + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);

            Log.d(TAG, "Found a location: " + address.toString());


            address.getLatitude();
            lat = address.getLatitude();
            address.getLongitude();
            lon = address.getLongitude();

            String countryName = address.getCountryName();

            //cityName = mSearchText.getText().toString();
            //cityName = address.getFeatureName();


            Log.d(TAG, "geoLocate: lat and lon address: " + address.getLatitude() + address.getLongitude());
            Log.d(TAG, "geoLocate: lat and lon var: " + lat + lon);
            Log.d(TAG, "geoLocate: city name: " + tvLocation);

            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    public void getDeviceLocation() {

        Log.d(TAG, "getDeviceLocation: Getting Device Current Location");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted) {
                Task location = mFusedLocationClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location");

                            Location currentLocation = (Location) task.getResult();

                            Log.d(TAG, "current Location is " + currentLocation);

                            //checks if not null first then returns lat lon value

                            if (currentLocation != null) {
                                lat = currentLocation.getLatitude();
                                lon = currentLocation.getLongitude();

                            } else {
                                //getWeather();
                                Toast.makeText(MainActivity.this, "Current Location Not Found", Toast.LENGTH_SHORT).show();
                            }


                            Log.d(TAG, "double value of: " + lat + lon);

                            //MARK - getWeatherByDeviceLocation() now works because it is taking the lat and lon from searching device location first
                            getWeatherByDeviceLocation();


                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MainActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: Security Exception " + e.getMessage());

        }
    }


    //pops up dialog box asking for location permission
    public void getLocationPermission() {

        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;


                //TODO once permissions are granted it will get the devices location and get the weather data from that
                //Toast.makeText(this, "location permission true", Toast.LENGTH_SHORT).show();

                init();
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
        loadDataPermissionTrue();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        mLocationPermissionGranted = false;

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionGranted = false;

                        //getWeather(); // works as a placeholder if the permissions are denied
                        //TODO need to test turning permissions back on if current location weather will show
                        return;
                    }
                }
                mLocationPermissionGranted = true;
                //once permission is granted it will get the device location weather

                //MARK - getDeviceLocation() now works and shows current device location weather because its called after permissions are allowed
                getDeviceLocation();
                Log.d(TAG, "onRequestPermissionsResult: ");
            }
        }
    }

    //once the permission is true to get device location weather this is called in oncreate method and loads the current weather when the app is closed and starts up again
    public void loadDataPermissionTrue() {
        if (mLocationPermissionGranted) {
            getDeviceLocation();
            Log.d(TAG, "loadDataPermissionTrue: " + mLocationPermissionGranted);
        }
    }
}


/* MARK: - SPARE CODE



 */