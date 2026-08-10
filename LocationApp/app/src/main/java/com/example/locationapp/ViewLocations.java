package com.example.locationapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ViewLocations extends AppCompatActivity {

    private ListView listView;
    private Spinner ratingSpinner;
    private HelperDatabase dbh;
    private ArrayList<Location> locationList;

    private int currentMode = 0; // 0: Default, 1: Protanopia, 2: Deuteranopia, 3: Tritanopia
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_locations);



        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Enable the "Home" button for navigation
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable home button
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home); // Use custom home icon
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Hide title text
        }

        textView = findViewById(R.id.textView);
        ImageButton accessibilityButton = findViewById(R.id.accessibilityButton);

        accessibilityButton.setOnClickListener(v -> {
            int newColor;
            String toastMessage;
            switch (currentMode) {
                case 1:
                    newColor = Color.parseColor("#FF0000"); // Protanopia-friendly color
                    toastMessage = "Protanopia-friendly mode";
                    break;
                case 2:
                    newColor = Color.parseColor("#00FF00"); // Deuteranopia-friendly color
                    toastMessage = "Deuteranopia-friendly mode";
                    break;
                case 3:
                    newColor = Color.parseColor("#0000FF"); // Tritanopia-friendly color
                    toastMessage = "Tritanopia-friendly mode";
                    break;
                default:
                    newColor = Color.BLACK; // Default color
                    toastMessage = "Default mode";
                    currentMode = 0; // Reset after the last mode
                    break;
            }
            currentMode++;  // Increment mode for next color

            updateTextColors((ViewGroup) findViewById(R.id.rootLayout), newColor);
            listView.setTag(R.id.currentMode);
            // Fetch updated locations and update the adapter
            ArrayList<Location> updatedLocations = fetchLocationsFromDatabase();
            ((LocationAdapter) listView.getAdapter()).updateLocations(updatedLocations);
            // Display toast with the message
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show();
        });

        // Initialize the ListView, Spinner, and database helper
        listView = findViewById(R.id.listView);
        ratingSpinner = findViewById(R.id.ratingSpinner);
        dbh = new HelperDatabase(this);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Location location = fetchLocationsFromDatabase().get(position);  // Get the selected item
            long locationId = location.getId();           // Extract the ID
            Intent intent = new Intent(ViewLocations.this, EditLocation.class);
            intent.putExtra("locationId", locationId);    // Pass the ID
            startActivity(intent);
        });

        // Fetch locations from the database
        locationList = fetchLocationsFromDatabase();

        // Set up ArrayAdapter to display the locations in ListView
        LocationAdapter adapter = new LocationAdapter(this, locationList);
        listView.setAdapter(adapter);

        // Set up the Spinner with sorting options
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.sorting_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ratingSpinner.setAdapter(spinnerAdapter);

        // Set an item selected listener on the Spinner
        ratingSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    // Sort by rating high to low
                    Collections.sort(locationList, new Comparator<Location>() {
                        @Override
                        public int compare(Location loc1, Location loc2) {
                            return Integer.compare(loc2.getRating(), loc1.getRating());
                        }
                    });
                } else if (position == 1) {
                    // Sort by rating low to high
                    Collections.sort(locationList, new Comparator<Location>() {
                        @Override
                        public int compare(Location loc1, Location loc2) {
                            return Integer.compare(loc1.getRating(), loc2.getRating());
                        }
                    });
                }

                // Notify the adapter that the data has changed
                ((LocationAdapter) listView.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parentView) {
                // No action needed
            }
        });
    }


    // Recursively update text colors for all TextViews and ListView items in the layout
    private void updateTextColors(ViewGroup parent, int color) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setTextColor(color);
            } else if (child instanceof ViewGroup) {
                updateTextColors((ViewGroup) child, color); // Recursively check child views
            } else if (child instanceof ListView) {
                // Update ListView items
                for (int j = 0; j < ((ListView) child).getChildCount(); j++) {
                    View listItem = ((ListView) child).getChildAt(j);
                    if (listItem instanceof TextView) {
                        ((TextView) listItem).setTextColor(color);
                    }
                }
            }
        }
    }

    // Fetch locations from the database
    private ArrayList<Location> fetchLocationsFromDatabase() {
        ArrayList<Location> locations = new ArrayList<>();

        // Query the database to fetch all locations
        Cursor cursor = dbh.getReadableDatabase().query(
                "LOCATION",
                new String[]{"_id", "LOCATION_NAME", "LOCATION_ADDRESS", "COUNTRY", "DATE", "RATING", "GPS_X", "GPS_Y"},
                null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                String locationName = cursor.getString(cursor.getColumnIndexOrThrow("LOCATION_NAME"));
                String locationAddress = cursor.getString(cursor.getColumnIndexOrThrow("LOCATION_ADDRESS"));
                String country = cursor.getString(cursor.getColumnIndexOrThrow("COUNTRY"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("DATE"));
                int rating = cursor.getInt(cursor.getColumnIndexOrThrow("RATING"));
                double gpsX = cursor.getDouble(cursor.getColumnIndexOrThrow("GPS_X"));
                double gpsY = cursor.getDouble(cursor.getColumnIndexOrThrow("GPS_Y"));

                // Add a Location object to the list
                locations.add(new Location(id, locationName, locationAddress, country, date, rating, gpsX, gpsY));
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Toast.makeText(this, "No locations found", Toast.LENGTH_SHORT).show();
        }

        return locations;
    }

    // Custom adapter for displaying locations
    public static class LocationAdapter extends ArrayAdapter<Location> {

        private final Context context;
        private final ArrayList<Location> locations;

        // Constructor
        public LocationAdapter(Context context, ArrayList<Location> locations) {
            super(context, 0, locations);
            this.context = context;
            this.locations = locations;
        }

        // Get view for each location item
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
            }

            // Get the current location
            Location location = getItem(position);

            // Set the location details in the TextView
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText(location.getLocationDetails()); // Automatically includes GPS coordinates

            // Update the color if changed
            int currentColor = textView.getCurrentTextColor();
            Integer savedColor = (Integer) parent.getTag(R.id.currentMode);

            if (savedColor != null && currentColor != savedColor) {
                textView.setTextColor(savedColor);
            }

            return convertView;
        }

        // Method to update data
        public void updateLocations(ArrayList<Location> newLocations) {
            this.locations.clear();
            this.locations.addAll(newLocations);
            notifyDataSetChanged();
        }
    }

    // Location class to represent a location object
    public static class Location {

        private final String locationName; // Add Location Name
        private final String locationAddress; // Add Location Address
        private final String country; // Add Country
        private final String date;  // Add Date
        private final int rating;   // Add Rating Value
        private final double gpsX;  // Add GPS_X
        private final double gpsY;  // Add GPS_Y

        private final long id; // Add Entry IDs

        // Constructor
        public Location(long id, String locationName, String locationAddress, String country, String date, int rating, double gpsX, double gpsY) {
            this.id = id;
            this.locationName = locationName;
            this.locationAddress = locationAddress;
            this.country = country;
            this.date = date;
            this.rating = rating;
            this.gpsX = gpsX;  // Initialize GPS_X
            this.gpsY = gpsY;  // Initialize GPS_Y
        }

        // Getter for rating
        public int getRating() {
            return rating;
        }

        // Getter for GPS coordinates
        public double getGpsX() {
            return gpsX;
        }

        public double getGpsY() {
            return gpsY;
        }

        public long getId() {
            return id;
        }

        // Getter for location details (for displaying in ListView)
        public String getLocationDetails() {
            return "Name: " + locationName + "\n" +
                    "Address: " + locationAddress + "\n" +
                    "Country: " + country + "\n" +
                    "Date: " + date + "\n" +
                    "Rating: " + rating + "\n" +
                    "GPS Coordinates: (" + gpsX + ", " + gpsY + ")"; // Show GPS coordinates
        }
    }

    public void onClickAddLocation(View view) {
        Intent intent = new Intent(this, NewLocation.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle toolbar item clicks
        if (item.getItemId() == android.R.id.home) {
            // Navigate back to the home page
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Finish current activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}