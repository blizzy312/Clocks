package com.blizzy312.clocks;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;


public class AlarmReceiver extends WakefulBroadcastReceiver {

    String description;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.e("debug", "ring ring" );
        Intent startAlarm = new Intent(context, RingtonePlayingService.class);
        startAlarm.putExtras(intent.getExtras());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(startAlarm);
        } else {
            context.startService(startAlarm);
        }
    }
}
