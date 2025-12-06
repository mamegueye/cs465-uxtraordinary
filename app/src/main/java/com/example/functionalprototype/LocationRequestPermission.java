package com.example.functionalprototype;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class LocationRequestPermission extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_LOCATION = 1001;

    private boolean inTutorial = false;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private float userLat = 0f;
    private float userLng = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // No layout — this activity is logic-only
        inTutorial = getIntent().getBooleanExtra(TutorialConstants.EXTRA_TOUR, false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        createLocationRequest();
        createLocationCallback();

        // Check if permission is already granted without
        // having white screen
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            Log.d("LocationPermission", "Already granted → skipping dialog");

            // If you want to JUST skip to filter without location:
            // goToFiltering(0, 0);

            // If you want actual GPS:
            startLocationUpdates();

            return; // Prevent executing requestLocationPermission()
        }
        // Immediately request permission
        requestLocationPermission();
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_LOCATION
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {

            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.d("LocationPermission", "Granted → starting updates");
                startLocationUpdates();

            } else {
                Log.d("LocationPermission", "Denied → continue without location");
                goToFiltering(0, 0);
            }
        }
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                500 // 0.5 second updates
        )
                .setMinUpdateIntervalMillis(500)
                .build();
    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {

                android.location.Location location = locationResult.getLastLocation();

                // Stop requesting updates now that we got one
                fusedLocationClient.removeLocationUpdates(locationCallback);

                if (location != null) {
                    userLat = (float) location.getLatitude();
                    userLng = (float) location.getLongitude();

                    Log.d("onLocationResult", "LatLng = (" + userLat + ", " + userLng + ")");
                    goToFiltering(userLat, userLng);

                } else {
                    Log.d("onLocationResult", "Null location, sending 0,0");
                    goToFiltering(0, 0);
                }
            }
        };
    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            goToFiltering(0, 0);
            return;
        }

        fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
        );
    }

    private void goToFiltering(float lat, float lng) {
        Intent intent = new Intent(this, SpaceFiltering.class);

        intent.putExtra("user_lat", lat);
        intent.putExtra("user_lng", lng);

        if (inTutorial) {
            intent.putExtra(TutorialConstants.EXTRA_TOUR, true);
        }

        startActivity(intent);
        finish(); // Ensure no flash when returning
    }
}
