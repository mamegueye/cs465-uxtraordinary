package com.example.functionalprototype;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "buildings.db";
    private static final int DATABASE_VERSION = 1;
    private final Context context;
    private String databasePath;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.databasePath = context.getFilesDir().getPath() +  "/" + DATABASE_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    // Copy database if it does not exist or overwrite
    public void createDatabase() throws IOException {
        File dbFile = new File(databasePath);
        if (!dbFile.exists()) {
            dbFile.getParentFile().mkdirs(); // ensure folder exists
            copyDatabase();
        }
    }

    private void copyDatabase() throws IOException {
        InputStream input = context.getAssets().open( DATABASE_NAME);
        OutputStream output = new FileOutputStream(databasePath);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        output.flush();
        output.close();
        input.close();
    }

    // Open the database
    public SQLiteDatabase openDatabase() throws SQLException {
        return SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
    }
}