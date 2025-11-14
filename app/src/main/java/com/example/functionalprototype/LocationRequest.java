package com.example.functionalprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Filter;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LocationRequest extends AppCompatActivity
    implements View.OnClickListener {

    // back buttons in bottom navbar

    private Button backButton;

    private Button homeButton;

    // Location options
    private Button allowWhileUsing;
    private Button allowOnce;
    private Button dontAllow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_request);

        // Added by Stella
        Button menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(LocationRequest.this, Menu.class);
            startActivity(intent);
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
            Intent intent = new Intent(this, FilterActivity.class);

            // Carry location preference to the filtering page
            intent.putExtra("location_preference", "allow_while_using");

            startActivity(intent);
        } else if (v.getId() == R.id.allow_once_button) {
            Intent intent = new Intent(this, FilterActivity.class);

            // Carry location preference to the filtering page
            intent.putExtra("location_preference", "allow_once");

            startActivity(intent);
        } else if (v.getId() == R.id.dont_allow_button) {
            Intent intent = new Intent(this, FilterActivity.class);

            // Carry location preference to the filtering page
            intent.putExtra("location_preference", "dont_allow");

            startActivity(intent);
        }

    }
}