package com.example.functionalprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Filter;
import android.widget.RadioButton;
import android.widget.Space;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class LocationRequest extends AppCompatActivity
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_request);

        inTutorial = getIntent().getBooleanExtra(TutorialConstants.EXTRA_TOUR, false);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        Button menuButton = findViewById(R.id.menu_button);
        Button drawerBuildings = findViewById(R.id.buildings_list_button);
        Button drawerReport = findViewById(R.id.report_issue_button);

        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(Gravity.START));
        drawerBuildings.setOnClickListener(v -> {
            startActivity(new Intent(LocationRequest.this, BuildingList.class));
            drawerLayout.closeDrawer(Gravity.START);
        });

        drawerReport.setOnClickListener(v -> {
            startActivity(new Intent(LocationRequest.this, ReportFeature.class));
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
            //Toast.makeText(this, "third if", Toast.LENGTH_SHORT).show();
            startActivity(intent);

            // Deal with different location access answers
        } else if (v.getId() == R.id.allow_while_using_button) {
            Intent intent = new Intent(this, SpaceFiltering.class);

            // Carry location preference to the filtering page
            intent.putExtra("location_preference", "Thomas M. Siebel Center for Computer Science");
            if (inTutorial) {
                intent.putExtra(TutorialConstants.EXTRA_TOUR, true);
            }

            startActivity(intent);
        } else if (v.getId() == R.id.allow_once_button) {
            Intent intent = new Intent(this, SpaceFiltering.class);

            // Carry location preference to the filtering page
            intent.putExtra("location_preference", "Thomas M. Siebel Center for Computer Science");
            if (inTutorial) {
                intent.putExtra(TutorialConstants.EXTRA_TOUR, true);
            }

            startActivity(intent);
        } else if (v.getId() == R.id.dont_allow_button) {
            Intent intent = new Intent(this, SpaceFiltering.class);

            // Carry location preference to the filtering page
            intent.putExtra("location_preference", "Location Not Available");
            if (inTutorial) {
                intent.putExtra(TutorialConstants.EXTRA_TOUR, true);
            }

            startActivity(intent);
        }

    }
}
