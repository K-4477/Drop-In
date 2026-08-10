package com.example.locationapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.EditText;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.Dialog;
import java.util.Calendar;
import android.widget.Toast;
import android.view.View;
import android.database.sqlite.SQLiteDatabase;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;



public class NewLocation extends AppCompatActivity  {
    private EditText editLocationName, editLocationAddress, editCountry, editGPSX, editGPSY;
    private TextView textDate, ratingVal;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private ImageButton populateGPSButton;
    private SeekBar seekBar;


    public SQLiteDatabase db;
    public HelperDatabase dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_location);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize form fields
        editGPSX = findViewById(R.id.gpsX);
        editGPSY = findViewById(R.id.gpsY);
        populateGPSButton = findViewById(R.id.populateGPSButton);
        // Set click listener for the button
        populateGPSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });

        dbh = new HelperDatabase(this);
        db = dbh.getWritableDatabase();

        // Reference the SeekBar and TextView
        seekBar = findViewById(R.id.seekBar2);
        ratingVal = findViewById(R.id.ratingValue);
        // Set a listener to update the TextView as the SeekBar is moved
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the TextView with the current SeekBar value
                ratingVal.setText("" + progress);

         }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        editLocationName = findViewById(R.id.locationName);
        editLocationAddress = findViewById(R.id.locationAddress);
        editCountry = findViewById(R.id.country);
        editGPSX = findViewById(R.id.gpsX);
        editGPSY = findViewById(R.id.gpsY);
        textDate = findViewById(R.id.dateVisited);
        ratingVal = findViewById(R.id.ratingValue);
        Button locationSubmit = findViewById(R.id.submitLocation);

        locationSubmit.setOnClickListener(view -> {
            String locationName = editLocationName.getText().toString().trim();
            String locationAddress = editLocationAddress.getText().toString().trim();
            String country = editCountry.getText().toString().trim();
            String dateStr = textDate.getText().toString().trim();

            try {
                float gpsXStr = Float.parseFloat(editGPSX.getText().toString().trim());
                float gpsYStr = Float.parseFloat(editGPSY.getText().toString().trim());
                int rating = Integer.parseInt(ratingVal.getText().toString().trim());

                if (locationName.isEmpty() || locationAddress.isEmpty() || country.isEmpty() || dateStr.isEmpty()) {
                    Toast.makeText(NewLocation.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(() -> {
                    long recordId = HelperDatabase.insertLocation(db, locationName, locationAddress, country, dateStr, gpsXStr, gpsYStr, rating);
                    runOnUiThread(() -> {
                        if (recordId != -1) {
                            Toast.makeText(NewLocation.this, "Record inserted with ID: " + recordId, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(NewLocation.this, ViewLocations.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(NewLocation.this, "Failed to insert record", Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();
            }   catch (NumberFormatException e) {
                Toast.makeText(NewLocation.this, "Invalid number format", Toast.LENGTH_SHORT).show();
            }
        });

        Button dateButton = findViewById(R.id.visitDate);
        dateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Use the native FragmentManager to show the DatePickerFragment
                FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(fragmentManager, "datePicker");

            }
        });
        Intent intent = getIntent();





    }

    private void getCurrentLocation(){
        // Check permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        // Fetch the last known location
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            // Update the form fields with the location data
                            editGPSX.setText(String.valueOf(location.getLatitude()));
                            editGPSY.setText(String.valueOf(location.getLongitude()));
                        } else {
                            Toast.makeText(NewLocation.this, "Unable to fetch location. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, fetch location
                getCurrentLocation();
            } else {
                // Permission denied
                Toast.makeText(this, "Location permission is required to fetch GPS coordinates.", Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void onClickGoBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }




    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker.
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it.
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) { // User selects a date

            // Update a TextView with the selected date
            int correctedMonth = month + 1; // Adjust for zero-based months
            String selectedDate = year + "-" + correctedMonth + "-" + day;

            // Find the TextView and set the date
            TextView dateTextView = getActivity().findViewById(R.id.dateVisited);
            dateTextView.setText(selectedDate);

        }


    }


}