package com.example.functionalprototype;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Results extends AppCompatActivity {

    RecyclerView recyclerView;
    BuildingAdapter adapter;
    List<Building> buildingList;
    private float userLat;
    private float userLng;

    private float filterDistance;
    private boolean filterOpenNow;
    private boolean filterCafeFood;
    private String filterLocation;

    private View tutorialOverlay;
    private Button btnTutorialSkip;
    private Button btnTutorialGotIt;
    private boolean inTutorial = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        inTutorial = getIntent().getBooleanExtra(TutorialConstants.EXTRA_TOUR, false);

        // Set back button listener
        Button backButton = findViewById(R.id.back);
        //backButton.setOnClickListener(v -> {
            //Intent intent = new Intent(this, SpaceFiltering.class);
            //startActivity(intent);
        //});
        backButton.setOnClickListener(v -> {
            finish();
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        // Set menu button listener
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        Button menuButton = findViewById(R.id.menu_button);
        Button drawerBuildings = findViewById(R.id.buildings_list_button);
        Button drawerReport = findViewById(R.id.report_issue_button);

        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(Gravity.START));
        drawerBuildings.setOnClickListener(v -> {
            startActivity(new Intent(Results.this, BuildingList.class));
            drawerLayout.closeDrawer(Gravity.START);
        });

        drawerReport.setOnClickListener(v -> {
            startActivity(new Intent(Results.this, ReportFeature.class));
            drawerLayout.closeDrawer(Gravity.START);
        });

        // Set home button listener
        Button homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        tutorialOverlay = findViewById(R.id.tutorialOverlay);
        btnTutorialSkip = findViewById(R.id.btnTutorialSkip);
        btnTutorialGotIt = findViewById(R.id.btnTutorialGotIt);

        if (inTutorial) {
            tutorialOverlay.setVisibility(View.VISIBLE);
        } else {
            tutorialOverlay.setVisibility(View.GONE);
        }

        btnTutorialSkip.setOnClickListener(v -> {
            inTutorial = false;
            tutorialOverlay.setVisibility(View.GONE);
        });

        btnTutorialGotIt.setOnClickListener(v -> {
            tutorialOverlay.setVisibility(View.GONE);
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

            // --- NO RESULTS FOUND LOGIC ---
            if (buildingList.isEmpty()) {
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("No Results Found")
                        .setMessage("Try adjusting your distance or filter options.")
                        .setCancelable(false)
                        .setPositiveButton("Go Back", (dialog, which) -> {
                            // Go back to SpaceFiltering, keeping previous filter settings
                            finish();
                        })
                        .show();
            }

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
            if (inTutorial) {
                intent.putExtra(TutorialConstants.EXTRA_TOUR, true);
            }
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
        userLat = intent.getFloatExtra("user_lat", 0);
        userLng = intent.getFloatExtra("user_lng", 0);
        filterLocation = intent.getStringExtra("filter_location");


        Log.d("FilterDataFromIntent", "filterDistance="+filterDistance);
        Log.d("FilterDataFromIntent", "filterOpenNow="+filterOpenNow);
        Log.d("FilterDataFromIntent", "filterCafeFood="+filterCafeFood);
        Log.d("FilterDataFromIntent", "userLat="+userLat);
        Log.d("FilterDataFromIntent", "userLng="+userLng);
        Log.d("FilterDataFromIntent", "filterLocation="+filterLocation);
    }

    private ArrayList<Float> getBuildingLatLng(SQLiteDatabase db, String building_name) {
        ArrayList<Float> latLng = new ArrayList<Float>();
        Cursor cursor = db.rawQuery(String.format("SELECT building_name, latitude, longitude FROM building_hours WHERE building_name=\"%s\"", building_name), null);
        if (cursor.moveToFirst()) {
            do {
                latLng.add(cursor.getFloat(1));
                latLng.add(cursor.getFloat(2));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return latLng;
    }

    private List<Building> getFilteredBuildings(SQLiteDatabase db) {
        // ArrayList<Float> latLng = getBuildingLatLng(db, filterLocation);

        // Debugging (Hannah)
        /* if (latLng.isEmpty()) {
            Log.e("RESULTS", "No lat/lng found for building: " + filterLocation);

            // Prevent crash by using default lat and long for UIUC
            UserLat = 40.1106f;
            UserLng = -88.2284f;

            // You can return empty list or continue using default position
            return new ArrayList<>();
        }

        // -------

        UserLat = latLng.get(0);
        UserLng = latLng.get(1);

         */

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
                building.setUserLatLng(userLat, userLng);

                // Apply filters
                if (building.calculateDistanceFrom(userLat, userLng) < filterDistance
                        && (!filterCafeFood || building.cafe.equals("yes"))
                        && (!filterOpenNow || building.isOpenNow())) {
                    buildings.add(building);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        buildings.sort(Comparator.comparing(Building::calculateDistanceFromUserLatLng));
        if (buildings.size() > 5) {
            buildings.subList(5, buildings.size()).clear();
        }
        return buildings;
    }
}
