package com.example.functionalprototype;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BuildingDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_detail);

        Button backButton = findViewById(R.id.back_butt);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(BuildingDetail.this, BuildingList.class);
            startActivity(intent);
        });

        TextView monday = findViewById(R.id.monday);
        TextView tuesday = findViewById(R.id.tuesday);
        TextView wednesday = findViewById(R.id.wednesday);
        TextView thursday = findViewById(R.id.thursday);
        TextView friday = findViewById(R.id.friday);
        TextView saturday = findViewById(R.id.saturday);
        TextView sunday = findViewById(R.id.sunday);
        TextView cleanliness = findViewById(R.id.cleanliness);

        //Days of the week
        monday.setText("Monday: " + (getIntent().getStringExtra("monday") != null ? getIntent().getStringExtra("monday") : "N/A"));
        tuesday.setText("Tuesday: " + (getIntent().getStringExtra("tuesday") != null ? getIntent().getStringExtra("tuesday") : "N/A"));
        wednesday.setText("Wednesday: " + (getIntent().getStringExtra("wednesday") != null ? getIntent().getStringExtra("wednesday") : "N/A"));
        thursday.setText("Thursday: " + (getIntent().getStringExtra("thursday") != null ? getIntent().getStringExtra("thursday") : "N/A"));
        friday.setText("Friday: " + (getIntent().getStringExtra("friday") != null ? getIntent().getStringExtra("friday") : "N/A"));
        saturday.setText("Saturday: " + (getIntent().getStringExtra("saturday") != null ? getIntent().getStringExtra("saturday") : "N/A"));
        sunday.setText("Sunday: " + (getIntent().getStringExtra("sunday") != null ? getIntent().getStringExtra("sunday") : "N/A"));

        // Cleanliness
        Float clean = getIntent().getFloatExtra("cleanliness", -1f);
        cleanliness.setText("Cleanliness: " + (clean != -1f ? String.valueOf(clean) : "N/A"));
    }
}
