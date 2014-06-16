package com.napontaratan.BusApp.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.napontaratan.BusApp.R;
import com.napontaratan.BusApp.controller.AlarmReceiver;
import com.napontaratan.BusApp.controller.ServerConnection;
import com.napontaratan.BusApp.model.BusStop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Napon Taratan on 2014-06-10.
 */
public class MapView extends Activity {

    private GoogleMap map;
    private MapFragment mapFragment;
    private LocationManager locationManager;
    private List<BusStop> stops = new ArrayList<BusStop>();
    private final Context cxt = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.mapview);
        locationManager = (LocationManager) cxt.getSystemService(Context.LOCATION_SERVICE);

        if(map == null){
            setupMap();
        }

        String[] destination = this.getIntent().getStringArrayExtra("destination");
        double latitude = Double.parseDouble(destination[0]);
        double longitude = Double.parseDouble(destination[1]);

        map.setIndoorEnabled(false);
        map.setBuildingsEnabled(false);
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        map.animateCamera(CameraUpdateFactory.zoomTo(13));

        ServerConnection server = ServerConnection.getInstance();
        stops = server.getStops();
        for(BusStop b : stops) {
            plotStop(b);
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                System.out.println("clicked");
                createDialogBox(marker.getTitle(),marker.getPosition().latitude, marker.getPosition().longitude);
                return true;
            }
        });
    }

    private void createDialogBox(final String name, final double lat, final double lon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(cxt)
            .setTitle("Create an alarm")
            .setMessage("Do you want to set an alarm for this stop location?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(cxt, name + ": " + lat + ", " + lon, Toast.LENGTH_SHORT).show();
                    setAlarm(name, lat, lon);
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void plotStop(BusStop b) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(b.getLat(), b.getLon()))
                .title(b.getStopName())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.stop_icon)));
    }

    private void setupMap(){
        mapFragment= ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
        map = mapFragment.getMap();
    }

    private void setAlarm(String location_name, double lat, double lon){

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("location_name", location_name);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        locationManager.addProximityAlert(lat,lon, 500, -1, pi);
        System.out.println("AlarmReceiver set!");

    }
}
