package model.taxes;

import java.io.Serializable;
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

    public Calendar getEndDate() {
        return endDate;
    }

    public double getAmount() {
        return amount;
    }
}
