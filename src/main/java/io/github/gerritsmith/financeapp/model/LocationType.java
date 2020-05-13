package io.github.gerritsmith.financeapp.model;

public enum LocationType {

    DROPOFF("Dropoff"),
    PICKUP("Pickup");

    private final String displayName;

    // Constructors
    LocationType(String displayName) {
        this.displayName = displayName;
    }

    // Getters
    public String getDisplayName() {
        return displayName;
    }

}
