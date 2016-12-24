package com.carcalendar.dmdev.carcalendar.recycle;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carcalendar.dmdev.carcalendar.R;

public class VehicleViewHolder extends RecyclerView.ViewHolder {

    protected ImageView vehicleImage;
    protected TextView vehicleName;
    protected TextView vehicleYear;
    protected TextView vehicleRange;

    public VehicleViewHolder(View view){
        super(view);
        vehicleImage = (ImageView) view.findViewById(R.id.vehicle_image);
        vehicleName  = (TextView) view.findViewById(R.id.vehicle_name);
        vehicleYear = (TextView) view.findViewById(R.id.year_vehicle_view_TV);
        vehicleRange = (TextView) view.findViewById(R.id.range_vehicle_view_TV);
    }
}
