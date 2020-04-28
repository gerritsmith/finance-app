package io.github.gerritsmith.financeapp.data;

import io.github.gerritsmith.financeapp.model.Shift;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ShiftRepository extends CrudRepository<Shift, Long> {

    Iterable<Shift> findByUserAndDate(User user, LocalDate date);

    Iterable<Shift> findAllByUser(User user);

}
