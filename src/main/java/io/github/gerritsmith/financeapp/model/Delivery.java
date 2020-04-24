package io.github.gerritsmith.financeapp.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Delivery extends AbstractEntity {

    @ManyToOne
    private User user;

    private LocalDate date;
    private LocalTime time;
    private double total;

    // Constructors
    public Delivery() {}

    public Delivery(User user, LocalDate date, LocalTime time, double total) {
        this.user = user;
        this.date = date;
        this.time = time;
        this.total = total;
    }

    // Methods
    public String displayTime() {
        return time.format(DateTimeFormatter.ofPattern("h:mm a"));
    }

    // Setters
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Getters
    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public double getTotal() {
        return total;
    }

    public User getUser() {
        return user;
    }

    // Equals, hash, toString
    @Override
    public String toString() {
        return "Delivery: " + date + " " + time +
                ", total=" + total;
    }

}
