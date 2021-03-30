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
    int fromStopCount=-1,toStopCount=-1,filterCount=0;
    String fromDepRange="",toDepRange="",departureDate="",returnDate="";
    TextView clear,count,maxprice,minprice;
    LinearLayout fromzerostop,tozerostop,fromonestop,toonestop,fromtwostop,totwostop,frombtnDep1,tobtnDep1,frombtnDep2,tobtnDep2,frombtnDep3,tobtnDep3,frombtnDep4,tobtnDep4;
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

        fromzerostop=findViewById(R.id.fromzerostop);
        tozerostop=findViewById(R.id.tozerostop);
        fromonestop=findViewById(R.id.fromonestop);
        toonestop=findViewById(R.id.toonestop);
        fromtwostop=findViewById(R.id.fromtwostop);
        totwostop=findViewById(R.id.totwostop);
        frombtnDep1=findViewById(R.id.frombtnDep1);
        tobtnDep1=findViewById(R.id.tobtnDep1);
        frombtnDep2=findViewById(R.id.frombtnDep2);
        tobtnDep2=findViewById(R.id.tobtnDep2);
        frombtnDep3=findViewById(R.id.frombtnDep3);
        tobtnDep3=findViewById(R.id.tobtnDep3);
        frombtnDep4=findViewById(R.id.frombtnDep4);
        tobtnDep4=findViewById(R.id.tobtnDep4);

        try{
            oneway = (ArrayList<ArrayList<OneItinerary>>) getIntent().getSerializableExtra("oneway");
            returnway= (ArrayList<ArrayList<ReturnItinerary>>) getIntent().getSerializableExtra("returnway");
            newoneway = (ArrayList<ArrayList<OneItinerary>>) getIntent().getSerializableExtra("oneway");
            newreturnway= (ArrayList<ArrayList<ReturnItinerary>>) getIntent().getSerializableExtra("returnway");
            departureDate=b.getString("departureDate");
            returnDate=b.getString("returnDate");
            fromStopCount=b.getInt("stopcount");
            fromDepRange=b.getString("depRange");
            flightscount=b.getInt("flightsCount");

            Log.d(TAG, "onCreate: "+fromStopCount+" "+fromDepRange);

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
            if(fromStopCount!=-1){
                fromStopCountBtnColor(fromStopCount);
            }
            if(fromDepRange!=""){
                fromDepRangeBtnColor(fromDepRange);
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
                bundle1.putInt("stopcount",fromStopCount);
                bundle1.putString("depRange",fromDepRange);
                bundle1.putInt("stopcount",toStopCount);
                bundle1.putString("depRange",fromDepRange);

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
                    filterByStopCount("from");
                    filterDepTime("from");
                }

                updateDataOnSliderChanger(value);
                sliderPrice=value;
            }
        });


        clear=findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fromzerostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                fromonestop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                fromtwostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));

                tozerostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                toonestop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                totwostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));

                frombtnDep1.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                frombtnDep2.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                frombtnDep3.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                frombtnDep4.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));

                tobtnDep1.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                tobtnDep2.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                tobtnDep3.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
                tobtnDep4.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));

                fromStopCount=-1;
                fromDepRange="";
                toStopCount=-1;
                toDepRange="";
                newoneway=oneway;
                newreturnway=returnway;
            }
        });


        onclick();
    }

    public void onclick(){
        String from="from",to="to";
//stop count from time filter
        fromzerostop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromStopCount=1;
                fromStopCountBtnColor(fromStopCount);
                filterCount=1;

                if(fromDepRange!="")
                    ifBothClicked(from);
                else
                    ifStopCountClicked(from);

            }
        });
        fromonestop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fromStopCount=2;
                fromStopCountBtnColor(fromStopCount);
                filterCount=1;

                if(fromDepRange!="")
                    ifBothClicked(from);
                else
                    ifStopCountClicked(from);

            }
        });
        fromtwostop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fromStopCount=3;
                fromStopCountBtnColor(fromStopCount);
                filterCount=1;

                if(fromDepRange!="")
                    ifBothClicked(from);
                else
                    ifStopCountClicked(from);

            }
        });

        //stop count to filter
        tozerostop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toStopCount=1;
                toStopCountBtnColor(toStopCount);
                filterCount=1;

                if(toDepRange!="")
                    ifBothClicked(to);
                else
                    ifStopCountClicked(to);

            }
        });
        toonestop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toStopCount=2;
                toStopCountBtnColor(toStopCount);
                filterCount=1;

                if(toDepRange!="")
                    ifBothClicked(to);
                else
                    ifStopCountClicked(to);

            }
        });
        totwostop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toStopCount=3;
                toStopCountBtnColor(toStopCount);
                filterCount=1;

                if(toDepRange!="")
                    ifBothClicked(to);
                else
                    ifStopCountClicked(to);

            }
        });

//deptarture time filter
        frombtnDep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCount=2;

                fromDepRange="Before6am";
                fromDepRangeBtnColor(fromDepRange);
                if(fromStopCount!=0){
                    ifBothClicked(from);
                }else
                    ifDepRangeClicked(from);

            }
        });
        frombtnDep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCount = 2;

                fromDepRange = "6amTo12pm";
                fromDepRangeBtnColor(fromDepRange);
                if(fromStopCount!=0){
                    ifBothClicked(from);
                }else
                    ifDepRangeClicked(from);

            }
        });
        frombtnDep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCount=2;

                fromDepRange="12pmTo6pm";
                fromDepRangeBtnColor(fromDepRange);
                if(fromStopCount!=0){
                    ifBothClicked(from);
                }else
                    ifDepRangeClicked(from);

            }
        });
        frombtnDep4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCount=2;
                fromDepRange="After6pm";
                fromDepRangeBtnColor(fromDepRange);
                if(fromStopCount!=0){
                    ifBothClicked(from);
                }else
                    ifDepRangeClicked(from);


            }
        });

        //departure to filter
        tobtnDep1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCount=2;

                toDepRange="Before6am";
                toDepRangeBtnColor(toDepRange);
                if(toStopCount!=0){
                    ifBothClicked(to);
                }else
                    ifDepRangeClicked(to);

            }
        });
        tobtnDep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCount = 2;

                toDepRange = "6amTo12pm";
                toDepRangeBtnColor(toDepRange);
                if(toStopCount!=0){
                    ifBothClicked(to);
                }else
                    ifDepRangeClicked(to);

            }
        });
        tobtnDep3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCount=2;

                toDepRange="12pmTo6pm";
                toDepRangeBtnColor(toDepRange);
                if(toStopCount!=0){
                    ifBothClicked(to);
                }else
                    ifDepRangeClicked(to);

            }
        });
        tobtnDep4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCount=2;
                toDepRange="After6pm";
                toDepRangeBtnColor(toDepRange);
                if(toStopCount!=0){
                    ifBothClicked(to);
                }else
                    ifDepRangeClicked(to);


            }
        });
    }

    public void fromStopCountBtnColor(int stopCount){
        if(stopCount==1){
            fromzerostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
            fromonestop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            fromtwostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
        }
        else if (stopCount==2){
            fromzerostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            fromonestop.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
            fromtwostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
        }else if (stopCount==3){
            fromzerostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            fromonestop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            fromtwostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
        }
    }

    public void toStopCountBtnColor(int stopCount){
        if(stopCount==1){
            tozerostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
            toonestop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            totwostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
        }
        else if (stopCount==2){
            tozerostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            toonestop.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
            totwostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
        }else if (stopCount==3){
            tozerostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            toonestop.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            totwostop.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
        }
    }

    public void fromDepRangeBtnColor(String depRange){
        Log.d(TAG, "fun "+depRange);
        if(depRange.equals("Before6am")){
            Log.d(TAG, "if");
            frombtnDep1.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
            frombtnDep2.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            frombtnDep3.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            frombtnDep4.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
        }else if(depRange.equals("6amTo12pm")){
            frombtnDep1.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            frombtnDep2.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
            frombtnDep3.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            frombtnDep4.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
        }else if(depRange.equals("12pmTo6pm")){
            frombtnDep1.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            frombtnDep2.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            frombtnDep3.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
            frombtnDep4.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
        }else if(depRange.equals("After6pm")){
            frombtnDep1.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            frombtnDep2.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            frombtnDep3.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            frombtnDep4.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
        }
    }

    public void toDepRangeBtnColor(String depRange){
        Log.d(TAG, "fun "+depRange);
        if(depRange.equals("Before6am")){
            Log.d(TAG, "if");
            tobtnDep1.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
            tobtnDep2.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            tobtnDep3.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            tobtnDep4.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
        }else if(depRange.equals("6amTo12pm")){
            tobtnDep1.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            tobtnDep2.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
            tobtnDep3.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            tobtnDep4.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
        }else if(depRange.equals("12pmTo6pm")){
            tobtnDep1.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            tobtnDep2.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            tobtnDep3.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
            tobtnDep4.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
        }else if(depRange.equals("After6pm")){
            tobtnDep1.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            tobtnDep2.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            tobtnDep3.setBackground(getResources().getDrawable(R.drawable.roundcorner_grey));
            tobtnDep4.setBackground(getResources().getDrawable(R.drawable.roundcorner_red));
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

    public void ifStopCountClicked(String val){
        updateDataOnSliderChanger(sliderPrice);
        filterByStopCount(val);
    }

    public void ifDepRangeClicked(String val){
        updateDataOnSliderChanger(sliderPrice);
        filterDepTime(val);
    }

    public void ifBothClicked(String val){
        updateDataOnSliderChanger(sliderPrice);
        filterByStopCount(val);
        filterDepTime(val);
    }


    public void filterByStopCount(String val){
        Log.d(TAG, "fsc: "+newoneway.size()+" "+newreturnway.size());

        ArrayList index=new ArrayList();
        if(val=="from"){
            for(int j=0;j<newoneway.size();j++){
                if(fromStopCount==1 && !(newoneway.get(j).size()==1)){
                    Log.d(TAG, "filterByStopCount: "+newoneway.get(j).size());
                    index.add(j);
                }
                else if(fromStopCount==2 && !(newoneway.get(j).size()==2)){
                    Log.d(TAG, "filterByStopCount: "+newoneway.get(j).size());
                    index.add(j);
                }
                else if(fromStopCount>2 && !(newoneway.get(j).size()>2)){
                    Log.d(TAG, "filterByStopCount: "+newoneway.get(j).size());
                    index.add(j);
                }
            }
        }
        else if(val=="to"){
            for(int i=0;i<newoneway.size();i++){

                if(toStopCount==1 && !(newoneway.get(i).size()==1)){
                    index.add(i);
                }
                else if(toStopCount==2 && !(newoneway.get(i).size()==2)){
                    index.add(i);
                }
                else if(toStopCount>2 && !(newoneway.get(i).size()>2)){
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


    public void filterDepTime(String val){

        Log.d(TAG, "fdt: "+newoneway.size()+" "+newreturnway.size()+" * +"+fromDepRange+" * "+toDepRange);

        if(filterCount==2 && fromDepRange!="" && toDepRange!="" && fromStopCount==0 && toStopCount==0) {
            updateDataOnSliderChanger(sliderPrice);
            fromDepRange="";
            toDepRange="";
            Log.d(TAG, "fdt");
        }

        ArrayList index=new ArrayList();

        if(val=="from"){
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
        else if(val=="to"){
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