package com.tommypurkissdev.weather;

import android.Manifest;
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
    //public TextView tvLocDetails;
    public EditText mSearchText;
    public TextView tvLastUpdated;
    //public TextView tvTempIcon;
    public TextView tvTempMin;
    public TextView tvTempMax;

    public TextView tvDayOne;
    public ImageView ivDayOne;
    public TextView tvDayOneTempMin;
    public TextView tvDayOneTempMax;

    public TextView tvDayTwo;
    public TextView tvDayThree;
    public TextView tvDayFour;
    public TextView tvDayFive;


    public ImageButton ibRefresh;
    public ImageButton ibSettings;
    public ImageView imageView;

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
        tvDayOne = findViewById(R.id.tv_day_one);
        ivDayOne = findViewById(R.id.iv_day_one);
        tvDayOneTempMin = findViewById(R.id.tv_day_one_temp_min);
        tvDayOneTempMax = findViewById(R.id.tv_day_one_temp_max);

        tvDayTwo = findViewById(R.id.tv_day_two);
        tvDayThree = findViewById(R.id.tv_day_three);
        tvDayFour = findViewById(R.id.tv_day_four);
        tvDayFive = findViewById(R.id.tv_day_five);



        imageView = findViewById(R.id.iv_weather_icon);


        mRequestQueue = Volley.newRequestQueue(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        //keeps the keyboard closed on app opening - was previously opening automatically?
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        /* -------------- METHODS CALLED IN ONCREATE -------------- */

        getLocationPermission();
        getDeviceLocation();

        //loads the device current location weather data even after app has closed and open again because permission is true
        loadDataPermissionTrue();

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
                    geoLocate();
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

        String url = "https://api.openweathermap.org/data/2.5/find?q=London,uk&units=metric&appid=7b35bff27c44e02a0ed4347c65c2ffa8";

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
        //String urlAPI = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&units=metric&appid=7b35bff27c44e02a0ed4347c65c2ffa8";


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
                        imageView.setImageResource(R.drawable.group_2_big);
                    }
                    if (weatherIcon.contentEquals("10d")) {
                        imageView.setImageResource(R.drawable.rain_cloud);

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

        String urlAPILatLong = "https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&units=metric&appid=" + API_KEY;

        Log.d(TAG, "getForecastWeatherByDeviceLocation: " + urlAPILatLong);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlAPILatLong, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //String[] cityForecast = new String[0];
                ArrayList<String> cityForecast;


                try {
                    JSONArray jsonArray = response.getJSONArray("list");

                    Log.d(TAG, "onResponse: length " + jsonArray.length()); //length 40
                    Log.d(TAG, "onResponse: " + jsonArray); // shows json


                    for (int i = 0; i < response.length(); i += 8) {

                        // day one
                        JSONObject jso0 = jsonArray.getJSONObject(0);

                        //date/day
                        //String day0 = jso0.getString("dt_txt");
                        //tvDayOne.setText(day0);


                        //weather icon
                        JSONArray jsaWeather0 = jso0.getJSONArray("weather");
                        JSONObject jsoWeather0 = jsaWeather0.getJSONObject(0);

                        String weatherIcon = jsoWeather0.getString("icon");

                        //TODO finish off icons
                        if (weatherIcon.contentEquals("50d")) {
                            ivDayOne.setImageResource(R.drawable.group_2_big);
                        }
                        if (weatherIcon.contentEquals("10n")) {
                            ivDayOne.setImageResource(R.drawable.rain_cloud);
                        }

                        //temperature
                        JSONObject jsoMain0 = jso0.getJSONObject("main");
                        String tempMin0 = jsoMain0.getString("temp_min");
                        String tempMin0Format = String.valueOf(tempMin0).split("\\.")[0];
                        tvDayOneTempMin.setText(tempMin0Format);

                        String tempMax0 = jsoMain0.getString("temp_max");
                        String tempMax0Format = String.valueOf(tempMax0).split("\\.")[0];
                        tvDayOneTempMax.setText(tempMax0Format);


                    }


                    // day two
                    JSONObject jso1 = jsonArray.getJSONObject(8);

                    JSONObject jsoMain1 = jso1.getJSONObject("main");

                    String temp1 = jsoMain1.getString("temp");
                    tvDayTwo.setText(temp1);

                    Log.d(TAG, "onResponse: " + jso1);


                    // day three
                    JSONObject jso2 = jsonArray.getJSONObject(16);

                    JSONObject jsoMain2 = jso2.getJSONObject("main");

                    String temp2 = jsoMain2.getString("temp");

                    tvDayThree.setText(temp2);


                    // day four

                    JSONObject jso3 = jsonArray.getJSONObject(24);

                    JSONObject jsoMain3 = jso3.getJSONObject("main");

                    String temp3 = jsoMain3.getString("temp");

                    tvDayFour.setText(temp3);


                    // day five

                    JSONObject jso4 = jsonArray.getJSONObject(32);

                    JSONObject jsoMain4 = jso4.getJSONObject("main");

                    String temp4 = jsoMain4.getString("temp");

                    tvDayFive.setText(temp4);


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