package com.example.functionalprototype;

import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        public TextView name, distance, cleanliness, capacity, hours;
        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tvBuildingName);
            // distance = view.findViewById(R.id.tvBuildingDistance);
            cleanliness = view.findViewById(R.id.tvBuildingCleanliness);
            capacity = view.findViewById(R.id.tvBuildingCapacity);
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
        // holder.distance.setText("");
        holder.cleanliness.setText(" "+(building.cleanliness != null ? building.cleanliness : "N/A"));
        holder.capacity.setText("  N/A");
        holder.hours.setText("  " + getOpeningHours(building));
        if (matchOpenNow(building)) {
            holder.hours.setTextColor(Color.GREEN);
        } else {
            holder.hours.setTextColor(Color.WHITE);
        }
        holder.itemView.setOnClickListener(v -> listener.onItemClick(building));
    }

    @Override
    public int getItemCount() {
        return buildingList.size();
    }

    private boolean matchOpenNow(Building building) {
        String openingHours = getOpeningHours(building);

        // If building is locked or closed today
        if (openingHours == null || openingHours.equalsIgnoreCase("LOCKED") || !openingHours.contains("-")) {
            return false;
        }

        LocalTime currentTime = LocalTime.now();
        ArrayList<LocalTime> buildingTimes = parseOpeningHours(openingHours);

        return !(currentTime.isBefore(buildingTimes.get(0)) || currentTime.isAfter(buildingTimes.get(1)));
    }

    private ArrayList<LocalTime> parseOpeningHours(String openingHours) {
        ArrayList<LocalTime> times = new ArrayList<>();

        if (openingHours == null || openingHours.equalsIgnoreCase("LOCKED")) {
            return times; // empty list
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[hh:mma][h:mma][hha][ha]");
        for (String s : openingHours.split("-")) {
            s = s.trim();
            times.add(LocalTime.parse(s, formatter));
        }
        return times;
    }

    private String getOpeningHours(Building building) {
        int currentDayOfWeek = LocalDate.now().getDayOfWeek().getValue();
        String[] openingHours = {building.sunday, building.monday, building.tuesday, building.wednesday, building.thursday, building.friday, building.saturday};
        return openingHours[currentDayOfWeek];
    }
}