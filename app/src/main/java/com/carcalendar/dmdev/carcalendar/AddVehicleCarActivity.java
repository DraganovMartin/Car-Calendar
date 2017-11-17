package com.carcalendar.dmdev.carcalendar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.carcalendar.dmdev.carcalendar.dialogs.DatePickerFragment;
import com.carcalendar.dmdev.carcalendar.utils.DatabaseManager;

import java.io.File;
import java.io.InputStream;
import java.util.Calendar;

import model.Stickers.AnnualVignette;
import model.Stickers.IVignette;
import model.Stickers.Insurance;
import model.Stickers.MonthVignette;
import model.Stickers.WeekVignette;
import model.UserManager;
import model.Vehicle.Car;
import model.Vehicle.Vehicle;
import model.taxes.VehicleTax;
import model.util.ImageUtils;

public class AddVehicleCarActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener,DatePickerFragment.cancelDate{
    private Button saveBtn;
    private Button cancelBtn;
    private ImageButton carBtn;
    private String pathToImage;
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
    private boolean taxDatePickerActivated = false;
    private boolean inEditMode = false;
    private UserManager manager = UserManager.getInstance();

    private Uri photoURIFromCamera;
    private Bitmap cameraBitmap;
    private Bitmap galleryBitmap;

    private static final int REQUEST_IMAGE_CAMERA = 0;
    private static final int REQUEST_IMAGE_GALLERY = 1;
    public static final String GET_VEHICLE_TYPE = "Car";


    private Car copyCar(Car carToCpy) {
        Car car = new Car();

        car.setId(carToCpy.getId());
        car.setRegistrationPlate(carToCpy.getRegistrationPlate());
        car.setCarType(carToCpy.getCarType());
        car.setEngineType(carToCpy.getEngineType());
        car.setKmRange(carToCpy.getKmRange());
        car.setVignette(carToCpy.getVignette());
        car.setBrand(carToCpy.getBrand());
        car.setInsurance(carToCpy.getInsurance());
        car.setModel(carToCpy.getModel());
        car.setNextOilChange(carToCpy.getNextOilChange());
        car.setPathToImage(carToCpy.getPathToImage());
        car.setProductionYear(carToCpy.getProductionYear());
        car.setTax((VehicleTax) carToCpy.getTax());

        return car;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle_car);
        vehicleType = getIntent().getIntExtra("Car", 0); // Extra was int instead of String and a silent exception was thrown
        saveBtn = (Button) findViewById(R.id.btn_car_save);
        cancelBtn = (Button) findViewById(R.id.cancel_car_btn);
        carBtn = (ImageButton) findViewById(R.id.imageButton_car_add);
        carBtn.setImageResource(getIntent().getIntExtra("Car", R.mipmap.car_add_image));
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
        yearText = (EditText) findViewById(R.id.yearEText);
        rangeText = (EditText) findViewById(R.id.rangeEText);


        // TODO : fucking fix image on orientation change ...
        if (savedInstanceState != null){
            if (savedInstanceState.get("camera") != null){
                this.cameraBitmap = (Bitmap) savedInstanceState.get("camera");
                carBtn.setImageBitmap(cameraBitmap);
            }
            else if (savedInstanceState.get("gallery") != null){
                this.galleryBitmap = (Bitmap) savedInstanceState.get("gallery");
                carBtn.setImageBitmap(galleryBitmap);
            }
            else {
                carBtn.setImageResource(getIntent().getIntExtra("Car", R.mipmap.car_add_image));
                cameraBitmap = null;
                galleryBitmap = null;
            }
            carBtn.refreshDrawableState();
        }


        carBtn.setDrawingCacheEnabled(true);

        // Gets the data from an already registered car
        // Sets the the data fields using the extra Car object
        final Intent launchingIntent = getIntent();
        if (launchingIntent.hasExtra("Car object")) {
            inEditMode = true;

            registrationNumber.setEnabled(false);

            // Copy data from original reference so manager.removeVehicle() works properly
            car = copyCar((Car) launchingIntent.getSerializableExtra("Car object"));


            // Caching old values in in order to use it in an update query
            car.setRegistrationPlateCache(car.getRegistrationPlate());

            Bitmap carImage = ImageUtils.getImageForVehicle(car);
            carBtn.setImageBitmap(carImage);
            this.cameraBitmap = carImage;

            // Sets the car type for ex. : Sedan, Jeep ...
            switch (car.getCarType()) {
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
            switch (car.getEngineType()) {
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
            switch (car.getVignette().getType()) {
                case "week-vignette":
                    vignetteTypeSpinner.setSelection(0);
                    break;
                case "month-vignette":
                    vignetteTypeSpinner.setSelection(1);
                    break;
                case "annual-vignette":
                    vignetteTypeSpinner.setSelection(2);
                    break;
            }

            // Sets the insurance type
            switch (car.getInsurance().getTypeCount()) {
                case 1:
                    insuranceTypeSpinner.setSelection(3);
                    break;
                case 2:
                    insuranceTypeSpinner.setSelection(2);
                    break;
                case 3:
                    insuranceTypeSpinner.setSelection(1);
                    break;
                case 4:
                    insuranceTypeSpinner.setSelection(0);
                    break;
            }

            // Sets the vehicle brand
            brand.setText(car.getBrand());

            // Sets the car model
            model.setText(car.getModel());

            // Sets the tax amount
            taxAmount.setText(String.valueOf(car.getTax().getAmount()));

            // Sets the production year
            yearText.setText(String.valueOf(car.getProductionYear()));

            // Sets the range
            rangeText.setText(car.getKmRange());

            // Sets the registrationPlate
            registrationNumber.setText(car.getRegistrationPlate());

            // Sets the next Oil change
            oilET.setText(car.getNextOilChange());

            // Sets the insurance price
            insuranceAmmount.setText(String.valueOf(car.getInsurance().getPrice()));

           if (car.getTax().getEndDateAsCalendarObject().get(Calendar.YEAR) > 1900){
               taxDatePickerActivated = true;
           }
           pathToImage = car.getPathToImage();

        } else {
            // Initialize an empty car object
            car = new Car();
            pathToImage = null;
            photoURIFromCamera = null;
            this.cameraBitmap = null;
            saveBtn.setClickable(false);
        }


        carTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
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
                switch (i) {
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
                IVignette vignetteBeforeEdit = car.getVignette();
                switch (i) {
                    case 0:         //Weekly
                        vignette = new WeekVignette();
                        if(car.getVignette() != null) {
                            Calendar tmp = vignetteBeforeEdit.getStartDateAsCalender();
                            ((WeekVignette) vignette).setStartDate(tmp.get(Calendar.YEAR),
                                    tmp.get(Calendar.MONTH),
                                    tmp.get(Calendar.DAY_OF_MONTH));
                        }
                        break;
                    case 1:         //Monthly
                        vignette = new MonthVignette();
                        if(car.getVignette() != null) {
                            Calendar tmp = vignetteBeforeEdit.getStartDateAsCalender();
                            ((MonthVignette) vignette).setStartDate(tmp.get(Calendar.YEAR),
                                    tmp.get(Calendar.MONTH),
                                    tmp.get(Calendar.DAY_OF_MONTH));
                        }
                        break;
                    case 2:         //Annual
                        vignette = new AnnualVignette();
                        if(car.getVignette() != null) {
                            Calendar tmp = vignetteBeforeEdit.getStartDateAsCalender();
                            ((AnnualVignette) vignette).setStartDate(tmp.get(Calendar.YEAR),
                                    tmp.get(Calendar.MONTH),
                                    tmp.get(Calendar.DAY_OF_MONTH));
                        }
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
                switch (i) {
                    case 0:         // FOUR
                        car.getInsurance().setTypeCount(Insurance.Payments.FOUR);
                        break;
                    case 1:         // THREE
                        car.getInsurance().setTypeCount(Insurance.Payments.THREE);
                        break;
                    case 2:         // TWO
                        car.getInsurance().setTypeCount(Insurance.Payments.TWO);
                        break;
                    case 3:
                        car.getInsurance().setTypeCount(Insurance.Payments.ONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                car.getInsurance().setTypeCount(null);
            }
        });

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

                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            File photoFile = null;
                            try {
                                File directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                                photoFile = ImageUtils.createImageFile(directory.toString());
                            } catch (Exception ex) {
                                System.err.println("Something went wrong with creating file for image");
                                ex.printStackTrace();
                            }
                            if (photoFile != null) {
                                pathToImage = photoFile.getAbsolutePath();
                                photoURIFromCamera = FileProvider.getUriForFile(getApplicationContext(),
                                        "com.carcalendar.dmdev.carcalendar.fileprovider",photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURIFromCamera);
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAMERA);
                            }
                        }
                    }
                });
                /**
                 * This way is better because we enforce only images to be chosen, also there is no need to provide permission
                 * @see <a href="http://codetheory.in/android-pick-select-image-from-gallery-with-intents/">Gallery intent options</a>
                 */
                builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        // Show only images, no videos or anything else
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        // Always show the chooser (if there are multiple options available)
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_GALLERY);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!registrationNumber.getText().toString().isEmpty()) {
                    car.setRegistrationPlate(registrationNumber.getText().toString());
                } else {
                    registrationNumber.setError("Please enter a registration number!");
                    registrationNumber.requestFocus();
                }
                if (!brand.getText().toString().isEmpty()) {
                    car.setBrand(brand.getText().toString());
                } else {
                    brand.setError("Please input brand !!");
                    brand.requestFocus();
                }
                if (!model.getText().toString().isEmpty()) {
                    car.setModel(model.getText().toString());
                } else {
                    model.setError("Please input model !!");
                    model.requestFocus();
                }
                if (vignette == null) {
                    Toast.makeText(getApplicationContext(), "Please set vignette type", Toast.LENGTH_SHORT).show();
                    vignetteTypeSpinner.requestFocus();
                    return;
                } else {
                    car.setVignette(vignette);
                }
                if (car.getCarType() == null || car.getCarType().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please set car type", Toast.LENGTH_SHORT).show();
                    carTypeSpinner.requestFocus();
                    return;
                }
                if (car.getEngineType() == null || car.getEngineType().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please set engine type", Toast.LENGTH_SHORT).show();
                    engineTypeSpinner.requestFocus();
                    return;
                }
                if (!yearText.getText().toString().isEmpty()) {
                    if (Integer.valueOf(yearText.getText().toString()) < 0) {
                        yearText.setError("Please set year bigger or equal to 0");
                        yearText.requestFocus();
                        return;
                    } else {
                        car.setProductionYear(Integer.valueOf(yearText.getText().toString()));
                    }
                }
                if (!rangeText.getText().toString().isEmpty()) {
                    if (Integer.valueOf(rangeText.getText().toString()) < 0) {
                        rangeText.setError("Please set range bigger or equal to 0");
                        rangeText.requestFocus();
                        return;
                    } else {
                        car.setKmRange(rangeText.getText().toString());
                    }
                }

                if (!taxAmount.getText().toString().isEmpty()) {
                    car.setTax(Double.valueOf(taxAmount.getText().toString()));
                } else {
                    taxAmount.setError("Please input tax amount");
                    taxAmount.requestFocus();
                    return;
                }

                if (!taxDatePickerActivated) {
                    Toast.makeText(getApplicationContext(), "Please choose date for next tax payment!!!", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (car.getInsurance().getTypeForDB() == null){
                    Toast.makeText(view.getContext(),"Please choose insurance period !",Toast.LENGTH_SHORT).show();
                    insuranceTypeSpinner.requestFocus();
                }

                if(insuranceAmmount.getText() == null) {
                    Toast.makeText(view.getContext(),"Please choose insurance price !",Toast.LENGTH_SHORT).show();
                    insuranceAmmount.requestFocus();
                } else {
                    car.getInsurance().setPrice(Double.parseDouble(insuranceAmmount.getText().toString()));
                }

                if (cameraBitmap != null){
                    ImageUtils.mapImageToVehicle(car, cameraBitmap);
                }
                else {
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.car_add_image);
                    File picsDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    try {
                        String resourcePath = ImageUtils.saveBitmapImage(picsDir.getAbsolutePath(),bm);
                        car.setPathToImage(resourcePath);
                    } catch (Exception e) {
                        System.err.println("Problem in saving resource bitmap");
                        e.printStackTrace();
                    }
                    ImageUtils.mapImageToVehicle(car,bm);
                }

                if (oilET.getText().toString().isEmpty()){
                    oilET.setError("Please enter next oil change");
                    oilET.requestFocus();
                }
                else {
                    car.setNextOilChange(oilET.getText().toString());
                }

                DatabaseManager databaseManager = new DatabaseManager(getApplicationContext());
                try {
                    if(inEditMode){
                        if(databaseManager.insert(car, true) == -3) {
                            Toast.makeText(saveBtn.getContext(),"Vehicle not updated !",Toast.LENGTH_SHORT).show();
                        }

                        manager.removeVehicle((Vehicle) launchingIntent.getSerializableExtra("Car object"), false);

                    } else {
                        manager.addVehicleForDB(car);
                        //UserManager.saveDataUserManager(view.getContext(),manager);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    setResult(GarageActivity.SOMETHING_WENT_WRONG);
                    finish();
                }

                manager.addVehicle(car);
                setResult(GarageActivity.VEHICLE_ADDED_SUCCESSFULLY);
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = car.getPathToImage();
                if (!inEditMode && path != null && !path.isEmpty()) {
                    new File(car.getPathToImage()).delete();

                    car.setEngineType(null);
                    car.setCarType(null);
                    car.setKmRange(null);
                    car.setProductionYear(0);
                    car.setVignette(null);
                    car.setPathToImage(null);
                    car.setVignette(null);
                }

                setResult(GarageActivity.VEHICLE_ADD_CANCELED);
                finish();

            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.cameraBitmap != null){
            outState.putParcelable("camera",this.cameraBitmap);
        }
        else if (this.galleryBitmap != null){
            outState.putParcelable("gallery",this.galleryBitmap);
        }
        Log.d("saveInstance","onSaveInstanceState called in add car activity");
    }

    @Override
    public void onRestoreInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (outState.get("camera") != null){
            this.cameraBitmap = (Bitmap) outState.get("camera");
            carBtn.setImageBitmap(cameraBitmap);
        }
        else if (outState.get("gallery") != null){
            this.galleryBitmap = (Bitmap) outState.get("gallery");
            carBtn.setImageBitmap(galleryBitmap);
        }
        else {
            carBtn.setImageResource(getIntent().getIntExtra("Car", R.mipmap.car_add_image));
            cameraBitmap = null;
            galleryBitmap = null;
        }
        carBtn.refreshDrawableState();
        outState.clear();
    }

    @Override
    public void onBackPressed() {
        cancelBtn.callOnClick();
    }

    public void selectDate(View view) {
        switch (view.getId()) {
            case R.id.date_btn:
                DatePickerFragment dialogFragment = new DatePickerFragment();
                dialogFragment.setTag("vignetteDatePick");
                dialogFragment.show(getSupportFragmentManager(), "vignetteDatePick");
                return;
            case R.id.btn_next_payment:
                DatePickerFragment taxDateDialog = new DatePickerFragment();
                taxDateDialog.setTag("taxDatePick");
                taxDateDialog.show(getSupportFragmentManager(), "taxDatePick");
                taxDatePickerActivated = true;
                return;
            case R.id.btn_insurance_start:
                DatePickerFragment insuranceDateDialog = new DatePickerFragment();
                insuranceDateDialog.setTag("insuranceDatePick");
                insuranceDateDialog.show(getSupportFragmentManager(), "taxDatePick");
                return;

            default:
                return;
        }

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

       String tag = (String) datePicker.getTag();
        switch (tag){
            case "vignetteDatePick":
                Toast.makeText(datePicker.getContext(),"vignette",Toast.LENGTH_SHORT).show();
                if (vignette instanceof WeekVignette) {
                    ((WeekVignette) vignette).setStartDate(year, month, day);
                }
                if (vignette instanceof MonthVignette) {
                    ((MonthVignette) vignette).setStartDate(year, month, day);
                }
                if (vignette instanceof AnnualVignette) {
                    ((AnnualVignette) vignette).setStartDate(year, month, day);
                }
                break;
            case "taxDatePick":
                car.getTax().setEndDate(year,month,day);
                break;
            case "insuranceDatePick":
                car.getInsurance().setStartDate(year, month, day);
                break;
        }

    }

    // TODO : Implement onCancelDate for dialogFragments and distinguish different pickers(Tax,Vignette and etc.)
    @Override
    public void onCancelDate(DialogInterface dialog) {
        DatePickerDialog realDialog = (DatePickerDialog) dialog;
        String tag = (String)realDialog.getDatePicker().getTag();
        switch (tag){
            case "vignetteDatePick":
                if (vignette instanceof WeekVignette) {
                    ((WeekVignette) vignette).setStartDate(0, 0, 0);
                }
                if (vignette instanceof MonthVignette) {
                    ((MonthVignette) vignette).setStartDate(0, 0, 0);
                }
                if (vignette instanceof AnnualVignette) {
                    ((AnnualVignette) vignette).setStartDate(0, 0, 0);
                }
                break;
            case "taxDatePick":
                taxDatePickerActivated = false;
                car.getTax().setEndDate(-1,-1,-1);
                break;
            //TODO: after Insurance class is complete add insurance object to Vehicle object and set insurance to null here
            case "insuranceDatePick":
                car.getInsurance().setStartDate(-1,-1,-1);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                this.galleryBitmap = BitmapFactory.decodeStream(inputStream, null,null);
                new SaveAndLoadImage().execute(this.galleryBitmap);

            } catch (Exception e) {
                System.err.println("Problem in getting bitmap from gallery");
                e.printStackTrace();
            }
        }else if (requestCode == REQUEST_IMAGE_CAMERA && resultCode == RESULT_OK) {
            this.cameraBitmap = ImageUtils.getScaledBitmapFromPath(pathToImage, carBtn.getWidth(), carBtn.getHeight());
            String realPath = null;
            try {
                realPath = ImageUtils.saveBitmapImage(pathToImage, this.cameraBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            carBtn.setImageBitmap(this.cameraBitmap);
            car.setPathToImage(realPath);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //carBtn.getDrawingCache().recycle();
        Log.d("onDestroy","onDestroy add car activity called");
    }

    private class SaveAndLoadImage extends AsyncTask<Bitmap,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(Bitmap... voids) {
            File directoryPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            String pathToBitmap = null;
            Bitmap bm = null;
            try {
                pathToBitmap = ImageUtils.saveBitmapImage(directoryPath.getAbsolutePath(),voids[0]);
                car.setPathToImage(pathToBitmap);
                pathToImage = pathToBitmap;
                bm = BitmapFactory.decodeFile(pathToBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap != null){
                carBtn.setImageBitmap(bitmap);
                carBtn.refreshDrawableState();
                cameraBitmap = bitmap;
                saveBtn.setClickable(true);
            }
        }
    }

}

