package com.example.functionalprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ReportingFilter extends AppCompatActivity
implements AdapterView.OnItemSelectedListener,
        RadioGroup.OnCheckedChangeListener,
        TextView.OnEditorActionListener,
        View.OnClickListener {

    // User building choice
    private Spinner reportBuildingAnswer;

    // User floor choice
    private RadioGroup reportFloorAnswer;

    // User report comments/text
    private EditText userReportAnswer;

    // Next and back buttons in bottom navbar
    private Button nextButton;

    private Button backButton;

    private Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporting_filter);

        // Grab Building choice
        reportBuildingAnswer = (Spinner) findViewById(R.id.report_building_dropdown);
        reportBuildingAnswer.setOnItemSelectedListener(this);

        // Grab Floor choice
        reportFloorAnswer = (RadioGroup) findViewById(R.id.report_floor_choices);
        reportFloorAnswer.setOnCheckedChangeListener(this);

        // Grab Report Comments/Text
        userReportAnswer = (EditText) findViewById(R.id.user_enter_report);
        userReportAnswer.setOnEditorActionListener(this);

        // Added by Stella
        Button menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(ReportingFilter.this, Menu.class);
            startActivity(intent);
        });

        // The user has pressed the 'Next' Button
        nextButton = (Button) findViewById(R.id.next_button_report_filter);
        nextButton.setOnClickListener(this);

        // The user has pressed the 'Back' Button
        backButton = (Button) findViewById(R.id.back_button_report_filter);
        backButton.setOnClickListener(this);

        // The user has pressed the 'Home' Button
        homeButton = (Button) findViewById(R.id.home_button);
        homeButton.setOnClickListener(this);
    }

    // Get building user wants to report
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        // Get the user's reporting building
        String userChoice = parent.getItemAtPosition(position).toString();

        // Ensure that the user does not choose the placeholder text
        if (userChoice.equals("Choose an option")) {
            Toast.makeText(this, "Unsuccessful: Please select 'App' or 'Study Space or Bathroom'", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Selected: " + userChoice, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // This method is required if we implement OnItemSelectedListener
    }


    // Get the user's floor they want to report
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // Get the user's selected radio button
        RadioButton selectedFloor = findViewById(checkedId);

        // Display the user's selected floor to report
        if (selectedFloor != null) {
            String userChoice = selectedFloor.getText().toString();
            Toast.makeText(this, "Selected floor: " + userChoice, Toast.LENGTH_SHORT).show();
        }
    }



    // Get the user's report comments
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // Get the user's reporting building
        String userInput = v.getText().toString();

        Toast.makeText(this, "Solution: " + userInput, Toast.LENGTH_SHORT).show();

        return true;
    }


    // For when the user presses the 'Next' Button or 'Back' Button
    public void onClick(View v) {
        // Save the user's responses to display on the next page
        // 1) What building do they want to report?
        String buildingChoice = reportBuildingAnswer.getSelectedItem().toString();

        // 2) What floor do they want to report?
        int getSelectedFloorId = reportFloorAnswer.getCheckedRadioButtonId();
        RadioButton selectedFloor = findViewById(getSelectedFloorId);
        String floorChoice = selectedFloor.getText().toString();

        // 3) What is their report blurb?
        String reportComment = userReportAnswer.getText().toString();

        // If the user presses, the 'Next' Button, go to
        // the next reporting screen to select a BUILDING, FLOOR
        if (v.getId() == R.id.next_button_report_filter){
            Intent intent = new Intent(this, ReportingConfirmation.class);

            // Carry the information to next activity
            intent.putExtra("building_choice", buildingChoice);
            intent.putExtra("floor_choice", floorChoice);
            intent.putExtra("report_comments", reportComment);

            // Go to next activity
            startActivity(intent);
        } else if (v.getId() == R.id.back_button_report_filter) {
            Intent intent = new Intent(this, ReportFeature.class);
            startActivity(intent);
        }

        // If they want to go home
        if (v.getId() == R.id.home_button) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }

}