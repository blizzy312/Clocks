package com.blizzy312.clocks;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface AlarmDao {

    @Insert(onConflict = REPLACE)
    void insert(AlarmDetail newAlarm);

    @Query("DELETE FROM AlarmList")
    void deleteAll();

    @Query("SELECT * from AlarmList ORDER BY time ASC")
    LiveData<List<AlarmDetail>> getAllAlarms();

    @Query("SELECT * from AlarmList LIMIT 1")
    AlarmDetail[] getAnyAlarm();

    @Delete
    void deleteAlarm(AlarmDetail alarm);

    @Update
    void update(AlarmDetail alarm);
}
