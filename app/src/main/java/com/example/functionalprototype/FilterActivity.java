package com.example.functionalprototype;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * FilterActivity allows users to set filter criteria for finding study spaces
 * Filters include: distance, open now status, and cafe/food facilities
 */
public class FilterActivity extends AppCompatActivity {

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
    private String currentLocation = "Thomas M. Siebel Center for Computer Science";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        // Initialize all UI components
        initializeViews();

        // Set up event listeners
        setupListeners();
    }

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

        // TODO: When ResultsActivity is implemented, uncomment the following:
        // Intent intent = new Intent(FilterActivity.this, ResultsActivity.class);
        // intent.putExtra("filter_distance", criteria.getDistanceMiles());
        // intent.putExtra("filter_open_now", criteria.isOpenNow());
        // intent.putExtra("filter_cafe_food", criteria.isHasCafeFood());
        // intent.putExtra("filter_location", criteria.getCurrentLocation());
        // startActivity(intent);

        // For now, just log the criteria (or show a Toast for debugging)
        // Log.d("FilterActivity", "Filter applied: " + criteria.toString());

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