package com.blizzy312.clocks.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;


import com.blizzy312.clocks.AlarmCreateActivity;
import com.blizzy312.clocks.AlarmDetail;
import com.blizzy312.clocks.AlarmListAdapter;
import com.blizzy312.clocks.AlarmReceiver;
import com.blizzy312.clocks.AlarmViewModel;
import com.blizzy312.clocks.MainActivity;
import com.blizzy312.clocks.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.ALARM_SERVICE;
import static com.blizzy312.clocks.AlarmCreateActivity.DESCRIPTION_REPLY;
import static com.blizzy312.clocks.AlarmCreateActivity.HOURS_REPLY;
import static com.blizzy312.clocks.AlarmCreateActivity.ID_REPLY;
import static com.blizzy312.clocks.AlarmCreateActivity.IS_FRIDAY_REPLY;
import static com.blizzy312.clocks.AlarmCreateActivity.IS_MONDAY_REPLY;
import static com.blizzy312.clocks.AlarmCreateActivity.IS_SATURDAY_REPLY;
import static com.blizzy312.clocks.AlarmCreateActivity.IS_SUNDAY_REPLY;
import static com.blizzy312.clocks.AlarmCreateActivity.IS_THURSDAY_REPLY;
import static com.blizzy312.clocks.AlarmCreateActivity.IS_TUESDAY_REPLY;
import static com.blizzy312.clocks.AlarmCreateActivity.IS_WEDNESDAY_REPLY;
import static com.blizzy312.clocks.AlarmCreateActivity.MINUTES_REPLY;

public class FragmentAlarmClock extends Fragment {

    AlarmManager alarmManager;

    private AlarmViewModel mAlarmViewModel;
    View view;
    AlarmListAdapter mAdapter;
    Switch alarmState;
    public FragmentAlarmClock() {
        // Required empty public constructor
    }

    public static FragmentAlarmClock newInstance(String param1, String param2) {
        FragmentAlarmClock fragment = new FragmentAlarmClock();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_alarm_clock, container, false);
        ((MainActivity) getActivity()).showNav();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewAlarms);
        final AlarmListAdapter adapter = new AlarmListAdapter((MainActivity)getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager((MainActivity)getContext()));


        // Get a new or existing ViewModel from the ViewModelProvider.
        mAlarmViewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mAlarmViewModel.getAllAlarms().observe(this, new Observer<List<AlarmDetail>>() {
            @Override
            public void onChanged(@Nullable final List<AlarmDetail> alarms) {
                // Update the cached copy of the words in the adapter.
                adapter.setAlarms(alarms);
            }
        });



        mAdapter = new AlarmListAdapter((MainActivity)getContext());



        mAdapter.setOnItemClickListener(new AlarmListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.d("debug", "onItemClick position: " + position);
                AlarmDetail alarm = adapter.getAlarmAtPosition(position);
                Intent createAlarm = new Intent((MainActivity)getActivity(), AlarmCreateActivity.class);
                createAlarm.putExtra(ID_REPLY, alarm.getId());
                createAlarm.putExtra(HOURS_REPLY, alarm.getAlarmHour());
                createAlarm.putExtra(MINUTES_REPLY, alarm.getAlarmMinute());
                createAlarm.putExtra(DESCRIPTION_REPLY, alarm.getDescription());
                createAlarm.putExtra(IS_SUNDAY_REPLY, alarm.isSunday());
                createAlarm.putExtra(IS_MONDAY_REPLY, alarm.isMonday());
                createAlarm.putExtra(IS_TUESDAY_REPLY, alarm.isTuesday());
                createAlarm.putExtra(IS_WEDNESDAY_REPLY, alarm.isWednesday());
                createAlarm.putExtra(IS_THURSDAY_REPLY, alarm.isThursday());
                createAlarm.putExtra(IS_FRIDAY_REPLY, alarm.isFriday());
                createAlarm.putExtra(IS_SATURDAY_REPLY, alarm.isSaturday());
                startActivityForResult(createAlarm, 1);
            }

            @Override
            public void onItemLongClick(int position, View v) {
                Log.d("debug", "onItemLongClick pos = " + position);
                AlarmDetail alarm = adapter.getAlarmAtPosition(position);
                Toast.makeText((MainActivity)getActivity(), "Deleting " +
                        alarm.getTime(), Toast.LENGTH_SHORT).show();

                // Delete the word
                mAlarmViewModel.deleteAlarm(alarm);
            }
        });

        FloatingActionButton flButton = (FloatingActionButton)view.findViewById(R.id.addAlarm);
        flButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createAlarm = new Intent((MainActivity)getActivity(), AlarmCreateActivity.class);
                createAlarm.putExtra(ID_REPLY, -1);
                startActivityForResult(createAlarm, 1);
            }
        });


        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("debug", "onActivityResult: " + requestCode + ":" + resultCode );

        if (resultCode == RESULT_OK) {
            AlarmDetail newAlarm = new AlarmDetail();
            if(data.getIntExtra(AlarmCreateActivity.ID_REPLY, -1) != -1){
                newAlarm.setId(data.getIntExtra(AlarmCreateActivity.ID_REPLY, -1));
            }
            newAlarm.setTime(data.getStringExtra(AlarmCreateActivity.TIME_REPLY));
            newAlarm.setDescription(data.getStringExtra(AlarmCreateActivity.DESCRIPTION_REPLY));
            newAlarm.setAlarmState(data.getBooleanExtra(AlarmCreateActivity.STATE_REPLY, true));
            newAlarm.setDays(data.getStringExtra(AlarmCreateActivity.DAYS_REPLY));
            newAlarm.setAlarmHour(data.getIntExtra(AlarmCreateActivity.HOURS_REPLY, 0));
            newAlarm.setAlarmMinute(data.getIntExtra(AlarmCreateActivity.MINUTES_REPLY, 0));

            newAlarm.setMonday(data.getBooleanExtra(AlarmCreateActivity.IS_MONDAY_REPLY, false));
            newAlarm.setTuesday(data.getBooleanExtra(AlarmCreateActivity.IS_TUESDAY_REPLY, false));
            newAlarm.setWednesday(data.getBooleanExtra(AlarmCreateActivity.IS_WEDNESDAY_REPLY, false));
            newAlarm.setThursday(data.getBooleanExtra(AlarmCreateActivity.IS_THURSDAY_REPLY, false));
            newAlarm.setFriday(data.getBooleanExtra(AlarmCreateActivity.IS_FRIDAY_REPLY, false));
            newAlarm.setSaturday(data.getBooleanExtra(AlarmCreateActivity.IS_SATURDAY_REPLY, false));
            newAlarm.setSunday(data.getBooleanExtra(AlarmCreateActivity.IS_SUNDAY_REPLY, false));


            mAlarmViewModel.insert(newAlarm);

            Intent registerAlarm = new Intent(this.getContext(), AlarmReceiver.class);
//            registerAlarm.putExtra(DESCRIPTION_REPLY, data.getStringExtra(AlarmCreateActivity.DESCRIPTION_REPLY));
            registerAlarm.putExtras(data.getExtras());
            PendingIntent pi = PendingIntent.getBroadcast(this.getContext(), 0,
                    registerAlarm, PendingIntent.FLAG_ONE_SHOT);

            int h = data.getIntExtra(AlarmCreateActivity.HOURS_REPLY, 0);
            int m = data.getIntExtra(AlarmCreateActivity.MINUTES_REPLY, 0);
            AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);
            Calendar futureTime = Calendar.getInstance();
            futureTime.add(Calendar.SECOND, calcTime(h,m));
            alarmManager.set(AlarmManager.RTC_WAKEUP, futureTime.getTimeInMillis(),pi);
        } else {
            Toast.makeText((MainActivity)getActivity(), "Cancelled",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.clear_data) {
            // Add a toast just for confirmation
            Toast.makeText((MainActivity)getActivity(), "Clearing the data...",
                    Toast.LENGTH_SHORT).show();

            // Delete the existing data
            mAlarmViewModel.deleteAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int calcTime(int setHour, int setMinute){
        Date dt = new Date();
        int systemHours = dt.getHours();
        int systemMinutes = dt.getMinutes();
        int systemSeconds = dt.getSeconds();
        Log.e("debug", "system hours: " + systemHours );
        Log.e("debug", "system minutes: " + systemMinutes );

        Log.e("debug", "set hours: " + setHour );
        Log.e("debug", "set minutes: " + setMinute );

        String popUpText = "";

        systemSeconds = systemSeconds + systemMinutes*60 + systemHours*3600;
        int setSeconds = setMinute*60 + setHour*3600;

        if(setSeconds > systemSeconds){
            int diff = setSeconds - systemSeconds;
            int h = diff/3600;
            if( h > 0){
                if (h == 1){
                    popUpText += h + " hour and ";
                }else {
                    popUpText += h + " hours and ";
                }

            }
            int m = ( diff - 3600 * h ) / 60;
            if( m!= 0){
                if (m == 1){
                    popUpText += m + " minute";
                }else{
                    popUpText += m + " minutes";
                }

            }else{
                popUpText += (diff - 3600 * h - 60 * m) + " seconds";
            }

            Toast.makeText((MainActivity)getActivity(), "Alarm is set for "+popUpText + " from now",
                    Toast.LENGTH_SHORT).show();
            return diff;
        }
        int diff = ( 24*60*60 - (systemSeconds-setSeconds) );
        int h = diff/3600;
        if( h > 0){
            if (h == 1){
                popUpText += h + " hour and ";
            }else {
                popUpText += h + " hours and ";
            }

        }
        int m = ( diff - 3600 * h ) / 60;
        if( m!= 0){
            if (m == 1){
                popUpText += m + " minute";
            }else{
                popUpText += m + " minutes";
            }

        }else{
            popUpText += (diff - 3600 * h - 60 * m) + " seconds";
        }

        Toast.makeText((MainActivity)getActivity(), "Alarm is set for "+popUpText + " from now",
                Toast.LENGTH_SHORT).show();
        return diff;
    }

}