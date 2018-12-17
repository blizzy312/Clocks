package com.blizzy312.clocks;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "AlarmList")
public class AlarmDetail {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String time;
    private String description;
    private String days;
    private int alarmHour, alarmMinute;
    private boolean alarmState, isRepeatSelected;
    private boolean isSunday, isMonday, isTuesday, isWednesday, isThursday, isFriday, isSaturday;



    public AlarmDetail(){
    }

    @Ignore
    public AlarmDetail(int id, @NonNull String time) {
        this.id = id;
        this.time = time;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAlarmState() {
        return alarmState;
    }

    public void setAlarmState(boolean alarmState) {
        this.alarmState = alarmState;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public int getAlarmHour() {
        return alarmHour;
    }

    public void setAlarmHour(int alarmHour) {
        this.alarmHour = alarmHour;
    }

    public int getAlarmMinute() {
        return alarmMinute;
    }

    public void setAlarmMinute(int alarmMinute) {
        this.alarmMinute = alarmMinute;
    }

    public boolean isSunday() {
        return isSunday;
    }

    public void setSunday(boolean sunday) {
        isSunday = sunday;
    }

    public boolean isMonday() {
        return isMonday;
    }

    public void setMonday(boolean monday) {
        isMonday = monday;
    }

    public boolean isTuesday() {
        return isTuesday;
    }

    public void setTuesday(boolean tuesday) {
        isTuesday = tuesday;
    }

    public boolean isWednesday() {
        return isWednesday;
    }

    public void setWednesday(boolean wednesday) {
        isWednesday = wednesday;
    }

    public boolean isThursday() {
        return isThursday;
    }

    public void setThursday(boolean thursday) {
        isThursday = thursday;
    }

    public boolean isFriday() {
        return isFriday;
    }

    public void setFriday(boolean friday) {
        isFriday = friday;
    }

    public boolean isSaturday() {
        return isSaturday;
    }

    public void setSaturday(boolean saturday) {
        isSaturday = saturday;
    }

    public boolean isRepeatSelected() {
        return isRepeatSelected;
    }

    public void setRepeatSelected(boolean repeatSelected) {
        isRepeatSelected = repeatSelected;
    }


}
