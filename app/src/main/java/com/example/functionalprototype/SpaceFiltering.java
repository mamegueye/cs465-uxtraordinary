package com.example.functionalprototype;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Space;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class SpaceFiltering extends AppCompatActivity
    implements View.OnClickListener {

    // Hannah
    private Button applyButton;
    private Button backButton;
    private Button homeButton;
    private TextView locationPlaceholder;

    // From Sanjit's Code
    // UI Components
    private TextView tvCurrentLocation;
    private TextView tvDistanceValue;
    private SeekBar seekBarDistance;
    private CheckBox checkBoxOpenNow;
    private CheckBox checkBoxCafeFood;
    private Button btnApply;

    // Filter values stored in class variables for database querying
    private float currentDistanceMiles = 0.5f;
    private boolean isOpenNow = false;
    private boolean hasCafeFood = false;
    private String currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_filtering);

        // Set menu button listener
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        Button menuButton = findViewById(R.id.menu_button);
        Button drawerBuildings = findViewById(R.id.buildings_list_button);
        Button drawerReport = findViewById(R.id.report_issue_button);

        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(Gravity.START));
        drawerBuildings.setOnClickListener(v -> {
            startActivity(new Intent(SpaceFiltering.this, BuildingList.class));
            drawerLayout.closeDrawer(Gravity.START);
        });

        drawerReport.setOnClickListener(v -> {
            startActivity(new Intent(SpaceFiltering.this, ReportFeature.class));
            drawerLayout.closeDrawer(Gravity.START);
        });

        // Hannah Code
        // Grab the user's location preference from intent
        Intent intent_received = getIntent();
        String location_preference = intent_received.getStringExtra("location_preference");
        currentLocation = location_preference;

        // Grab XML location to replace
        locationPlaceholder = findViewById(R.id.tvCurrentLocation);

        // Replace XML placeholder with intent data
        if (location_preference != null && !location_preference.isEmpty()) {
            locationPlaceholder.setText(location_preference);
        } else {
            locationPlaceholder.setText("Location Not Available");
        }

        // The user has pressed the 'Back' Button
        backButton = (Button) findViewById(R.id.back_button_filtering);
        backButton.setOnClickListener(this);

        // The user has pressed the 'Home' Button
        homeButton = (Button) findViewById(R.id.home_button);
        homeButton.setOnClickListener(this);


        // Sanjit's Code:
        // Initialize all UI components
        initializeViews();

        // Set up event listeners
        setupListeners();
    }

    // Hannah
    // For when the user presses the 'Next' Button or 'Back' Button
    public void onClick(View v) {

            // User pressed BACK BUTTON
        if (v.getId() == R.id.back_button_filtering) {
            Intent intent = new Intent(this, LocationRequest.class);
            startActivity(intent);

            // User pressed HOME BUTTON
        } else if (v.getId() == R.id.home_button) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }

    // Everything below is Sanjit's Code
    /**
     * Initialize all UI components by connecting XML views to Java variables
     */
    private void initializeViews() {
        tvCurrentLocation = findViewById(R.id.tvCurrentLocation);
        tvDistanceValue = findViewById(R.id.tvDistanceValue);
        seekBarDistance = findViewById(R.id.seekBarDistance);
        checkBoxOpenNow = findViewById(R.id.checkBoxOpenNow);
        checkBoxCafeFood = findViewById(R.id.checkBoxCafeFood);
        btnApply = findViewById(R.id.btnApply);

        // Set initial values
        tvCurrentLocation.setText(currentLocation);
        updateDistanceDisplay(currentDistanceMiles);
    }

    /**
     * Set up listeners for SeekBar, CheckBoxes, and Apply button
     */
    private void setupListeners() {
        // SeekBar listener for distance filter
        // SeekBar max is 10, each increment represents 0.5 miles (0.0 to 5.0 miles)
        seekBarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Convert progress (0-10) to miles (0.0-5.0) with 0.5 increments
                currentDistanceMiles = progress * 0.5f;
                updateDistanceDisplay(currentDistanceMiles);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed for this implementation
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed for this implementation
            }
        });

        // CheckBox listener for "Open Now" filter
        checkBoxOpenNow.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isOpenNow = isChecked;
        });

        // CheckBox listener for "Cafe/Food" facility filter
        checkBoxCafeFood.setOnCheckedChangeListener((buttonView, isChecked) -> {
            hasCafeFood = isChecked;
        });

        // Apply button click listener
        btnApply.setOnClickListener(v -> onApplyClicked());
    }

    /**
     * Update the distance display TextView with current value
     * @param distance Distance in miles
     */
    private void updateDistanceDisplay(float distance) {
        tvDistanceValue.setText(String.format("%.1f miles", distance));
    }

    /**
     * Collect all filter values and prepare to pass to results activity
     * This will be called when user clicks the Apply button
     */
    private void onApplyClicked() {
        // Get the current filter criteria
        FilterCriteria criteria = getFilterCriteria();

        Intent intent = new Intent(SpaceFiltering.this, Results.class);
        intent.putExtra("filter_distance", criteria.getDistanceMiles());
        intent.putExtra("filter_open_now", criteria.isOpenNow());
        intent.putExtra("filter_cafe_food", criteria.isHasCafeFood());
        intent.putExtra("filter_location", criteria.getCurrentLocation());
        startActivity(intent);

        // For now, just log the criteria (or show a Toast for debugging)
        Log.d("FilterActivity", "Filter applied: " + criteria.toString());

        // Placeholder: Return to previous activity
        finish();
    }

    /**
     * Return a FilterCriteria object with current filter selections
     * This object will be used for database queries
     * @return FilterCriteria object containing all current filter values
     */
    public FilterCriteria getFilterCriteria() {
        return new FilterCriteria(
                currentDistanceMiles,
                isOpenNow,
                hasCafeFood,
                currentLocation
        );
    }

    /**
     * Optional: Method to update current location if needed
     * This can be called when GPS location is updated or user selects a different location
     * @param location New location name
     */
    public void setCurrentLocation(String location) {
        this.currentLocation = location;
        if (tvCurrentLocation != null) {
            tvCurrentLocation.setText(location);
        }
    }
}