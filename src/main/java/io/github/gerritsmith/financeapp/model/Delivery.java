package io.github.gerritsmith.financeapp.model;

import io.github.gerritsmith.financeapp.dto.DeliveryFormDTO;
import io.github.gerritsmith.financeapp.dto.DeliveryLegFormDTO;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Delivery extends AbstractEntity {

    @ManyToOne
    private User user;

    private LocalDate date;
    private LocalTime time;
    private double total;

    private Double appMiles;
    private Double appWaitTime;
    private Double totalMiles;
    private Duration totalTime;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    private List<DeliveryLeg> legs = new ArrayList<>();

    // Constructors
    public Delivery() {}

    public Delivery(User user, DeliveryFormDTO deliveryFormDTO) {
        this.user = user;
        date = deliveryFormDTO.getDate();
        time = deliveryFormDTO.getTime();
        String totalString = deliveryFormDTO.getTotal();
        total = totalString.isEmpty() ? 0 : Double.parseDouble(totalString);
        String appMilesString = deliveryFormDTO.getAppMiles();
        appMiles = appMilesString.isEmpty() ? null : Double.parseDouble(appMilesString);
        String appWaitTimeString = deliveryFormDTO.getAppWaitTime();
        appWaitTime = appWaitTimeString.isEmpty() ? null : Double.parseDouble(appWaitTimeString);
        String totalMilesString = deliveryFormDTO.getTotalMiles();
        totalMiles = totalMilesString.isEmpty() ? null : Double.parseDouble(totalMilesString);
        String totalTimeString = deliveryFormDTO.getTotalTime();
        totalTime = totalTimeString.isEmpty() ? null : Duration.ofMinutes(Long.parseLong(totalTimeString));
        for (DeliveryLegFormDTO deliveryLegFormDTO : deliveryFormDTO.getLegs()) {
            legs.add(new DeliveryLeg(this, deliveryLegFormDTO));
        }
    }

    // Methods
    public String displayTime() {
        return time.format(DateTimeFormatter.ofPattern("h:mm a"));
    }

    public void update(Delivery delivery) {
        date = delivery.getDate();
        time = delivery.getTime();
        total = delivery.getTotal();
        appMiles = delivery.getAppMiles();
        appWaitTime = delivery.getAppWaitTime();
        totalMiles = delivery.getTotalMiles();
        totalTime = delivery.getTotalTime();
        int originalSize = legs.size();
        int newSize = delivery.getLegs().size();
        for (int i = 0; i < Math.min(originalSize, newSize); i++) {
            legs.get(i).update(delivery.getLegs().get(i));
        }
        for (int i = originalSize; i < newSize; i++) {
            DeliveryLeg deliveryLeg = delivery.getLegs().get(i);
            deliveryLeg.setDelivery(this);
            legs.add(deliveryLeg);
        }
        for (int i = newSize; i < originalSize; i++) {
            legs.remove(legs.size() - 1);
        }
        System.out.println("before save");
        System.out.println(legs);
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

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Double getAppMiles() {
        return appMiles;
    }

    public void setAppMiles(Double appMiles) {
        this.appMiles = appMiles;
    }

    public Double getAppWaitTime() {
        return appWaitTime;
    }

    public void setAppWaitTime(Double appWaitTime) {
        this.appWaitTime = appWaitTime;
    }

    public Double getTotalMiles() {
        return totalMiles;
    }

    public void setTotalMiles(Double totalMiles) {
        this.totalMiles = totalMiles;
    }

    public Duration getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Duration totalTime) {
        this.totalTime = totalTime;
    }

    public List<DeliveryLeg> getLegs() {
        return legs;
    }

    public void setLegs(List<DeliveryLeg> legs) {
        this.legs = legs;
    }

    // Equals, hash, toString
    @Override
    public String toString() {
        return "Delivery: " + date + " " + time +
                ", total=" + total;
    }

}
