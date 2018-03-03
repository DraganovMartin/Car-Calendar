package com.carcalendar.dmdev.carcalendar.recycle;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.carcalendar.dmdev.carcalendar.R;

public class VehicleViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

    protected ImageView vehicleImage;
    protected TextView vehicleBrand;
    protected TextView vehicleModel;
    protected TextView vehicleYear;
    protected TextView vehicleRange;
    protected TextView regNumber;

    private OnRecyclerViewItemLongPressListener recyclerViewLongPressListener;
    private OnRecyclerViewItemClickListener recyclerViewItemClickListener;

    public VehicleViewHolder(View view,OnRecyclerViewItemLongPressListener rViewLongPressListener, OnRecyclerViewItemClickListener rViewClickListener){
        super(view);
        vehicleImage = (ImageView) view.findViewById(R.id.vehicle_image);
        vehicleBrand = (TextView) view.findViewById(R.id.vehicle_brand);
        vehicleModel = (TextView) view.findViewById(R.id.vehicle_model_view_TV);
        vehicleYear = (TextView) view.findViewById(R.id.year_vehicle_view_TV);
        vehicleRange = (TextView) view.findViewById(R.id.range_vehicle_view_TV);
        regNumber = (TextView) view.findViewById(R.id.regNumber);
        view.setOnLongClickListener(this);
        view.setOnClickListener(this);
        recyclerViewLongPressListener = rViewLongPressListener;
        recyclerViewItemClickListener = rViewClickListener;
    }

    @Override
    public boolean onLongClick(View v) {
        recyclerViewLongPressListener.onRecyclerViewItemLongPress(v,getAdapterPosition());
        return true;
    }

    @Override
    public void onClick(View view) {
        recyclerViewItemClickListener.onRecyclerViewClick(view,getAdapterPosition());
    }

    /**
     * This interface provides the means for handling long click events on recycler view items
     */
    public interface OnRecyclerViewItemLongPressListener{
        /**
         *
         * @param v the view on which the user long pressed
         * @param pos the position of <code>v</code> in the recycler view
         */
        void onRecyclerViewItemLongPress(View v,final int pos);
    }

    public  interface  OnRecyclerViewItemClickListener{
        void onRecyclerViewClick(View view,final int pos);
    }
}
