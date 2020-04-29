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
    public UserStatsDTO setDeliveryCount(long deliveryCount) {
        this.deliveryCount = deliveryCount;
        return this;
    }

    public UserStatsDTO setDeliveryTotalRevenue(double deliveryTotalRevenue) {
        this.deliveryTotalRevenue = deliveryTotalRevenue;
        return this;
    }

    public UserStatsDTO setRevenuePerDelivery(double revenuePerDelivery) {
        this.revenuePerDelivery = revenuePerDelivery;
        return this;
    }

    public UserStatsDTO setShiftCount(long shiftCount) {
        this.shiftCount = shiftCount;
        return this;
    }

    public UserStatsDTO setShiftTotalDuration(Duration shiftTotalDuration) {
        this.shiftTotalDuration = shiftTotalDuration;
        return this;
    }

    public UserStatsDTO setRevenuePerHour(double revenuePerHour) {
        this.revenuePerHour = revenuePerHour;
        return this;
    }

}
