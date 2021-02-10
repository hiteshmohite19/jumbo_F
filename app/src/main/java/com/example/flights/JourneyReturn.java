package com.example.flights;

import java.util.ArrayList;

public class JourneyReturn {

    private ArrayList<ReturnItinerary> lstItinerary;

    public ArrayList<ReturnItinerary> getLstItinerary() {
        return lstItinerary;
    }

    public void setLstItinerary(ArrayList<ReturnItinerary> lstItinerary) {
        this.lstItinerary = lstItinerary;
    }

    @Override
    public String toString() {
        return "JourneyOneWay{" +
                "lstItinerary=" + lstItinerary +
                '}';
    }
}
