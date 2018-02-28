package model.Stickers;

import com.carcalendar.dmdev.carcalendar.utils.CalendarUtils;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by DevM on 12/20/2016.
 */

public interface IVignette extends Serializable {

    /**
     * Finds the number of days after which the vignette expires.
     *
     * @param notificationInterval the interval (in days) during which the app notifies the user
     * @return number of days after which the tax expires, -1 if the tax has already expired
     * and -2 if the remaining days are more than the days in the notificationInterval
     */
    default int getRemainingDays(int notificationInterval) {
        Calendar today = CalendarUtils.getOnlyDateAsCalendar();
        Calendar endDate = this.getEndDateAsCalendar();

        if(today.after(endDate)) {
            return -1;
        }

        return CalendarUtils.getDaysBetween(today, endDate, notificationInterval);
    }

    boolean isValid();
    double getPrice();
    String getStartDate();
    String getEndDate();
    String getType();
    Calendar getStartDateAsCalender();
    Calendar getEndDateAsCalendar();
}