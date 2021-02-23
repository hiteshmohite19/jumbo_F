package com.example.flights;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.slider.Slider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

public class Filters extends AppCompatActivity {

    private static final String TAG = "project";
    int stopCount=-1,filterCount=0;
    String depRange="",fromto="from",departureDate="",returnDate="";
    TextView from,to,clear,count,maxprice,minprice;
    LinearLayout zerostop,onestop,twostop,btnDep1,btnDep2,btnDep3,btnDep4;
    ArrayList<ArrayList<OneItinerary>> oneway,newoneway;
    ArrayList<ArrayList<ReturnItinerary>> returnway,newreturnway;
    Button apply;
    Float sliderPrice;
    Slider slider;
    Float minPrice,maxPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Bundle b=getIntent().getExtras();

        slider=findViewById(R.id.slider);
        maxprice=findViewById(R.id.maxPrice);
        minprice=findViewById(R.id.minPrice);
        count=findViewById(R.id.count);

        try{
            oneway = (ArrayList<ArrayList<OneItinerary>>) getIntent().getSerializableExtra("oneway");
            returnway= (ArrayList<ArrayList<ReturnItinerary>>) getIntent().getSerializableExtra("returnway");;
            newoneway = (ArrayList<ArrayList<OneItinerary>>) getIntent().getSerializableExtra("oneway");
            newreturnway= (ArrayList<ArrayList<ReturnItinerary>>) getIntent().getSerializableExtra("returnway");
            departureDate=b.getString("departureDate");
            returnDate=b.getString("returnDate");

            Log.d(TAG, "onCreate: "+departureDate+" "+returnDate);
            minPrice=b.getFloat("minPrice",0.0f);
            maxPrice= b.getFloat("maxPrice", 0.0f);
//            Log.d(TAG, "data: "+b.getFloat("maxPrice", 0.0f)+" "+b.getFloat("minPrice",0.0f));

            slider.setValueFrom((b.getFloat("minPrice",0.0f)));
            slider.setValueTo(b.getFloat("maxPrice", 0.0f));
            slider.setValue(b.getFloat("maxPrice", 0.0f));
            sliderPrice=b.getFloat("maxPrice", 0.0f);
            minprice.setText(String.valueOf(b.getFloat("minPrice",0.0f)));
            maxprice.setText(String.valueOf(b.getFloat("maxPrice", 0.0f)));
            count.setText(newoneway.size()+" of "+newoneway.size());
            Log.d(TAG, "onCreate: xyz "+" "+minPrice+" "+maxPrice);

        }catch (Exception e){
            Log.d(TAG, "exception "+e.getMessage());
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        apply=findViewById(R.id.apply);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                Bundle bundle1=new Bundle();
                bundle1.putSerializable("oneway",newoneway);
                bundle1.putSerializable("returnway",newreturnway);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtras(bundle1);
                startActivity(intent);
//                startActivity(new Intent(getApplicationContext(),MainActivity.class).putExtra("stopCount",stopCount).putExtra("depRange",depRange).putExtra("fromto",fromto).putExtra("oneway",oneway).putExtra("returnway",returnway));
            }
        });


        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                Log.d(TAG, "onValueChange: "+value);
                sliderPrice=value;
                updateDataOnSliderChanger(sliderPrice);
            }
        });


        clear=findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                zerostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                onestop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                twostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));

                btnDep1.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                btnDep2.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                btnDep3.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                btnDep4.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));

                stopCount=-1;
                depRange="";
            }
        });


        onclick();
    }

    public void onclick(){

        from=findViewById(R.id.from);
        to=findViewById(R.id.to);
        zerostop=findViewById(R.id.zerostop);
        onestop=findViewById(R.id.onestop);
        twostop=findViewById(R.id.twostop);
        btnDep1=findViewById(R.id.btnDep1);
        btnDep2=findViewById(R.id.btnDep2);
        btnDep3=findViewById(R.id.btnDep3);
        btnDep4=findViewById(R.id.btnDep4);


        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                from.setBackground(getResources().getDrawable(R.drawable.roundcorner_white));
                to.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                fromto="from";
            }
        });

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                from.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                to.setBackground(getResources().getDrawable(R.drawable.roundcorner_white));
                fromto="to";
            }
        });


        zerostop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zerostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
                onestop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                twostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                stopCount=1;
                filterCount=1;
                filterByStopCount(stopCount);

            }
        });
        onestop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zerostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                onestop.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
                twostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                stopCount=2;
                filterCount=1;
                filterByStopCount(stopCount);
            }
        });
        twostop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zerostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                onestop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                twostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
                stopCount=3;
                filterCount=1;
                filterByStopCount(stopCount);
            }
        });


        btnDep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDep1.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
                btnDep2.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                btnDep3.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                btnDep4.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                depRange="Before6am";
                filterCount=2;
                filterDepTime();
            }
        });
        btnDep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDep1.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                btnDep2.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
                btnDep3.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                btnDep4.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                depRange="6amTo12pm";
                filterCount=2;
                filterDepTime();
            }
        });
        btnDep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDep1.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                btnDep2.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                btnDep3.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
                btnDep4.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                depRange="12pmTo6pm";
                filterCount=2;
                filterDepTime();
            }
        });
        btnDep4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDep1.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                btnDep2.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                btnDep3.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                btnDep4.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
                depRange="After6pm";
                filterCount=2;
                filterDepTime();
            }
        });
    }


    public void updateDataOnSliderChanger(Float val){
        Log.d(TAG, "price: "+val);
        newoneway=new ArrayList<>();
        newreturnway=new ArrayList<>();
        for(int i=0;i<oneway.size();i++){
            if(Float.valueOf(oneway.get(i).get(0).getTotalPrice())<=val){
                newoneway.add(oneway.get(i));
                newreturnway.add(returnway.get(i));
                count.setText(newoneway.size()+" of "+oneway.size());
            }
        }

        Log.d(TAG, "updateDataOnSliderChanger: "+newoneway.size());
    }



    public void filterByStopCount(int stopCount){
        Log.d(TAG, "filterByStopCount: asdfghjkl"+" "+stopCount+" "+newoneway.size()+ " ");
        int i=0;
        if(filterCount==1)
            updateDataOnSliderChanger(sliderPrice);

        ArrayList<Integer> index=new ArrayList<>();
        for(ArrayList<OneItinerary> one : newoneway){

            if(stopCount==1 && one.size()!=1){
                index.add(i);
                i++;
            }
            else if(stopCount==2 && one.size()!=2){
                index.add(i);
                i++;
            }
            else if(stopCount>2 && one.size()>2){
                index.add(i);
                i++;
            }
        }

        stopCountFilter(index);

        count.setText(newoneway.size()+" "+oneway.size());
    }

    public void stopCountFilter(ArrayList<Integer> indexesToRemove){

        Collections.reverse(indexesToRemove);
        for (Integer indexToRemove : indexesToRemove) {
            newoneway.remove((int)indexToRemove);
            newreturnway.remove((int)indexToRemove);
        }
    }


    public int filterDepTime(){
        int c=0;
        if(filterCount==2)
            updateDataOnSliderChanger(sliderPrice);


        if(fromto=="from"){
            if(depRange=="Before6am"){
                Date endDate = new Date(departureDate+" 06:00:00");
                for(ArrayList<OneItinerary> one : newoneway){

                }
            }
            else if(depRange=="6amTo12pm"){
                Date startDate = new Date(departureDate+" 06:00:00");
                Date endDate = new Date(departureDate+" 12:00:00");
            }
            else if(depRange=="12pmTo6pm"){
                Date startDate = new Date(departureDate+" 12:00:00");
                Date endDate = new Date(departureDate+" 18:00:00");
            }
            else if(depRange=="After6pm"){
                Date startDate = new Date(departureDate+" 18:00:00");
            }

            count.setText(newoneway.size()+" "+oneway.size());
        }
        else if(fromto=="to"){
            Log.d(TAG, "filterDepTime: "+fromto);
            if(depRange=="Before6am"){
                Date endDate = new Date(returnDate+" 06:00:00");
            }
            else if(depRange=="6amTo12pm"){
                Date startDate = new Date(returnDate+" 06:00:00");
                Date endDate = new Date(returnDate+" 12:00:00");
            }
            else if(depRange=="12pmTo6pm"){
                Date startDate = new Date(returnDate+" 12:00:00");
                Date endDate = new Date(returnDate+" 18:00:00");
            }
            else if(depRange=="After6pm"){
                Date startDate = new Date(returnDate+" 18:00:00");
            }

            count.setText(newoneway.size()+" "+oneway.size());
        }


        Log.d(TAG, "filterDepTime: "+c+" ** "+newoneway.size());


        return -1;
    }

    public boolean filterDate(Date oneDate, Date startDate, Date endDate){
        Log.d(TAG, "filterDate: "+oneDate+" "+startDate+" "+endDate);
        if(startDate==null){
            if(oneDate.before(endDate)){
                Log.d(TAG, "c: ");
                return true;
            }
        }
        else if(endDate==null){
            if(oneDate.after(startDate)){
                return true;
            }
        }
        else{
            if(oneDate.after(startDate)&&oneDate.before(endDate)){
                return true;
            }
        }
        return false;
    }

//    public boolean totalPrice(String price){
//        Float totalprice=Float.parseFloat(price);
//
//        if(slider.getValue()>0){
//            if(totalprice<slider.getValue()){
//                return true;
//            }
//        }else {
//            return true;
//        }
//        return false;
//    }

    public Date stringToDate(String date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try{
            Calendar calendar=Calendar.getInstance();
            Date d=formatter.parse(date);
            calendar.setTime(d);

//            Log.d(TAG, "stringToDate: "+d);
            return calendar.getTime();
        }catch (Exception e){
            Log.d(TAG, "stringToDate: "+e.getMessage());
        }
        return null;
    }

//    public int intComparator(int num1,int num2){
//
//    }
}