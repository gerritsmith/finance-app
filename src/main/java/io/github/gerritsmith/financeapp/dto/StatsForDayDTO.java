package io.github.gerritsmith.financeapp.dto;

import io.github.gerritsmith.financeapp.model.Delivery;
import io.github.gerritsmith.financeapp.model.Expense;
import io.github.gerritsmith.financeapp.model.Shift;

import java.time.Duration;
import java.util.List;

public class StatsForDayDTO {

    private int deliveryCount;

    private double totalIncome;
    private Duration totalHours;
    private double totalMiles;

    private double deliveriesPerHour;
    private double incomePerHour;
    private double milesPerHour;
    private double incomePerDelivery;
    private double milesPerDelivery;

    private List<Delivery> deliveries;
    private List<Shift> shifts;
    private List<Expense> expenses;

    // Constructors
    public StatsForDayDTO() {}

    // Getters and Builder Setters
    public int getDeliveryCount() {
        return deliveryCount;
    }

    public StatsForDayDTO setDeliveryCount(int deliveryCount) {
        this.deliveryCount = deliveryCount;
        return this;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public StatsForDayDTO setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
        return this;
    }

    public Duration getTotalHours() {
        return totalHours;
    }

    public StatsForDayDTO setTotalHours(Duration totalHours) {
        this.totalHours = totalHours;
        return this;
    }

    public double getTotalMiles() {
        return totalMiles;
    }

    public StatsForDayDTO setTotalMiles(double totalMiles) {
        this.totalMiles = totalMiles;
        return this;
    }

    public double getDeliveriesPerHour() {
        return deliveriesPerHour;
    }

    public StatsForDayDTO setDeliveriesPerHour(double deliveriesPerHour) {
        this.deliveriesPerHour = deliveriesPerHour;
        return this;
    }

    public double getIncomePerHour() {
        return incomePerHour;
    }

    public StatsForDayDTO setIncomePerHour(double incomePerHour) {
        this.incomePerHour = incomePerHour;
        return this;
    }

    public double getMilesPerHour() {
        return milesPerHour;
    }

    public StatsForDayDTO setMilesPerHour(double milesPerHour) {
        this.milesPerHour = milesPerHour;
        return this;
    }

    public double getIncomePerDelivery() {
        return incomePerDelivery;
    }

    public StatsForDayDTO setIncomePerDelivery(double incomePerDelivery) {
        this.incomePerDelivery = incomePerDelivery;
        return this;
    }

    public double getMilesPerDelivery() {
        return milesPerDelivery;
    }

    public StatsForDayDTO setMilesPerDelivery(double milesPerDelivery) {
        this.milesPerDelivery = milesPerDelivery;
        return this;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public StatsForDayDTO setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
        return this;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public StatsForDayDTO setShifts(List<Shift> shifts) {
        this.shifts = shifts;
        return this;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public StatsForDayDTO setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        return this;
    }

}
