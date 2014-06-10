package com.napontaratan.BusApp;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import com.napontaratan.BusApp.controller.ServerConnection;
import com.napontaratan.BusApp.model.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by napontaratan on 2014-06-09.
 */
public class SelectionPage extends ListActivity {

    ArrayAdapter<String> adapter;
    ArrayList<String> listItems = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection);

        ServerConnection server = ServerConnection.getInstance();
        List<Location> locations = server.getLocations();
        for(Location loc : locations) {
            listItems.add(loc.getName());
        }

        adapter = new ArrayAdapter<String>(this, R.layout.selection_item, listItems);
        setListAdapter(adapter);
    }


}
