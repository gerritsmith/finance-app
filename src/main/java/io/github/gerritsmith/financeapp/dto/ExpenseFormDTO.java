package io.github.gerritsmith.financeapp.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalTime;

public class ExpenseFormDTO {

    @NotNull(message = "date is required")
    @PastOrPresent(message = "date can't be in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "time is required")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;

    @Pattern(regexp = "(\\d+\\.?\\d{0,2}|\\.\\d{1,2})?", message = "amount must be in the form 0000.00")
    private String amount;

    @Pattern(regexp = "\\d*", message = "mileage must be an non-negative integer")
    private Integer mileage;

    @NotNull
    private String description;
    
    // Constructors
    public ExpenseFormDTO() {}
    
    // Getters and Setters
    public LocalDate getDate() {
        return date;
    }

    public ExpenseFormDTO setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public LocalTime getTime() {
        return time;
    }

    public ExpenseFormDTO setTime(LocalTime time) {
        this.time = time;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public ExpenseFormDTO setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public Integer getMileage() {
        return mileage;
    }

    public ExpenseFormDTO setMileage(Integer mileage) {
        this.mileage = mileage;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ExpenseFormDTO setDescription(String description) {
        this.description = description;
        return this;
    }
    
}
