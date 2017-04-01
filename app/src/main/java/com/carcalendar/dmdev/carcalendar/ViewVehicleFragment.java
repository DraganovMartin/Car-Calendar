package com.carcalendar.dmdev.carcalendar;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import model.Vehicle.Car;
import model.Vehicle.Motorcycle;
import model.Vehicle.Vehicle;
import model.util.ImageUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewVehicleFragment extends Fragment {
    private ImageView imageView;
    private TextView registation;
    private TextView brand;
    private TextView model;
    private TextView type;
    private TextView engine;
    private TextView year;
    private TextView rangeKM;
    private TextView vignetteStartDate;
    private TextView vignetteEndDate;
    private TextView nextOilChange;
    private TextView vehicleTax;
    private TextView nextTaxPayment;
    private TextView insuranceAmount;
    private TextView insuranceStartDay;
    private TextView insuranceEndDay;

    private Vehicle vehicle;
    public ViewVehicleFragment() {
        // Required empty public constructor
    }

    public static ViewVehicleFragment newInstance(Vehicle vehicle){
        ViewVehicleFragment fragment = new ViewVehicleFragment();
        if(vehicle!= null) {
            Bundle data = new Bundle();
            data.putSerializable("vehicle", vehicle);
            fragment.setArguments(data);
        }
        return fragment;
    }

    private void updateUI(Vehicle vehicle){
        Car car = null;
        Motorcycle motorcycle = null;
        if (vehicle instanceof Car){
            car = (Car) vehicle;
        }
        else motorcycle = (Motorcycle) vehicle;
        if(car != null){
            imageView.setImageBitmap(ImageUtils.getScaledBitmapFromPath(car.getPathToImage(),imageView.getWidth(),imageView.getHeight()));
            registation.setText(car.getRegistrationPlate());
            brand.setText(car.getBrand());
            model.setText(car.getModel());
            type.setText(car.getCarType());
            engine.setText(car.getEngineType());
            year.setText(String.valueOf(car.getProductionYear()));
            rangeKM.setText(car.getKmRange());
            vignetteStartDate.setText(car.getVignette().getStartDate());
            vignetteEndDate.setText(car.getVignette().getEndDate());
            // TODO : in Car class add two fields one representing when to change in km's(122000 - 138000) which is current edit text and statically add Calendar field with +1 year of when the object is created see Vignette class
            vehicleTax.setText(String.valueOf(car.getTax().getAmount()));
            // TODO : in Car class add Calendar object representing next tax payment
            // TODO : in Stickers create another class named Insurance with name and price and start and end Calendar objects and add object in Car

        }else if(motorcycle!=null){
            imageView.setImageBitmap(ImageUtils.getScaledBitmapFromPath(motorcycle.getPathToImage(),imageView.getWidth(),imageView.getHeight()));
            registation.setText(motorcycle.getRegistrationPlate());
            brand.setText(motorcycle.getBrand());
            model.setText(motorcycle.getModel());
            type.setText(motorcycle.getMotorcycleType());
            engine.setText(motorcycle.getEngineType());
            year.setText(String.valueOf(motorcycle.getProductionYear()));
            rangeKM.setText(motorcycle.getKmRange());
            // TODO : in Car class add two fields one representing when to change in km's(122000 - 138000) which is current edit text and statically add Calendar field with +1 year of when the object is created see Vignette class
            vehicleTax.setText(String.valueOf(motorcycle.getTax().getAmount()));
            // TODO : in Car class add Calendar object representing next tax payment
            // TODO : in Stickers create another class named Insurance with name and price and start and end Calendar objects and add object in Car
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && getArguments().get("vehicle") != null){
            vehicle = (Vehicle) getArguments().get("vehicle");
        }
        else {
            Log.e("vehicle","vehicle in fragment is null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.view_vehicle_fragment_layout, container, false);
        imageView = (ImageView) fragmentView.findViewById(R.id.carImage);
        registation = (TextView) fragmentView.findViewById(R.id.regNumPlate);
        brand = (TextView) fragmentView.findViewById(R.id.vehicle_brand_TV);
        model = (TextView) fragmentView.findViewById(R.id.vehicle_model_real);
        type = (TextView) fragmentView.findViewById(R.id.type_carTV);
        engine = (TextView) fragmentView.findViewById(R.id.car_engineTV);
        year = (TextView) fragmentView.findViewById(R.id.yearTVVehicle);
        rangeKM = (TextView) fragmentView.findViewById(R.id.rangeTVVehicle);
        vignetteStartDate = (TextView) fragmentView.findViewById(R.id.vignette_startTV);
        vignetteEndDate = (TextView) fragmentView.findViewById(R.id.dateTV);
        nextOilChange = (TextView) fragmentView.findViewById(R.id.oilChangeTV);
        vehicleTax = (TextView) fragmentView.findViewById(R.id.tax_amount_TV);
        nextTaxPayment = (TextView) fragmentView.findViewById(R.id.tax_payment_atTV);
        insuranceAmount = (TextView) fragmentView.findViewById(R.id.insurance_amount_TV);
        insuranceStartDay = (TextView) fragmentView.findViewById(R.id.insurance_start_TV);
        insuranceEndDay = (TextView) fragmentView.findViewById(R.id.insurance_end_TV);
        updateUI(vehicle);


        return fragmentView;
    }

}
