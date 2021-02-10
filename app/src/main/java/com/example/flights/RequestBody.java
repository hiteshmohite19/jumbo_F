package com.example.flights;

public class RequestBody {
    String Currency,DepDate,Device,Flag,IPAddress,OfficeID,ROE,Sector,UserType,noOfAdult,noOfChild,noOfInfant,returnDate,returnFrom,returnTo,travClass,travelFrom,travelFromAirport,travelFromCity,travelTo,travelToAirport,travelToCity;

    public RequestBody(String currency, String depDate, String device, String flag, String IPAddress, String officeID, String ROE, String sector, String userType, String noOfAdult, String noOfChild, String noOfInfant, String returnDate, String returnFrom, String returnTo, String travClass, String travelFrom, String travelFromAirport, String travelFromCity, String travelTo, String travelToAirport, String travelToCity) {
        Currency = currency;
        DepDate = depDate;
        Device = device;
        Flag = flag;
        this.IPAddress = IPAddress;
        OfficeID = officeID;
        this.ROE = ROE;
        Sector = sector;
        UserType = userType;
        this.noOfAdult = noOfAdult;
        this.noOfChild = noOfChild;
        this.noOfInfant = noOfInfant;
        this.returnDate = returnDate;
        this.returnFrom = returnFrom;
        this.returnTo = returnTo;
        this.travClass = travClass;
        this.travelFrom = travelFrom;
        this.travelFromAirport = travelFromAirport;
        this.travelFromCity = travelFromCity;
        this.travelTo = travelTo;
        this.travelToAirport = travelToAirport;
        this.travelToCity = travelToCity;
    }

    public String getCurrency() {
        return Currency;
    }

    public String getDepDate() {
        return DepDate;
    }

    public String getDevice() {
        return Device;
    }

    public String getFlag() {
        return Flag;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public String getOfficeID() {
        return OfficeID;
    }

    public String getROE() {
        return ROE;
    }

    public String getSector() {
        return Sector;
    }

    public String getUserType() {
        return UserType;
    }

    public String getNoOfAdult() {
        return noOfAdult;
    }

    public String getNoOfChild() {
        return noOfChild;
    }

    public String getNoOfInfant() {
        return noOfInfant;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getReturnFrom() {
        return returnFrom;
    }

    public String getReturnTo() {
        return returnTo;
    }

    public String getTravClass() {
        return travClass;
    }

    public String getTravelFrom() {
        return travelFrom;
    }

    public String getTravelFromAirport() {
        return travelFromAirport;
    }

    public String getTravelFromCity() {
        return travelFromCity;
    }

    public String getTravelTo() {
        return travelTo;
    }

    public String getTravelToAirport() {
        return travelToAirport;
    }

    public String getTravelToCity() {
        return travelToCity;
    }
}
