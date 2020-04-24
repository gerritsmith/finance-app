package io.github.gerritsmith.financeapp.model;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Delivery extends AbstractEntity {

    private LocalDate date;
    private LocalTime time;
    private double total;

    // Constructors
    public Delivery() {}

    public Delivery(LocalDate date, LocalTime time, double total) {
        this.date = date;
        this.time = time;
        this.total = total;
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

    // Equals, hash, toString
    @Override
    public String toString() {
        return "Delivery: " + date + " " + time +
                ", total=" + total;
    }

}
