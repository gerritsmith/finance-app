package io.github.gerritsmith.financeapp.data;

import io.github.gerritsmith.financeapp.model.Expense;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ExpenseRepository extends CrudRepository<Expense, Long> {

    Expense findByIdAndUser(long id, User user);

    Expense findByUserAndDateAndTimeAndDescription(User user,
                                                   LocalDate date,
                                                   LocalTime time,
                                                   String description);

    List<Expense> findAllByUser(User user);

}
