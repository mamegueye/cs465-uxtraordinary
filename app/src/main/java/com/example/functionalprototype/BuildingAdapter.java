package com.example.functionalprototype;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.ViewHolder> {

    private List<Building> buildingList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Building building);
    }

    public BuildingAdapter(List<Building> buildings, OnItemClickListener listener) {
        this.buildingList = buildings;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, distance, cleanliness, cafe, hours;
        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tvBuildingName);
            distance = view.findViewById(R.id.tvBuildingDistance);
            cleanliness = view.findViewById(R.id.tvBuildingCleanliness);
            cafe = view.findViewById(R.id.tvBuildingCafe);
            hours = view.findViewById(R.id.tvBuildingHours);
        }
    }

    @Override
    public BuildingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.building_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Building building = buildingList.get(position);
        holder.name.setText(building.building_name);
        if (building.hasUserLatLng()) {
            holder.distance.setText(String.format("%.1f", building.calculateDistanceFromUserLatLng()) + " miles");
        }
        holder.cleanliness.setText(" "+(building.cleanliness != null ? building.cleanliness : "N/A"));
        holder.cafe.setVisibility(building.cafe.equals("yes") ? VISIBLE : INVISIBLE);
        holder.hours.setText("  " + building.getHoursToday());
        holder.hours.setTextColor(building.isOpenNow() ? Color.GREEN : Color.WHITE);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(building));
    }

    @Override
    public int getItemCount() {
        return buildingList.size();
    }
}