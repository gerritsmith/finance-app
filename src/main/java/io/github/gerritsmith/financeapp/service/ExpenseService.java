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
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Expense> findAllByUserInTemporal(User user, Temporal temporal) {
        if (temporal.getClass().equals(LocalDate.class)) {
            return expenseRepository.findByUserAndDate(user, (LocalDate) temporal);
        } else if (temporal.getClass().equals(YearMonth.class)) {
            return expenseRepository.findAllByUser(user)
                    .stream()
                    .filter(e -> YearMonth.from(e.getDate()).equals(temporal))
                    .collect(Collectors.toList());
        } else if (temporal.getClass().equals(Year.class)) {
            return expenseRepository.findAllByUser(user)
                    .stream()
                    .filter(d -> Year.from(d.getDate()).equals(temporal))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public List<Expense> findAllExpensesByUser(User user) {
        return expenseRepository.findAllByUser(user);
    }

    public Expense findByIdAsUser(long id, User user) {
        return expenseRepository.findByIdAndUser(id, user);
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

    // Update
    @Transactional
    public Expense updateExpense(long expenseId, Expense updatedExpense) throws ExpenseExistsException {
        Expense expenseToUpdate = findByIdAsUser(expenseId, updatedExpense.getUser());
        Expense expenseExistsAtDateTime = findByUserAndDateAndTimeAndDescription(updatedExpense.getUser(),
                updatedExpense.getDate(),
                updatedExpense.getTime(),
                updatedExpense.getDescription());
        if (expenseExistsAtDateTime != null && !expenseToUpdate.equals(expenseExistsAtDateTime)) {
            throw new ExpenseExistsException("Already have expense record at " +
                    " on " + expenseExistsAtDateTime.getDate().format(DateTimeFormatter.ofPattern("MMM dd yyyy")) +
                    " at " + expenseExistsAtDateTime.getTime().format(DateTimeFormatter.ofPattern("h:mm a")) +
                    " for " + expenseExistsAtDateTime.getDescription());
        }
        expenseToUpdate.update(updatedExpense);
        return expenseRepository.save(expenseToUpdate);
    }

}
