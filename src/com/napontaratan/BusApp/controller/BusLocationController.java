package com.napontaratan.BusApp.controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by Napon Taratan on 2014-06-25.
 */
public class BusLocationController {

    private Context context;
    private AlarmManager alarmManager;
    private LocationManager locationManager;
    private BusLocationListener listener;

    private boolean activated = false;

    public BusLocationController(Context cxt) {
        context = cxt;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void startListening(final double destination_lat, final double destination_lon, final String location_name) {
        stopListening();
        listener = new BusLocationListener( destination_lat, destination_lon, location_name);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 100, listener);
        activated = true;
    }

    public void stopListening() {
        if(isActive()) {
            locationManager.removeUpdates(listener);
            activated = false;
        }
    }

    public boolean isActive() {
        return activated;
    }

    private class BusLocationListener implements LocationListener {

        private double destination_lat;
        private double destination_lon;
        private String location_name;

        public BusLocationListener(double lat, double lon, String name) {
            this.destination_lat = lat;
            this.destination_lon = lon;
            this.location_name = name;
        }

        @Override
        public void onLocationChanged(Location location) {

            double currentLat = location.getLatitude();
            double currentLon = location.getLongitude();
            System.out.println("checked");
            float[] results = new float[1];
            Location.distanceBetween(destination_lat,destination_lon,currentLat,currentLon,results);
            System.out.println("radius to destination = " + results[0]);
            if(results[0] <= 500) {
                System.out.println("**** in bound ****");
                locationManager.removeUpdates(this);
                Intent intent = new Intent(context, AlarmReceiver.class);
                intent.putExtra("location_name", location_name);
                final PendingIntent pi = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, 1, pi);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}

    }
}
