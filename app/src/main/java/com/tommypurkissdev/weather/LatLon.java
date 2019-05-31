package com.tommypurkissdev.weather;

import android.util.Log;


public class LatLon {

    private static final String TAG = "LatLon";


    public double latLoc;
    public double lonLoc;

    public LatLon(double latLoc, double lonLoc) {
        this.latLoc = latLoc;
        this.lonLoc = lonLoc;

        Log.d(TAG, "LatLon: " + latLoc + lonLoc);
    }

    public double getLatLoc() {
        return latLoc;
    }

    public void setLatLoc(double latLoc) {
        this.latLoc = latLoc;
    }

    public double getLonLoc() {
        return lonLoc;
    }

    public void setLonLoc(double lonLoc) {
        this.lonLoc = lonLoc;
    }
}
