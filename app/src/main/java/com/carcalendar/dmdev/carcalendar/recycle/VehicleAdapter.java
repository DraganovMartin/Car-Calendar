package com.carcalendar.dmdev.carcalendar.recycle;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carcalendar.dmdev.carcalendar.R;

import java.util.List;

import model.UserManager;
import model.Vehicle.Car;
import model.Vehicle.Vehicle;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleViewHolder> {

    private List<Vehicle> vehicleList;
    private boolean defocused = false;
    private final UserManager userManager = UserManager.getInstance();
    private VehicleViewHolder.OnRecyclerViewItemLongPressListener lPressListener;

    public VehicleAdapter(List<Vehicle> vehicleList, VehicleViewHolder.OnRecyclerViewItemLongPressListener lPressListener){
        this.vehicleList = vehicleList;
        this.lPressListener = lPressListener;
    }

    @Override
    public VehicleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.vehicle_view,parent,false);

        // Pass the long click listener to the constructor
        return new VehicleViewHolder(itemView,lPressListener);
    }

    @Override
    public void onBindViewHolder(VehicleViewHolder holder, int position) {
        View itemView = holder.itemView;

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

        Vehicle vehicle = vehicleList.get(position);
        Car car = null;
        if(vehicleList.get(position) instanceof Car){
            car =(Car) vehicleList.get(position);
        }
        // TODO set data from a vehicle object
        holder.vehicleImage.setImageResource(car.getImage());
        holder.vehicleName.setText(car.getCarType());
        holder.vehicleYear.setText(String.valueOf(car.getProductionYear()));
        holder.vehicleRange.setText(car.getKmRange());
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    /**
     * Makes the items in the recycler view defocused.
     * When the <code>defocused</code> parameter is se to true the items of the recycler view
     * become un-clickable.
     *
     * @param defocused the flag that tells if the view should be defocused
     */
    public void setItemDefocus(boolean defocused){
        this.defocused = defocused;
        notifyDataSetChanged();
    }

    /**
     * Updates the recycler view with a new vehicle.
     *
     * The vehicle is also added to the current logged users's vehicle list
     *
     * @param v the <code>Vehicle</code> object to be put in the list
     */
    public void updateVehicleList(Vehicle v){
        userManager.addVehicle(v);
        vehicleList.add(v);
        notifyDataSetChanged();
    }
    /**
     * Deletes an item from the recycler view fro the specified <code>pos</code>.
     *
     * The vehicle is also delete from the current logged users's vehicle list
     *
     * @param pos the position of the item in the recycler view
     */
    public void deleteItemFromList(int pos){
        userManager.removeVehicle(vehicleList.remove(pos));
        notifyDataSetChanged();
    }
}
