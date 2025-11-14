package com.example.functionalprototype;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FilteredBuildingAdapter extends RecyclerView.Adapter<FilteredBuildingAdapter.ViewHolder> {

    private List<Building> buildings;

    public FilteredBuildingAdapter(List<Building> buildings) {
        this.buildings = buildings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_building, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Building building = buildings.get(position);

        holder.tvBuildingName.setText(building.building_name);
        holder.tvBuildingCleaniness.setText(building.cleanliness + "/5");
    }

    @Override
    public int getItemCount() {
        return buildings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvBuildingName, tvBuildingCleaniness, tvCafe;
        TextView tvMonday, tvTuesday, tvWednesday, tvThursday, tvFriday, tvSaturday, tvSunday;

        public ViewHolder(View itemView) {
            super(itemView);

            tvBuildingName = itemView.findViewById(R.id.tvBuildingName);
            tvBuildingCleaniness = itemView.findViewById(R.id.tvBuildingCleaniness);
        }
    }
}
