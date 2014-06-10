package com.napontaratan.BusApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.napontaratan.BusApp.controller.ServerConnection;
import com.napontaratan.BusApp.model.Location;

import java.util.List;

/**
 * Created by napontaratan on 2014-06-09.
 */
public class MainPage extends Activity{


    private EditText searchInput;
    private ImageButton searchButton;
    private final Context cxt = this;
    private final String API_KEY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Search query input
        searchInput = (EditText) findViewById(R.id.search_query);
        searchInput.setCursorVisible(false);
        searchInput.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchInput.setFocusable(true);
                searchInput.setCursorVisible(true);
            }
        });

        // Search button
        searchButton = (ImageButton) findViewById(R.id.search_icon);
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchInput, InputMethodManager.SHOW_IMPLICIT);
                searchInput.setCursorVisible(false);

                String locationQuery = searchInput.getText().toString();
                if(locationQuery.isEmpty())
                    Toast.makeText(getApplicationContext(), "Enter your destination", Toast.LENGTH_SHORT).show();
                else
                    new GeocodeTask(cxt).execute(locationQuery);
            }
        });

        // handle keyboard actions
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                String locationQuery;
                if(actionId ==  EditorInfo.IME_ACTION_DONE){ // user presses 'done' button
                    locationQuery = v.getText().toString();
                    if(!locationQuery.isEmpty())
                        new GeocodeTask(getApplicationContext()).execute(locationQuery);
                    else Toast.makeText(getApplicationContext(), "Enter your destination", Toast.LENGTH_SHORT).show();
                }
                searchInput.setCursorVisible(false);
                return false;
            }
        });
    }

    /**
     * Geocode address using Google Geocoding API
     * @author daniel
     */
    private class GeocodeTask extends AsyncTask<String, Void, List<Location>> {

        private ProgressDialog dialog;
        private ServerConnection server;

        public GeocodeTask(Context cxt) {
            dialog = new ProgressDialog(cxt);
            server = ServerConnection.getInstance();
        }

        @Override
        protected void onPreExecute() {
//            dialog.setMessage("Finding near by bus stops..");
//            dialog.show();
            Log.d("onPreExecute", "dialog should show up now");
        }

        @Override
        protected List<Location> doInBackground(String... locationName) {
            String query = server.makeJSONQuery("http://maps.googleapis.com/maps/api/geocode/json?address=" + locationName[0] + "&key=" + API_KEY);
            return server.parseJSONLocationData(query);
        }

        @Override
        protected void onPostExecute(List <Location> locations) {
            if(locations.size() == 0)
                Toast.makeText(getApplicationContext(), "No location found", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Number of locations: " + locations.size(), Toast.LENGTH_LONG).show();

            //dialog.dismiss();

            startActivity(new Intent("com.napontaratan.SELECTION"));
        }

    }
}
