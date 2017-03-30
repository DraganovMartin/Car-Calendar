package model.Stickers;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by dimcho on 10.03.17.
 * Worked by Marto-DevM on 29.03.2017
 */

public class Insurance implements Serializable{

    private String type;
    private double price;
    private  Calendar startDate;
    private Calendar endDate;
    public static final String THREE_MONTH = "Three Month";
    public static final String ANNUAL = "Annual";

    public Insurance(){
        type = null;
    }
    public Insurance(String type,double price,Calendar startDate,Calendar endDate){
        this.type = type;
        this.price = price;
        this.startDate = startDate;
        this.endDate=endDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price >= 0.0) {
            this.price = price;
        }
    }

    public void setStartDate(int year,int month,int day){
        if (year > 0 && month >= 0 && day >= 0) {
            startDate.clear();
            startDate.set(year, month, day);
            if (type != null && type.equals(ANNUAL)) {
                endDate.set(year + 1, month, day);
            }
            if (type != null && type.equals(THREE_MONTH)) {
                endDate.set(year, month + 3, day);
            }
        }
    }

    public Calendar getEndDate() {
        return endDate;
    }
}
