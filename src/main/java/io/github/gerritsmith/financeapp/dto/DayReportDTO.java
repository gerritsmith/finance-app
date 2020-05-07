package io.github.gerritsmith.financeapp.dto;

import io.github.gerritsmith.financeapp.model.Delivery;
import io.github.gerritsmith.financeapp.model.Expense;
import io.github.gerritsmith.financeapp.model.Shift;

import java.time.Duration;
import java.util.List;

public class DayReportDTO {

    private List<Delivery> deliveries;
    private List<Shift> shifts;
    private List<Expense> expenses;

    private int deliveryCount;

    private double totalRevenue;
    private Duration totalShiftHours;
    private double totalShiftMiles;
    private double totalExpenses;

    // Computed fields
    public int getDeliveryGroupCount() {
        return deliveries.size();
    }

    public double getDeliveriesPerGroup() {
        return deliveryCount / (double) getDeliveryGroupCount();
    }

    public double getTotalShiftHoursAsDecimal() {
        return totalShiftHours.toHours() + totalShiftHours.toMinutesPart()/60.0;
    }

    public double getDeliveriesPerHour() {
        return deliveryCount/getTotalShiftHoursAsDecimal();
    }

    public double getRevenuePerHour() {
        return totalRevenue/getTotalShiftHoursAsDecimal();
    }

    public double getShiftMilesPerHour() {
        return totalShiftMiles/getTotalShiftHoursAsDecimal();
    }

    public double getRevenuePerDelivery() {
        return totalRevenue/deliveryCount;
    }

    public double getShiftMilesPerDelivery() {
        return totalShiftMiles/deliveryCount;
    }

    public String getTotalShiftHoursString() {
        return totalShiftHours.toHours() + " hr " +
                totalShiftHours.toMinutesPart() + " min";
    }


    // Constructors
    public DayReportDTO() {}


    // Getters and Builder Setters
    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public DayReportDTO setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
        return this;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public DayReportDTO setShifts(List<Shift> shifts) {
        this.shifts = shifts;
        return this;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public DayReportDTO setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        return this;
    }

    public int getDeliveryCount() {
        return deliveryCount;
    }

    public DayReportDTO setDeliveryCount(int deliveryCount) {
        this.deliveryCount = deliveryCount;
        return this;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public DayReportDTO setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
        return this;
    }

    public Duration getTotalShiftHours() {
        return totalShiftHours;
    }

    public DayReportDTO setTotalShiftHours(Duration totalShiftHours) {
        this.totalShiftHours = totalShiftHours;
        return this;
    }

    public double getTotalShiftMiles() {
        return totalShiftMiles;
    }

    public DayReportDTO setTotalShiftMiles(double totalShiftMiles) {
        this.totalShiftMiles = totalShiftMiles;
        return this;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public DayReportDTO setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
        return this;
    }

}
