package com.napontaratan.BusApp.controller;

import com.napontaratan.BusApp.model.BusStop;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by napontaratan on 2014-06-10.
 */
public class BusStopHandler extends DefaultHandler {

    private String tempName;
    private String tempNumber;
    private double tempLat;
    private double tempLon;
    private BusStop tempStop;
    private String temp;

    private List<BusStop> busStops;

    public BusStopHandler(List<BusStop> stops) {
        super();
        busStops = stops;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri,localName, qName, attributes);
        temp = "";
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals("StopNo"))
            tempNumber = temp;
        else if(qName.equals("Name"))
            tempName = temp;
        else if(qName.equals("Latitude"))
            tempLat = Double.parseDouble(temp);
        else if(qName.equals("Longitude"))
            tempLon = Double.parseDouble(temp);
        else if(qName.equals("Stop")){
            tempStop = new BusStop(tempNumber, tempName, null, tempLat, tempLon);
            busStops.add(tempStop);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        temp = new String(ch, start, length);
    }
}
