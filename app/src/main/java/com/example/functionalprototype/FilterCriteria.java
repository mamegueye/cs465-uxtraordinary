package com.example.functionalprototype;

/**
 * Data class to store filter criteria for study space search
 * This will be passed to the results activity and used for database queries
 */
public class FilterCriteria {
    // Distance from current location in miles (0.0 - 5.0)
    private float distanceMiles;

    // Whether to show only spaces open now
    private boolean openNow;

    // Whether to filter for spaces with cafe/food facilities
    private boolean hasCafeFood;

    // Current location name (from GPS or user selection)
    private String currentLocation;

    /**
     * Constructor with default values
     */
    public FilterCriteria() {
        this.distanceMiles = 0.5f;
        this.openNow = false;
        this.hasCafeFood = false;
        this.currentLocation = "Thomas M. Siebel Center for Computer Science";
    }

    /**
     * Constructor with all parameters
     */
    public FilterCriteria(float distanceMiles, boolean openNow, boolean hasCafeFood, String currentLocation) {
        this.distanceMiles = distanceMiles;
        this.openNow = openNow;
        this.hasCafeFood = hasCafeFood;
        this.currentLocation = currentLocation;
    }

    // Getters and Setters
    public float getDistanceMiles() {
        return distanceMiles;
    }

    public void setDistanceMiles(float distanceMiles) {
        this.distanceMiles = distanceMiles;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public boolean isHasCafeFood() {
        return hasCafeFood;
    }

    public void setHasCafeFood(boolean hasCafeFood) {
        this.hasCafeFood = hasCafeFood;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    @Override
    public String toString() {
        return "FilterCriteria{" +
                "distanceMiles=" + distanceMiles +
                ", openNow=" + openNow +
                ", hasCafeFood=" + hasCafeFood +
                ", currentLocation='" + currentLocation + '\'' +
                '}';
    }
}