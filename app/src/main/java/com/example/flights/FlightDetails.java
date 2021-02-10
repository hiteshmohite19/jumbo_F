package com.example.flights;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FlightDetails {

    @SerializedName("lstJourneyOneWay")
    @Expose
    private ArrayList<JourneyOneWay> lstJourneyOneWay;

    @SerializedName("lstJourneyReturn")
    @Expose
    private ArrayList<JourneyReturn> lstJourneyReturn;

    @Override
    public String toString() {
        return "FlightDetails{" +
                "lstJourneyOneWay=" + lstJourneyOneWay +
                ", lstJourneyReturn=" + lstJourneyReturn +
                '}';
    }

    public ArrayList<JourneyOneWay> getLstJourneyOneWay() {
        return lstJourneyOneWay;
    }

    public void setLstJourneyOneWay(ArrayList<JourneyOneWay> lstJourneyOneWay) {
        this.lstJourneyOneWay = lstJourneyOneWay;
    }

    public ArrayList<JourneyReturn> getLstJourneyReturn() {
        return lstJourneyReturn;
    }

    public void setLstJourneyReturn(ArrayList<JourneyReturn> lstJourneyReturn) {
        this.lstJourneyReturn = lstJourneyReturn;
    }
}
