package com.napontaratan.BusApp.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.napontaratan.BusApp.R;
import com.napontaratan.BusApp.controller.BusLocationController;

/**
 * Created by Napon Taratan on 2014-06-10.
 */
public class MapView extends Activity {

    private GoogleMap map;
    private MapFragment mapFragment;
    private BusLocationController controller;
    private Marker marker;
    private final Context cxt = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.mapview);
        controller = new BusLocationController(cxt);

        if(map == null){
            setupMap();
        }

        double latitude = Double.parseDouble(getIntent().getStringExtra("latitude"));
        double longitude = Double.parseDouble(getIntent().getStringExtra("longitude"));

        map.setIndoorEnabled(false);
        map.setBuildingsEnabled(false);
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        map.animateCamera(CameraUpdateFactory.zoomTo(13));
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setTiltGesturesEnabled(false);
        map.getUiSettings().setCompassEnabled(false);

        marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.stop_icon)));

        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {}

            @Override
            public void onMarkerDrag(Marker marker) {}

            @Override
            public void onMarkerDragEnd(Marker marker) {}
        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                marker.setPosition(latLng);
            }
        });

        AlertDialog.Builder intro = new AlertDialog.Builder(this);
        intro.setMessage("Move the marker to your bus stop location to set an alarm");
        intro.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        intro.setCancelable(false);
        intro.create();
        intro.show();

        Button confirm = (Button) findViewById(R.id.btn_setAlarm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogBox(marker.getPosition().latitude, marker.getPosition().longitude);
            }
        });
    }

    private void createDialogBox(final double lat, final double lon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(cxt)
            .setTitle("Create an alarm")
            .setMessage("Do you want to set an alarm for this stop location?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(cxt, "Alarm is set. You may close the application.", Toast.LENGTH_LONG).show();
                    controller.startListening(lat, lon);
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

    private void setupMap(){
        mapFragment= ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
        map = mapFragment.getMap();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
