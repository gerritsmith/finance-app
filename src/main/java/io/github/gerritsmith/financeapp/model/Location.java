package io.github.gerritsmith.financeapp.model;

import io.github.gerritsmith.financeapp.dto.form.LocationFormDTO;
import io.github.gerritsmith.financeapp.dto.upload.DeliveryCSVRow;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Location extends AbstractEntity {

    @ManyToOne
    private User user;

    private String name;
    private String address;
    private String apt;
    private Double latitude;
    private Double longitude;
    private LocationType type;

    // Constructors
    public Location() {}

    public Location(User user, LocationFormDTO locationFormDTO) {
        this.user = user;
        this.name = locationFormDTO.getName();
        this.address = locationFormDTO.getAddress();
        this.apt = locationFormDTO.getApt();
        parseLatLongString(locationFormDTO.getLatLong());
        this.type = locationFormDTO.getType();
    }

    public Location(User user, LocationType type, DeliveryCSVRow row) {
        this.user = user;
        this.type = type;
        if (type == LocationType.PICKUP) {
            name = row.getPickupLocationName();
        } else {
            name = row.getDropoffName();
            address = row.getDropoffLocationAddress();
            apt = row.getApt();
            parseLatLongString(row.getLatLong());
        }
    }

    // Methods
    public void update(Location location) {
        name = location.getName();
        address = location.getAddress();
        apt = location.getApt();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        type = location.getType();
    }

    private void parseLatLongString(String latLong) {
        String[] latLongParts = latLong.split("\\s*,\\s*");
        if (latLongParts.length == 2) {
            this.latitude = latLongParts[0].isEmpty() ? null : Double.parseDouble(latLongParts[0]);
            this.longitude = latLongParts[1].isEmpty() ? null : Double.parseDouble(latLongParts[1]);
        } else {
            this.latitude = null;
            this.longitude = null;
        }
    }

    // Getters and Setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    // Equals, hash, toString
    @Override
    public String toString() {
        return type + " location: " + name + " at " + address;
    }

}
