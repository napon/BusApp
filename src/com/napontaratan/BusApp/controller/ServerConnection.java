package com.napontaratan.BusApp.controller;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import com.napontaratan.BusApp.model.BusStop;
import com.napontaratan.BusApp.model.Location;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;

/**
 * Created by Napon Taratan on 2014-06-09.
 */
public class ServerConnection {

    private static ServerConnection instance = null;

    private List<Location> locations = new ArrayList<Location>();
    private List<BusStop> bStops = new ArrayList<BusStop>();

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
    public List<BusStop> getStops() { return bStops; }

    public void clearLocationCache() { locations = new ArrayList<Location>();}
    public void clearStopCache() { bStops = new ArrayList<BusStop>(); }

    public List<Location> parseJSONLocationData(String response){
        try {
            JSONTokener raw 	    = new JSONTokener(response);
            JSONObject jsonObject	= new JSONObject(raw);
            String status = jsonObject.getString("status");
            System.out.print("status: " + status);

            if(!status.equals("OK")) return null;

            JSONArray results = jsonObject.getJSONArray("results");
            for(int i = 0; i < results.length(); i++) {
                JSONObject obj = (JSONObject) results.get(i);
                String address = obj.getString("formatted_address");
                System.out.println(address);
                JSONObject location = obj.getJSONObject("geometry").getJSONObject("location");

                double lat = round(location.getDouble("lat"), 6);
                double lon = round(location.getDouble("lng"), 6);

                Location newLocation = new Location(address, lat, lon);
                locations.add(newLocation);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return locations;
    }

    public List<BusStop> parseXMLBusStopData(String xml) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            BusStopHandler handler = new BusStopHandler(bStops);
            saxParser.parse(new InputSource(new StringReader(xml)), handler);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bStops;
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return responseString;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
