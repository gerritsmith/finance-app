package io.github.gerritsmith.financeapp.model;

import io.github.gerritsmith.financeapp.dto.DeliveryFormDTO;

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

    public Delivery(User user, DeliveryFormDTO deliveryFormDTO) {
        this.user = user;
        date = deliveryFormDTO.getDate();
        time = deliveryFormDTO.getTime();
        String totalString = deliveryFormDTO.getTotal();
        totalString = totalString.isEmpty() ? "0" : totalString;
        total = Double.parseDouble(totalString);
    }

    // Methods
    public String displayTime() {
        return time.format(DateTimeFormatter.ofPattern("h:mm a"));
    }

    public void update(Delivery delivery) {
        date = delivery.getDate();
        time = delivery.getTime();
        total = delivery.getTotal();
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
