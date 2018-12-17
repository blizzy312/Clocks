package com.blizzy312.clocks.Fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.blizzy312.clocks.MainActivity;
import com.blizzy312.clocks.R;

import java.util.concurrent.TimeUnit;

public class FragmentTimer extends Fragment {

    View view;
    TextView textViewTimerFrame;
    LinearLayout numberPickerFrame;
    CountDownTimer countDownTimer;
    NumberPicker numberPickerSeconds, numberPickerMinutes, numberPickerHours;
    Button startStopButton, resumeButton, resetButton;


    int secondsFromPickerNumber, minutesFromPickerNumber, hoursFromPickerNumber, UIState = 0;
    long seconds, minutes, hours, totalMs;
    boolean isRunning = false;


    public FragmentTimer() {
        // Required empty public constructor
    }

    public static FragmentTimer newInstance(String param1, String param2) {
        FragmentTimer fragment = new FragmentTimer();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_timer, container, false);

        textViewTimerFrame = view.findViewById(R.id.timerFrame);
        numberPickerFrame = view.findViewById(R.id.picker);

        ((MainActivity) getActivity()).showNav();

        startStopButton = view.findViewById(R.id.startStopButton);
        startStopButton.setOnClickListener(startStopButtonListener);

        resumeButton = view.findViewById(R.id.resumeButton);
        resumeButton.setOnClickListener(resumeButtonListener);
        
        resetButton = view.findViewById(R.id.resetButton);
        resetButton.setOnClickListener(resetButtonListener);

        numberPickerSeconds = view.findViewById(R.id.seconds_picker);
        numberPickerSeconds.setMinValue(0);
        numberPickerSeconds.setMaxValue(60);
        numberPickerSeconds.setOnValueChangedListener(onSecondsChangeListener);

        numberPickerMinutes = view.findViewById(R.id.minutes_picker);
        numberPickerMinutes.setMinValue(0);
        numberPickerMinutes.setMaxValue(60);
        numberPickerMinutes.setOnValueChangedListener(onMinutesChangeListener);

        numberPickerHours = view.findViewById(R.id.hours_picker);
        numberPickerHours.setMinValue(0);
        numberPickerHours.setMaxValue(99);
        numberPickerHours.setOnValueChangedListener(onHoursChangeListener);

        return view;
    }

    Button.OnClickListener startStopButtonListener =
            new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (!isRunning){
                        updateUI(1);
                        totalMs = ( hoursFromPickerNumber*3600000 + minutesFromPickerNumber*60000 + (secondsFromPickerNumber+1)*1000);
                        startTimer(totalMs);
                    } else {
                        updateUI(2);
                        countDownTimer.cancel();
                    }
                }
            };

    Button.OnClickListener resumeButtonListener =
            new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    updateUI(1);
                    setTimerText( hours, minutes, seconds);
                    totalMs = ( hours*3600000 + minutes*60000 + seconds*1000);
                    startTimer(totalMs);
                }
            };

    Button.OnClickListener resetButtonListener =
            new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    countDownTimer.cancel();
                    updateUI(0);
                }
            };

    NumberPicker.OnValueChangeListener onSecondsChangeListener =
            new 	NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    secondsFromPickerNumber = numberPicker.getValue();
                }
            };
    NumberPicker.OnValueChangeListener onMinutesChangeListener =
            new 	NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    minutesFromPickerNumber = numberPicker.getValue();
                }
            };
    NumberPicker.OnValueChangeListener onHoursChangeListener =
            new 	NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                    hoursFromPickerNumber = numberPicker.getValue();
                }
            };



    private void startTimer(long time){
        countDownTimer = new CountDownTimer(time, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);

                minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished));

                seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));

                setTimerText(hours, minutes, seconds);


            }

            public void onFinish() {

                updateUI(0);
            }
        }.start();
    }

    private void setTimerText(long hours, long minutes, long seconds){
        textViewTimerFrame.setText(formatNumber(hours) + ":" + formatNumber(minutes) + ":" + formatNumber(seconds));
    }

    private String formatNumber(long num){
        if (num < 10){
            return "0" + num;
        }
        return "" + num;
    }

    private void updateUI(int state){
        switch (state){
            case 0:
                // Initial state, where Timer is not running yet
                isRunning = false;
                startStopButton.setText("Start");
                numberPickerFrame.setVisibility(View.VISIBLE);
                textViewTimerFrame.setVisibility(View.INVISIBLE);
                startStopButton.setVisibility(View.VISIBLE);
                resumeButton.setVisibility(View.INVISIBLE);
                resetButton.setVisibility(View.INVISIBLE);

                UIState = 0;
                break;
            case 1:
                // Second state, where timer is running
                isRunning = true;
                startStopButton.setText("Stop");
                numberPickerFrame.setVisibility(View.INVISIBLE);
                textViewTimerFrame.setVisibility(View.VISIBLE);
                startStopButton.setVisibility(View.VISIBLE);
                resumeButton.setVisibility(View.INVISIBLE);
                resetButton.setVisibility(View.INVISIBLE);


                UIState = 1;
                break;
            case 2:
                // Third state,where timer is stopped
                numberPickerFrame.setVisibility(View.INVISIBLE);
                textViewTimerFrame.setVisibility(View.VISIBLE);
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
        setTimerText(hours, minutes, seconds);
    }

    @Override
    public void onPause() {

        super.onPause();
    }
}
