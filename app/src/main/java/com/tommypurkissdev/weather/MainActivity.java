package com.tommypurkissdev.weather;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.maps.model.*;

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
    public TextView tvTemperature;
    public TextView tvLocation;
    public TextView tvDescription;
    public ImageButton buttonCurrentLocation;
    //public TextView tvLocDetails;
    public EditText mSearchText;
    public TextView tvLastUpdated;
    public TextView tvTempIcon;
    public TextView tvTempMin;
    public TextView tvTempMax;

    public RequestQueue mRequestQueue;

    public Boolean mLocationPermissionGranted = false;
    public FusedLocationProviderClient mFusedLocationClient;




    //CONST
    public final static String TAG = "MainActivity";
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String API_KEY = "7b35bff27c44e02a0ed4347c65c2ffa8"; //openweathermap api key
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    public static final int ERROR_DIALOG_REQUEST = 9001;

    //string
    //public static String lat;
    //public static String lon;

    //double
    public static double lat;
    public static double lon;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvTemperature = findViewById(R.id.tv_temp);
        tvLocation = findViewById(R.id.tv_location);
        tvDescription = findViewById(R.id.tv_description);
        buttonCurrentLocation = findViewById(R.id.button_current_location);
        //tvLocDetails = findViewById(R.id.tv_location_details);
        mSearchText = findViewById(R.id.et_search);
        tvLastUpdated = findViewById(R.id.tv_last_updated);
        tvTempIcon = findViewById(R.id.iv_temp_icon);
        tvTempMin = findViewById(R.id.tv_temp_min);
        tvTempMax = findViewById(R.id.tv_temp_max);


        mRequestQueue = Volley.newRequestQueue(this);


        /* -------------- METHODS CALLED IN ONCREATE -------------- */

        getLocationPermission();
        getDeviceLocation();
        isServiceOK();
        //OnCreate loads up weather data
        //getWeather();

        //loads the device current location weather data even after app has closed and open again because permission is true
        loadDataPermissionTrue();

        /* -------------------------------------------------------- */


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //pulling down the screen refreshes the weather data
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshData();

                pullToRefresh.setRefreshing(true); // keep on false to keep refreshing?
            }
        });

        lastUpdated();

        buttonCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation(); //WORKS - gets device location and weather data
                Log.d(TAG, "Location Button: " + buttonCurrentLocation);
                Toast.makeText(MainActivity.this, "Current Location Weather", Toast.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isServiceOK() {
        Log.d(TAG, "isServiceOK: checking google services version");

        int available = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(MainActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServiceOK: Google Play Services is working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServiceOK: an error occured but can be fixed");
            Dialog dialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Cant make request", Toast.LENGTH_SHORT).show();
        }
        return false;
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
                    geoLocate();
                }
                return false;
            }
        });
    }



    //calls get data and shows a toast message
    private void refreshData() {
        //getWeather();

        //TODO - fix reloading animation
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);



        //TODO change toast text later on
        Toast.makeText(this, "Refreshed Weather Data Complete", Toast.LENGTH_SHORT).show();

    }


    private void lastUpdated() {

        //get current date and time and print it in the text view

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setTimeZone(TimeZone.getDefault());
        String currentDateAndTime = sdf.format(new Date());

        tvLastUpdated.setText("Last Updated: " + currentDateAndTime);

        Log.d(TAG, "refreshData: show date and time" + currentDateAndTime);

    }


    public void tempUnits() {

        /* TODO
        sudo

        if temp unit button? == c value {

        do the kelvin temp value - 273.15
        } if else? temp == f value {
        do the kelvin temp value * 9/(over or divided?)5 - 459.67

        } else {

        show kelvin
        }


         */
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
                    JSONArray jsonArray1 = jsonObject.getJSONArray("weather");
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(0);

                    //main get string calls temp
                    String temp = main.getString("temp");
                    //removes the decimal point from the string
                    String tempFormat = String.valueOf(temp).split("\\.")[0];
                    tvTemperature.setText(tempFormat);

                    //json object has name
                    String city = jsonObject.getString("name");
                    tvLocation.setText(city);

                    //json object1 get string calls description from array
                    String desc = jsonObject1.getString("description");
                    tvDescription.setText(desc);

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
                    JSONArray weather = response.getJSONArray("weather");
                    JSONObject jsoWeather = weather.getJSONObject(0);

                    //description
                    String description = jsoWeather.getString("description");
                    tvDescription.setText(description);

                    //icon
                    String weatherIcon = jsoWeather.getString("icon");
                    tvTempIcon.setText(weatherIcon);

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

                            //Double.toString(currentLocation.getLatitude());
                            //Double.toString(currentLocation.getLongitude());
                            //LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());


                            //tvLocDetails.setText(latLng.toString());


                            //store lat and lon in their respective vars as string

                            //lat = String.valueOf(currentLocation.getLatitude());
                            //lon = String.valueOf(currentLocation.getLongitude());

                            //Log.d(TAG, "string value of: " + lat + lon);


                            //store lat and lon in their respective vars as double

                            lat = currentLocation.getLatitude();
                            lon = currentLocation.getLongitude();

                            Log.d(TAG, "double value of: " + lat + lon);


                            //TODO MARK - getWeatherByDeviceLocation() now works because it is taking the lat and lon from searching device location first
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

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            getWeather(); // works as a placeholder if the permissions are denied
                            //TODO need to test turning permissions back on if current location weather will show
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    //once permission is granted it will get the device location weather

                    //getWeather(); //placeholder
                    //getWeatherByDeviceLocation();

                    //TODO MARK - getDeviceLocation() now works and shows current device location weather because its called after permissions are allowed
                    getDeviceLocation();

                }




            }
        }

    }


    //once the permission is true to get device location weather this is called in oncreate method and loads the current weather
    public void loadDataPermissionTrue() {

        // TODO look into this 'mLocationPermissionGranted == true' can be simplified to 'mLocationPermissionGranted'
        if (mLocationPermissionGranted == true) {
            //getWeather(); //placeholder


            getDeviceLocation();


        }
    }


}


/* MARK: - SPARE CODE


//reload activity
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);

 */

