package com.blizzy312.clocks.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blizzy312.clocks.MainActivity;
import com.blizzy312.clocks.R;

import java.util.Date;



public class FragmentClock extends Fragment {

    View view;
    SharedPreferences settings;
    TextView textViewCurrentTime, textViewCurrentDay, textViewCurrentDate;

    private String currentDay, currentDate, currentTime;
    private String[] days;
    private int hours, minutes, seconds;

    public FragmentClock() {
        // Required empty public constructor
    }


    public static FragmentClock newInstance(String param1, String param2) {
        FragmentClock fragment = new FragmentClock();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = ((MainActivity)getActivity()).getSharedPreferences("MySharedPref", 0);


        days = ((MainActivity)getActivity()).getResources().getStringArray(R.array.days_of_week);
        Thread myThread;
        Runnable runnable = new CountDownRunner();
        myThread= new Thread(runnable);
        myThread.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view  = inflater.inflate(R.layout.fragment_clock, container, false);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                ((MainActivity) getActivity()).showNav();
                ((MainActivity) getActivity()).waitTime();
                return true;
            }
        });

        textViewCurrentTime = view.findViewById(R.id.ClockFrame);

        textViewCurrentDay = view.findViewById(R.id.DaysFrame);
        textViewCurrentDay.setText(getDay());
        if (settings.getBoolean("showDaysCheckBox",false)){
            textViewCurrentDay.setVisibility(View.VISIBLE);
        }else {
            textViewCurrentDay.setVisibility(View.INVISIBLE);
        }


        textViewCurrentDate = view.findViewById(R.id.DateFrame);
        textViewCurrentDate.setText(getDate());
        if (settings.getBoolean("showDateCheckBox",false)){
            textViewCurrentDate.setVisibility(View.VISIBLE);
        }else {
            textViewCurrentDate.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    public void getCurrentTime() {
        ((MainActivity) getActivity()).runOnUiThread(new Runnable() {
            public void run() {
                try{

                    Date dt = new Date();
                    hours = dt.getHours();
                    minutes = dt.getMinutes();
                    seconds = dt.getSeconds();

                    currentTime = getClockText(hours, minutes, seconds);

                    textViewCurrentTime.setText(currentTime);

                    if (currentTime.equals("00:00:00")){
                        textViewCurrentDate.setText(getDate());
                        textViewCurrentDay.setText(getDay());
                    }

                }catch (Exception e) {}
            }
        });
    }


    class CountDownRunner implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    getCurrentTime();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }

    private String getDate(){
        /* -- Date returns 'month' as integer from 0 to 11, so if it's January, Date will return '0'.
           -- Date returns 'year' from 1900, so if it's 2018, Date will return '118'. */

        Date dt = new Date();
        int date = dt.getDate();
        int month = dt.getMonth()+1;          // add 1 to month
        int year = dt.getYear() + 1900;       // add 1900 to year

        currentDate = month + "." + date + "." + year;
        return currentDate;
    }

    private String getDay(){
        Date dt = new Date();
        int ind = dt.getDay();
        currentDay = days[ind];
        return currentDay;
    }

    private String getClockText(int hours, int minutes, int seconds){
        return formatNumber(hours) + ":" + formatNumber(minutes) + ":" + formatNumber(seconds);
    }

    private String formatNumber(int num){
        if (num < 10){
            return "0" + num;
        }
        return "" + num;
    }


    @Override
    public void onResume() {
        super.onResume();
        textViewCurrentTime.setText(currentTime);
        textViewCurrentDate.setText(currentDate);
        textViewCurrentDay.setText(currentDay);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}