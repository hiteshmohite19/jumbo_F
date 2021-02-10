package com.example.flights;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FlightApi {

    String base_url="http://b2cmobile.parikshan.net/api/";

    @Headers("Content-Type:application/json")
    @POST("Search_GDS")
    Call<FlightDetails> Search_GDS(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json")
    @POST("Search_STS")
    Call<FlightDetails> Search_STS(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json")
    @POST("Search_6E")
    Call<FlightDetails> Search_6E(@Body RequestBody requestBody);


    @Headers("Content-Type:application/json")
    @POST("Search_SG")
    Call<FlightDetails> Search_SG(@Body RequestBody requestBody);

    @Headers("Content-Type:application/json")
    @POST("Search_G8")
    Call<FlightDetails> Search_G8(@Body RequestBody requestBody);

//
//    @Headers("Content-Type:application/json")
//    @POST("Search_GDS")
//    Call<FlightDetails> Search_GDS(@Body RequestBody requestBody);
}
