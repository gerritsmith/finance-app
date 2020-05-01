package io.github.gerritsmith.financeapp.dto;

import io.github.gerritsmith.financeapp.model.Shift;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalTime;

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

    // Constructors
    public ShiftFormDTO() {}

    public ShiftFormDTO(Shift shift) {
        date = shift.getDate();
        startTime = shift.getStartTime();
        endTime = shift.getEndTime();
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

}
