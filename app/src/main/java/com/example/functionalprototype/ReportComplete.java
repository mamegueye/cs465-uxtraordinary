package com.example.functionalprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ReportComplete extends AppCompatActivity
    implements View.OnClickListener {




    private Button endButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_complete);


        // The user has pressed the 'End' Button
        endButton = (Button) findViewById(R.id.end_button_report_complete);
        endButton.setOnClickListener(this);
    }

    // For when the user presses the 'Back' Button or 'End' Button
    public void onClick(View v) {
        // If the user presses, the 'Next' Button, go to
        // the next reporting screen to select a BUILDING, FLOOR
        if (v.getId() == R.id.end_button_report_complete) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}