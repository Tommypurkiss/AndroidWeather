package com.tommypurkissdev.weather;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity mainActivity = new MainActivity();

        //TODO
        //call permission true from main activity
        //if access is allowed then go to main
        //else stay on splash screen with toast or message to allow location
        //or else go to an activity with 3 or 4 main cities locations


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

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