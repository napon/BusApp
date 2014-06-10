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
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
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

    public List<Location> getLocations() {
        return locations;
    }

    public List<Location> parseJSONLocationData(String response){
        try {
            JSONTokener raw 	    = new JSONTokener(response);
            JSONObject jsonObject	= new JSONObject(raw);
            String status = jsonObject.getString("status");

            if(!status.equals("OK")) return null;

            JSONObject obj = (JSONObject) jsonObject.getJSONArray("results").get(0);
            String address = obj.getString("formatted_address");
            System.out.println(address);
            JSONObject location = obj.getJSONObject("geometry").getJSONObject("location");

            double lat = location.getDouble("lat");
            double lon = location.getDouble("lng");

            Location newLocation = new Location(address, lat, lon);
            locations.add(newLocation);

        } catch(Exception e) {
            e.printStackTrace();
        }

        return locations;
    }

    public String makeJSONQuery(String server) {

        String responseString = null;
        System.out.println(server);

        try {
            Log.d("makeJSONQuery", "make JSON query to server");
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(new HttpGet(server));
            responseString = new BasicResponseHandler().handleResponse(response);
            System.out.println(responseString);
        } catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseString;
    }
}
