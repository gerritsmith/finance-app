package io.github.gerritsmith.financeapp.dto.form;

import io.github.gerritsmith.financeapp.model.Location;
import io.github.gerritsmith.financeapp.model.LocationType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class LocationFormDTO {

    @NotBlank(message = "location name is required")
    private String name;

    private String address;
    private String apt;

    @Pattern(regexp = "((-?\\d+\\.?\\d*|\\d*\\.\\d+)\\s*,\\s*(-?\\d+\\.?\\d*|\\d*\\.\\d+))?", message = "must be in the form 'lat, long'")
    private String latLong;

    @NotNull(message = "location type is required")
    private LocationType type;

    private final LocationType[] locationTypes = LocationType.values();

    // Constructors
    public LocationFormDTO() {}

    public LocationFormDTO(Location location) {
        name = location.getName();
        address = location.getAddress();
        apt = location.getApt();
        if (location.getLatitude() == null || location.getLongitude() == null) {
            latLong = "";
        } else {
            latLong = location.getLatitude() + ", " + location.getLongitude();
        }
        type = location.getType();
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApt() {
        return apt;
    }

    public void setApt(String apt) {
        this.apt = apt;
    }

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public LocationType[] getLocationTypes() {
        return locationTypes;
    }
    
}
