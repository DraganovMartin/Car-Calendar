package model.taxes;

import com.carcalendar.dmdev.carcalendar.utils.CalendarUtils;
import com.carcalendar.dmdev.carcalendar.utils.CalendarUtils.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by DevM on 3/30/2017.
 */

public abstract class Tax implements Serializable {

    private Calendar endDate;
    private double amount;

    protected Tax(){
        endDate =Calendar.getInstance();
    }
    protected Tax(int year, int month, int day, double amount){
        endDate = Calendar.getInstance();
        endDate.clear();
        endDate.set(year,month,day);
        this.amount = amount;
    }
    public void setEndDate(int year,int month, int day){
        if(year >0 && month >=0 && day>=0) {
            endDate.clear();
            endDate.set(year, month, day);
        }
    }
    public void setAmount(double amount){
        this.amount = amount;
    }

    public String getEndDate() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

        return format1.format(endDate.getTime());
    }

    public Calendar getEndDateAsCalendarObject(){
        return endDate;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {return "tax";}

    /**
     * Finds the number of days after which the tax expires.
     *
     * @param notificationInterval the interval (in days) during which the app notifies the user
     * @return the number of days after which the tax expires, -1 if the tax has already expired
     * and -2 if the remaining days are more than the days in the notificationInterval
     */
    public int getRemainingDays(int notificationInterval) {
        Calendar today = CalendarUtils.getOnlyDateAsCalendar();

        if(today.after(endDate)) {
            return -1;
        }

        return CalendarUtils.getDaysBetween(today, endDate, notificationInterval);
    }
}