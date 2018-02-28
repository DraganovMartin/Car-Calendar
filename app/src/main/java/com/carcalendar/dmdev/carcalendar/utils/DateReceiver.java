package com.carcalendar.dmdev.carcalendar.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DateReceiver extends BroadcastReceiver {

    private CheckerIntentService service;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TIME_SET only temporary solution
        if (intent.getAction().equals(Intent.ACTION_TIME_CHANGED)){
            service = new CheckerIntentService();
            Intent startService = new Intent(context, CheckerIntentService.class);
            context.startService(startService);
        }else throw new UnsupportedOperationException("Not yet implemented");
    }
}
