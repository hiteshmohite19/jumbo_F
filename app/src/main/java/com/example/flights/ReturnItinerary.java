package com.example.flights;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReturnItinerary implements Serializable {
    @SerializedName("AirlineName")
    private String AirlineName;

    @SerializedName("TotalPrice")
    private  String TotalPrice;

    @SerializedName("ArrivalDateTime")
    private  String ArrivalDateTime;

    @SerializedName("DepartureDateTime")
    private  String DepartureDateTime;

    @SerializedName("FromAirport")
    private  String FromAirport;

    @SerializedName("ToAirport")
    private  String ToAirport;

    public void setFromAirport(String fromAirport) {
        FromAirport = fromAirport;
    }

    public void setToAirport(String toAirport) {
        ToAirport = toAirport;
    }

    public String getFromAirport() {
        return FromAirport;
    }

    public String getToAirport() {
        return ToAirport;
    }

    public String getAirlineName() {
        return AirlineName;
    }

    public void setAirlineName(String airlineName) {
        AirlineName = airlineName;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getArrivalDateTime() {
        return ArrivalDateTime;
    }

    public void setArrivalDateTime(String arrivalDateTime) {
        ArrivalDateTime = arrivalDateTime;
    }

    public String getDepartureDateTime() {
        return DepartureDateTime;
    }

    public void setDepartureDateTime(String departureDateTime) {
        DepartureDateTime = departureDateTime;
    }

    @Override
    public String toString() {
        return "OneItinerary{" +
                "AirlineName='" + AirlineName + '\'' +
                ", TotalPrice='" + TotalPrice + '\'' +
                ", ArrivalDateTime='" + ArrivalDateTime + '\'' +
                ", DepartureDateTime='" + DepartureDateTime + '\'' +
                ", FromAirport='" + FromAirport + '\'' +
                ", ToAirport='" + ToAirport + '\'' +
                '}';
    }
}
