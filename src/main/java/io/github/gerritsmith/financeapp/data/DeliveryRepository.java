package io.github.gerritsmith.financeapp.data;

import io.github.gerritsmith.financeapp.model.Delivery;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface DeliveryRepository extends CrudRepository<Delivery, Long> {

    Delivery findByUserAndDateAndTime(User user, LocalDate date, LocalTime time);

    Iterable<Delivery> findAllByUser(User user);

}
