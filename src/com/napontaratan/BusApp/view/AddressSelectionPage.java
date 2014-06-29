package com.napontaratan.BusApp.view;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import com.napontaratan.BusApp.R;
import com.napontaratan.BusApp.controller.ServerConnection;
import com.napontaratan.BusApp.model.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Napon Taratan on 2014-06-09.
 */
public class AddressSelectionPage extends ListActivity {

    private final Context cxt = this;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listItems = new ArrayList<String>();

    private String latitude;
    private String longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.selection);

        ServerConnection server = ServerConnection.getInstance();
        final List<Location> locations = server.getLocations();

        for(Location loc : locations) {
            listItems.add(loc.getName());
        }

        adapter = new ArrayAdapter<String>(this, R.layout.selection_item, listItems);
        setListAdapter(adapter);
        ListView lv = this.getListView();
        lv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i = 0; i < parent.getCount(); i++){
                    View v = parent.getChildAt(i);
                    v.setBackgroundColor(Color.WHITE);
                }

                view.setBackgroundColor(Color.GRAY);
                Location loc = locations.get(position);
                latitude = String.valueOf(loc.getLatitude());
                longitude = String.valueOf(loc.getLongitude());

                Intent intent = new Intent(cxt, MapView.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
