package io.github.gerritsmith.financeapp.model;

import io.github.gerritsmith.financeapp.dto.form.ShiftFormDTO;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Shift extends AbstractEntity {

    @ManyToOne
    private User user;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer startMileage;
    private Integer endMileage;
    private Double miles;
    private Double mpg;

    @OneToMany(mappedBy = "shift")
    private final List<Delivery> deliveries = new ArrayList<>();

    // Constructors
    public Shift() {}

    public Shift(User user, ShiftFormDTO shiftFormDTO) {
        this.user = user;
        this.date = shiftFormDTO.getDate();
        this.startTime = shiftFormDTO.getStartTime();
        this.endTime = shiftFormDTO.getEndTime();
        String startMileageString = shiftFormDTO.getStartMileage();
        startMileage = startMileageString.isEmpty() ? null : Integer.parseInt(startMileageString);
        String endMileageString = shiftFormDTO.getEndMileage();
        endMileage = endMileageString.isEmpty() ? null : Integer.parseInt(endMileageString);
        String milesString = shiftFormDTO.getMiles();
        miles = milesString.isEmpty() ? null : Double.parseDouble(milesString);
        String mpgString = shiftFormDTO.getMpg();
        mpg = mpgString.isEmpty() ? null : Double.parseDouble(mpgString);
    }

    // Methods
    public void update(Shift shift) {
        date = shift.getDate();
        startTime = shift.getStartTime();
        endTime = shift.getEndTime();
        startMileage = shift.getStartMileage();
        endMileage = shift.getEndMileage();
        miles = shift.getMiles();
        mpg = shift.getMpg();
    }

    // Getters and Setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

    public Integer getStartMileage() {
        return startMileage;
    }

    public void setStartMileage(Integer startMileage) {
        this.startMileage = startMileage;
    }

    public Integer getEndMileage() {
        return endMileage;
    }

    public void setEndMileage(Integer endMileage) {
        this.endMileage = endMileage;
    }

    public Double getMiles() {
        return miles;
    }

    public void setMiles(Double miles) {
        this.miles = miles;
    }

    public Double getMpg() {
        return mpg;
    }

    public void setMpg(Double mpg) {
        this.mpg = mpg;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    // Equals, hash, toString
    @Override
    public String toString() {
        return "Shift: " + date +
                " from " + startTime +
                " to " + endTime;
    }
    
}
