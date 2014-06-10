package com.napontaratan.BusApp.view;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import com.napontaratan.BusApp.R;
import com.napontaratan.BusApp.controller.ServerConnection;
import com.napontaratan.BusApp.model.BusStop;
import com.napontaratan.BusApp.model.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by napontaratan on 2014-06-09.
 */
public class AddressSelectionPage extends ListActivity {

    private final Context cxt = this;
    private final String API_KEY = "";
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listItems = new ArrayList<String>();

    private String latitude;
    private String longitude;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                view.setSelected(true);
                //Toast.makeText(getApplicationContext(), listItems.get(position) + ": lat=" + locations.get(position).getLatitude() + " lon=" + locations.get(position).getLongitude(), Toast.LENGTH_SHORT).show();
                Location loc = locations.get(position);
                latitude = String.valueOf(loc.getLatitude());
                longitude = String.valueOf(loc.getLongitude());
                //new GeocodeTask(cxt).execute(lat,lon);
            }
        });

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchInput = (EditText) findViewById(R.id.bus_stop);
                String userInput = searchInput.getText().toString();
                if(userInput.isEmpty()) Toast.makeText(getApplicationContext(), "Enter a bus number", Toast.LENGTH_SHORT);
                else new GeocodeTask(cxt).execute(latitude,longitude,userInput);
            }
        });
    }


    private class GeocodeTask extends AsyncTask<String, Void, List<BusStop>> {

        private ProgressDialog dialog;
        private ServerConnection server;

        public GeocodeTask(Context cxt) {
            dialog = new ProgressDialog(cxt);
            server = ServerConnection.getInstance();
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Finding bus stops..");
            dialog.show();
        }

        @Override
        protected List<BusStop> doInBackground(String... geo) {
            String query = server.makeJSONQuery("http://api.translink.ca/rttiapi/v1/stops?apikey=" + API_KEY + "&lat=" + geo[0] + "&long=" + geo[1] + "&routeNo=" + geo[2] + "&radius=1000");
            return server.parseXMLBusStopData(query);
        }

        @Override
        protected void onPostExecute(List <BusStop> stops) {
            dialog.hide();
            if(stops != null)
                Toast.makeText(getApplicationContext(), "succeeded: size is " + stops.size(), Toast.LENGTH_SHORT).show();
        }
    }


}
