package com.carcalendar.dmdev.carcalendar.recycle;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.carcalendar.dmdev.carcalendar.R;

import java.util.List;

import model.UserManager;
import model.Vehicle.Car;
import model.Vehicle.Motorcycle;
import model.Vehicle.Vehicle;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleViewHolder> {

    private List<Vehicle> vehicleList;
    private boolean defocused = false;
    private final UserManager userManager = UserManager.getInstance();

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
        View itemView = holder.itemView;
        final int pos = position;
        // Opens a popup menu when an item is pressed
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(),v);
                // Handles the popup menu item's click
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.it_edit:
                                return true;
                            case R.id.it_diary:
                                return true;
                            case R.id.it_delete:
                                // updates the user manager and the Recycler View
                                userManager.removeVehicle(vehicleList.remove(pos));
                                notifyDataSetChanged();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.vehicle_options_menu, popup.getMenu());
                popup.show();

                return true;
            }
        });

        // Defocus Recycler view's items
        if (defocused){
            itemView.setBackgroundColor(0xBFFFFFFF);
            itemView.setClickable(false);
            itemView.setLongClickable(false);
        }else{
            itemView.setBackgroundColor(0xFF424242);
            itemView.setClickable(true);
            itemView.setLongClickable(true);
        }

        // set data from a vehicle object

        Car car = null;
        Motorcycle motorcycle = null;
        if(vehicleList.get(position) instanceof Car){
            car =(Car) vehicleList.get(position);

            holder.vehicleImage.setImageResource(car.getImage());
            holder.vehicleBrand.setText(car.getBrand());
            holder.vehicleModel.setText(car.getModel());
            holder.vehicleYear.setText(String.valueOf(car.getProductionYear()));
            holder.vehicleRange.setText(car.getKmRange());
        }
//        else{
//            motorcycle = (Motorcycle) vehicleList.get(position);
//
//            holder.vehicleImage.setImageResource(motorcycle.getImage());
//            holder.vehicleBrand.setText(motorcycle.getCarType());
//            holder.vehicleModel.setText(motorcycle.getModel());
//            holder.vehicleYear.setText(motorcycle.valueOf(car.getProductionYear()));
//            holder.vehicleRange.setText(motorcycle.getKmRange());
//
//        }
        }
        // set data from a vehicle object



    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    public void setItemDefocus(boolean defocused){
        this.defocused = defocused;
        notifyDataSetChanged();
    }

    public void updateVechicleList(Vehicle v){
        userManager.addVehicle(v);
        vehicleList = userManager.getRegisteredUserVehicles();      // Only this way the data will be sorted by the user TreeSet
        notifyDataSetChanged();
    }
}
