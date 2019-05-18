package com.tommypurkissdev.weather;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {


    //VARS
    public static TextView tvTemperature;
    public TextView tvLocation;
    public TextView tvDescription;
    public ImageButton buttonCurrentLocation;
    public EditText mSearchText;
    public TextView tvLastUpdated;
    public TextView tvTempMin;
    public TextView tvTempMax;

    public String cityName;

    // forecast 1
    public TextView tvForecastOneTitle;
    public ImageView ivForecastOne;
    public TextView tvForecastOneTemp;

    // forecast 2
    public TextView tvForecastTwoTitle;
    public ImageView ivForecastTwo;
    public TextView tvForecastTwoTemp;

    // forecast 3
    public TextView tvForecastThreeTitle;
    public ImageView ivForecastThree;
    public TextView tvForecastThreeTemp;

    // forecast 4
    public TextView tvForecastFourTitle;
    public ImageView ivForecastFour;
    public TextView tvForecastFourTemp;

    // forecast 5
    public TextView tvForecastFiveTitle;
    public ImageView ivForecastFive;
    public TextView tvForecastFiveTemp;

    // forecast 6
    public TextView tvForecastSixTitle;
    public ImageView ivForecastSix;
    public TextView tvForecastSixTemp;

    // forecast 7
    public TextView tvForecastSevenTitle;
    public ImageView ivForecastSeven;
    public TextView tvForecastSevenTemp;

    // forecast 8
    public TextView tvForecastEightTitle;
    public ImageView ivForecastEight;
    public TextView tvForecastEightTemp;




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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tvTemperature = findViewById(R.id.tv_temp);
        tvLocation = findViewById(R.id.tv_location);
        tvDescription = findViewById(R.id.tv_description);
        buttonCurrentLocation = findViewById(R.id.button_current_location);
        //tvLocDetails = findViewById(R.id.tv_location_details);
        mSearchText = findViewById(R.id.et_search);
        tvLastUpdated = findViewById(R.id.tv_last_updated);
        //tvTempIcon = findViewById(R.id.iv_temp_icon);
        tvTempMin = findViewById(R.id.tv_temp_min);
        tvTempMax = findViewById(R.id.tv_temp_max);
        ibSettings = findViewById(R.id.ib_settings);
        ibRefresh = findViewById(R.id.ib_refresh);

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



        mRequestQueue = Volley.newRequestQueue(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        //keeps the keyboard closed on app opening - was previously opening automatically?
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        /* -------------- METHODS CALLED IN ONCREATE -------------- */

        getLocationPermission();
        getDeviceLocation();

        //getWeatherByCityName();

        //loads the device current location weather data even after app has closed and open again because permission is true
        //loadDataPermissionTrue();

        init();

        /* -------------------------------------------------------- */


        lastUpdated();

        buttonCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation(); //WORKS - gets device location and weather data
                Log.d(TAG, "Location Button: " + buttonCurrentLocation);
                Toast.makeText(MainActivity.this, "Current Location WeatherActivity", Toast.LENGTH_SHORT).show();
            }
        });

        ibRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshActivity();

            }
        });

        ibSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // intent to settings activity

                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);

            }
        });

    }
    // END OF ON CREATE


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


                    //getWeatherByDeviceLocation();
                    //getForecastWeatherByDeviceLocation();

                    getWeatherByCityName();
                    //getForecastWeatherByDeviceLocation();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mSearchText.getWindowToken(), 0);
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


    /* -------------- WEATHER BY LAT LONG -------------- */

    public void getWeatherByLatLng() {

        String urlAPILatLong = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=metric&appid=" + API_KEY;

        // MARK - https://api.openweathermap.org/data/2.5/weather?lat=51.50853&lon=-0.12574&appid=7b35bff27c44e02a0ed4347c65c2ffa8 - London latlng example

        Log.d(TAG, "string url latlon total: " + urlAPILatLong);

    }


    /* -------------- WEATHER BY CITY NAME -------------- */

    public void getWeatherByCityName() {

        //TODO - get city name from geo locate and pass it through this url api

        cityName = mSearchText.getText().toString();

        String urlAPI = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=metric&appid=" + API_KEY;

        Log.d(TAG, "getWeatherByCityName: cityname searched: " + cityName);
        Log.d(TAG, "getWeatherByCityName: urlAPI: " + urlAPI);

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

                    getForecastWeatherByDeviceLocation();


                    Log.d(TAG, "onResponse: lat lon " + lat + lon);


                    JSONObject jsoMain = response.getJSONObject("main");
                    String temp = jsoMain.getString("temp");
                    String tempFormat = String.valueOf(temp).split("\\.")[0];
                    tvTemperature.setText(tempFormat);


                    String name = response.getString("name");
                    tvLocation.setText(name);

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


        String urlAPILatLong = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=metric&appid=" + API_KEY;

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
                    String weatherIcon = jsoWeather.getString("icon");

                    //TODO finish off icons
                    if (weatherIcon.contentEquals("50d")) {
                        ivWeatherIcon.setImageResource(R.drawable.group_2_big);
                    }
                    if (weatherIcon.contentEquals("10d")) {
                        ivWeatherIcon.setImageResource(R.drawable.rain_cloud);

                    }

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

        getForecastWeatherByDeviceLocation();

    }





    /* -------------- 5 DAY WEATHER BY DEVICE LOCATION -------------- */

    public void getForecastWeatherByDeviceLocation() {

        //TODO Get forecast weather from either the device location weather or searched city weather

        String urlAPILatLong = "https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&units=metric&appid=" + API_KEY;

        Log.d(TAG, "getForecastWeatherByDeviceLocation: " + urlAPILatLong);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlAPILatLong, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //String[] cityForecast = new String[0];
                //ArrayList<String> cityForecast;


                try {
                    JSONArray jsonArray = response.getJSONArray("list");

/*                    Log.d(TAG, "onResponse: length " + jsonArray.length()); //length 40
                    Log.d(TAG, "onResponse: " + jsonArray); // shows json*/


                    // forecast one

                    JSONObject jso0 = jsonArray.getJSONObject(0);

                        //date/day
                    String date0 = jso0.getString("dt_txt");
                    //Log.d(TAG, "onResponse: day0" + date0);
                    tvForecastOneTitle.setText(date0);


                        //weather icon
                        JSONArray jsaWeather0 = jso0.getJSONArray("weather");
                        JSONObject jsoWeather0 = jsaWeather0.getJSONObject(0);

                    String weatherIcon0 = jsoWeather0.getString("icon");

                        //TODO finish off icons
                    if (weatherIcon0.contentEquals("50d")) {
                        ivForecastOne.setImageResource(R.drawable.group_2_big);
                        }
                    if (weatherIcon0.contentEquals("10n")) {
                        ivForecastOne.setImageResource(R.drawable.rain_cloud);
                        }

                        //temperature
                        JSONObject jsoMain0 = jso0.getJSONObject("main");
                    String temp0 = jsoMain0.getString("temp");
                    String temp0Format = String.valueOf(temp0).split("\\.")[0];
                    tvForecastOneTemp.setText(temp0Format);


                    // forecast two

                    JSONObject jso1 = jsonArray.getJSONObject(1);

                    //date/day
                    String date1 = jso1.getString("dt_txt");
                    //Log.d(TAG, "onResponse: day0" + date1);
                    tvForecastTwoTitle.setText(date1);

                    //weather icon
                    JSONArray jsaWeather1 = jso1.getJSONArray("weather");
                    JSONObject jsoWeather1 = jsaWeather1.getJSONObject(0);

                    String weatherIcon1 = jsoWeather1.getString("icon");

                    //TODO finish off icons
                    if (weatherIcon1.contentEquals("50d")) {
                        ivForecastTwo.setImageResource(R.drawable.group_2_big);
                    }
                    if (weatherIcon1.contentEquals("10n")) {
                        ivForecastTwo.setImageResource(R.drawable.rain_cloud);
                    }


                    // temp
                    JSONObject jsoMain1 = jso1.getJSONObject("main");
                    String temp1 = jsoMain1.getString("temp");
                    String temp1Format = String.valueOf(temp1).split("\\.")[0];
                    tvForecastTwoTemp.setText(temp1Format);

                    Log.d(TAG, "onResponse: " + jso1);


                    // forecast three

                    JSONObject jso2 = jsonArray.getJSONObject(2);

                    //date/day
                    String date2 = jso2.getString("dt_txt");
                    //Log.d(TAG, "onResponse: day0" + date2);
                    tvForecastThreeTitle.setText(date2);

                    //weather icon
                    JSONArray jsaWeather2 = jso2.getJSONArray("weather");
                    JSONObject jsoWeather2 = jsaWeather2.getJSONObject(0);

                    String weatherIcon2 = jsoWeather2.getString("icon");

                    //TODO finish off icons
                    if (weatherIcon2.contentEquals("50d")) {
                        ivForecastThree.setImageResource(R.drawable.group_2_big);
                    }
                    if (weatherIcon2.contentEquals("10n")) {
                        ivForecastThree.setImageResource(R.drawable.rain_cloud);
                    }

                    // temp
                    JSONObject jsoMain2 = jso2.getJSONObject("main");
                    String temp2 = jsoMain2.getString("temp");
                    String temp2Format = String.valueOf(temp2).split("\\.")[0];
                    tvForecastThreeTemp.setText(temp2Format);


                    // forecast four

                    JSONObject jso3 = jsonArray.getJSONObject(3);

                    //date/day
                    String date3 = jso3.getString("dt_txt");
                    //Log.d(TAG, "onResponse: day3" + date3);
                    tvForecastFourTitle.setText(date3);

                    //weather icon
                    JSONArray jsaWeather3 = jso3.getJSONArray("weather");
                    JSONObject jsoWeather3 = jsaWeather3.getJSONObject(0);

                    String weatherIcon3 = jsoWeather3.getString("icon");

                    //TODO finish off icons
                    if (weatherIcon3.contentEquals("50d")) {
                        ivForecastFour.setImageResource(R.drawable.group_2_big);
                    }
                    if (weatherIcon3.contentEquals("10n")) {
                        ivForecastFour.setImageResource(R.drawable.rain_cloud);
                    }

                    // temp
                    JSONObject jsoMain3 = jso3.getJSONObject("main");
                    String temp3 = jsoMain3.getString("temp");
                    String temp3Format = String.valueOf(temp3).split("\\.")[0];
                    tvForecastFourTemp.setText(temp3Format);


                    // forecast five

                    JSONObject jso4 = jsonArray.getJSONObject(4);

                    //date/day
                    String date4 = jso4.getString("dt_txt");
                    //Log.d(TAG, "onResponse: day4" + date4);
                    tvForecastFiveTitle.setText(date4);

                    //weather icon
                    JSONArray jsaWeather4 = jso4.getJSONArray("weather");
                    JSONObject jsoWeather4 = jsaWeather4.getJSONObject(0);

                    String weatherIcon4 = jsoWeather4.getString("icon");

                    //TODO finish off icons
                    if (weatherIcon4.contentEquals("50d")) {
                        ivForecastFive.setImageResource(R.drawable.group_2_big);
                    }
                    if (weatherIcon4.contentEquals("10n")) {
                        ivForecastFive.setImageResource(R.drawable.rain_cloud);
                    }

                    // temp
                    JSONObject jsoMain4 = jso4.getJSONObject("main");
                    String temp4 = jsoMain4.getString("temp");
                    String temp4Format = String.valueOf(temp4).split("\\.")[0];
                    tvForecastFiveTemp.setText(temp4Format);


                    // forecast six

                    JSONObject jso5 = jsonArray.getJSONObject(5);

                    //date/day
                    String date5 = jso5.getString("dt_txt");
                    //Log.d(TAG, "onResponse: day5" + date5);
                    tvForecastSixTitle.setText(date5);

                    //weather icon
                    JSONArray jsaWeather5 = jso5.getJSONArray("weather");
                    JSONObject jsoWeather5 = jsaWeather5.getJSONObject(0);

                    String weatherIcon5 = jsoWeather5.getString("icon");

                    //TODO finish off icons
                    if (weatherIcon5.contentEquals("50d")) {
                        ivForecastSix.setImageResource(R.drawable.group_2_big);
                    }
                    if (weatherIcon5.contentEquals("10n")) {
                        ivForecastSix.setImageResource(R.drawable.rain_cloud);
                    }

                    // temp
                    JSONObject jsoMain5 = jso5.getJSONObject("main");
                    String temp5 = jsoMain5.getString("temp");
                    String temp5Format = String.valueOf(temp5).split("\\.")[0];
                    tvForecastSixTemp.setText(temp5Format);


                    // forecast seven

                    JSONObject jso6 = jsonArray.getJSONObject(6);

                    //date/day
                    String date6 = jso6.getString("dt_txt");
                    //Log.d(TAG, "onResponse: day6" + date6);
                    tvForecastSevenTitle.setText(date6);

                    //weather icon
                    JSONArray jsaWeather6 = jso6.getJSONArray("weather");
                    JSONObject jsoWeather6 = jsaWeather6.getJSONObject(0);

                    String weatherIcon6 = jsoWeather6.getString("icon");

                    //TODO finish off icons
                    if (weatherIcon6.contentEquals("50d")) {
                        ivForecastSeven.setImageResource(R.drawable.group_2_big);
                    }
                    if (weatherIcon6.contentEquals("10n")) {
                        ivForecastSeven.setImageResource(R.drawable.rain_cloud);
                    }

                    // temp
                    JSONObject jsoMain6 = jso6.getJSONObject("main");
                    String temp6 = jsoMain6.getString("temp");
                    String temp6Format = String.valueOf(temp6).split("\\.")[0];
                    tvForecastSevenTemp.setText(temp6Format);


                    // forecast eight

                    JSONObject jso7 = jsonArray.getJSONObject(7);

                    //date/day
                    String date7 = jso7.getString("dt_txt");
                    //Log.d(TAG, "onResponse: day7" + date7);
                    tvForecastEightTitle.setText(date7);

                    //weather icon
                    JSONArray jsaWeather7 = jso7.getJSONArray("weather");
                    JSONObject jsoWeather7 = jsaWeather7.getJSONObject(0);

                    String weatherIcon7 = jsoWeather7.getString("icon");

                    //TODO finish off icons
                    if (weatherIcon7.contentEquals("50d")) {
                        ivForecastEight.setImageResource(R.drawable.group_2_big);
                    }
                    if (weatherIcon7.contentEquals("10n")) {
                        ivForecastEight.setImageResource(R.drawable.rain_cloud);
                    }

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
                                getWeather();
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
                Toast.makeText(this, "location permission true", Toast.LENGTH_SHORT).show();

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

                        getWeather(); // works as a placeholder if the permissions are denied
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