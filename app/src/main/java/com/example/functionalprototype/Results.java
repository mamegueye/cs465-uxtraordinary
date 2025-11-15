package com.example.functionalprototype;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;
import java.text.SimpleDateFormat;

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
        setContentView(R.layout.activity_results);

        // Set back button listener
        Button backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SpaceFiltering.class);
            startActivity(intent);
        });

        // Set menu button listener
        Button menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, Menu.class);
            startActivity(intent);
        });

        // Set home button listener
        Button homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        recyclerView = findViewById(R.id.buildingRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get filter data from Intent
        getFilterDataFromIntent();

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
        filterDistance = intent.getFloatExtra("filter_distance", 0.5f);
        filterOpenNow = intent.getBooleanExtra("filter_open_now", false);
        filterCafeFood = intent.getBooleanExtra("filter_cafe_food", false);
        filterLocation = intent.getStringExtra("filter_location");
        Log.d("FilterDataFromIntent", "filterDistance="+filterDistance);
        Log.d("FilterDataFromIntent", "filterOpenNow="+filterOpenNow);
        Log.d("FilterDataFromIntent", "filterCafeFood="+filterCafeFood);
        Log.d("FilterDataFromIntent", "filterLocation="+filterLocation);
    }

    // Method to get user location
    private void getUserLonLat(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT building_name, latitude, longitude FROM building_hours", null);
        if (cursor.moveToFirst()) {
            do {
                UserLat = cursor.isNull(cursor.getColumnIndexOrThrow("latitude")) ? null : cursor.getFloat(cursor.getColumnIndexOrThrow("latitude"));
                UserLon = cursor.isNull(cursor.getColumnIndexOrThrow("longitude")) ? null : cursor.getFloat(cursor.getColumnIndexOrThrow("longitude"));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private List<Building> getFilteredBuildings(SQLiteDatabase db) {
        getUserLonLat(db);

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
                if (matchDistance(building) && (!filterCafeFood || matchCafeFood(building)) && (!filterOpenNow || matchOpenNow(building))) {
                    buildings.add(building);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return buildings;
    }

    private boolean matchDistance(Building building) {
        // Compare distance
        double buildingDistance = calculateDistance(building.latitude, building.longitude, UserLat, UserLon);
        if (buildingDistance > filterDistance) {
            Log.d("MatchFailed", building.building_name + " is " + buildingDistance + " miles away.");
            return false;
        }
        return true;
    }

    private boolean matchOpenNow(Building building) {
        LocalTime currentTime = LocalTime.now();
        int currentDayOfWeek = LocalDate.now().getDayOfWeek().getValue();
        String[] openingHours = {building.sunday, building.monday, building.tuesday, building.wednesday, building.thursday, building.friday, building.saturday};
        ArrayList<LocalTime> buildingTimes = parseOpeningHours(openingHours[currentDayOfWeek]);
        if (currentTime.isBefore(buildingTimes.get(0)) || currentTime.isAfter(buildingTimes.get(1))) {
            Log.d("MatchFailed", building.building_name + " is opened from " + buildingTimes.get(0) + " to " + buildingTimes.get(1));
            return false;
        }
        return true;
    }

    private boolean matchCafeFood(Building building) {
        if (building.cafe == null || building.cafe.isEmpty()) {
            Log.d("MatchFailed", building.building_name + "does not have a cafe.");
            return false;
        }
        return true;
    }

    private double calculateDistance(float lat1, float lng1, float lat2, float lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distanceInMeters = earthRadius * c;
        return distanceInMeters / 1609.34;
    }

    // Extract hours as LocalTimes
    private ArrayList<LocalTime> parseOpeningHours(String openingHours) {
        ArrayList<LocalTime> times = new ArrayList<>();
        for (String s : openingHours.split("-")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[hh:mma][h:mma][hha][ha]");
            times.add(LocalTime.from(formatter.parse(s)));
        }
        return times;
    }
}
