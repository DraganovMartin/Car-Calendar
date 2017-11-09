package model.Stickers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by DevM on 12/20/2016.
 */

public class WeekVignette implements IVignette {
    private Calendar startDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();
    private double price;

    public WeekVignette(){}

    public WeekVignette(int startYear, int startMonth, int startDay,double price){
        startDate.clear();
        startDate.set(startYear,startMonth,startDay);
        endDate.clear();
        endDate.set(startYear,startMonth,startDay);
        endDate.add(Calendar.WEEK_OF_MONTH,+1);
        this.price = price;
    }

    public Calendar getStartDateObject() {
        return startDate;
    }

    public void setStartDate(int startYear, int startMonth, int startDay) {
        startDate.clear();
        startDate.set(startYear,startMonth,startDay);
        endDate.clear();
        endDate.set(startYear,startMonth,startDay);
        endDate.add(Calendar.WEEK_OF_MONTH,+1);
    }

    public Calendar getEndDateObject() {
        return endDate;
    }


    @Override
    public boolean isValid() {
        //today.setTime(new Date());
        Calendar today = Calendar.getInstance();
        if(today.before(endDate) || today.compareTo(endDate) == 0){
            return true;
        }
        else return false;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String getStartDate() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String date = format1.format(startDate.getTime());
        return date;
    }

    @Override
    public String getEndDate() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String date = format1.format(endDate.getTime());
        return date;
    }

    @Override
    public Calendar getStartDateAsCalender(){
        return startDate;
    }

    @Override
    public String getType() {
        return "week-vignette";
    }
}