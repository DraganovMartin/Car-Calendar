package com.carcalendar.dmdev.carcalendar.recycle;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carcalendar.dmdev.carcalendar.R;

public class VehicleViewHolder extends RecyclerView.ViewHolder {
    protected ImageView vehicleImage;
    protected TextView vehicleName;
    protected TextView vehicleDescription;

    public VehicleViewHolder(View view){
        super(view);
        vehicleImage = (ImageView) view.findViewById(R.id.vehicle_image);
        vehicleName  = (TextView) view.findViewById(R.id.vehicle_name);
        vehicleDescription = (TextView) view.findViewById(R.id.vehicle_description);
    }
}
