package model.util;


import java.io.Serializable;

/**
 * Created by DevM on 12/20/2017.
 */

public class DateHolder implements Serializable{

    private int year;
    private int month;
    private int dayOfMonth;

    public DateHolder(int year,int month, int dayOfMonth){
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }
}
