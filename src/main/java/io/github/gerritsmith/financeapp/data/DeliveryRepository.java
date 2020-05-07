package io.github.gerritsmith.financeapp.data;

import io.github.gerritsmith.financeapp.model.Delivery;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface DeliveryRepository extends CrudRepository<Delivery, Long> {

    Delivery findByIdAndUser(long id, User user);

    Delivery findByUserAndDateAndTime(User user, LocalDate date, LocalTime time);

    List<Delivery> findByUserAndDate(User user, LocalDate date);

    List<Delivery> findAllByUser(User user);

}
