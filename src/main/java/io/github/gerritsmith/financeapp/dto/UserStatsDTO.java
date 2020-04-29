package io.github.gerritsmith.financeapp.dto;

import java.time.Duration;

public class UserStatsDTO {

    private long deliveryCount;
    private double deliveryTotalRevenue;
    private double revenuePerDelivery;

    private long shiftCount;
    private Duration shiftTotalDuration;
    private double revenuePerHour;


    // Constructors
    public UserStatsDTO() {}

    // Getters
    public long getDeliveryCount() {
        return deliveryCount;
    }

    public double getDeliveryTotalRevenue() {
        return deliveryTotalRevenue;
    }

    public double getRevenuePerDelivery() {
        return revenuePerDelivery;
    }

    public long getShiftCount() {
        return shiftCount;
    }

    public String getShiftTotalDuration() {
        String displayDuration = shiftTotalDuration.toHours() + " hr " +
                shiftTotalDuration.toMinutesPart() + " min";
        return displayDuration;
    }

    public double getRevenuePerHour() {
        return revenuePerHour;
    }

    // Setters
    public void setDeliveryCount(long deliveryCount) {
        this.deliveryCount = deliveryCount;
    }

    public void setDeliveryTotalRevenue(double deliveryTotalRevenue) {
        this.deliveryTotalRevenue = deliveryTotalRevenue;
    }

    public void setRevenuePerDelivery(double revenuePerDelivery) {
        this.revenuePerDelivery = revenuePerDelivery;
    }

    public void setShiftCount(long shiftCount) {
        this.shiftCount = shiftCount;
    }

    public void setShiftTotalDuration(Duration shiftTotalDuration) {
        this.shiftTotalDuration = shiftTotalDuration;
    }

    public void setRevenuePerHour(double revenuePerHour) {
        this.revenuePerHour = revenuePerHour;
    }

}
