package com.example.functionalprototype;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.time.LocalDate;

public class BuildingDetail extends AppCompatActivity {

    private String buildingName;
    private View tutorialOverlay;
    private Button btnTutorialSkip;
    private Button btnTutorialGotIt;
    private boolean inTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        inTutorial = getIntent().getBooleanExtra(TutorialConstants.EXTRA_TOUR, false);

        tutorialOverlay = findViewById(R.id.tutorialOverlay);
        btnTutorialSkip = findViewById(R.id.btnTutorialSkip);
        btnTutorialGotIt = findViewById(R.id.btnTutorialGotIt);

        if (inTutorial) {
            tutorialOverlay.setVisibility(View.VISIBLE);
        } else {
            tutorialOverlay.setVisibility(View.GONE);
        }

        View.OnClickListener endTutorial = v -> {
            inTutorial = false;
            tutorialOverlay.setVisibility(View.GONE);
        };

        btnTutorialSkip.setOnClickListener(endTutorial);
        btnTutorialGotIt.setOnClickListener(endTutorial);

        // Back button
        Button backButton = findViewById(R.id.back_butt);
        backButton.setOnClickListener(v -> {
            Intent intent = getIntent();
            float filterDistance = intent.getFloatExtra("filter_distance", 0.5f);
            boolean filterOpenNow = intent.getBooleanExtra("filter_open_now", false);
            boolean filterCafeFood = intent.getBooleanExtra("filter_cafe_food", false);
            float userLat = intent.getFloatExtra("user_lat", 0);
            float userLng = intent.getFloatExtra("user_lng", 0);
            String filterLocation = intent.getStringExtra("filter_location");
            Log.d("FilterDataFromIntent", "filterDistance="+filterDistance);
            Log.d("FilterDataFromIntent", "filterOpenNow="+filterOpenNow);
            Log.d("FilterDataFromIntent", "filterCafeFood="+filterCafeFood);
            Log.d("FilterDataFromIntent", "userLat="+userLat);
            Log.d("FilterDataFromIntent", "userLng="+userLng);
            Log.d("FilterDataFromIntent", "filterLocation="+filterLocation);

            intent = new Intent(BuildingDetail.this, Results.class);
            intent.putExtra("filter_distance", filterDistance);
            intent.putExtra("filter_open_now", filterOpenNow);
            intent.putExtra("filter_cafe_food", filterCafeFood);
            intent.putExtra("user_lat", userLat);
            intent.putExtra("user_lng", userLng);
            intent.putExtra("filter_location", filterLocation);
            startActivity(intent);
        });

        // Drawer buttons
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        Button menuButton = findViewById(R.id.menu_button);
        Button drawerBuildings = findViewById(R.id.buildings_list_button);
        Button drawerReport = findViewById(R.id.report_issue_button);

        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        drawerBuildings.setOnClickListener(v -> {
            startActivity(new Intent(BuildingDetail.this, BuildingList.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });
        drawerReport.setOnClickListener(v -> {
            startActivity(new Intent(BuildingDetail.this, ReportFeature.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        // Home button
        Button homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));

        // Receive intent data
        Intent intent = getIntent();
        buildingName = intent.getStringExtra("building_name");

        TextView name = findViewById(R.id.tvBuildingName);
        TextView cleanliness = findViewById(R.id.tvBuildingCleanliness);
        TextView hours = findViewById(R.id.tvBuildingHours);
        TextView cafe = findViewById(R.id.tvBuildingCafe);
        ImageView buildingImage = findViewById(R.id.buildingImage);
        TextView address = findViewById(R.id.tvBuildingAddress);
        TextView info = findViewById(R.id.tvBuildingInfo);

        // Set text views
        name.setText(buildingName);
        cleanliness.setText(" " + intent.getFloatExtra("cleanliness", 0.f) + "/5");
        cafe.setVisibility(intent.getStringExtra("cafe").equals("yes") ? VISIBLE : INVISIBLE);
        String currentDayOfWeek = LocalDate.now().getDayOfWeek().name().toLowerCase();
        hours.setText("  " + intent.getStringExtra(currentDayOfWeek));
        String addressValue = intent.getStringExtra("address");
        String infoValue = intent.getStringExtra("info");

        address.setText(addressValue != null ? addressValue : "Address unavailable");
        info.setText(infoValue != null ? infoValue : "No description available.");

        // Set study here button
        Button studyHereButton = findViewById(R.id.studyHere_butt);
        studyHereButton.setOnClickListener(v -> {
            Intent studyIntent = new Intent(this, StudyHere.class);
            studyIntent.putExtra("building_name", buildingName);
            studyIntent.putExtra(TutorialConstants.EXTRA_TOUR, inTutorial);
            startActivity(studyIntent);
        });

        // Load the correct image dynamically
        String drawableName = buildingName.toLowerCase()
                .replace("&", "and")
                .replace(",", "")
                .replace(" ", "_");
        int imageResId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
        if (imageResId != 0) {
            buildingImage.setImageResource(imageResId);
        } else {
            buildingImage.setImageResource(R.drawable.spaces_grainger_drawable); // fallback
        }
    }
}