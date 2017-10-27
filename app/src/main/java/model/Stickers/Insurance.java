package model.Stickers;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by dimcho on 10.03.17.
 * Worked by Marto-DevM on 29.03.2017
 */

public class Insurance implements Serializable{

    private int type;
    private double price;
    private  Calendar startDate;
    private Calendar endDate;
    private Calendar[] endDates;
    public  enum Payments {
        ONE (1),
        TWO (2),
        THREE (3),
        FOUR (4);

        private final int levelCode;

        private Payments(int levelCode) {
            this.levelCode = levelCode;
        }

    }

    public Insurance(){
        endDate =  Calendar.getInstance();
        endDates = new Calendar[]{null, Calendar.getInstance(),Calendar.getInstance(),Calendar.getInstance()};
        startDate =  (Calendar) endDate.clone();
    }
    public Insurance(Payments count,double price,Calendar startDate){
        this();
        this.type = count.levelCode;
        this.price = price;
        setStartDate(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Check if the insurance is still valid
     * @return true if the insurance is still valid false otherwise
     */
    public boolean isValid(){
        Calendar lastDate = endDates[getTypeCount() - 1];
        Calendar now = Calendar.getInstance();

        if(lastDate != null){
            if(now.before(lastDate) || lastDate.equals(now)){
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the still active endDate from the specified period.
     * The method also checks if the insurance is still valid.
     *
     * @return the active end date of the current period or null if the insurance expired
     */
    public Calendar getActiveEndDate(){
        if(!isValid())
            return null;

        // Checks which is the active period
        // If the getTypeCount is 1 the loop won't run
        // Otherwise the loop will continue to check the dates until it reaches the last date.
        // The last date will not be checked in the loop.
        // It was already checked in the isValid() method so no need to check it again.
        Calendar now = Calendar.getInstance();
        for(int currentPeriod = 0; currentPeriod < getTypeCount() - 1; currentPeriod++){
            if(now.before(endDates[currentPeriod])){
                return endDates[currentPeriod];
            }
        }

        // if the insurance is valid and non of the
        return endDates[getTypeCount() - 1];
    }

    public int getTypeCount() {
        return type;
    }

    public void setTypeCount(Payments type) {
        switch (type){
            case ONE:
                this.type = type.levelCode;
                break;
            case TWO:
                this.type = type.levelCode;
                break;
            case THREE:
                this.type = type.levelCode;
                break;
            case FOUR:
                this.type = type.levelCode;
                break;
            default:
                this.type = 0;
        }

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
            endDate.clear();
            startDate.set(year, month, day);
            if (type > 0 && type == (Payments.ONE.levelCode)) {
                endDate.set(year + 1, month, day);
                endDates[0] = (Calendar) endDate.clone();
            }
            if (type > 0 && type ==(Payments.TWO.levelCode)) {
                endDate.set(year, month+6, day);
                endDates[0] = (Calendar) endDate.clone();
                endDates[1].set(endDate.get(Calendar.YEAR),endDate.get(Calendar.MONTH)+6,day);
            }
            if (type > 0 && type == (Payments.THREE.levelCode)) {
                endDate.set(year, month +4, day);
                endDates[0] = (Calendar) endDate.clone();
                endDates[1].set(endDate.get(Calendar.YEAR),endDate.get(Calendar.MONTH)+4,day);
                endDates[2].set(endDates[1].get(Calendar.YEAR),endDates[1].get(Calendar.MONTH)+4,day);

            }
            if (type > 0 && type == (Payments.FOUR.levelCode)) {
                endDate.set(year, month + 3, day);
                endDates[0] = (Calendar) endDate.clone();
                endDates[1].set(endDate.get(Calendar.YEAR),endDate.get(Calendar.MONTH)+3,day);
                endDates[2].set(endDates[1].get(Calendar.YEAR),endDates[1].get(Calendar.MONTH)+3,day);
                endDates[3].set(endDates[2].get(Calendar.YEAR),endDates[2].get(Calendar.MONTH)+3,day);
            }
        }
    }

    public Calendar[] getEndDates() {
        return endDates;
    }
    public String getStartDate(){
        return new SimpleDateFormat("yyyy-MM-dd").format(startDate.getTime());
    }

    /**
     *
     * @return date as String : yyyy-mm-dd
     */
    public String getEndDateString(){
        return new SimpleDateFormat("yyyy-MM-dd").format(endDate.getTime());
    }

    /**
     * Gets the final end date of the insurance.
     * @return final end date formatted as string " yyyy-MM-dd "
     */
    public String getTotalEndDate() {return new SimpleDateFormat("yyyy-MM-dd").format(endDates[type - 1].getTime());}

    public String getTypeForDB(){
        switch (type){
            case 1: return "1-insurance";
            case 2: return "2-insurance";
            case 3: return "3-insurance";
            case 4: return "4-insurance";
            default: return null;
        }
    }
}