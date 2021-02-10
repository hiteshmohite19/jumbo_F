package com.example.flights;

import java.util.ArrayList;

public class JourneyOneWay {

    private ArrayList<OneItinerary> lstItinerary;

    public ArrayList<OneItinerary> getLstItinerary() {
        return lstItinerary;
    }

    public void setLstItinerary(ArrayList<OneItinerary> lstItinerary) {
        this.lstItinerary = lstItinerary;
    }

    @Override
    public String toString() {
        return "JourneyOneWay{" +
                "lstItinerary=" + lstItinerary +
                '}';
    }
}
