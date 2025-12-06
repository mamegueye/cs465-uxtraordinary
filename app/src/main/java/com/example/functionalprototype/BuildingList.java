package com.example.functionalprototype;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BuildingList extends AppCompatActivity {

    RecyclerView recyclerView;
    BuildingAdapter adapter;
    List<Building> buildingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_list);

        recyclerView = findViewById(R.id.buildingRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        try {
            dbHelper.createDatabase();
            SQLiteDatabase db = dbHelper.openDatabase();

            // <<< ADD DEBUG HERE >>>
            Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            while (cursor.moveToNext()) {
                Log.d("DB_DEBUG", "Table found: " + cursor.getString(0));
            }
            cursor.close();

            buildingList = getAllBuildings(db);

        } catch (IOException e) {
            e.printStackTrace();
            buildingList = new ArrayList<>();
        }

        adapter = new BuildingAdapter(buildingList, building -> {
            Intent intent = new Intent(BuildingList.this, BuildingDetail.class);
            intent.putExtra("building_name", building.building_name);
            intent.putExtra("monday", building.monday);
            intent.putExtra("tuesday", building.tuesday);
            intent.putExtra("wednesday", building.wednesday);
            intent.putExtra("thursday", building.thursday);
            intent.putExtra("friday", building.friday);
            intent.putExtra("saturday", building.saturday);
            intent.putExtra("sunday", building.sunday);
            intent.putExtra("cleanliness", building.cleanliness);
            intent.putExtra("latitude", building.latitude);
            intent.putExtra("longitude", building.longitude);
            intent.putExtra("cafe", building.cafe);
            intent.putExtra("address", building.address);
            intent.putExtra("info", building.info);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        Button backButton = findViewById(R.id.back);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(BuildingList.this, Menu.class);
            startActivity(intent);
        });
    }

    private List<Building> getAllBuildings(SQLiteDatabase db) {
        List<Building> buildings = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM building_hours ORDER BY building_name", null);

        if (cursor.moveToFirst()) {
            do {
                buildings.add(new Building(
                        cursor.getString(cursor.getColumnIndexOrThrow("building_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("monday")),
                        cursor.getString(cursor.getColumnIndexOrThrow("tuesday")),
                        cursor.getString(cursor.getColumnIndexOrThrow("wednesday")),
                        cursor.getString(cursor.getColumnIndexOrThrow("thursday")),
                        cursor.getString(cursor.getColumnIndexOrThrow("friday")),
                        cursor.getString(cursor.getColumnIndexOrThrow("saturday")),
                        cursor.getString(cursor.getColumnIndexOrThrow("sunday")),
                        cursor.isNull(cursor.getColumnIndexOrThrow("cleanliness")) ? null :
                                cursor.getFloat(cursor.getColumnIndexOrThrow("cleanliness")),
                        cursor.isNull(cursor.getColumnIndexOrThrow("latitude")) ? null :
                                cursor.getFloat(cursor.getColumnIndexOrThrow("latitude")),
                        cursor.isNull(cursor.getColumnIndexOrThrow("longitude")) ? null :
                                cursor.getFloat(cursor.getColumnIndexOrThrow("longitude")),
                        cursor.getString(cursor.getColumnIndexOrThrow("cafe")),
                        cursor.getString(cursor.getColumnIndexOrThrow("address")),
                        cursor.getString(cursor.getColumnIndexOrThrow("info"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return buildings;
    }
}
