package com.carcalendar.dmdev.carcalendar;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import model.Vehicle.Vehicle;

/**
 * Created by dimcho on 25.02.18.
 */

public class ExpirationNotifier {
    private final Context context;
    private final NotificationManager notificationManager;
    private static int notificationId;

    /**
     * Creates a pending intent to the LoginActivity with a vehicle extra and an owner extra.
     * Look at LoginActivity for more information.
     *
     * @param v the vehicle for the extra
     * @param owner the owner for the extra
     *
     * @return a pending intent with a vehicle and an owner extra
     */
    private PendingIntent createPendingIntent(Vehicle v, String owner) {
        Intent resultIntent = new Intent(context, LoginActivity.class);
        // Dummy action. If no action is set the extra for the upcoming notifications is dropped.
        resultIntent.setAction(Long.toString(System.currentTimeMillis()));
        resultIntent.putExtra("vehicle", v);
        resultIntent.putExtra("username", owner);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(resultIntent);

        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    public ExpirationNotifier(Context context) {
        this.context = context;
        notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationId = 1;
    }

    /**
     * Spawns a notification with a unique id using the notificationMessage for text
     * and the owner as summary.
     *
     * @param v the vehicle for which the notification is spawned
     * @param owner the owner of the vehicle
     * @param notificationMessage the message in the notification body
     */
    public void spawnNotification(Vehicle v, String owner, String notificationMessage) {
        NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.mipmap.car_calendar_icon)
                                .setContentTitle("Calendar")
                                .setContentText("You have expiring events!")
                                .setContentIntent(createPendingIntent(v, owner))
                                .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText(notificationMessage)
                                            .setSummaryText("User: " + owner)
                                )
                                .setAutoCancel(true);

        notificationManager.notify(notificationId++, mBuilder.build());
    }
}
