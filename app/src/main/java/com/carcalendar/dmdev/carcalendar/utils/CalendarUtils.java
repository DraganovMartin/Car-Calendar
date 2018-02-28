package com.carcalendar.dmdev.carcalendar.utils;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by dimcho on 24.02.18.
 */

public class CalendarUtils {
    public static boolean areDatesEqual(Calendar f, Calendar s) {
        return f.get(Calendar.YEAR) == s.get(Calendar.YEAR) &&
                f.get(Calendar.MONTH) == s.get(Calendar.MONTH) &&
                f.get(Calendar.DAY_OF_MONTH) == s.get(Calendar.DAY_OF_MONTH);
    }

    /**
     *
     * @param from the starting date
     * @param to the end date
     * @param notificationInterval the interval in which a notification must be shown
     * @return the days between from and to or -2 if the days between from and two are
     * greater than the notificationInterval
     */
    public static int getDaysBetween(Calendar from, Calendar to, int notificationInterval){
        for (int remainingDays = 0; remainingDays <= notificationInterval; remainingDays++) {
            if (areDatesEqual(from, to)) {
                return remainingDays;
            }

            Log.d("Test", "Day: " + from.get(Calendar.DAY_OF_MONTH));
            // Adds one day each iteration
            from.add(Calendar.DAY_OF_MONTH, 1);
        }

        return -2;
    }

    /**
     * Cleans the time from the calendar object, so only the date remains
     * @return a clean calendar object representing today
     */
    public static Calendar getOnlyDateAsCalendar() {
        Calendar today = Calendar.getInstance();

        today.clear(Calendar.HOUR_OF_DAY);
        today.clear(Calendar.HOUR);
        today.clear(Calendar.MINUTE);
        today.clear(Calendar.SECOND);
        today.clear(Calendar.MILLISECOND);

        return today;
    }
}
