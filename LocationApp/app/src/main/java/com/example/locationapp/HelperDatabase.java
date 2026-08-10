package com.example.locationapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HelperDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "Locations";
    private static final int DB_VERSION = 1;
    public SQLiteDatabase db;

    public HelperDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = this.getWritableDatabase();

        Cursor cursor = db.query("LOCATION",
                new String[]{"_id", "LOCATION_NAME", "LOCATION_ADDRESS",
                        "COUNTRY", "GPS_X", "GPS_Y"},
                null, null, null, null, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE LOCATION (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "LOCATION_NAME TEXT, " +
                "LOCATION_ADDRESS TEXT, " +
                "COUNTRY TEXT, " +
                "DATE DATE, " +
                "GPS_X DOUBLE, " +
                "GPS_Y DOUBLE, " +
                "RATING INTEGER" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS LOCATION");
        onCreate(db);
    }

    public static long insertLocation(SQLiteDatabase db, String locationName,
                                      String locationAddress, String country, String dateStr,
                                      float gpsXStr, float gpsYStr, int rating) {
        ContentValues recordValues = new ContentValues();
        recordValues.put("LOCATION_NAME", locationName);
        recordValues.put("LOCATION_ADDRESS", locationAddress);
        recordValues.put("COUNTRY", country);
        recordValues.put("DATE", dateStr); // Use the formatted date string
        recordValues.put("GPS_X", gpsXStr);
        recordValues.put("GPS_Y", gpsYStr);
        recordValues.put("RATING", rating);

        return db.insert("LOCATION", null, recordValues);
    }
}
