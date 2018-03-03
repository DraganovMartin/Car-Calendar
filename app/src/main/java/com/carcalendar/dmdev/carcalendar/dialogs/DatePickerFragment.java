package com.carcalendar.dmdev.carcalendar.dialogs;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.carcalendar.dmdev.carcalendar.AddVehicleCarActivity;
import com.carcalendar.dmdev.carcalendar.AddVehicleMotorcycleActivity;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment {


    private cancelDate cancelDateListener;
    private String tag=null;

    public interface cancelDate{
        public void onCancelDate(DialogInterface dialog);
    }
    // Use the current date as the default date in the picker
    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DatePickerDialog dialog;
        AddVehicleCarActivity carActivity =null;
        AddVehicleMotorcycleActivity motorcycleActivity = null;
        if(getActivity() instanceof AddVehicleCarActivity){
            carActivity= (AddVehicleCarActivity) getActivity();
            dialog = new DatePickerDialog(getActivity(),carActivity, year, month, day);
        }else{
            motorcycleActivity = (AddVehicleMotorcycleActivity) getActivity();
            dialog = new DatePickerDialog(getActivity(),motorcycleActivity, year, month, day);
        }

        dialog.getDatePicker().setTag(tag);
        cancelDateListener = carActivity != null? carActivity : motorcycleActivity;
        return dialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        cancelDateListener.onCancelDate(dialog);
    }
}
