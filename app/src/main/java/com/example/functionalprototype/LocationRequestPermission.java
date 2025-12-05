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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class LocationRequestPermission extends AppCompatActivity
        implements View.OnClickListener {

    // back buttons in bottom navbar

    private Button backButton;

    private Button homeButton;

    // Location options
    private Button allowWhileUsing;
    private Button allowOnce;
    private Button dontAllow;

    private View tutorialOverlay;
    private Button btnTutorialSkip;
    private Button btnTutorialGotIt;
    private boolean inTutorial = false;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private float userLat = 0;
    private float userLng = 0;

    private static final int PERMISSIONS_REQUEST_LOCATION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_request);

        inTutorial = getIntent().getBooleanExtra(TutorialConstants.EXTRA_TOUR, false);

        // enable location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
        createLocationCallback();
        startLocationUpdates();

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        Button menuButton = findViewById(R.id.menu_button);
        Button drawerBuildings = findViewById(R.id.buildings_list_button);
        Button drawerReport = findViewById(R.id.report_issue_button);

        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(Gravity.START));
        drawerBuildings.setOnClickListener(v -> {
            startActivity(new Intent(LocationRequestPermission.this, BuildingList.class));
            drawerLayout.closeDrawer(Gravity.START);
        });

        drawerReport.setOnClickListener(v -> {
            startActivity(new Intent(LocationRequestPermission.this, ReportFeature.class));
            drawerLayout.closeDrawer(Gravity.START);
        });


        // The user has pressed the 'Back' Button
        backButton = (Button) findViewById(R.id.back_button_location_request);
        backButton.setOnClickListener(this);

        // The user has pressed the 'Home' Button
        homeButton = (Button) findViewById(R.id.home_button);
        homeButton.setOnClickListener(this);

        // Grab the location option buttons
        allowWhileUsing = (Button) findViewById(R.id.allow_while_using_button);
        allowWhileUsing.setOnClickListener(this);
        allowOnce = (Button) findViewById(R.id.allow_once_button);
        allowOnce.setOnClickListener(this);
        dontAllow = (Button) findViewById(R.id.dont_allow_button);
        dontAllow.setOnClickListener(this);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("onRequestPermissionsResult", "Permission granted, start location updates.");
                startLocationUpdates();
            } else {
                Log.d("onRequestPermissionsResult", "Permission denied");
            }
        }
    }

    // For when the user presses the 'Next' Button or 'Back' Button
    public void onClick(View v) {
        // User pressed BACK button
        if (v.getId() == R.id.back_button_location_request) {
            Intent intent = new Intent(this, MainActivity.class);
            //Toast.makeText(this, "second if", Toast.LENGTH_SHORT).show();
            startActivity(intent);

            // User pressed HOME button
        } else if (v.getId() == R.id.home_button) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.allow_while_using_button || v.getId() == R.id.allow_once_button) {
            Intent intent = new Intent(this, SpaceFiltering.class);
            if (inTutorial) {
                intent.putExtra(TutorialConstants.EXTRA_TOUR, true);
            }
            intent.putExtra("user_lat", userLat);
            intent.putExtra("user_lng", userLng);
            startActivity(intent);
        } else if (v.getId() == R.id.dont_allow_button) {
            Intent intent = new Intent(this, SpaceFiltering.class);
            if (inTutorial) {
                intent.putExtra(TutorialConstants.EXTRA_TOUR, true);
            }
            startActivity(intent);
        }

    }
    private void createLocationRequest() {
        Log.d("createLocationRequest", "createLocationRequest");
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 100) // High accuracy, 10-second interval
                .setMinUpdateIntervalMillis(100) // Minimum 5-second interval
                .setWaitForAccurateLocation(true) // Wait for accurate location
                .build();
    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (android.location.Location location : locationResult.getLocations()) {
                    userLat = (float) location.getLatitude();
                    userLng = (float) location.getLongitude();
                    Log.d("onLocationResult", "(" + userLat + "," + userLng + ")");
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
}
