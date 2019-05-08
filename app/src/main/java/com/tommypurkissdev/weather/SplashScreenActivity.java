package com.tommypurkissdev.weather;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;

public class SplashScreenActivity extends AppCompatActivity {

    public static final int ERROR_DIALOG_REQUEST = 9001;
    public final static String TAG = "SplashScreenActivity";


    public RequestQueue mRequestQueue;

    public Boolean mLocationPermissionGranted = false;
    public FusedLocationProviderClient mFusedLocationClient;


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


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

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