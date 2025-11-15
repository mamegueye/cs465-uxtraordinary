package com.example.functionalprototype;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;

public class BuildingDetail extends AppCompatActivity {

    private String buildingName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Set back button listener
        Button backButton = findViewById(R.id.back_butt);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(BuildingDetail.this, BuildingList.class);
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

        // Set study here button listener
        Button studyHereButton = findViewById(R.id.studyHere_butt);
        studyHereButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, StudyHere.class);
            intent.putExtra("building_name", buildingName);
            startActivity(intent);
        });

        TextView name = findViewById(R.id.tvBuildingName);
        TextView cleanliness = findViewById(R.id.tvBuildingCleanliness);
        TextView capacity = findViewById(R.id.tvBuildingCapacity);
        TextView hours = findViewById(R.id.tvBuildingHours);

        Intent intent = getIntent();
        name.setText(intent.getStringExtra("building_name"));
        cleanliness.setText(" " + intent.getFloatExtra("cleanliness", 0.f) + "/5");
        capacity.setText("  N/A"); // TODO: replace this
        String currentDayOfWeek = LocalDate.now().getDayOfWeek().name().toLowerCase();
        hours.setText("  " + intent.getStringExtra(currentDayOfWeek));

        buildingName = intent.getStringExtra("building_name");
    }
}
