package com.example.locationapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.EditText;
import android.database.Cursor;
import android.content.ContentValues;
import android.widget.Button;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

public class EditLocation extends AppCompatActivity {

    private EditText editLocationName, editLocationAddress, editCountry, editGPSX, editGPSY;

    private TextView textDate, ratingVal;
    private SeekBar seekBar;
    private SQLiteDatabase db;
    private HelperDatabase dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_location);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize database helper
        dbh = new HelperDatabase(this);
        db = dbh.getWritableDatabase();

        seekBar = findViewById(R.id.seekBar2);
        ratingVal = findViewById(R.id.ratingValue);
        // Set a listener to update the TextView as the SeekBar is moved
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the TextView with the current SeekBar value
                ratingVal.setText("" + progress);
                // textViewValue.setText("Value: " + progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Optional: Add code if you want to handle the start of the user interaction
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Optional: Add code if you want to handle the end of the user interaction
            }
        });

        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the Home button for navigation to Home Page
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable home button
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home); // Use custom home icon
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Hide title text}
        }



        // Get the ID passed from ViewLocation
        long locationId = getIntent().getLongExtra("locationId", -1);

        // Initialize views
        editLocationName = findViewById(R.id.editLocationName);
        editLocationAddress = findViewById(R.id.editLocationAddress);
        editCountry = findViewById(R.id.editCountry);
        editGPSX = findViewById(R.id.editGPSX);
        editGPSY = findViewById(R.id.editGPSY);
        textDate = findViewById(R.id.dateVisited);
        ratingVal = findViewById(R.id.ratingValue);

        // Load existing data
        loadLocationData(locationId);


    }

    private void loadLocationData(long locationId) {
        Cursor cursor = db.query("LOCATION",
                new String[]{"LOCATION_NAME", "LOCATION_ADDRESS", "COUNTRY", "GPS_X", "GPS_Y","DATE", "RATING"},
                "_id = ?",
                new String[]{String.valueOf(locationId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            editLocationName.setText(cursor.getString(cursor.getColumnIndexOrThrow("LOCATION_NAME")));
            editLocationAddress.setText(cursor.getString(cursor.getColumnIndexOrThrow("LOCATION_ADDRESS")));
            editCountry.setText(cursor.getString(cursor.getColumnIndexOrThrow("COUNTRY")));
            editGPSX.setText(String.valueOf(cursor.getFloat(cursor.getColumnIndexOrThrow("GPS_X"))));
            editGPSY.setText(String.valueOf(cursor.getFloat(cursor.getColumnIndexOrThrow("GPS_Y"))));
            textDate.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("DATE"))));
            ratingVal.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("RATING"))));
            cursor.close();
        }
    }

    private void updateLocationData(long locationId) {
        String locationName = editLocationName.getText().toString().trim();
        String locationAddress = editLocationAddress.getText().toString().trim();
        String country = editCountry.getText().toString().trim();
        float gpsXStr = Float.parseFloat(editGPSX.getText().toString().trim());
        float gpsYStr = Float.parseFloat(editGPSY.getText().toString().trim());
        String dateStr = textDate.getText().toString().trim();
        int rating = Integer.parseInt(ratingVal.getText().toString().trim());

        ContentValues recordValues = new ContentValues();
        recordValues.put("LOCATION_NAME", locationName);
        recordValues.put("LOCATION_ADDRESS", locationAddress);
        recordValues.put("COUNTRY", country);
        recordValues.put("DATE", dateStr); // Use the formatted date string
        recordValues.put("GPS_X", gpsXStr);
        recordValues.put("GPS_Y", gpsYStr);
        recordValues.put("RATING", rating);

        int rowsAffected = db.update("LOCATION", recordValues, "_id = ?", new String[]{String.valueOf(locationId)});

        if (rowsAffected > 0) {
            Toast.makeText(this, "Location updated successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update location.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Use the item's ID to determine the action
        int id = item.getItemId();

        if (id == R.id.action_save) {
            // Handle save button click
            saveLocation();
            return true;
        } else if (id == android.R.id.home) {
            // Handle the toolbar back button
            // Navigate back to the home page
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Finish current activity
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void saveLocation() {
        // Save the edited location details here
        Toast.makeText(this, "Location saved", Toast.LENGTH_SHORT).show();
    }
    

    public void onClickDiscardChanges(View view) {
        Intent intent = new Intent(this, ViewLocations.class);
        startActivity(intent);
    }
}
