package com.carcalendar.dmdev.carcalendar.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DateReceiver extends BroadcastReceiver {

    private CheckerIntentService service;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_DATE_CHANGED)){
            service = new CheckerIntentService();
            Intent startService = new Intent(context.getApplicationContext(),CheckerIntentService.class);
            context.startService(startService);
        }else throw new UnsupportedOperationException("Not yet implemented");
    }
}
