package io.github.gerritsmith.financeapp.model;

import io.github.gerritsmith.financeapp.dto.ShiftFormDTO;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Shift extends AbstractEntity {

    @ManyToOne
    private User user;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    // Constructors
    public Shift() {}

    public Shift(User user, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.user = user;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Shift(User user, ShiftFormDTO shiftFormDTO) {
        this.user = user;
        this.date = shiftFormDTO.getDate();
        this.startTime = shiftFormDTO.getStartTime();
        this.endTime = shiftFormDTO.getEndTime();
    }

    // Setters
    public void setUser(User user) {
        this.user = user;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    // Getters
    public User getUser() {
        return user;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    // Equals, hash, toString

    @Override
    public String toString() {
        return "Shift: " + date +
                " from " + startTime +
                " to " + endTime;
    }
    
}
