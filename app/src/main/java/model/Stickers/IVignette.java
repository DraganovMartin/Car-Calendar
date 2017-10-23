package model.Stickers;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by DevM on 12/20/2016.
 */

public interface IVignette extends Serializable {
    boolean isValid();
    double getPrice();
    String getStartDate();
    String getEndDate();
    String getType();
    Calendar getEndDateAsCalender();
}
