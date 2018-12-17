package com.blizzy312.clocks;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

public class AlarmViewModel extends AndroidViewModel {

    private AlarmRepository mRepository;

    private LiveData<List<AlarmDetail>> mAllAlarms;

    public AlarmViewModel (Application application) {
        super(application);
        mRepository = new AlarmRepository(application);
        mAllAlarms = mRepository.getAllAlarms();
    }

//    public AlarmDetail getAlarmByPosition(int ind){
//        return mAllAlarms(ind);
//    }

    public LiveData<List<AlarmDetail>> getAllAlarms() {
        Log.e("debug", "all alarms returned " );
        return mAllAlarms;

    }

    public void insert(AlarmDetail newAlarm) {
        mRepository.insert(newAlarm);
    }

    public void deleteAll() {mRepository.deleteAll();}

    public void deleteAlarm(AlarmDetail alarm) {mRepository.deleteAlarm(alarm);}

//    public void update(AlarmDetail alarm) {
//        mRepository.update(alarm);
//    }
}
