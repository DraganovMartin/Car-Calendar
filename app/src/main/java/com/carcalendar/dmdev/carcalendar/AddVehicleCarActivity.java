package com.carcalendar.dmdev.carcalendar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.carcalendar.dmdev.carcalendar.dialogs.DatePickerFragment;
import com.carcalendar.dmdev.carcalendar.services.ImageSaver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import model.Stickers.AnnualVignette;
import model.Stickers.IVignette;
import model.Stickers.MonthVignette;
import model.Stickers.WeekVignette;
import model.UserManager;
import model.Vehicle.Car;

public class AddVehicleCarActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener {
    private Button saveBtn;
    private Button cancelBtn;
    private ImageButton carBtn;
    private Spinner carTypeSpinner;
    private Spinner engineTypeSpinner;
    private Spinner vignetteTypeSpinner;
    private EditText yearText;
    private EditText rangeText;
    private EditText brand;
    private EditText model;
    private EditText oilET;
    private EditText taxAmount;
    private EditText insuranceAmmount;
    private EditText registrationNumber;
    private Spinner insuranceTypeSpinner;
    private Car car;
    private int vehicleType;
    private IVignette vignette = null;
    private boolean datePickerActivated = false;
    private UserManager manager = UserManager.getInstance();

    private static final int REQUEST_IMAGE_CAMERA = 0;
    private static final int REQUEST_IMAGE_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle_car);
        vehicleType = getIntent().getIntExtra("Car",0); // Extra was int instead of String and a silent exception was thrown
        saveBtn = (Button) findViewById(R.id.btn_car_save);
        cancelBtn = (Button) findViewById(R.id.cancel_car_btn);
        carBtn = (ImageButton) findViewById(R.id.imageButton_car_add);
        carBtn.setImageResource(getIntent().getIntExtra("Car",R.mipmap.motorcycle_black));
        carTypeSpinner = (Spinner) findViewById(R.id.spinner_type_car);
        engineTypeSpinner = (Spinner) findViewById(R.id.spinner_car_engine);
        vignetteTypeSpinner = (Spinner) findViewById(R.id.vignette_type_spinner);
        brand = (EditText) findViewById(R.id.vehicle_brand);
        model = (EditText) findViewById(R.id.vehicle_model);
        oilET = (EditText) findViewById(R.id.oilEditText);
        registrationNumber = (EditText) findViewById(R.id.txt_licence_plate);
        taxAmount = (EditText) findViewById(R.id.tax_ammount_ET);
        insuranceAmmount = (EditText) findViewById(R.id.insurance_ammount_ET);
        insuranceTypeSpinner = (Spinner) findViewById(R.id.insurance_spinner);

        String [] carType = getResources().getStringArray(R.array.CarTypes);
        populateSpinner(carTypeSpinner,carType);

        String [] engineType = getResources().getStringArray(R.array.EngineTypes);
        populateSpinner(engineTypeSpinner,engineType);

        String [] vignetteType = getResources().getStringArray(R.array.VignetteTypes);
        populateSpinner(vignetteTypeSpinner,vignetteType);

        String [] insuranceType = getResources().getStringArray(R.array.InsurancePeriod);
        populateSpinner(insuranceTypeSpinner,insuranceType);

        yearText = (EditText) findViewById(R.id.yearEText);
        rangeText = (EditText) findViewById(R.id.rangeEText);

        // Gets the data from an already registered car
        // Sets the the data fields using the extra Car object
        Intent launchingIntent = getIntent();
        if(launchingIntent.hasExtra("Car object")){
            car = (Car) launchingIntent.getSerializableExtra("Car object");


            // Sets the car image
            carBtn.setImageBitmap(car.getImage());

            // Sets the car type for ex. : Sedan, Jeep ...
            switch (car.getCarType()){
                case "Sedan":
                    carTypeSpinner.setSelection(0);
                    break;
                case "Jeep":
                    carTypeSpinner.setSelection(1);
                    break;
                case "Hatchback":
                    carTypeSpinner.setSelection(2);
                    break;
                case "Coupe":
                    carTypeSpinner.setSelection(3);
            }

            // Sets engine type
            switch (car.getEngineType()){
                case "Gasoline":
                    engineTypeSpinner.setSelection(0);
                    break;
                case "Diesel":
                    engineTypeSpinner.setSelection(1);
                    break;
                case "Hybrid":
                    engineTypeSpinner.setSelection(2);
            }

            // Sets the vignette type
            switch (car.getVignette().getType()){
                case "Weekly":
                    vignetteTypeSpinner.setSelection(0);
                    break;
                case "Monthly":
                    vignetteTypeSpinner.setSelection(1);
                    break;
                case "Annual" :
                    vignetteTypeSpinner.setSelection(2);
                    break;
            }

            // Sets the vehicle brand
            brand.setText(car.getBrand());

            // Sets the car model
            model.setText(car.getModel());

            // Sets the tax amount
            taxAmount.setText(String.valueOf(car.getVehicleTaxAmount()));

            // Sets the production year
            yearText.setText(String.valueOf(car.getProductionYear()));

            // Sets the range
            rangeText.setText(car.getKmRange());

            // Sets the registrationPlate
            registrationNumber.setText(car.getRegistrationPlate());

            // TODO implement oilET, insuranceAmmount, insuranceTypeSpinner in model, vehicle dates

        }else{
            // Initialize an empty car object
            car=new Car();
        }


        carTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:         // Sedan
                        car.setCarType("Sedan");
                        break;
                    case 1:         // Jeep
                        car.setCarType("Jeep");
                        break;
                    case 2:         // HatchBack
                        car.setCarType("Hatchback");
                        break;
                    case 3:          // Coupe
                        car.setCarType("Coupe");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                car.setCarType("");
            }
        });

        engineTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:         //Gasoline
                        car.setEngineType("Gasoline");
                        break;
                    case 1:         //Diesel
                        car.setEngineType("Diesel");
                        break;
                    case 2:         //Hybrid
                        car.setEngineType("Hybrid");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                car.setEngineType("");
            }
        });

        vignetteTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:         //Weekly
                        vignette = new WeekVignette();
                        break;
                    case 1:         //Monthly
                        vignette = new MonthVignette();
                        break;
                    case 2:         //Annual
                        vignette = new AnnualVignette();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                vignette = null;
            }
        });

        insuranceTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:         // Three Month
                        break;
                    case 1:         // Annual
                        break;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // TODO : Debug why image is not displaying using URI and why is not saving using Bitmap
        // TODO : Fix UI, test on different OS versions and screen sizes, polish most of the bugs to remain only he service next week :)

        carBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Upload image");
                builder.setMessage("Choose image from");
                builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        Intent imageSaver = new Intent(getApplicationContext(),ImageSaver.class);
                        startService(imageSaver);

                        // TODO Get result from Service
                        // TODO add storage permission
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAMERA);
                        }
                    }
                });
                builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!registrationNumber.getText().toString().isEmpty()){
                    car.setRegistrationPlate(registrationNumber.getText().toString());
                }else{
                    registrationNumber.setError("Please enter a registration number!");
                }
                if(!brand.getText().toString().isEmpty()){
                    car.setBrand(brand.getText().toString());
                }
                else{
                    brand.setError("Please input brand !!");
                }
                if(!model.getText().toString().isEmpty()){
                    car.setModel(model.getText().toString());
                }
                else{
                    model.setError("Please input model !!");
                }
                if(vignette == null){
                    Toast.makeText(getApplicationContext(),"Please set vignette type",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    car.setVignette(vignette);
                }
                if(car.getCarType() == null || car.getCarType().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please set car type",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(car.getEngineType() == null || car.getEngineType().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please set engine type",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!yearText.getText().toString().isEmpty()) {
                    if (Integer.valueOf(yearText.getText().toString()) < 0) {
                        yearText.setError("Please set year bigger or equal to 0");
                        //Toast.makeText(getApplicationContext(), "Please set year bigger or equal to 0", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        car.setProductionYear(Integer.valueOf(yearText.getText().toString()));
                    }
                }
                if(!rangeText.getText().toString().isEmpty() ) {
                    if (Integer.valueOf(rangeText.getText().toString()) < 0) {
                        rangeText.setError("Please set range bigger or equal to 0");
                        //Toast.makeText(getApplicationContext(), "Please set range bigger or equal to 0", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        car.setKmRange(rangeText.getText().toString());
                    }
                }

                if(!taxAmount.getText().toString().isEmpty()){
                    car.setVehicleTaxAmount(Double.valueOf(taxAmount.getText().toString()));
                                    }
                else{
                    taxAmount.setError("Please input tax amount");
                    return;
                }

                if(!datePickerActivated){
                    Toast.makeText(getApplicationContext(),"Please choose vignette start day !!!",Toast.LENGTH_SHORT).show();
                    return;
                }

                manager.addVehicle(car);
                setResult(GarageActivity.VEHICLE_ADDED_SUCCESSFULLY);
                //Log.e("calendar",String.valueOf(((AnnualVignette) vignette).getEndDateObject().get(Calendar.YEAR)) + " " + ((AnnualVignette) vignette).getEndDateObject().get(Calendar.MONTH) + " " + ((AnnualVignette) vignette).getEndDateObject().get(Calendar.DAY_OF_MONTH));
                finish();

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                car.setEngineType("");
                car.setCarType("");
                car.setKmRange("");
                car.setProductionYear(0);
                car.setVignette(null);
                vignette = null;
                setResult(GarageActivity.VEHICLE_ADDED_UNSUCCESSFULLY);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        cancelBtn.callOnClick();
    }

    private void populateSpinner(Spinner spinner, String [] arr){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,arr);
        spinner.setAdapter(adapter);
    }

    public void selectDate(View view){
        switch (view.getId()){
            case R.id.date_btn :
                DatePickerFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getSupportFragmentManager(),"datePick");
                datePickerActivated = true;
                return;
            case R.id.btn_next_payment:
                return;
            case R.id.btn_insurance_start :
                return;

            default:
                return;
        }

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        if(vignette instanceof WeekVignette){
            ((WeekVignette) vignette).setStartDate(year,month,day);
        }
        if(vignette instanceof  MonthVignette){
            ((MonthVignette) vignette).setStartDate(year,month,day);
        }
        if(vignette instanceof  AnnualVignette){
            ((AnnualVignette) vignette).setStartDate(year,month,day);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap photo=null;
        if (requestCode == REQUEST_IMAGE_CAMERA && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
        }

        try{
            if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
                Uri fullPhotoUri = data.getData();
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fullPhotoUri);
            }
        } catch (IOException e) {
            Log.e(AddVehicleCarActivity.class.getName(),e.getMessage());
        }

        carBtn.setImageBitmap(photo);
        car.setImage(photo);
        carBtn.refreshDrawableState();
    }
}

