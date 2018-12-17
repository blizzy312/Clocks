package com.blizzy312.clocks;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

@Database(entities = {AlarmDetail.class}, version = 1)
public abstract class AlarmDatabase extends RoomDatabase {

    public abstract AlarmDao alarmDao();

    private static volatile AlarmDatabase INSTANCE;

    static AlarmDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AlarmDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AlarmDatabase.class, "alarm_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                    Log.e("debug", "AlarmDatabase, getDatabase is called " );
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     *
     * If you want to populate the database only when the database is created for the 1st time,
     * override RoomDatabase.Callback()#onCreate
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    /**
     * Populate the database in the background.
     * If you want to start with more words, just add them.*/

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AlarmDao mDao;

        PopulateDbAsync(AlarmDatabase db) {
            mDao = db.alarmDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
//            mDao.deleteAll();

//            AlarmDetail word = new AlarmDetail();
////            word.setTime("12:30");
////            word.setAlarmState(true);
////            word.setDescription("Hello");
//            mDao.insert(word);
            // If we have no words, then create the initial list of words
            /*if (mDao.getAnyAlarm().length < 1) {
                for (int i = 0; i <= Alarms.length - 1; i++) {
                    AlarmDetail word = new AlarmDetail(alarms[i]);
                    mDao.insert(word);
                }
            }*/
            Log.e("debug", "doInBackground must be added" );
            return null;
        }
    }

}


