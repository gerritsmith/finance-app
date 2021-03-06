package io.github.gerritsmith.financeapp.dto;

import io.github.gerritsmith.financeapp.model.Delivery;
import io.github.gerritsmith.financeapp.model.Expense;
import io.github.gerritsmith.financeapp.model.Shift;

import java.time.Duration;
import java.util.List;

public abstract class AbstractReportDTO {

    private List<Delivery> deliveries;
    private List<Shift> shifts;
    private List<Expense> expenses;

    private double totalRevenue;
    private Duration totalShiftHours;
    private double totalShiftMiles;
    private double totalExpenses;

    private DeliveryStatsDTO deliveryStatsDTO;

    // Computed fields
    public int getDeliveryGroupCount() {
        return deliveries.size();
    }

    public double getDeliveriesPerGroup() {
        return deliveryStatsDTO.getDeliveryCount() / (double) getDeliveryGroupCount();
    }

    public double getTotalShiftHoursAsDecimal() {
        return totalShiftHours.toHours() + totalShiftHours.toMinutesPart()/60.0;
    }

    public double getDeliveriesPerHour() {
        return deliveryStatsDTO.getDeliveryCount()/getTotalShiftHoursAsDecimal();
    }

    public double getRevenuePerHour() {
        return totalRevenue/getTotalShiftHoursAsDecimal();
    }

    public double getShiftMilesPerHour() {
        return totalShiftMiles/getTotalShiftHoursAsDecimal();
    }

    public double getRevenuePerDelivery() {
        return totalRevenue/deliveryStatsDTO.getDeliveryCount();
    }

    public double getShiftMilesPerDelivery() {
        return totalShiftMiles/deliveryStatsDTO.getDeliveryCount();
    }

    public String getTotalShiftHoursString() {
        return totalShiftHours.toHours() + " hr " +
                totalShiftHours.toMinutesPart() + " min";
    }

    // Constructor
    public AbstractReportDTO() {}

    // Getters and Builder Setters
    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public AbstractReportDTO setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
        return this;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public AbstractReportDTO setShifts(List<Shift> shifts) {
        this.shifts = shifts;
        return this;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public AbstractReportDTO setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        return this;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public AbstractReportDTO setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
        return this;
    }

    public Duration getTotalShiftHours() {
        return totalShiftHours;
    }

    public AbstractReportDTO setTotalShiftHours(Duration totalShiftHours) {
        this.totalShiftHours = totalShiftHours;
        return this;
    }

    public double getTotalShiftMiles() {
        return totalShiftMiles;
    }

    public AbstractReportDTO setTotalShiftMiles(double totalShiftMiles) {
        this.totalShiftMiles = totalShiftMiles;
        return this;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public AbstractReportDTO setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
        return this;
    }

    public DeliveryStatsDTO getDeliveryStatsDTO() {
        return deliveryStatsDTO;
    }

    public AbstractReportDTO setDeliveryStatsDTO(DeliveryStatsDTO deliveryStatsDTO) {
        this.deliveryStatsDTO = deliveryStatsDTO;
        return this;
    }

}
