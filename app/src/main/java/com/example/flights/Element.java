package com.example.flights;

public class Element {
    String AirlineName,TotalPrice,ArrivalDateTime,DepartureDateTime,FromAirport,ToAirport;

    public Element(String airlineName, String totalPrice, String arrivalDateTime, String departureDateTime, String fromAirport, String toAirport) {
        AirlineName = airlineName;
        TotalPrice = totalPrice;
        ArrivalDateTime = arrivalDateTime;
        DepartureDateTime = departureDateTime;
        FromAirport = fromAirport;
        ToAirport = toAirport;
    }

    public String getAirlineName() {
        return AirlineName;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public String getArrivalDateTime() {
        return ArrivalDateTime;
    }

    public String getDepartureDateTime() {
        return DepartureDateTime;
    }

    public String getFromAirport() {
        return FromAirport;
    }

    public String getToAirport() {
        return ToAirport;
    }
}
