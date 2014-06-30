package com.napontaratan.BusApp.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.napontaratan.BusApp.R;
import com.napontaratan.BusApp.controller.BusLocationController;
import com.napontaratan.BusApp.controller.ServerConnection;
import com.napontaratan.BusApp.model.Location;

import java.util.List;

/**
 * Created by Napon Taratan on 2014-06-09.
 */
public class MainPage extends Activity{

    private EditText searchInput;
    private ImageButton searchButton;
    private final Context cxt = this;
    private BusLocationController controller;
    private final String API_KEY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);

        controller = new BusLocationController(cxt);
        if(controller.isActive()) {
            disableAlarmDialog();
        }

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
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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
                        new GeocodeTask(cxt).execute(locationQuery);
                    else Toast.makeText(getApplicationContext(), "Enter your destination", Toast.LENGTH_SHORT).show();
                }
                searchInput.setCursorVisible(false);
                return false;
            }
        });
    }

    private void disableAlarmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(cxt)
                .setTitle("Pending alarm found")
                .setMessage("Do you want to remove the pending alarm?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        controller.stopListening();
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

    private class GeocodeTask extends AsyncTask<String, Void, List<Location>> {

        private ProgressDialog dialog;
        private ServerConnection server;

        public GeocodeTask(Context cxt) {
            dialog = new ProgressDialog(cxt);
            server = ServerConnection.getInstance();
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Finding matching locations..");
            dialog.show();
            if(server.getLocations()!=null) {
                server.clearLocationCache();
            }
        }

        @Override
        protected List<Location> doInBackground(String... locationName) {
            String request = locationName[0].replace(' ', '+');
            String query = server.makeJSONQuery("https://maps.googleapis.com/maps/api/geocode/json?address=" + request + "&key=" + API_KEY);
            return server.parseJSONLocationData(query);
        }

        @Override
        protected void onPostExecute(List <Location> locations) {
            dialog.dismiss();
            if(locations == null || locations.size() == 0)
                Toast.makeText(getApplicationContext(), "No location found. Please make sure an internet connection is available and the address is correct.", Toast.LENGTH_SHORT).show();
            else
                startActivity(new Intent("com.napontaratan.SELECTION"));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
