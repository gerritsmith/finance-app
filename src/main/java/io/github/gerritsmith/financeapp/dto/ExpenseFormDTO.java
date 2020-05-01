package io.github.gerritsmith.financeapp.dto;

import io.github.gerritsmith.financeapp.model.Expense;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
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
    private String mileage;

    @NotBlank(message = "description is required")
    private String description;
    
    // Constructors
    public ExpenseFormDTO() {}

    public ExpenseFormDTO(Expense expense) {
        date = expense.getDate();
        time = expense.getTime();
        amount = Double.toString(expense.getAmount());
        mileage = (expense.getMileage() == null) ? "" : expense.getMileage().toString();
        description = expense.getDescription();
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
