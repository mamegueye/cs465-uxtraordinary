package com.example.functionalprototype;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Results extends AppCompatActivity {

    RecyclerView recyclerView;
    BuildingAdapter adapter;
    List<Building> buildingList;
    private float UserLat;
    private float UserLon;

    private float filterDistance;
    private boolean filterOpenNow;
    private boolean filterCafeFood;
    private String filterLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_list);

        recyclerView = findViewById(R.id.buildingRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get filter data from Intent
        getFilterDataFromIntent();

        // Get user location
        getUserLocation();

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        try {
            dbHelper.createDatabase();
            SQLiteDatabase db = dbHelper.openDatabase();

            // <<< ADD DEBUG HERE >>>
            Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            while (cursor.moveToNext()) {
                Log.d("DB_DEBUG", "Table found: " + cursor.getString(0));
            }
            cursor.close();

            buildingList = getFilteredBuildings(db);

        } catch (IOException e) {
            e.printStackTrace();
            buildingList = new ArrayList<>();
        }

        adapter = new BuildingAdapter(buildingList, building -> {
            Intent intent = new Intent(Results.this, BuildingDetail.class);
            intent.putExtra("building_name", building.building_name);
            intent.putExtra("monday", building.monday);
            intent.putExtra("tuesday", building.tuesday);
            intent.putExtra("wednesday", building.wednesday);
            intent.putExtra("thursday", building.thursday);
            intent.putExtra("friday", building.friday);
            intent.putExtra("saturday", building.saturday);
            intent.putExtra("sunday", building.sunday);
            intent.putExtra("cleanliness", building.cleanliness);
            intent.putExtra("latitude", building.latitude);
            intent.putExtra("longitude", building.longitude);
            intent.putExtra("cafe", building.cafe);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }

    // Method to get filter data from the intent
    private void getFilterDataFromIntent() {
        Intent intent = getIntent();
        filterDistance = intent.getFloatExtra("filter_distance", 0.0f);
        filterOpenNow = intent.getBooleanExtra("filter_open_now", false);
        filterCafeFood = intent.getBooleanExtra("filter_cafe_food", false);
        filterLocation = intent.getStringExtra("filter_location");
        Log.d("getFilterDataFromIntent", filterDistance + " " + filterOpenNow + " " + filterCafeFood + " " + filterLocation );
    }

    // Method to get user location
    private void getUserLocation() {
        // TODO: extract UserLat UserLon from filterLocation
        UserLat = 40.1106f;
        UserLon = -88.2073f;
    }

    private List<Building> getFilteredBuildings(SQLiteDatabase db) {
        List<Building> buildings = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM building_hours", null);

        if (cursor.moveToFirst()) {
            do {
                // Get building data from cursor
                String buildingName = cursor.getString(cursor.getColumnIndexOrThrow("building_name"));
                String monday = cursor.getString(cursor.getColumnIndexOrThrow("monday"));
                String tuesday = cursor.getString(cursor.getColumnIndexOrThrow("tuesday"));
                String wednesday = cursor.getString(cursor.getColumnIndexOrThrow("wednesday"));
                String thursday = cursor.getString(cursor.getColumnIndexOrThrow("thursday"));
                String friday = cursor.getString(cursor.getColumnIndexOrThrow("friday"));
                String saturday = cursor.getString(cursor.getColumnIndexOrThrow("saturday"));
                String sunday = cursor.getString(cursor.getColumnIndexOrThrow("sunday"));
                Float cleanliness = cursor.isNull(cursor.getColumnIndexOrThrow("cleanliness")) ? null : cursor.getFloat(cursor.getColumnIndexOrThrow("cleanliness"));
                Float latitude = cursor.isNull(cursor.getColumnIndexOrThrow("latitude")) ? null : cursor.getFloat(cursor.getColumnIndexOrThrow("latitude"));
                Float longitude = cursor.isNull(cursor.getColumnIndexOrThrow("longitude")) ? null : cursor.getFloat(cursor.getColumnIndexOrThrow("longitude"));
                String cafe = cursor.getString(cursor.getColumnIndexOrThrow("cafe"));

                // Create a Building object
                Building building = new Building(buildingName, monday, tuesday, wednesday, thursday, friday, saturday, sunday, cleanliness, latitude, longitude, cafe);

                // Apply filters
                if (isBuildingMatchingFilter(building)) {
                    buildings.add(building);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return buildings;
    }

    // Method to check if a building matches the filter criteria
    private boolean isBuildingMatchingFilter(Building building) {
        boolean matchesDistance = filterDistance < getDistance(building.latitude, building.longitude, UserLat, UserLon);
        boolean matchesOpenNow = filterOpenNow; // Add logic to check if the building is open now
        boolean matchesCafeFood = !filterCafeFood || (building.cafe != null && !building.cafe.isEmpty());
        boolean matchesLocation = filterLocation == null || building.building_name.contains(filterLocation); // Check if building name matches location filter

        return matchesDistance && matchesOpenNow && matchesCafeFood && matchesLocation;
    }

    private double getDistance(float lat1, float lng1, float lat2, float lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadius * c;
    }
}
