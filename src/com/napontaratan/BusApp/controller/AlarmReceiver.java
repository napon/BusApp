package com.napontaratan.BusApp.controller;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

/**
 * Created by Napon Taratan on 2014-06-16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "RING RING", Toast.LENGTH_LONG);
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeProximityAlert(PendingIntent.getBroadcast(context, 1, new Intent(context, AlarmReceiver.class), PendingIntent.FLAG_CANCEL_CURRENT));
        System.out.println("AlarmReceiver activated!");

        Intent alarm = new Intent("com.napontaratan.ALARM");
        alarm.putExtra("location_name", intent.getStringExtra("location_name"));
        alarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarm);
    }
}
