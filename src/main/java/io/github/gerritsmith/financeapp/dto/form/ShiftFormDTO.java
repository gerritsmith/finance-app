package io.github.gerritsmith.financeapp.dto.form;

import io.github.gerritsmith.financeapp.model.Delivery;
import io.github.gerritsmith.financeapp.model.Shift;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ShiftFormDTO {

    @NotNull(message = "date is required")
    @PastOrPresent(message = "date can't be in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "start time is required")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull(message = "end time is required")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Pattern(regexp = "\\d*", message = "mileage must be an non-negative integer")
    private String startMileage;

    @Pattern(regexp = "\\d*", message = "mileage must be an non-negative integer")
    private String endMileage;

    @Pattern(regexp = "(\\d+\\.?\\d{0,1}|\\.\\d)?", message = "miles must be in the form 0.0")
    private String miles;

    @Pattern(regexp = "(\\d+\\.?\\d{0,1}|\\.\\d)?", message = "miles must be in the form 0.0")
    private String mpg;

    private List<Delivery> deliveries;

    // Constructors
    public ShiftFormDTO() {}

    public ShiftFormDTO(Shift shift) {
        date = shift.getDate();
        startTime = shift.getStartTime();
        endTime = shift.getEndTime();
        startMileage = (shift.getStartMileage() == null) ? "" : shift.getStartMileage().toString();
        endMileage = (shift.getEndMileage() == null) ? "" : shift.getEndMileage().toString();
        miles = (shift.getMiles() == null) ? "" : shift.getMiles().toString();
        mpg = (shift.getMpg() == null) ? "" : shift.getMpg().toString();
        deliveries = shift.getDeliveries();
    }

    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getStartMileage() {
        return startMileage;
    }

    public void setStartMileage(String startMileage) {
        this.startMileage = startMileage;
    }

    public String getEndMileage() {
        return endMileage;
    }

    public void setEndMileage(String endMileage) {
        this.endMileage = endMileage;
    }

    public String getMiles() {
        return miles;
    }

    public void setMiles(String miles) {
        this.miles = miles;
    }

    public String getMpg() {
        return mpg;
    }

    public void setMpg(String mpg) {
        this.mpg = mpg;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

}
