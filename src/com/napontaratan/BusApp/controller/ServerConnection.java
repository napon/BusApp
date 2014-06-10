package com.napontaratan.BusApp.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import com.napontaratan.BusApp.model.Location;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Establish connection with the remote server for
 * - fetch wifi points
 * - push new wifi points
 *
 * @author Napon Taratan
 */
public class ServerConnection {

    private static ServerConnection instance = null;

    final List<Location> locations = new ArrayList<Location>();

    // allow only one instance of WiFinderServerConnection
    private ServerConnection() {}

    public static ServerConnection getInstance() {
        if(instance == null)
            instance = new ServerConnection();
        return instance;
    }

    public List<Location> parseJSONLocationData(String response){
        try {
            JSONTokener raw 	    = new JSONTokener(response);
            JSONObject jsonObject	= new JSONObject(raw);
            String status = jsonObject.getString("status");

            if(!status.equals("OK")) return null;

            JSONObject obj = (JSONObject) jsonObject.getJSONArray("results").get(0);
            String address = obj.getString("formatted_address");
            JSONObject location = obj.getJSONObject("location");
            double lat = location.getDouble("lat");
            double lon = location.getDouble("lon");

            Location newLocation = new Location(address, lat, lon);
            locations.add(newLocation);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return locations;
    }



    /**
     * Creates an HTTP request to the server and returns the server's response
     *
     * @param server - url of request
     * @author Napon Taratan
     */
    public String makeJSONQuery(String server) {
        URL url = null;
        HttpURLConnection client = null;
        InputStream in = null;
        BufferedReader br = null;

        try {
            url = new URL(server);
            Log.d("makeJSONQuery", "make JSON query to server");
            client = (HttpURLConnection) url.openConnection();

            in = client.getInputStream();
            br = new BufferedReader(
                    new InputStreamReader(in));
            String r;
            while ((r = br.readLine()) != null) {
                System.out.println(r);
            }
            Log.d("makeJSONQuery", "Return: " + r);
            return r;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.disconnect();
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "";
    }
}
