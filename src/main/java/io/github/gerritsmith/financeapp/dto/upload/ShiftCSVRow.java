package io.github.gerritsmith.financeapp.dto.upload;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import java.time.LocalDate;
import java.time.LocalTime;

public class ShiftCSVRow {

    @CsvDate(value = "M/d/yy")
    @CsvBindByName(column = "Date", required = true)
    private LocalDate date;

    @CsvDate(value = "h:mm a")
    @CsvBindByName(column = "Start Time", required = true)
    private LocalTime startTime;

    @CsvDate(value = "h:mm a")
    @CsvBindByName(column = "End Time", required = true)
    private LocalTime endTime;

    @CsvBindByName(column = "Start Mileage")
    private Integer startMileage;

    @CsvBindByName(column = "End Mileage")
    private Integer endMileage;

    @CsvBindByName(column = "Miles")
    private Double miles;

    @CsvBindByName(column = "MPG")
    private Double mpg;

    public ShiftCSVRow() {}

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Integer getStartMileage() {
        return startMileage;
    }

    public void setStartMileage(Integer startMileage) {
        this.startMileage = startMileage;
    }

    public Integer getEndMileage() {
        return endMileage;
    }

    public void setEndMileage(Integer endMileage) {
        this.endMileage = endMileage;
    }

    public Double getMiles() {
        return miles;
    }

    public void setMiles(Double miles) {
        this.miles = miles;
    }

    public Double getMpg() {
        return mpg;
    }

    public void setMpg(Double mpg) {
        this.mpg = mpg;
    }

}
