package com.carcalendar.dmdev.carcalendar.recycle;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carcalendar.dmdev.carcalendar.R;

import java.util.List;

import model.UserManager;
import model.Vehicle.Car;
import model.Vehicle.Motorcycle;
import model.Vehicle.Vehicle;
import model.util.ImageUtils;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleViewHolder> {

    private List<Vehicle> vehicleList;
    private final UserManager userManager = UserManager.getInstance();
    private VehicleViewHolder.OnRecyclerViewItemLongPressListener lPressListener;
    private VehicleViewHolder.OnRecyclerViewItemClickListener clickListener;

    public VehicleAdapter(List<Vehicle> vehicleList, VehicleViewHolder.OnRecyclerViewItemLongPressListener lPressListener, VehicleViewHolder.OnRecyclerViewItemClickListener clickListener){
        this.vehicleList = vehicleList;
        this.lPressListener = lPressListener;
        this.clickListener = clickListener;
    }

    @Override
    public VehicleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.vehicle_item_view,parent,false);

        // Pass the long click listener to the constructor
        return new VehicleViewHolder(itemView,lPressListener,clickListener);
    }

    @Override
    public void onBindViewHolder(VehicleViewHolder holder, int position) {

        Car car = null;
        Motorcycle motorcycle = null;
        if (vehicleList.get(position) instanceof Car) {
            car = (Car) vehicleList.get(position);
            // Below line works but not for production, only for Bai Martin
            if (car.getPathToImage() != null) {
                holder.vehicleImage.setImageBitmap(ImageUtils.getBitmapFromPath(car.getPathToImage()));
            }
            else holder.vehicleImage.setImageResource(R.mipmap.car_add_image);
            holder.vehicleBrand.setText(car.getBrand());
            holder.vehicleModel.setText(car.getModel());
            holder.vehicleYear.setText(String.valueOf(car.getProductionYear()));
            holder.vehicleRange.setText(car.getKmRange());
            holder.regNumber.setText(car.getRegistrationPlate());
        } else {
            motorcycle = (Motorcycle) vehicleList.get(position);
            if (motorcycle.getPathToImage() != null) {
                holder.vehicleImage.setImageBitmap(ImageUtils.getBitmapFromPath(motorcycle.getPathToImage()));
            }
            else  holder.vehicleImage.setImageResource(R.mipmap.motorcycle_black);
            holder.vehicleBrand.setText(motorcycle.getBrand());
            holder.vehicleModel.setText(motorcycle.getModel());
            holder.vehicleYear.setText(String.valueOf(motorcycle.getProductionYear()));
            holder.vehicleRange.setText(motorcycle.getKmRange());
            holder.regNumber.setText(motorcycle.getRegistrationPlate());
        }
        // set data from a vehicle object
    }


    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    /**
     * Updates the recycler view with a new vehicle.
     *
     * The vehicle is also added to the current logged users's vehicle list
     *
     */

    // Because of alphabetical sort when adding vehicle we should rebind all items in the recyclerView
    public void updateVehicleList(){
        vehicleList = userManager.getRegisteredUserVehicles();      // Only this way the data will be sorted by the user TreeSet
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
        Vehicle v = vehicleList.remove(pos);
        userManager.removeVehicle(v,true);
        userManager.removeVehicleForDB(v);
        notifyItemRemoved(pos);
    }
}