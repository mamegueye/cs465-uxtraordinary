package com.example.functionalprototype;

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
        public TextView name, monday;
        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            monday = view.findViewById(R.id.monday);
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
        holder.monday.setText("Cleanliness: " + (building.cleanliness != null ? building.cleanliness : "N/A"));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(building));
    }

    @Override
    public int getItemCount() {
        return buildingList.size();
    }
}