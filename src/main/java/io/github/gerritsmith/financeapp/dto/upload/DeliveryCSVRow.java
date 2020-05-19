package io.github.gerritsmith.financeapp.dto.upload;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DeliveryCSVRow {

    @CsvDate(value = "M/d/yy")
    @CsvBindByName(column = "Date", required = true)
    private LocalDate date;

    @CsvDate(value = "h:mm a")
    @CsvBindByName(column = "Accept Time", required = true)
    private LocalTime time;

    @CsvBindByName(column = "Food Amount")
    private Double foodTotal;

    @CsvBindByName(column = "Duration (min)")
    private Double totalTime;

    @CsvBindByName(column = "Total Miles")
    private Double totalMiles;

    @CsvBindByName(column = "App Miles")
    private Double appMiles;

    @CsvBindByName(column = "App Wait (min)")
    private Double appWaitTime;

    @CsvBindByName(column = "Adjustments")
    private Double adjustments;

    @CsvBindByName(column = "Base Pay")
    private Double basePay;

    @CsvBindByName(column = "Tip")
    private Double tip;

    @CsvBindByName(column = "Total Pay")
    private Double total;

    @CsvBindByName(column = "Pick Up Location", required = true)
    private String pickupLocationName;

    @CsvBindByName(column = "Drop Off Location")
    private String dropoffLocationAddress;

    @CsvBindByName(column = "Lat, Long")
    private String latLong;

    @CsvBindByName(column = "Name")
    private String dropoffName;

    @CsvBindByName(column = "Apt #")
    private String apt;

    @CsvBindByName(column = "Notes")
    private String note;

    @CsvBindByName(column = "Cash")
    private Double cash;

    private final List<Exception> errors = new ArrayList<>();
    private boolean isAddedToDatabase = false;

    // Constructor
    public DeliveryCSVRow() {}

    // Methods
    public void addError(Exception e) {
        errors.add(e);
    }

    // Getters and Setters
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

    public Double getFoodTotal() {
        return foodTotal;
    }

    public void setFoodTotal(Double foodTotal) {
        this.foodTotal = foodTotal;
    }

    public Double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Double totalTime) {
        this.totalTime = totalTime;
    }

    public Double getTotalMiles() {
        return totalMiles;
    }

    public void setTotalMiles(Double totalMiles) {
        this.totalMiles = totalMiles;
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

    public Double getAdjustments() {
        return adjustments;
    }

    public void setAdjustments(Double adjustments) {
        this.adjustments = adjustments;
    }

    public Double getBasePay() {
        return basePay;
    }

    public void setBasePay(Double basePay) {
        this.basePay = basePay;
    }

    public Double getTip() {
        return tip;
    }

    public void setTip(Double tip) {
        this.tip = tip;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getPickupLocationName() {
        return pickupLocationName;
    }

    public void setPickupLocationName(String pickupLocationName) {
        this.pickupLocationName = pickupLocationName;
    }

    public String getDropoffLocationAddress() {
        return dropoffLocationAddress;
    }

    public void setDropoffLocationAddress(String dropoffLocationAddress) {
        this.dropoffLocationAddress = dropoffLocationAddress;
    }

    public String getLatLong() {
        return latLong;
    }

    public void setLatLong(String latLong) {
        this.latLong = latLong;
    }

    public String getDropoffName() {
        return dropoffName;
    }

    public void setDropoffName(String dropoffName) {
        this.dropoffName = dropoffName;
    }

    public String getApt() {
        return apt;
    }

    public void setApt(String apt) {
        this.apt = apt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        this.cash = cash;
    }

    public List<Exception> getErrors() {
        return errors;
    }

    public boolean isAddedToDatabase() {
        return isAddedToDatabase;
    }

    public void setAddedToDatabase(boolean addedToDatabase) {
        isAddedToDatabase = addedToDatabase;
    }

    // Equals, hash, toString
    @Override
    public String toString() {
        return "DeliveryCSVRow{" +
                "date=" + date +
                ", time=" + time +
                ", total=" + total +
                '}';
    }

}
