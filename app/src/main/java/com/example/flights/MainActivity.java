package com.example.flights;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private static final String TAG = "project";
    private static final String base_url = "http://b2cmobile.parikshan.net/api/";
    RecyclerAdapter adapter;
    ArrayList<ArrayList<OneItinerary>> oneway;
    ArrayList<ArrayList<ReturnItinerary>> returnway;
    ArrayList<JourneyOneWay> journeyOneWays;
    ArrayList<JourneyReturn> journeyReturns;
    ArrayList<OneItinerary> oneItineraries;
    ArrayList<ReturnItinerary> returnItineraries;

    Bundle bundle;
    String depTimeRange="",fromto="";
    int stopCount=-1,count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bundle=getIntent().getExtras();

        oneway = new ArrayList<>();
        returnway = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getAPIData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        recyclerView = findViewById(R.id.recyclerView);
        ImageView filter=findViewById(R.id.filter);

        onclick();


        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float minPrice = 0,maxPice = 0;
                Log.d(TAG, "onClick: "+oneway.size());
                String values[]=getMinMaxPrice().split(" ");
                minPrice=Float.parseFloat(values[0]);
                maxPice=Float.parseFloat(values[1]);
                Log.d(TAG, "getAPIData: "+minPrice+" "+maxPice);
                Bundle bundle1=new Bundle();
                bundle1.putSerializable("oneway",oneway);
                bundle1.putSerializable("returnway",returnway);
                bundle1.putFloat("minPrice",minPrice);
                bundle1.putFloat("maxPrice",maxPice);

                Intent intent = new Intent(MainActivity.this, Filters.class);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });


    }

    public void getAPIData() {

        Log.e("suraj", "getApidAta call");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FlightApi flightApi = retrofit.create(FlightApi.class);

        RequestBody requestBody=new RequestBody("INR", "2021-03-02", "Android", "IN", "255.177.148.251", "BOMI228DS", "1", "I", "B2C", "1", "0", "0", "2021-03-22", "BKK", "BOM", "Y", "BOM", "Mumbai  [BOM] - Chhatrapati Shivaji Maharaj Airport", "Mumbai", "BKK", "Bangkok  [BKK] - Bangkok", "Bangkok");

        searchGDS(flightApi,requestBody);
        searchSTS(flightApi,requestBody);
        search6E(flightApi,requestBody);
        searchSG(flightApi,requestBody);
        searchG8(flightApi,requestBody);


    }

    public void searchGDS(FlightApi flightApi,RequestBody requestBody){
        Call<FlightDetails> call = flightApi.Search_GDS(requestBody);

        call.enqueue(new Callback<FlightDetails>() {
            @Override
            public void onResponse(Call<FlightDetails> call, Response<FlightDetails> response) {

                Log.e("result " , response.toString());

                if(response.isSuccessful()){
                    journeyOneWays = response.body().getLstJourneyOneWay();

                    journeyReturns = response.body().getLstJourneyReturn();

                    Log.d(TAG, "onResponse: " + journeyOneWays.size());
                    Log.d(TAG, "onResponse return : " + journeyReturns.size());

                    for (int i = 0; i < journeyOneWays.size(); i++) {
                        oneItineraries = response.body().getLstJourneyOneWay().get(i).getLstItinerary();
                        returnItineraries = response.body().getLstJourneyReturn().get(i).getLstItinerary();

                        oneway.add(oneItineraries);
                        returnway.add(returnItineraries);
                    }
                    Log.d(TAG, "onResponse: "+oneway);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(new RecyclerAdapter(oneway, returnway));
                }
            }

            @Override
            public void onFailure(Call<FlightDetails> call, Throwable t) {
                Log.d(TAG, "onResponse: went wrong " + t);
            }
        });
    }

    public void searchSTS(FlightApi flightApi,RequestBody requestBody){
        Call<FlightDetails> call1 = flightApi.Search_STS((requestBody));


        call1.enqueue(new Callback<FlightDetails>() {
            @Override
            public void onResponse(Call<FlightDetails> call1, Response<FlightDetails> response) {

                if(response.isSuccessful()) {
                    journeyOneWays = response.body().getLstJourneyOneWay();

                    journeyReturns = response.body().getLstJourneyReturn();

                    Log.d(TAG, "onResponse: " + journeyOneWays.size());
                    for (int i = 0; i < journeyOneWays.size(); i++) {
                        oneItineraries = response.body().getLstJourneyOneWay().get(i).getLstItinerary();
                        returnItineraries = response.body().getLstJourneyReturn().get(i).getLstItinerary();
                        oneway.add(oneItineraries);
                        returnway.add(returnItineraries);
                    }
                    adapter = new RecyclerAdapter(oneway, returnway);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<FlightDetails> call1, Throwable t) {
                Log.d(TAG, "onResponse: went wrong " + t);
            }
        });
    }

    public void search6E(FlightApi flightApi,RequestBody requestBody){
        Call<FlightDetails> call2 = flightApi.Search_6E((requestBody));


        call2.enqueue(new Callback<FlightDetails>() {
            @Override
            public void onResponse(Call<FlightDetails> call2, Response<FlightDetails> response) {

                if(response.isSuccessful()) {
                    journeyOneWays = response.body().getLstJourneyOneWay();

                    journeyReturns = response.body().getLstJourneyReturn();

                    Log.d(TAG, "onResponse: " + journeyOneWays.size());
                    for (int i = 0; i < journeyOneWays.size(); i++) {
                        oneItineraries = response.body().getLstJourneyOneWay().get(i).getLstItinerary();
                        returnItineraries = response.body().getLstJourneyReturn().get(i).getLstItinerary();
                        oneway.add(oneItineraries);
                        returnway.add(returnItineraries);
                    }
                    adapter = new RecyclerAdapter(oneway, returnway);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<FlightDetails> call2, Throwable t) {
                Log.d(TAG, "onResponse: went wrong " + t);
            }
        });
    }

    public void searchSG(FlightApi flightApi,RequestBody requestBody){
        Call<FlightDetails> call3 = flightApi.Search_SG((requestBody));

        call3.enqueue(new Callback<FlightDetails>() {
            @Override
            public void onResponse(Call<FlightDetails> call3, Response<FlightDetails> response) {

                if(response.isSuccessful()) {
                    journeyOneWays = response.body().getLstJourneyOneWay();

                    journeyReturns = response.body().getLstJourneyReturn();

                    for (int i = 0; i < journeyOneWays.size(); i++) {
                        oneItineraries = response.body().getLstJourneyOneWay().get(i).getLstItinerary();
                        returnItineraries = response.body().getLstJourneyReturn().get(i).getLstItinerary();
                        oneway.add(oneItineraries);
                        returnway.add(returnItineraries);
                    }

                    adapter = new RecyclerAdapter(oneway, returnway);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<FlightDetails> call3, Throwable t) {
                Log.d(TAG, "onResponse: went wrong " + t);
            }
        });
    }

    public void searchG8(FlightApi flightApi,RequestBody requestBody){
        Call<FlightDetails> call3 = flightApi.Search_G8((requestBody));

        call3.enqueue(new Callback<FlightDetails>() {
            @Override
            public void onResponse(Call<FlightDetails> call3, Response<FlightDetails> response) {

                if(response.isSuccessful()) {
                    journeyOneWays = response.body().getLstJourneyOneWay();

                    journeyReturns = response.body().getLstJourneyReturn();

                    for (int i = 0; i < journeyOneWays.size(); i++) {
                        oneItineraries = response.body().getLstJourneyOneWay().get(i).getLstItinerary();
                        returnItineraries = response.body().getLstJourneyReturn().get(i).getLstItinerary();
                        oneway.add(oneItineraries);
                        returnway.add(returnItineraries);
                    }

                    adapter = new RecyclerAdapter(oneway, returnway);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<FlightDetails> call3, Throwable t) {
                Log.d(TAG, "onResponse: went wrong " + t);
            }
        });
    }



    public void onclick() {
        Button earliest, cheapest, fastest;

        earliest = findViewById(R.id.earliest);
        cheapest = findViewById(R.id.cheapest);
        fastest = findViewById(R.id.fastest);

        earliest.setOnClickListener(new View.OnClickListener()   {
            @Override
            public void onClick(View v) {
                sortBy("earliest");
                earliest.setTextColor(Color.parseColor("#FF000000"));
                cheapest.setTextColor(Color.parseColor("#D3D3D3"));
                fastest.setTextColor(Color.parseColor("#D3D3D3"));
            }
        });

        cheapest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortBy("cheapest");
                earliest.setTextColor(Color.parseColor("#D3D3D3"));
                cheapest.setTextColor(Color.parseColor("#FF000000"));
                fastest.setTextColor(Color.parseColor("#D3D3D3"));
            }
        });

        fastest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortBy("fastest");
                earliest.setTextColor(Color.parseColor("#D3D3D3"));
                cheapest.setTextColor(Color.parseColor("#D3D3D3"));
                fastest.setTextColor(Color.parseColor("#FF000000"));
            }
        });

    }


    public void sortBy(String sort) {

        if (sort == "earliest") {
            Log.d(TAG, "sortBy: earliest");

            Collections.sort(oneway, new Comparator<ArrayList<OneItinerary>>() {
                @Override
                public int compare(ArrayList<OneItinerary> o1, ArrayList<OneItinerary> o2) {
                    Date dep1=stringToDate(o1.get(0).getDepartureDateTime());
                    Date dep2=stringToDate(o2.get(0).getDepartureDateTime());
                    return dep1.compareTo(dep2);
                }
            });
            adapter.notifyDataSetChanged();
        }

        else if (sort == "cheapest") {
            Collections.sort(oneway, new Comparator<ArrayList<OneItinerary>>() {
                @Override
                public int compare(ArrayList<OneItinerary> o1, ArrayList<OneItinerary> o2) {
                    String dep1=(o1.get(0).getTotalPrice());
                    String dep2=(o2.get(0).getTotalPrice());
//                    Log.d(TAG, "compare: "+dep1+" "+dep2);
                    return ((Float)Float.parseFloat(dep1)).compareTo((Float)Float.parseFloat(dep2));
                }
            });
            adapter.notifyDataSetChanged();
        }
        else if (sort == "fastest") {

            Collections.sort(oneway, new Comparator<ArrayList<OneItinerary>>() {
                @Override
                public int compare(ArrayList<OneItinerary> o1, ArrayList<OneItinerary> o2) {
                    Duration d1,d2;
                    if (o1.size() > 0) {

                        LocalDateTime dateTime1 = LocalDateTime.parse(o1.get(0).getDepartureDateTime());
                        LocalDateTime dateTime2 = LocalDateTime.parse(o1.get(o1.size() - 1).getArrivalDateTime());

                        d1 = Duration.between(dateTime1, dateTime2);

                        LocalDateTime dateTime3 = LocalDateTime.parse(o2.get(0).getDepartureDateTime());
                        LocalDateTime dateTime4 = LocalDateTime.parse(o2.get(o2.size() - 1).getArrivalDateTime());

                        d2 = Duration.between(dateTime3, dateTime4);
//                        Log.d(TAG, "compare: "+d1.compareTo(d2));
                    } else {

                        LocalDateTime dateTime1 = LocalDateTime.parse(o1.get(0).getDepartureDateTime());
                        LocalDateTime dateTime2 = LocalDateTime.parse(o1.get(0).getArrivalDateTime());

                        d1 = Duration.between(dateTime1, dateTime2);

                        LocalDateTime dateTime3 = LocalDateTime.parse(o2.get(0).getDepartureDateTime());
                        LocalDateTime dateTime4 = LocalDateTime.parse(o2.get(0).getArrivalDateTime());

                        d2 = Duration.between(dateTime3, dateTime4);
//                        Log.d(TAG, "compare: "+d1.compareTo(d2));
                    }
                    return d1.compareTo(d2);
                }
            });
            adapter.notifyDataSetChanged();

        }

//        Log.d(TAG, "sortBy: " + " " + oneway.size() + oneway + " " + oneway.size());
//        for (ArrayList<OneItinerary> one : oneway) {
//            Log.d(TAG, "sortBy: " +" ** "+ one);
//        }
//        for (ArrayList<ReturnItinerary> ret : returnway) {
//            Log.d(TAG, "sortBy: " +" ** "+ ret);
//        }

    }

    public Date stringToDate(String date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try{
            Date d=formatter.parse(date);
            return d;
        }catch (Exception e){
            Log.d(TAG, "stringToDate: "+e.getMessage());
        }

//        Log.d(TAG, "stringToDate: "+date);
        return null;
    }

    public String getTotalTime (String departure, String arrival){
        LocalDateTime dateTime1 = LocalDateTime.parse(departure);
        LocalDateTime dateTime2 = LocalDateTime.parse(arrival);

        Duration duration = Duration.between(dateTime1, dateTime2);
//        Log.d(TAG, "getTotalTime: " + duration.toHours() + " " + duration.toMillis());

        long diffMinutes = (duration.toMillis() / (1000 * 60)) % 60;

        return (duration.toHours()) + "h " + (diffMinutes) + "m";
    }

    public String getMinMaxPrice(){
        Collections.sort(oneway, new Comparator<ArrayList<OneItinerary>>() {
            @Override
            public int compare(ArrayList<OneItinerary> o1, ArrayList<OneItinerary> o2) {
                Float dep1=Float.parseFloat(o1.get(0).getTotalPrice());
                Float dep2=Float.parseFloat(o2.get(0).getTotalPrice());
                return dep1.compareTo(dep2);
            }
        });
//        Log.d(TAG, "getMinMaxPrice: "+oneway.size());
        return (oneway.get(0).get(0).getTotalPrice())+" "+oneway.get(oneway.size()-1).get(0).getTotalPrice();
    }
}