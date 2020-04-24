package io.github.gerritsmith.financeapp.service;

import io.github.gerritsmith.financeapp.data.DeliveryRepository;
import io.github.gerritsmith.financeapp.exception.DeliveryExistsException;
import io.github.gerritsmith.financeapp.model.Delivery;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class DeliveryService {

    private DeliveryRepository deliveryRepository;

    // Constructors
    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    // Read
    public Delivery findByUserAndDateAndTime(User user, LocalDate date, LocalTime time) {
        return deliveryRepository.findByUserAndDateAndTime(user, date, time);
    }

    public Iterable<Delivery> findAllDeliveriesByUser(User user) {
        return deliveryRepository.findAllByUser(user);
    }

    // Create and Update
    public Delivery saveDelivery(Delivery delivery) throws DeliveryExistsException {
        Delivery deliveryExists = findByUserAndDateAndTime(delivery.getUser(), delivery.getDate(), delivery.getTime());
        if (deliveryExists != null) {
            throw new DeliveryExistsException("Already have delivery record at " +
                    delivery.displayTime() +
                    " on " + delivery.getDate().format(DateTimeFormatter.ofPattern("LLL dd yyyy")));
        }
        return deliveryRepository.save(delivery);
    }

}
