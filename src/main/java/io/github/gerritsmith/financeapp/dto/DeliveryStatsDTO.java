package io.github.gerritsmith.financeapp.dto;

public class DeliveryStatsDTO {

    private int deliveryCount = 0;

    private double totalAppMiles = 0.0;
    private double totalAppWaitTime = 0.0;
    private double totalDeliveryMiles = 0.0;
    private double totalDeliveryTime = 0.0;
    private double totalBasePay = 0.0;

    private double totalTip = 0.0;
    private double totalCash = 0.0;

    // Constructor
    public DeliveryStatsDTO() {}

    public DeliveryStatsDTO(int deliveryCount,
                            Double totalAppMiles,
                            Double totalAppWaitTime,
                            Double totalDeliveryMiles,
                            Double totalDeliveryTime,
                            Double totalBasePay,
                            double totalTip,
                            double totalCash) {
        this.deliveryCount = deliveryCount;
        this.totalAppMiles = (totalAppMiles == null) ? 0 : totalAppMiles;
        this.totalAppWaitTime = (totalAppWaitTime == null) ? 0 : totalAppWaitTime;
        this.totalDeliveryMiles = (totalDeliveryMiles == null) ? 0 : totalDeliveryMiles;
        this.totalDeliveryTime = (totalDeliveryTime == null) ? 0 : totalDeliveryTime;
        this.totalBasePay = (totalBasePay == null) ? 0 : totalBasePay;
        this.totalTip = totalTip;
        this.totalCash = totalCash;
    }

    // Static Methods
    public static DeliveryStatsDTO sum(DeliveryStatsDTO a, DeliveryStatsDTO b) {
        return new DeliveryStatsDTO(
                a.deliveryCount + b.deliveryCount,
                a.totalAppMiles + b.totalAppMiles,
                a.totalAppWaitTime + b.totalAppWaitTime,
                a.totalDeliveryMiles + b.totalDeliveryMiles,
                a.totalDeliveryTime + b.totalDeliveryTime,
                a.totalBasePay + b.totalBasePay,
                a.totalTip + b.totalTip,
                a.totalCash + b.totalCash);
    }

    // Getters and Builder Setters
    public int getDeliveryCount() {
        return deliveryCount;
    }

    public DeliveryStatsDTO setDeliveryCount(int deliveryCount) {
        this.deliveryCount = deliveryCount;
        return this;
    }

    public double getTotalAppMiles() {
        return totalAppMiles;
    }

    public DeliveryStatsDTO setTotalAppMiles(double totalAppMiles) {
        this.totalAppMiles = totalAppMiles;
        return this;
    }

    public double getTotalAppWaitTime() {
        return totalAppWaitTime;
    }

    public DeliveryStatsDTO setTotalAppWaitTime(double totalAppWaitTime) {
        this.totalAppWaitTime = totalAppWaitTime;
        return this;
    }

    public double getTotalDeliveryMiles() {
        return totalDeliveryMiles;
    }

    public DeliveryStatsDTO setTotalDeliveryMiles(double totalDeliveryMiles) {
        this.totalDeliveryMiles = totalDeliveryMiles;
        return this;
    }

    public double getTotalDeliveryTime() {
        return totalDeliveryTime;
    }

    public DeliveryStatsDTO setTotalDeliveryTime(double totalDeliveryTime) {
        this.totalDeliveryTime = totalDeliveryTime;
        return this;
    }

    public double getTotalBasePay() {
        return totalBasePay;
    }

    public DeliveryStatsDTO setTotalBasePay(double totalBasePay) {
        this.totalBasePay = totalBasePay;
        return this;
    }

    public double getTotalTip() {
        return totalTip;
    }

    public DeliveryStatsDTO setTotalTip(double totalTip) {
        this.totalTip = totalTip;
        return this;
    }

    public double getTotalCash() {
        return totalCash;
    }

    public DeliveryStatsDTO setTotalCash(double totalCash) {
        this.totalCash = totalCash;
        return this;
    }

}
