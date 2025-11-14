package com.example.functionalprototype;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find buttons
        Button buildingsListButton = findViewById(R.id.buildings_list_button);
        Button reportIssueButton = findViewById(R.id.report_issue_button);
        Button backButton = findViewById(R.id.back_button);

        // Set up click listeners
        buildingsListButton.setOnClickListener(v -> {
            Intent intent = new Intent(Menu.this, BuildingList.class);
            startActivity(intent);
        });

        reportIssueButton.setOnClickListener(v -> {
            Intent intent = new Intent(Menu.this, ReportFeature.class);
            startActivity(intent);
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Menu.this, MainActivity.class);
            startActivity(intent);
        });
    }
}