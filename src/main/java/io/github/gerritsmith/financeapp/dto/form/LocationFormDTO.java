package io.github.gerritsmith.financeapp.dto.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LocationFormDTO {

    @NotBlank(message = "location name is required")
    private String name;

    private String address;
    private String apt;
    
    private String latLong;

    @NotNull(message = "location type is required")
    private String type;

    // Constructors
    public LocationFormDTO() {}

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
