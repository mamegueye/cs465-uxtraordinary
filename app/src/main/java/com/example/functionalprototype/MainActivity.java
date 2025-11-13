package com.example.functionalprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Declare tutorial overlay and buttons
    private View tutorialOverlay;
    private Button btnTutorial;
    private Button btnTutorialSkip;
    private Button btnTutorialGotIt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tutorialOverlay = findViewById(R.id.tutorialOverlay);
        btnTutorial = findViewById(R.id.btnTutorial);
        btnTutorialSkip = findViewById(R.id.btnTutorialSkip);
        btnTutorialGotIt = findViewById(R.id.btnTutorialGotIt);

        // Show overlay when user taps “How does this app work?”
        btnTutorial.setOnClickListener(v -> tutorialOverlay.setVisibility(View.VISIBLE));

        // Hide overlay for both Skip and Got it
        View.OnClickListener hideOverlay = v -> tutorialOverlay.setVisibility(View.GONE);
        btnTutorialSkip.setOnClickListener(hideOverlay);
        btnTutorialGotIt.setOnClickListener(hideOverlay);

        // Stella's code -- inserted by Hannah
        TextView menuButton = findViewById(R.id.menuIcon);
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Menu.class);
            startActivity(intent);
        });

    }
}
