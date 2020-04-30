package io.github.gerritsmith.financeapp.data;

import io.github.gerritsmith.financeapp.model.Shift;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShiftRepository extends CrudRepository<Shift, Long> {

    Shift findByIdAndUser(long id, User user);

    List<Shift> findByUserAndDate(User user, LocalDate date);

    List<Shift> findAllByUser(User user);

}
