package io.github.gerritsmith.financeapp.data;

import io.github.gerritsmith.financeapp.model.Expense;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface ExpenseRepository extends CrudRepository<Expense, Long> {

    Expense findByUserAndDateAndTimeAndDescription(User user, LocalDate date, LocalTime time, String description);

}
