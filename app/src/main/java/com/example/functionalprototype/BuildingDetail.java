package com.example.functionalprototype;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BuildingDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_detail);

        TextView name = findViewById(R.id.name);
        TextView monday = findViewById(R.id.monday);
        TextView tuesday = findViewById(R.id.tuesday);
        TextView wednesday = findViewById(R.id.wednesday);
        TextView thursday = findViewById(R.id.thursday);
        TextView friday = findViewById(R.id.friday);
        TextView saturday = findViewById(R.id.saturday);
        TextView sunday = findViewById(R.id.sunday);
        TextView cleanliness = findViewById(R.id.cleanliness);
        TextView latitude = findViewById(R.id.latitude);
        TextView longitude = findViewById(R.id.longitude);

        name.setText(getIntent().getStringExtra("name"));
        monday.setText(getIntent().getStringExtra("monday"));
        tuesday.setText(getIntent().getStringExtra("tuesday"));
        wednesday.setText(getIntent().getStringExtra("wednesday"));
        thursday.setText(getIntent().getStringExtra("thursday"));
        friday.setText(getIntent().getStringExtra("friday"));
        saturday.setText(getIntent().getStringExtra("saturday"));
        sunday.setText(getIntent().getStringExtra("sunday"));

        Float clean = getIntent().getFloatExtra("cleanliness", -1f);
        cleanliness.setText(clean != -1f ? String.valueOf(clean) : "N/A");

        Float lat = getIntent().getFloatExtra("latitude", -1f);
        latitude.setText(lat != -1f ? String.valueOf(lat) : "N/A");

        Float lon = getIntent().getFloatExtra("longitude", -1f);
        longitude.setText(lon != -1f ? String.valueOf(lon) : "N/A");
    }
}
