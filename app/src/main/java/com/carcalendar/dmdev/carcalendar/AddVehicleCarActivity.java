package com.carcalendar.dmdev.carcalendar;

import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class AddVehicleCarActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener {
    private Button saveBtn;
    private ImageButton carBtn;
    private Spinner carTypeSpinner;
    private Spinner engineTypeSpinner;
    private Spinner vignetteTypeSpinner;
    private EditText yearText;
    private EditText rangeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle_car);
        saveBtn = (Button) findViewById(R.id.btn_car_save);
        carBtn = (ImageButton) findViewById(R.id.imageButton_car_add);
        carTypeSpinner = (Spinner) findViewById(R.id.spinner_type_car);
        engineTypeSpinner = (Spinner) findViewById(R.id.spinner_car_engine);
        vignetteTypeSpinner = (Spinner) findViewById(R.id.vignette_type_spinner);

        String [] carType = getResources().getStringArray(R.array.CarTypes);
        populateSpinner(carTypeSpinner,carType);

        String [] engineType = getResources().getStringArray(R.array.EngineTypes);
        populateSpinner(engineTypeSpinner,engineType);

        String [] vignetteType = getResources().getStringArray(R.array.VignetteTypes);
        populateSpinner(vignetteTypeSpinner,vignetteType);

        yearText = (EditText) findViewById(R.id.yearEText);
        rangeText = (EditText) findViewById(R.id.rangeEText);


    }

    private void populateSpinner(Spinner spinner,String [] arr){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,arr);
        spinner.setAdapter(adapter);
    }

    public void selectDate(View view){
        DatePickerFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getSupportFragmentManager(),"datePick");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        if(year == 2015){
            Toast.makeText(this,"2015",Toast.LENGTH_SHORT).show();
        }
    }
}
