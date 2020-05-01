package io.github.gerritsmith.financeapp.service;

import io.github.gerritsmith.financeapp.data.ExpenseRepository;
import io.github.gerritsmith.financeapp.exception.ExpenseExistsException;
import io.github.gerritsmith.financeapp.model.Expense;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class ExpenseService {

    private ExpenseRepository expenseRepository;

    // Constructors
    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    // Read
    public Expense findByUserAndDateAndTimeAndDescription(User user, LocalDate date, LocalTime time, String description) {
        return expenseRepository.findByUserAndDateAndTimeAndDescription(user, date, time, description);
    }

    // Create
    @Transactional
    public Expense addExpense(Expense expense) throws ExpenseExistsException {
        Expense expenseExists = findByUserAndDateAndTimeAndDescription(expense.getUser(), expense.getDate(), expense.getTime(), expense.getDescription());
        if (expenseExists != null) {
            throw new ExpenseExistsException("Already have expense record at " +
                    " on " + expense.getDate().format(DateTimeFormatter.ofPattern("MMM dd yyyy")) +
                    " at " + expense.getTime().format(DateTimeFormatter.ofPattern("h:mm a")) +
                    " for " + expense.getDescription());
        }
        return expenseRepository.save(expense);
    }

}
