package com.example.functionalprototype;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import java.util.Locale;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class StudyHere extends AppCompatActivity {
    private int seconds = 0;

    private boolean running = true;

    private boolean wasRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_here);
        if (savedInstanceState != null) {
            seconds
                    = savedInstanceState
                    .getInt("seconds");
            running
                    = savedInstanceState
                    .getBoolean("running");
            wasRunning
                    = savedInstanceState
                    .getBoolean("wasRunning");
        }

        TextView name = findViewById(R.id.tvBuildingName);
        name.setText(getIntent().getStringExtra("building_name"));

        // Set menu button listener
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        Button menuButton = findViewById(R.id.menu_button);
        Button drawerBuildings = findViewById(R.id.buildings_list_button);
        Button drawerReport = findViewById(R.id.report_issue_button);

        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(Gravity.START));
        drawerBuildings.setOnClickListener(v -> {
            startActivity(new Intent(StudyHere.this, BuildingList.class));
            drawerLayout.closeDrawer(Gravity.START);
        });

        drawerReport.setOnClickListener(v -> {
            startActivity(new Intent(StudyHere.this, ReportFeature.class));
            drawerLayout.closeDrawer(Gravity.START);
        });

        // Set home button listener
        Button homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        // Set end button listener
        Button endButton = findViewById(R.id.btnEnd);
        endButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        // Set report button listener
        Button reportButton = findViewById(R.id.btnReport);
        reportButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReportingFilter.class);
            startActivity(intent);
        });

        runTimer();
    }

    @Override
    public void onSaveInstanceState(
            Bundle savedInstanceState)
    {
        savedInstanceState
                .putInt("seconds", seconds);
        savedInstanceState
                .putBoolean("running", running);
        savedInstanceState
                .putBoolean("wasRunning", wasRunning);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        wasRunning = running;
        running = false;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (wasRunning) {
            running = true;
        }
    }

    public void onClickStart(View view)
    {
        running = true;
    }

    public void onClickStop(View view)
    {
        running = false;
    }

    public void onClickReset(View view)
    {
        running = false;
        seconds = 0;
    }

    private void runTimer()
    {

        final TextView timeView
                = findViewById(
                R.id.tvStopwatch);

        final Handler handler
                = new Handler();

        handler.post(new Runnable() {
            @Override

            public void run()
            {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                String time
                        = String
                        .format(Locale.getDefault(),
                                "%d:%02d:%02d", hours,
                                minutes, secs);

                timeView.setText(time);

                if (running) {
                    seconds++;
                }

                handler.postDelayed(this, 1000);
            }
        });
    }
}
