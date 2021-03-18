package com.example.flights;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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

        int flightscount=0;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Bundle b=getIntent().getExtras();

        slider=findViewById(R.id.slider);
        maxprice=findViewById(R.id.maxPrice);
        minprice=findViewById(R.id.minPrice);
        count=findViewById(R.id.count);

        from=findViewById(R.id.from);
        to=findViewById(R.id.to);
        zerostop=findViewById(R.id.zerostop);
        onestop=findViewById(R.id.onestop);
        twostop=findViewById(R.id.twostop);
        btnDep1=findViewById(R.id.btnDep1);
        btnDep2=findViewById(R.id.btnDep2);
        btnDep3=findViewById(R.id.btnDep3);
        btnDep4=findViewById(R.id.btnDep4);

        try{
            oneway = (ArrayList<ArrayList<OneItinerary>>) getIntent().getSerializableExtra("oneway");
            returnway= (ArrayList<ArrayList<ReturnItinerary>>) getIntent().getSerializableExtra("returnway");
            newoneway = (ArrayList<ArrayList<OneItinerary>>) getIntent().getSerializableExtra("oneway");
            newreturnway= (ArrayList<ArrayList<ReturnItinerary>>) getIntent().getSerializableExtra("returnway");
            departureDate=b.getString("departureDate");
            returnDate=b.getString("returnDate");
            stopCount=b.getInt("stopcount");
            depRange=b.getString("depRange");
            flightscount=b.getInt("flightsCount");

            Log.d(TAG, "onCreate: "+stopCount+" "+depRange);

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

        if(b!=null){
            if(stopCount!=-1){
                changeStopCountBtnColor(stopCount);
            }
            if(depRange!=""){
                changeDepRangeBtnColor(depRange);
            }
            count.setText(flightscount+" of "+oneway.size());
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
                Log.d(TAG, "onClick: "+newoneway+" "+newreturnway);
                bundle1.putSerializable("newoneway",newoneway);
                bundle1.putSerializable("newreturnway",newreturnway);
                bundle1.putSerializable("oneway",oneway);
                bundle1.putSerializable("returnway",returnway);
                bundle1.putInt("stopcount",stopCount);
                bundle1.putString("depRange",depRange);
                bundle1.putString("fromto",fromto);

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


                if(sliderPrice!=0.0f && sliderPrice!=value){
                    filterByStopCount();
                    filterDepTime();
                }

                updateDataOnSliderChanger(value);
                sliderPrice=value;
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
                newoneway=oneway;
                newreturnway=returnway;
            }
        });


        onclick();
    }

    public void onclick(){





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
                stopCount=1;
                changeStopCountBtnColor(stopCount);
                filterCount=1;

                if(depRange!="")
                    ifBothClicked();
                else
                    ifStopCountClicked();

            }
        });
        onestop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopCount=2;
                changeStopCountBtnColor(stopCount);
                filterCount=1;

                if(depRange!="")
                    ifBothClicked();
                else
                    ifStopCountClicked();

            }
        });
        twostop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopCount=3;
                changeStopCountBtnColor(stopCount);
                filterCount=1;

                if(depRange!="")
                    ifBothClicked();
                else
                    ifStopCountClicked();

            }
        });


        btnDep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCount=2;

                depRange="Before6am";
                changeDepRangeBtnColor(depRange);
                if(stopCount!=0){
                    ifBothClicked();
                }else
                    ifDepRangeClicked();

            }
        });
        btnDep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCount = 2;

                depRange = "6amTo12pm";
                changeDepRangeBtnColor(depRange);
                if(stopCount!=0){
                    ifBothClicked();
                }else
                    ifDepRangeClicked();

            }
        });
        btnDep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCount=2;

                depRange="12pmTo6pm";
                changeDepRangeBtnColor(depRange);
                if(stopCount!=0){
                    ifBothClicked();
                }else
                    ifDepRangeClicked();

            }
        });
        btnDep4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCount=2;
                depRange="After6pm";
                changeDepRangeBtnColor(depRange);
                if(stopCount!=0){
                    ifBothClicked();
                }else
                    ifDepRangeClicked();


            }
        });
    }

    public void changeStopCountBtnColor(int stopCount){
        if(stopCount==1){
            zerostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
            onestop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            twostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
        }
        else if (stopCount==2){
            zerostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            onestop.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
            twostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
        }else if (stopCount==3){
            zerostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            onestop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            twostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
        }
    }

    public void changeDepRangeBtnColor(String depRange){
        Log.d(TAG, "fun "+depRange);
        if(depRange.equals("Before6am")){
            Log.d(TAG, "if");
            btnDep1.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
            btnDep2.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            btnDep3.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            btnDep4.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
        }else if(depRange.equals("6amTo12pm")){
            btnDep1.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            btnDep2.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
            btnDep3.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            btnDep4.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
        }else if(depRange.equals("12pmTo6pm")){
            btnDep1.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            btnDep2.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            btnDep3.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
            btnDep4.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
        }else if(depRange.equals("After6pm")){
            btnDep1.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            btnDep2.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            btnDep3.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            btnDep4.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
        }
    }


    public void updateDataOnSliderChanger(Float val){
        newoneway=new ArrayList<>();
        newreturnway=new ArrayList<>();
        for(int i=0;i<oneway.size();i++){
            if(Float.valueOf(oneway.get(i).get(0).getTotalPrice())<=val){
                newoneway.add(oneway.get(i));
                newreturnway.add(returnway.get(i));
                count.setText(newoneway.size()+" of "+oneway.size());
            }
        }

    }

    public void ifStopCountClicked(){
        updateDataOnSliderChanger(sliderPrice);
        filterByStopCount();
    }

    public void ifDepRangeClicked(){
        updateDataOnSliderChanger(sliderPrice);
        filterDepTime();
    }

    public void ifBothClicked(){
        updateDataOnSliderChanger(sliderPrice);
        filterByStopCount();
        filterDepTime();
    }


    public void filterByStopCount(){
        Log.d(TAG, "fsc: "+newoneway.size()+" "+newreturnway.size());

        ArrayList index=new ArrayList();
        if(fromto=="from"){
            for(int j=0;j<newoneway.size();j++){
                if(stopCount==1 && !(newoneway.get(j).size()==1)){
                    Log.d(TAG, "filterByStopCount: "+newoneway.get(j).size());
                    index.add(j);
                }
                else if(stopCount==2 && !(newoneway.get(j).size()==2)){
                    Log.d(TAG, "filterByStopCount: "+newoneway.get(j).size());
                    index.add(j);
                }
                else if(stopCount>2 && !(newoneway.get(j).size()>2)){
                    Log.d(TAG, "filterByStopCount: "+newoneway.get(j).size());
                    index.add(j);
                }
            }
        }
        else if(fromto=="to"){
            for(int i=0;i<newoneway.size();i++){

                if(stopCount==1 && newoneway.get(i).size()==1){
                    index.add(i);
                }
                else if(stopCount==2 && newoneway.get(i).size()==2){
                    index.add(i);
                }
                else if(stopCount>2 && newoneway.get(i).size()>2){
                    index.add(i);
                }
            }
        }

        removeByStopCountFilter(index);
        Log.d(TAG, "count "+newoneway.size()+ " "+newreturnway.size()+" "+index);
    }

    public void removeByStopCountFilter(ArrayList<Integer> indexesToRemove){
        Log.d(TAG, "removeByStopCountFilter: "+indexesToRemove.size()+" "+indexesToRemove);

        Collections.reverse(indexesToRemove);
        for (Integer indexToRemove : indexesToRemove) {
            newoneway.remove((int)indexToRemove);
            newreturnway.remove((int)indexToRemove);
        }
        count.setText(newoneway.size()+" "+oneway.size());
    }


    public void filterDepTime(){

        Log.d(TAG, "fdt: "+newoneway.size()+" "+newreturnway.size()+" * +"+depRange);

        if(filterCount==2 && depRange!="" && stopCount==0) {
            updateDataOnSliderChanger(sliderPrice);
            depRange="";
            Log.d(TAG, "fdt");
        }

        ArrayList index=new ArrayList();

        if(fromto=="from"){
            int c=0;
            if(depRange=="Before6am"){
                Date endDate = new Date(departureDate+" 06:00:00");
                for(ArrayList<OneItinerary> one : newoneway){
                    if(!filterDate(stringToDate(one.get(0).getDepartureDateTime()),null,endDate)){
                        index.add(c);
                    }
                    c++;
                }
            }
            else if(depRange=="6amTo12pm"){
                Date startDate = new Date(departureDate+" 06:00:00");
                Date endDate = new Date(departureDate+" 12:00:00");
                for(ArrayList<OneItinerary> one : newoneway){
                    if(!filterDate(stringToDate(one.get(0).getDepartureDateTime()),startDate,endDate)){
                        index.add(c);
                    }
                    c++;
                }
            }
            else if(depRange=="12pmTo6pm"){
                Date startDate = new Date(departureDate+" 12:00:00");
                Date endDate = new Date(departureDate+" 18:00:00");
                for(ArrayList<OneItinerary> one : newoneway){
                    if(!filterDate(stringToDate(one.get(0).getDepartureDateTime()),startDate,endDate)){
                        index.add(c);
                    }
                    c++;
                }
            }
            else if(depRange=="After6pm"){
                Date startDate = new Date(departureDate+" 18:00:00");
                for(ArrayList<OneItinerary> one : newoneway){
                    if(!filterDate(stringToDate(one.get(0).getDepartureDateTime()),startDate,null)){
                        index.add(c);
                    }
                    c++;
                }
            }

        }
        else if(fromto=="to"){
            int c=0;
            if(depRange=="Before6am"){
                Date endDate = new Date(returnDate+" 06:00:00");
                for(ArrayList<ReturnItinerary> one : newreturnway){
                    if(!filterDate(stringToDate(one.get(0).getDepartureDateTime()),null,endDate)){
                        index.add(c);
                    }
                    c++;
                }
            }
            else if(depRange=="6amTo12pm"){
                Date startDate = new Date(returnDate+" 06:00:00");
                Date endDate = new Date(returnDate+" 12:00:00");

                for(ArrayList<ReturnItinerary> one : newreturnway){
                    if(!filterDate(stringToDate(one.get(0).getDepartureDateTime()),startDate,endDate)){
                        index.add(c);
                    }
                    c++;
                }

            }
            else if(depRange=="12pmTo6pm"){
                Date startDate = new Date(returnDate+" 12:00:00");
                Date endDate = new Date(returnDate+" 18:00:00");

                for(ArrayList<ReturnItinerary> one : newreturnway){
                    if(!filterDate(stringToDate(one.get(0).getDepartureDateTime()),startDate,endDate)){
                        index.add(c);
                    }
                    c++;
                }
            }
            else if(depRange=="After6pm"){
                Date startDate = new Date(returnDate+" 18:00:00");

                for(ArrayList<ReturnItinerary> one : newreturnway){
                    if(!filterDate(stringToDate(one.get(0).getDepartureDateTime()),startDate,null)){
                        index.add(c);
                    }
                    c++;
                }
            }
        }
        removeByDepTimeFilter(index);

        Log.d(TAG, "count "+newoneway.size()+ " "+newreturnway.size());

    }

    public void removeByDepTimeFilter(ArrayList<Integer> indexesToRemove){
        Collections.reverse(indexesToRemove);
        for (Integer indexToRemove : indexesToRemove) {
            newoneway.remove((int)indexToRemove);
            newreturnway.remove((int)indexToRemove);
        }
        count.setText(newoneway.size()+" "+oneway.size());
    }

    public boolean filterDate(Date oneDate, Date startDate, Date endDate){
        if(startDate==null){
            if(oneDate.before(endDate)){
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


    public Date stringToDate(String date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try{
            Calendar calendar=Calendar.getInstance();
            Date d=formatter.parse(date);
            calendar.setTime(d);

            return calendar.getTime();
        }catch (Exception e){
            Log.d(TAG, "stringToDate: "+e.getMessage());
        }
        return null;
    }
}