package com.example.functionalprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReportingConfirmation extends AppCompatActivity
    implements View.OnClickListener{

    // Get information from reporting filter
    private TextView buildingText;
    private TextView floorText;
    private TextView commentText;


    // Next and back buttons in bottom navbar
    private Button nextButton;

    private Button backButton;

    private Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporting_confirmation);

        // Grab information from 'Reporting Filter' activity via intent passed
        Intent intent_received = getIntent();
        String buildingChoice = intent_received.getStringExtra("building_choice");
        String floorChoice = intent_received.getStringExtra("floor_choice");
        String comment = intent_received.getStringExtra("report_comments");

        // Grab XML textviews to replace from intent data
        buildingText = findViewById(R.id.confirmation_building);
        floorText = findViewById(R.id.confirmation_floor);
        commentText = findViewById(R.id.confirmation_comment);

        // Replace XML textviews with intent data
        if (buildingChoice != null && !buildingChoice.isEmpty()) {
            buildingText.setText(buildingChoice);
        } else {
            buildingText.setText("(No building selected)");
        }

        if (floorChoice != null && !floorChoice.isEmpty()) {
            floorText.setText(floorChoice);
        } else {
            floorText.setText("(No floor selected)");
        }

        if (comment != null && !comment.isEmpty()) {
            commentText.setText(comment);
        } else {
            commentText.setText("(No comment entered)");
        }

        // Added by Stella
        Button menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(ReportingConfirmation.this, Menu.class);
            startActivity(intent);
        });

        // The user has pressed the 'Next' Button
        nextButton = (Button) findViewById(R.id.next_button_report_confirmation);
        nextButton.setOnClickListener(this);

        // The user has pressed the 'Back' Button
        backButton = (Button) findViewById(R.id.back_button_report_confirmation);
        backButton.setOnClickListener(this);

        // The user has pressed the 'Home' Button
        homeButton = (Button) findViewById(R.id.home_button);
        homeButton.setOnClickListener(this);
    }

    // For when the user presses the 'Next' Button or 'Back' Button
    public void onClick(View v) {

        // Next Button was clicked
        if (v.getId() == R.id.next_button_report_confirmation){
            Intent intent = new Intent(this, ReportComplete.class);
            startActivity(intent);

        // Back Button was clicked
        } else if (v.getId() == R.id.back_button_report_confirmation) {
            Intent intent = new Intent(this, ReportingFilter.class);
            startActivity(intent);

        // Home Button was clicked
        } else if (v.getId() == R.id.home_button) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }
}