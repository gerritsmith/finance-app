package io.github.gerritsmith.financeapp.model;

import io.github.gerritsmith.financeapp.dto.form.ExpenseFormDTO;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Expense extends AbstractEntity {

    @ManyToOne
    private User user;

    private LocalDate date;
    private LocalTime time;
    private double amount;
    private Integer mileage;
    private String description;

    // Constructors
    public Expense() {}

    public Expense(User user, ExpenseFormDTO expenseFormDTO) {
        this.user = user;
        date = expenseFormDTO.getDate();
        time = expenseFormDTO.getTime();
        String amountString = expenseFormDTO.getAmount();
        amount = amountString.isEmpty() ? 0 : Double.parseDouble(amountString);
        String mileageString = expenseFormDTO.getMileage();
        mileage = mileageString.isEmpty() ? null : Integer.parseInt(expenseFormDTO.getMileage());
        description = expenseFormDTO.getDescription();
    }

    // Methods
    public void update(Expense expense) {
        date = expense.getDate();
        time = expense.getTime();
        amount = expense.getAmount();
        mileage = expense.getMileage();
        description = expense.getDescription();
    }

    // Getters and Setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Equals, hash, toString
    @Override
    public String toString() {
        return "Expense: " + date + " " + time +
                ", amount=" + amount +
                ", description='" + description;
    }

}
