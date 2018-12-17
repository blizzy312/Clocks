package com.blizzy312.clocks;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

import java.util.Calendar;

public class RingtonePlayingService extends Service {

    MediaPlayer media_song;
    Bundle alarmData;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        alarmData = intent.getExtras();
        checkForNextAlarm(alarmData);
        media_song = MediaPlayer.create(this, R.raw.eminem_psychopath_killer);
        media_song.start();

        displayAlert();

        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(1,new Notification());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void displayAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.alarm_dialog_title)).setCancelable(
                false).setPositiveButton(getString(R.string.alarm_dialog_dismiss),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        media_song.stop();
                        media_song.release();
                        dialog.cancel();
                    }
                }).setNegativeButton(getString(R.string.alarm_dialog_snooze),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        media_song.stop();
                        media_song.release();
                        snooze();
                        dialog.cancel();
                    }
                });

        final AlertDialog dialog = builder.create();
        final Window dialogWindow = dialog.getWindow();
        final WindowManager.LayoutParams dialogWindowAttributes = dialogWindow.getAttributes();

        // Set fixed width (280dp) and WRAP_CONTENT height
        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogWindowAttributes);
        lp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280, getResources().getDisplayMetrics());
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);

        // Set to TYPE_SYSTEM_ALERT so that the Service can display it
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dialogWindow.setType(WindowManager.LayoutParams.TYPE_TOAST);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialogWindow.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            dialogWindow.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        dialog.show();
    }

    private void snooze(){

        Calendar futureTime = Calendar.getInstance();
        futureTime.add(Calendar.MINUTE, 5);
        long nextSnoozeTime = futureTime.getTimeInMillis();
        // set new snooze alarm
        Intent intent = new Intent(getApplicationContext(),
                AlarmReceiver.class);
//        intent.putExtra("SNOOZE_COUNTER", snoozeCounter);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext()
                .getSystemService(Context.ALARM_SERVICE);
        Log.d("debug", "Snooze set to: " + futureTime.getTime().toString());
        alarmManager
                .set(AlarmManager.RTC_WAKEUP, nextSnoozeTime, pendingIntent);
    }

    private void checkForNextAlarm(Bundle data){
        /*AlarmRepository noteRepository = new AlarmRepository(getApplicationContext());
        Note note = noteRepository.getTask(2);
        AlarmDetail x = new AlarmDetail();*/
    }
}

