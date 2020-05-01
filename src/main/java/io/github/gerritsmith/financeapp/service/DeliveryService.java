package io.github.gerritsmith.financeapp.service;

import io.github.gerritsmith.financeapp.data.DeliveryRepository;
import io.github.gerritsmith.financeapp.exception.DeliveryExistsException;
import io.github.gerritsmith.financeapp.model.Delivery;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    public List<Delivery> findAllDeliveriesByUser(User user) {
        return deliveryRepository.findAllByUser(user);
    }

    public Delivery findByIdAsUser(long id, User user) {
        return deliveryRepository.findByIdAndUser(id, user);
    }

    // Create
    @Transactional
    public Delivery addDelivery(Delivery delivery) throws DeliveryExistsException {
        Delivery deliveryExists = findByUserAndDateAndTime(delivery.getUser(), delivery.getDate(), delivery.getTime());
        if (deliveryExists != null) {
            throw new DeliveryExistsException("Already have delivery record at " +
                    delivery.displayTime() +
                    " on " + delivery.getDate().format(DateTimeFormatter.ofPattern("LLL dd yyyy")));
        }
        return deliveryRepository.save(delivery);
    }

    // Update
    @Transactional
    public Delivery updateDelivery(long deliveryId, Delivery updatedDelivery) throws DeliveryExistsException {
        Delivery deliveryToUpdate = findByIdAsUser(deliveryId, updatedDelivery.getUser());
        Delivery deliveryExistsAtDateTime = findByUserAndDateAndTime(updatedDelivery.getUser(),
                updatedDelivery.getDate(),
                updatedDelivery.getTime());
        if (deliveryExistsAtDateTime !=null && !deliveryToUpdate.equals(deliveryExistsAtDateTime)) {
            throw new DeliveryExistsException("Already have delivery record at " +
                    deliveryExistsAtDateTime.displayTime() +
                    " on " + deliveryExistsAtDateTime.getDate().format(DateTimeFormatter.ofPattern("MMM dd yyyy")));
        }
        deliveryToUpdate.update(updatedDelivery);
        deliveryRepository.save(deliveryToUpdate);
        return deliveryToUpdate;
    }

}
