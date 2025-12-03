package com.example.functionalprototype;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;


public class MainActivity extends AppCompatActivity {

    // Tutorial overlay and buttons
    private View tutorialOverlay;
    private Button btnTutorial;
    private Button btnTutorialSkip;
    private Button btnTutorialGotIt;

    private DrawerLayout drawerLayout;

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

        drawerLayout = findViewById(R.id.drawerLayout);

        tutorialOverlay = findViewById(R.id.tutorialOverlay);
        btnTutorial = findViewById(R.id.btnTutorial);
        btnTutorialSkip = findViewById(R.id.btnTutorialSkip);
        btnTutorialGotIt = findViewById(R.id.btnTutorialGotIt);

        btnTutorial.setOnClickListener(v -> tutorialOverlay.setVisibility(View.VISIBLE));

        View.OnClickListener hideOverlay = v -> tutorialOverlay.setVisibility(View.GONE);
        btnTutorialSkip.setOnClickListener(hideOverlay);
        btnTutorialGotIt.setOnClickListener(hideOverlay);

        TextView menuButton = findViewById(R.id.menuIcon);
        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(Gravity.START));

        // Drawer menu buttons

        Button drawerBuildings = findViewById(R.id.buildings_list_button);
        Button drawerReport = findViewById(R.id.report_issue_button);
        RelativeLayout drawerTopBar = findViewById(R.id.top_bar);

        drawerBuildings.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, BuildingList.class));
            drawerLayout.closeDrawer(Gravity.START);
        });

        drawerReport.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ReportFeature.class));
            drawerLayout.closeDrawer(Gravity.START);
        });

        ViewCompat.setOnApplyWindowInsetsListener(drawerTopBar, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });



        Button btnGo = findViewById(R.id.btnGo);
        btnGo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LocationRequest.class);
            startActivity(intent);
        });
    }
}
