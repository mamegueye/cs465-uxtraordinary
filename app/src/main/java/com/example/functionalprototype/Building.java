package com.example.functionalprototype;

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

    public Building(String building_name, String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday, Float cleanliness, Float latitude, Float longitude, String cafe) {
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
    }
}
