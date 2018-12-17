package com.blizzy312.clocks;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class AlarmRepository {

    private AlarmDao mAlarmDao;
    private LiveData<List<AlarmDetail>> mAllAlarms;

    AlarmRepository(Application application) {
        AlarmDatabase db = AlarmDatabase.getDatabase(application);
        mAlarmDao = db.alarmDao();
        mAllAlarms = mAlarmDao.getAllAlarms();
    }

    LiveData<List<AlarmDetail>> getAllAlarms() {
        return mAllAlarms;
    }


    public void insert (AlarmDetail newAlarm) {
        new insertAsyncTask(mAlarmDao).execute(newAlarm);
    }

    private static class insertAsyncTask extends AsyncTask<AlarmDetail, Void, Void> {

        private AlarmDao mAsyncTaskDao;

        insertAsyncTask(AlarmDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final AlarmDetail... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void deleteAll()  {
        new deleteAllAlarmsAsyncTask(mAlarmDao).execute();
    }

    private static class deleteAllAlarmsAsyncTask extends AsyncTask<Void, Void, Void> {
        private AlarmDao mAsyncTaskDao;

        deleteAllAlarmsAsyncTask(AlarmDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    public void deleteAlarm(AlarmDetail alarm)  {
        new deleteAlarmAsyncTask(mAlarmDao).execute(alarm);
    }

    private static class deleteAlarmAsyncTask extends AsyncTask<AlarmDetail, Void, Void> {
        private AlarmDao mAsyncTaskDao;

        deleteAlarmAsyncTask(AlarmDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final AlarmDetail... params) {
            mAsyncTaskDao.deleteAlarm(params[0]);
            return null;
        }
    }
}
