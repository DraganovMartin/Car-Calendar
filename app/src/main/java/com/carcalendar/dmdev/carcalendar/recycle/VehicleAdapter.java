package com.carcalendar.dmdev.carcalendar.recycle;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carcalendar.dmdev.carcalendar.R;

import java.util.List;

import model.Vehicle.Vehicle;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleViewHolder>{
    private List<Vehicle> vehicleList;

    public VehicleAdapter(List<Vehicle> vehicleList){
        this.vehicleList = vehicleList;
    }

    @Override
    public VehicleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.vehicle_view,parent,false);

        return new VehicleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VehicleViewHolder holder, int position) {
        Vehicle vechicle = vehicleList.get(position);
        // TODO set data from a vehicle object
        holder.vehicleImage.setImageResource(R.mipmap.ic_motorcycle_white_48dp);
        holder.vehicleName.setText("Renault megan");
        holder.vehicleDescription.setText("Very spacious and nice car.\nColor blue.");
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }
}
