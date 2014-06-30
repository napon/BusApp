package com.napontaratan.BusApp.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Napon Taratan on 2014-06-16.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent alarm = new Intent("com.napontaratan.ALARM");
        alarm.putExtra("location_name", intent.getStringExtra("location_name"));
        alarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarm);
    }
}
