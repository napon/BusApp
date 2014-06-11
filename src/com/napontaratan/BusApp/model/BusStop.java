package com.napontaratan.BusApp.model;

import java.util.List;

/**
 * Created by napontaratan on 2014-06-10.
 */
public class BusStop {

    private String stopNumber;
    private String stopName;
    private List<Integer> buses;
    private double lat;
    private double lon;

    public BusStop(String number, String name, List<Integer> buses, double lat, double lon){
        this.stopNumber = number;
        this.stopName = name;
        this.buses = buses;
        this.lat = lat;
        this.lon = lon;
    }

    public String getStopNumber() {
        return stopNumber;
    }

    public String getStopName() {
        return stopName;
    }

    public List<Integer> getBuses() {
        return buses;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
