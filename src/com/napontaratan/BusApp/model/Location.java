package com.napontaratan.BusApp.model;

/**
 * Created by napontaratan on 2014-06-09.
 */
public class Location {

    private String name;
    private double lat;
    private double lon;

    public Location(String name, double lat, double lon){
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() { return this.name; }

    public double getLatitude() {
        return this.lat;
    }

    public double getLongitude() {
        return this.lon;
    }
}
