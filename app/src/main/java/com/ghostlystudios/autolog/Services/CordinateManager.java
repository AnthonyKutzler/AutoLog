package com.ghostlystudios.autolog.Services;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Manage Current Location data
 */

class CordinateManager {

    private double lat = 0.0, lon = 0.0;
    private LocationManager locationManager;
    private LocationListener locationListener;

    /**
     * @param locationManager GPS or Wifi for Location management
     */
    CordinateManager(LocationManager locationManager){

        this.locationManager = locationManager;

        initLocationListener();
    }
    private void initLocationListener(){
        setLastKnown();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lon = location.getLongitude();
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }
            @Override
            public void onProviderEnabled(String s) {
            }
            @Override
            public void onProviderDisabled(String s) {
            }
        };
        register();
    }

    /**
     * Manage location update interval
     * @throws SecurityException If not authorized to use GPS
     */
    private void register() throws SecurityException{
        //TODO:CHANGE INTERVAL SETTINGS IN ServiceFinals
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                Float.parseFloat(String.valueOf(ServiceFinals.OFFSET*2)), locationListener);
    }

    /**
     * To be used in disconnect or Killing of application
     * @throws SecurityException Same as Register()
     */
    private void unRegister() throws SecurityException{
        locationManager.removeUpdates(locationListener);
    }

    double getLat() {
        return lat;
    }

    double getLon() {
        return lon;
    }

    /**
     * Used if current location is unavailable, use last known location
     * *****Probably redundant******
     * @throws SecurityException same as Register()
     */
    private void setLastKnown() throws SecurityException{
        lat = (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)).getLatitude();
        lon = (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)).getLongitude();
    }
}
