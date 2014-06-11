package com.napontaratan.BusApp.view;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.napontaratan.BusApp.R;

/**
 * Created by napontaratan on 2014-06-10.
 */
public class MapView extends Activity {

    private GoogleMap map;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapview);

        if(map == null){
            setupMap();
        }

        String[] destination = this.getIntent().getStringArrayExtra("destination");
        double latitude = Double.parseDouble(destination[0]);
        double longitude = Double.parseDouble(destination[1]);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        map.animateCamera(CameraUpdateFactory.zoomTo(13));
    }

    private void setupMap(){
        mapFragment= ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
        map = mapFragment.getMap();
    }
}
