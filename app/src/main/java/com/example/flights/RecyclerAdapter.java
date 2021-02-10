package com.example.flights;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static final String TAG="project";
    ArrayList<ArrayList<OneItinerary>> oneway;
    ArrayList<ArrayList<ReturnItinerary>> returnway;

    public RecyclerAdapter(ArrayList<ArrayList<OneItinerary>> oneway, ArrayList<ArrayList<ReturnItinerary>> returnway) {
        this.oneway = oneway;
        this.returnway = returnway;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recycler_adapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        dataBind(holder,position);

    }

    public void dataBind(ViewHolder holder,int position){

        ArrayList<OneItinerary> one=oneway.get(position);
        Log.d(TAG, "one: "+one);
//        Log.d(TAG, "one : "+one);

        int max_count=one.size();

        holder.OflightName.setText(one.get(0).getAirlineName());

        holder.totolPrice.setText(one.get(0).getTotalPrice());



        holder.OnumOfFlights.setText(String.valueOf(max_count));

        if (oneway.get(position).size() > 1) {
            holder.OtotalTime.setText(getTotalTime(one.get(0).getDepartureDateTime(), one.get(max_count - 1).getArrivalDateTime()));
            holder.OtotalStop.setText((max_count - 1) + " stop");
            holder.OfromTime.setText(one.get(0).getDepartureDateTime().substring(11, 13) + ":" + one.get(max_count - 1).getDepartureDateTime().substring(14, 16) + "");
            holder.OtoTime.setText(one.get(0).getArrivalDateTime().substring(11, 13) + ":" + one.get(max_count - 1).getArrivalDateTime().substring(14, 16) + "");
        } else {
            holder.OtotalTime.setText(getTotalTime(one.get(0).getDepartureDateTime(), one.get(0).getArrivalDateTime()));
            holder.OtotalStop.setText((max_count - 1) + " stop");
            holder.OfromTime.setText(one.get(0).getDepartureDateTime().substring(11, 13) + ":" + one.get(0).getDepartureDateTime().substring(14, 16) + "");
            holder.OtoTime.setText(one.get(0).getArrivalDateTime().substring(11, 13) + ":" + one.get(0).getArrivalDateTime().substring(14, 16) + "");
        }


        ArrayList<ReturnItinerary> ret= returnway.get(position);
        int max_count1=one.size();
        Log.d(TAG, "ret : "+ret);

        holder.RflightName.setText(ret.get(0).getAirlineName());

//        Log.d(TAG, "returnwayDataBind: "+max_count1);

//        holder.RfromTime.setText(ret.get(0).getDepartureDateTime().substring(11,13)+":"+ret.get(max_count1-1).getDepartureDateTime().substring(14,16)+"");
//        holder.RtoTime.setText(ret.get(0).getArrivalDateTime().substring(11,13)+":"+ret.get(max_count1-1).getArrivalDateTime().substring(14,16)+"");
//        holder.RtotalStop.setText((max_count1-1)+" stop");
        holder.RnumOfFlights.setText(String.valueOf(max_count1));

        if(returnway.get(position).size()>1) {
            holder.RtotalTime.setText(getTotalTime(ret.get(0).getDepartureDateTime(), ret.get(max_count1 - 1).getArrivalDateTime()));
            holder.RfromTime.setText(ret.get(0).getDepartureDateTime().substring(11,13)+":"+ret.get(max_count1-1).getDepartureDateTime().substring(14,16)+"");
            holder.RtoTime.setText(ret.get(0).getArrivalDateTime().substring(11,13)+":"+ret.get(max_count1-1).getArrivalDateTime().substring(14,16)+"");
            holder.RtotalStop.setText((max_count1-1)+" stop");
        }else {
            holder.RfromTime.setText(ret.get(0).getDepartureDateTime().substring(11,13)+":"+ret.get(0).getDepartureDateTime().substring(14,16)+"");
            holder.RtoTime.setText(ret.get(0).getArrivalDateTime().substring(11,13)+":"+ret.get(0).getArrivalDateTime().substring(14,16)+"");
            holder.RtotalStop.setText((max_count1-1)+" stop");
            holder.RtotalTime.setText(getTotalTime(ret.get(0).getDepartureDateTime(), ret.get(0).getArrivalDateTime()));
        }

//        Log.d(TAG, "one : "+one);
//        Log.d(TAG, "return : "+ret);
    }


    public String getTotalTime(String departure, String arrival){

        LocalDateTime dateTime1 = LocalDateTime.parse(departure);
        LocalDateTime dateTime2 = LocalDateTime.parse(arrival);

        Duration duration = Duration.between(dateTime1, dateTime2);
//        Log.d(TAG, "getTotalTime: "+duration.toHours()+" "+duration.toMillis());

        long diffMinutes = (duration.toMillis()/ (1000 * 60))% 60;

        return (duration.toHours())+"h "+(diffMinutes)+"m";

    }



    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+oneway.size());
        return oneway.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView OflightName,OnumOfFlights,OfromTime,OtotalTime,OtotalStop,OtoTime;
        TextView RflightName,RnumOfFlights,RfromTime,RtotalTime,RtotalStop,RtoTime;

        TextView totolPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            OflightName=itemView.findViewById(R.id.OflightName);
            OfromTime=itemView.findViewById(R.id.OfromTime);
            OtotalTime=itemView.findViewById(R.id.OtotalTime);
            OtotalStop=itemView.findViewById(R.id.OtotalStop);
            OtoTime=itemView.findViewById(R.id.OtoTime);
            OnumOfFlights=itemView.findViewById(R.id.OnumOfFlights);

            RflightName=itemView.findViewById(R.id.RflightName);
            RnumOfFlights=itemView.findViewById(R.id.RnumOfFlights);
            RfromTime=itemView.findViewById(R.id.RfromTime);
            RtotalTime=itemView.findViewById(R.id.RtotalTime);
            RtotalStop=itemView.findViewById(R.id.RtotalStop);
            RtoTime=itemView.findViewById(R.id.RtoTime);

            totolPrice=itemView.findViewById(R.id.totolPrice);
        }
    }
}