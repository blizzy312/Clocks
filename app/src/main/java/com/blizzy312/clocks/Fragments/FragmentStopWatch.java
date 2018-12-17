package com.blizzy312.clocks.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blizzy312.clocks.MainActivity;
import com.blizzy312.clocks.R;

public class FragmentStopWatch extends Fragment {

    View view;
    Runnable runnable;
    Thread myThread = null;
    TextView textViewStopWatch;
    Button startStopButton, resumeButton, resetButton;

    private int UIState = 0, hours = 0, minutes = 0, seconds = 0;
    private boolean isRunning = false;
    

    public FragmentStopWatch() {
        // Required empty public constructor
    }

    public static FragmentStopWatch newInstance(String param1, String param2) {
        FragmentStopWatch fragment = new FragmentStopWatch();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runnable = new FragmentStopWatch.CountDownRunner();
        myThread= new Thread(runnable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_stop_watch, container, false);
        ((MainActivity) getActivity()).showNav();

        textViewStopWatch = view.findViewById(R.id.stopWatchFrame);

        startStopButton = view.findViewById(R.id.startStopButton);
        startStopButton.setOnClickListener(startStopListener);

        resumeButton = view.findViewById(R.id.resumeButton);
        resumeButton.setOnClickListener(resumeListener);

        resetButton = view.findViewById(R.id.resetButton);
        resetButton.setOnClickListener(resetListener);

        return view;
    }

    Button.OnClickListener startStopListener =
            new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!isRunning){
                    myThread.start();
                    updateUI(1);
                } else {
                    myThread.interrupt();
//                    myThread.s
                    updateUI(2);
                }
            }
    };

    Button.OnClickListener resumeListener =
            new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    runnable = new FragmentStopWatch.CountDownRunner();
                    myThread= new Thread(runnable);
                    myThread.start();
                    updateUI(1);
                }
            };

    Button.OnClickListener resetListener =
            new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    myThread.interrupt();
                    runnable = new FragmentStopWatch.CountDownRunner();
                    myThread= new Thread(runnable);
                    updateUI(0);
                }
            };

    public void startCount() {
        ((MainActivity) getActivity()).runOnUiThread(new Runnable() {
            public void run() {
                try{
                    seconds++;
                    if (seconds == 60){
                        seconds = 0;
                        minutes++;
                    }
                    if(minutes == 60){
                        minutes = 0;
                        hours++;
                    }
                    setStopWatchText(hours, minutes, seconds);
                }catch (Exception e) {}
            }
        });
    }


    class CountDownRunner implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    startCount();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }


    private void setStopWatchText(int hours, int minutes, int seconds){
        textViewStopWatch.setText(formatNumber(hours) + ":" + formatNumber(minutes) + ":" + formatNumber(seconds));
    }

    private String formatNumber(int num){
        if (num < 10){
            return "0" + num;
        }
        return "" + num;
    }

    private void updateUI(int state){
        switch (state){
            case 0:
                // Initial State, where timer is not running yet
                isRunning = false;

                textViewStopWatch.setText("00:00:00");

                seconds = 0;
                minutes = 0;
                hours = 0;

                startStopButton.setText(getString(R.string.start));
                startStopButton.setVisibility(View.VISIBLE);
                resumeButton.setVisibility(View.INVISIBLE);
                resetButton.setVisibility(View.INVISIBLE);

                UIState = 0;
                break;
            case 1:
                // Second state, where timer is running
                isRunning = true;

                startStopButton.setText(getString(R.string.stop));
                startStopButton.setVisibility(View.VISIBLE);
                resumeButton.setVisibility(View.INVISIBLE);
                resetButton.setVisibility(View.INVISIBLE);

                UIState = 1;
                break;
            case 2:
                // Third state, when 'Stop' button was pressed
                startStopButton.setVisibility(View.INVISIBLE);
                resumeButton.setVisibility(View.VISIBLE);
                resetButton.setVisibility(View.VISIBLE);

                UIState = 2;
                break;
            default:
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI(UIState);
        setStopWatchText(hours,minutes, seconds);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
