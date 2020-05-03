package io.github.gerritsmith.financeapp.dto;

import io.github.gerritsmith.financeapp.model.Delivery;
import io.github.gerritsmith.financeapp.model.DeliveryLeg;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DeliveryFormDTO {

    private List<DeliveryLeg> legs;

    public List<DeliveryLeg> getLegs() {
        return legs;
    }

    public void setLegs(List<DeliveryLeg> legs) {
        this.legs = legs;
    }

    @NotNull(message = "date is required")
    @PastOrPresent(message = "date can't be in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "time is required")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;

    @Pattern(regexp = "(\\d+\\.?\\d{0,2}|\\.\\d{1,2})?", message = "app miles must be in the form 0.00")
    private String appMiles;

    @Pattern(regexp = "(\\d+\\.?\\d{0,2}|\\.\\d{1,2})?", message = "app wait time must be in the form 0.00")
    private String appWaitTime;

    @Pattern(regexp = "(\\d+\\.?\\d{0,1}|\\.\\d)?", message = "total miles must be in the form 0.0")
    private String totalMiles;

    @Pattern(regexp = "\\d*", message = "total time must a non-negative integer")
    private String totalTime;

    @Pattern(regexp = "(\\d+\\.?\\d{0,2}|\\.\\d{1,2})?", message = "amount must be in the form 0000.00")
    private String total;


    // Constructors
    public DeliveryFormDTO() {}

    public DeliveryFormDTO(Delivery delivery) {
        date = delivery.getDate();
        time = delivery.getTime();
        total = Double.toString(delivery.getTotal());
        appMiles = (delivery.getAppMiles() == null) ? "" : Double.toString(delivery.getAppMiles());
        appWaitTime = (delivery.getAppWaitTime() == null) ? "" : Double.toString(delivery.getAppWaitTime());
        totalMiles = (delivery.getTotalMiles() == null) ? "" : Double.toString(delivery.getTotalMiles());
        totalTime = (delivery.getTotalTime() == null) ? "" : Long.toString(delivery.getTotalTime().toMinutes());
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

    public String getAppMiles() {
        return appMiles;
    }

    public void setAppMiles(String appMiles) {
        this.appMiles = appMiles;
    }

    public String getAppWaitTime() {
        return appWaitTime;
    }

    public void setAppWaitTime(String appWaitTime) {
        this.appWaitTime = appWaitTime;
    }

    public String getTotalMiles() {
        return totalMiles;
    }

    public void setTotalMiles(String totalMiles) {
        this.totalMiles = totalMiles;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}
