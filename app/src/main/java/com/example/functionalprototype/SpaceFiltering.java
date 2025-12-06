package com.example.functionalprototype;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Space;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

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
    private float currentLat = 0;
    private float currentLng = 0;
    private String currentLocation;

    private View tutorialOverlay;
    private Button btnTutorialSkip;
    private Button btnTutorialGotIt;
    private boolean inTutorial = false;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private static final int PERMISSIONS_REQUEST_LOCATION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_filtering);

        Intent intent = getIntent();
        inTutorial = intent.getBooleanExtra(TutorialConstants.EXTRA_TOUR, false);

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
        createLocationCallback();
        startLocationUpdates();

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
    }

    // Hannah
    // For when the user presses the 'Next' Button or 'Back' Button
    public void onClick(View v) {

        // User pressed BACK BUTTON
        if (v.getId() == R.id.back_button_filtering) {
            Intent intent = new Intent(this, LocationRequestPermission.class);
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
        // tvCurrentLocation.setText(currentLocation);
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
        intent.putExtra("user_lat", currentLat);
        intent.putExtra("user_lng", currentLng);
        if (inTutorial) {
            intent.putExtra(TutorialConstants.EXTRA_TOUR, true);
        }
        startActivity(intent);

        // For now, just log the criteria (or show a Toast for debugging)
        Log.d("FilterActivity", "Filter applied: " + criteria.toString());

        // Placeholder: Return to previous activity
        //finish();
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
    private void createLocationRequest() {
        Log.d("createLocationRequest", "createLocationRequest");
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 100)
                .setMinUpdateIntervalMillis(100)
                .setWaitForAccurateLocation(true) // Wait for accurate location
                .build();
    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (android.location.Location location : locationResult.getLocations()) {
                    currentLat = (float) location.getLatitude();
                    currentLng = (float) location.getLongitude();
                    Log.d("onLocationResult", "(" + currentLat + "," + currentLng + ")");
                    locationPlaceholder = findViewById(R.id.tvCurrentLocation);
                    if (currentLat != 0 && currentLng != 0) {
                        locationPlaceholder.setText("Using fine-grain GPS location.");
                    } else {
                        locationPlaceholder.setText("Location Not Available");
                    }
                }
            }
        };
    }
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("onRequestPermissionsResult", "Permission granted, starting location updates.");
                startLocationUpdates();
            } else {
                Log.d("onRequestPermissionsResult", "Permission denied");
            }
        }
    }
}
