package io.github.gerritsmith.financeapp.service;

import io.github.gerritsmith.financeapp.data.DeliveryRepository;
import io.github.gerritsmith.financeapp.exception.DeliveryExistsException;
import io.github.gerritsmith.financeapp.model.Delivery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class DeliveryService {

    private DeliveryRepository deliveryRepository;

    // Constructors
    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    // Read
    public Delivery findByDateAndTime(LocalDate date, LocalTime time) {
        return deliveryRepository.findByDateAndTime(date, time);
    }

    public Iterable<Delivery> findAllDeliveries() {
        return deliveryRepository.findAll();
    }

    // Create and Update
    public Delivery saveDelivery(Delivery delivery) throws DeliveryExistsException {
        Delivery deliveryExists = findByDateAndTime(delivery.getDate(), delivery.getTime());
        if (deliveryExists != null) {
            throw new DeliveryExistsException("Already have delivery record at " + delivery.getTime() + " on " + delivery.getDate());
        }
        return deliveryRepository.save(delivery);
    }

}
