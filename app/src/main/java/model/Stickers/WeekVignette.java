package model.Stickers;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by DevM on 12/20/2016.
 */

public class WeekVignette implements IVignette {
    private Calendar startDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();
    private Calendar today = Calendar.getInstance();
    private double price;

    public WeekVignette(){}

    public WeekVignette(int startYear, int startMonth, int startDay,double price){
        startDate.set(startYear,startMonth,startDay);
        endDate.set(startYear,startMonth,startDay);
        endDate.add(Calendar.WEEK_OF_MONTH,+1);
        this.price = price;
    }

    public Calendar getStartDateObject() {
        return startDate;
    }

    public void setStartDate(int startYear, int startMonth, int startDay) {
        startDate.set(startYear,startMonth,startDay);
    }

    public Calendar getEndDateObject() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }


    @Override
    public boolean isValid() {
        today.setTime(new Date());
        if(today.before(endDate)){
            return true;
        }
        else return false;
    }

    @Override
    public double getPrice() {
        return price;
    }
}
