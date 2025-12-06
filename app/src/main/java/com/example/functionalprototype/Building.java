package com.example.functionalprototype;

import android.util.Log;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Building {
    public String building_name;
    public String monday;
    public String tuesday;
    public String wednesday;
    public String thursday;
    public String friday;
    public String saturday;
    public String sunday;
    public Float cleanliness;
    public Float latitude;
    public Float longitude;
    public String cafe;
    private Float userLat;
    private Float userLng;
    public String address;
    public String info;

    public Building(String building_name, String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday, Float cleanliness, Float latitude, Float longitude, String cafe, String address, String info) {
        this.building_name = building_name;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.cleanliness = cleanliness;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cafe = cafe;
        this.address = address;
        this.info = info;
    }

    public String getHoursToday() {
        int currentDayOfWeek = LocalDate.now().getDayOfWeek().getValue();
        String[] hours = {
                this.sunday, this.monday, this.tuesday,
                this.wednesday, this.thursday, this.friday,
                this.saturday
        };
        return hours[currentDayOfWeek];
    }

    public float calculateDistanceFrom(float lat, float lng) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(this.latitude-lat);
        double dLng = Math.toRadians(this.longitude-lng);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(lat)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distanceInMeters = earthRadius * c;
        Log.d("calculateDistance", String.format(
                "Distance between (%f, %f) and (%f, %f) is %f meters.",
                this.latitude, this.longitude, lat, lng, distanceInMeters));
        return (float) (distanceInMeters / 1609.34);
    }

    public void setUserLatLng(float lat, float lng) {
        userLat = lat;
        userLng = lng;
    }

    public boolean hasUserLatLng() {
        return userLat != null && userLng != null;
    }

    public float calculateDistanceFromUserLatLng() {
        return calculateDistanceFrom(userLat, userLng);
    }

    public boolean isOpenNow() {
        ArrayList<LocalTime> openingAndClosing = parseHours(this.getHoursToday());
        LocalTime opening = openingAndClosing.get(0);
        LocalTime closing = openingAndClosing.get(1);
        LocalTime current = LocalTime.now();
        return !(current.isBefore(opening) || current.isAfter(closing));
    }

    private ArrayList<LocalTime> parseHours(String hours) {
        ArrayList<LocalTime> openingAndClosing = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[hh:mma][h:mma][hha][ha]");
        for (String s : hours.split("-")) {
            s = s.trim();
            openingAndClosing.add(LocalTime.parse(s, formatter));
        }
        return openingAndClosing;
    }
}
