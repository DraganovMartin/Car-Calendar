package com.carcalendar.dmdev.carcalendar;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

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

    private TextView vignetteStartLbl;
    private TextView vignetteEndLbl;
    private TextView vignettePriceLbl;
    private TextView vignettePrice;

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
        else {
            motorcycle = (Motorcycle) vehicle;
            vignetteStartDate.setVisibility(View.GONE);
            vignetteEndDate.setVisibility(View.GONE);
            vignetteStartLbl.setVisibility(View.GONE);
            vignetteEndLbl.setVisibility(View.GONE);
            vignettePriceLbl.setVisibility(View.GONE);
            vignettePrice.setVisibility(View.GONE);


        }
        if(car != null){
            if (car.getPathToImage() != null) {
                imageView.setImageBitmap(ImageUtils.getScaledBitmapFromPath(car.getPathToImage(), imageView.getWidth(), imageView.getHeight()));
            }
            else imageView.setImageResource(R.mipmap.car_add_image);
                registation.setText(car.getRegistrationPlate());
                brand.setText(car.getBrand());
                model.setText(car.getModel());
                type.setText(car.getCarType());
                engine.setText(car.getEngineType());
                year.setText(String.valueOf(car.getProductionYear()));
                rangeKM.setText(car.getKmRange());
                vignetteStartDate.setText(car.getVignette().getStartDate());
                vignetteEndDate.setText(car.getVignette().getEndDate());
                vignettePrice.setText(String.valueOf(car.getVignette().getPrice()));
                insuranceAmount.setText(String.valueOf(car.getInsurance().getPrice()));
                insuranceStartDay.setText(car.getInsurance().getStartDate());
                insuranceEndDay.setText(new SimpleDateFormat("yyyy-MM-dd").format(car.getInsurance().getActiveEndDate().getTime()));
                vehicleTax.setText(String.valueOf(car.getTax().getAmount()));
                nextOilChange.setText(car.getNextOilChange());
                nextTaxPayment.setText(car.getTax().getEndDate());

        }else if(motorcycle!=null){
            if (motorcycle.getPathToImage() != null) {
                imageView.setImageBitmap(ImageUtils.getScaledBitmapFromPath(motorcycle.getPathToImage(), imageView.getWidth(), imageView.getHeight()));
            }
            else imageView.setImageResource(R.mipmap.motorcycle_black);
            registation.setText(motorcycle.getRegistrationPlate());
            brand.setText(motorcycle.getBrand());
            model.setText(motorcycle.getModel());
            type.setText(motorcycle.getMotorcycleType());
            engine.setText(motorcycle.getEngineType());
            year.setText(String.valueOf(motorcycle.getProductionYear()));
            rangeKM.setText(motorcycle.getKmRange());
            vehicleTax.setText(String.valueOf(motorcycle.getTax().getAmount()));
            insuranceAmount.setText(String.valueOf(motorcycle.getInsurance().getPrice()));
            insuranceStartDay.setText(motorcycle.getInsurance().getStartDate());
            insuranceEndDay.setText(new SimpleDateFormat("yyyy-MM-dd").format(motorcycle.getInsurance().getActiveEndDate().getTime()));
            nextOilChange.setText(motorcycle.getNextOilChange());
            nextTaxPayment.setText(motorcycle.getTax().getEndDate());
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
        vignetteStartLbl = (TextView) fragmentView.findViewById(R.id.vignette_start);
        vignetteEndLbl = (TextView) fragmentView.findViewById(R.id.vignette_end_TV);
        vignetteStartDate = (TextView) fragmentView.findViewById(R.id.vignette_startTV);
        vignetteEndDate = (TextView) fragmentView.findViewById(R.id.dateTV);
        nextOilChange = (TextView) fragmentView.findViewById(R.id.oilChangeTV);
        vehicleTax = (TextView) fragmentView.findViewById(R.id.tax_amount_TV);
        nextTaxPayment = (TextView) fragmentView.findViewById(R.id.tax_payment_atTV);
        insuranceAmount = (TextView) fragmentView.findViewById(R.id.insurance_amount_TV);
        insuranceStartDay = (TextView) fragmentView.findViewById(R.id.insurance_start_TV);
        insuranceEndDay = (TextView) fragmentView.findViewById(R.id.insurance_end_TV);
        vignettePriceLbl = (TextView) fragmentView.findViewById(R.id.vignette_price_TV);
        vignettePrice = (TextView) fragmentView.findViewById(R.id.vignettePriceTV);

        updateUI(vehicle);


        return fragmentView;
    }

}
