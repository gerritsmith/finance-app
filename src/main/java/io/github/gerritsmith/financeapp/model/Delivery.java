package io.github.gerritsmith.financeapp.model;

import io.github.gerritsmith.financeapp.dto.form.DeliveryFormDTO;
import io.github.gerritsmith.financeapp.dto.form.DeliveryLegFormDTO;

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

    private Double appMiles;
    private Double appWaitTime;
    private Double totalMiles;
    private Duration totalTime;
    private Double basePay;
    private Double adjustments;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    private List<DeliveryLeg> legs = new ArrayList<>();

    private double total;

    @ManyToOne
    private Shift shift;

    // Constructors
    public Delivery() {}

    public Delivery(User user, DeliveryFormDTO deliveryFormDTO) {
        this.user = user;
        date = deliveryFormDTO.getDate();
        time = deliveryFormDTO.getTime();
        String appMilesString = deliveryFormDTO.getAppMiles();
        appMiles = appMilesString.isEmpty() ? null : Double.parseDouble(appMilesString);
        String appWaitTimeString = deliveryFormDTO.getAppWaitTime();
        appWaitTime = appWaitTimeString.isEmpty() ? null : Double.parseDouble(appWaitTimeString);
        String totalMilesString = deliveryFormDTO.getTotalMiles();
        totalMiles = totalMilesString.isEmpty() ? null : Double.parseDouble(totalMilesString);
        String totalTimeString = deliveryFormDTO.getTotalTime();
        totalTime = totalTimeString.isEmpty() ? null : Duration.ofMinutes(Long.parseLong(totalTimeString));
        String basePayString = deliveryFormDTO.getBasePay();
        basePay = basePayString.isEmpty() ? null : Double.parseDouble(basePayString);
        String adjustmentsString = deliveryFormDTO.getAdjustments();
        adjustments = adjustmentsString.isEmpty() ? null : Double.parseDouble(adjustmentsString);
        for (DeliveryLegFormDTO deliveryLegFormDTO : deliveryFormDTO.getLegs()) {
            legs.add(new DeliveryLeg(this, deliveryLegFormDTO));
        }
        total = computeTotal();
    }

    // Methods
    public String displayTime() {
        return time.format(DateTimeFormatter.ofPattern("h:mm a"));
    }

    private double computeTotal() {
        double sum = 0;
        sum += (basePay == null) ? 0 : basePay;
        sum += (adjustments == null) ? 0 : adjustments;
        for (DeliveryLeg deliveryLeg : legs) {
            sum += (deliveryLeg.getTip() == null) ? 0 : deliveryLeg.getTip();
        }
        return sum;
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

    public Double getBasePay() {
        return basePay;
    }

    public void setBasePay(Double basePay) {
        this.basePay = basePay;
    }

    public Double getAdjustments() {
        return adjustments;
    }

    public void setAdjustments(Double adjustments) {
        this.adjustments = adjustments;
    }

    public List<DeliveryLeg> getLegs() {
        return legs;
    }

    public void setLegs(List<DeliveryLeg> legs) {
        this.legs = legs;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    // Equals, hash, toString
    @Override
    public String toString() {
        return "Delivery: " + date + " " + time +
                ", total=" + total;
    }

}
