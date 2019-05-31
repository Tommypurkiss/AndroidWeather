package com.tommypurkissdev.weather;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SplashScreenActivity extends AppCompatActivity {

    public static final int ERROR_DIALOG_REQUEST = 9001;
    public final static String TAG = "SplashScreenActivity";

    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    public RequestQueue mRequestQueue;

    public Boolean mLocationPermissionGranted = false;
    public FusedLocationProviderClient mFusedLocationClient;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    /*public double latLoc;
    public double lonLoc;*/
    public double lat;
    public double lon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        MainActivity mainActivity = new MainActivity();


        //TODO
        //call permission true from main activity
        //if access is allowed then go to main
        //else stay on splash screen with toast or message to allow location
        //or else go to an activity with 3 or 4 main cities locations
        isServiceOK();

        //getDeviceLocation();
        getLocationPermission();





    }


    public boolean isServiceOK() {
        Log.d(TAG, "isServiceOK: checking google services version");

        int available = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(SplashScreenActivity.this);

        if (available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServiceOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServiceOK: an error occurred but can be fixed");
            Dialog dialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(SplashScreenActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Cant make request", Toast.LENGTH_SHORT).show();
        }
        return false;
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


                               /* latLoc = lat;
                                lonLoc = lon;*/


                                Log.d(TAG, "onComplete: latlon" + lat + lon);
                                //Log.d(TAG, "onComplete: latLoc lonLoc" + latLoc + lonLoc);


                                //LatLon latLon = new LatLon(lat, lon);


                            } else {
                                //getWeather();
                                Toast.makeText(SplashScreenActivity.this, "Current Location Not Found, Sorry. Try Restarting The App!", Toast.LENGTH_SHORT).show();
                            }


                            //Log.d(TAG, "double value of: " + lat + lon);

                            //MARK - getWeatherByDeviceLocation() now works because it is taking the lat and lon from searching device location first


                            //getWeatherByDeviceLocation();


                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(SplashScreenActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
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

                //init();
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

                getDeviceLocation();

                //once permission is granted it will get the device location weather
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();


                //MARK - getDeviceLocation() now works and shows current device location weather because its called after permissions are allowed
                //getDeviceLocation(); //TODO might not need since getDeviceLocation is called in loadDataPermissionTrue
            }
        }
    }

    //once the permission is true to get device location weather this is called in oncreate method and loads the current weather when the app is closed and starts up again
    public void loadDataPermissionTrue() {
        if (mLocationPermissionGranted) {


            getDeviceLocation();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

            Log.d(TAG, "loadDataPermissionTrue: " + mLocationPermissionGranted);
        }
    }
}





/*




Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();





           if (mainActivity.mLocationPermissionGranted == true) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(SplashScreenActivity.this, "You must Enable Location Services to continue", Toast.LENGTH_SHORT).show();

        }
 */