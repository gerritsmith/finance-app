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
import java.util.Optional;

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

    public Delivery findByIdAsUser(long id, User user) {
        Optional<Delivery> searchResult = deliveryRepository.findById(id);
        Delivery delivery = null;
        if (searchResult.isPresent()) {
            if (searchResult.get().getUser().equals(user)) {
                delivery = searchResult.get();
            }
        }
        return delivery;
    }

    // Create
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
    public Delivery updateDelivery(long deliveryId, Delivery updatedDelivery) throws DeliveryExistsException {
        Optional<Delivery> searchResult = deliveryRepository.findById(deliveryId);
        Delivery deliveryToUpdate = null;
        if (searchResult.isPresent()) {
            deliveryToUpdate = searchResult.get();
            if (deliveryToUpdate.getUser().equals(updatedDelivery.getUser())) {
                Delivery deliveryExistsAtDateTime = findByUserAndDateAndTime(updatedDelivery.getUser(),
                        updatedDelivery.getDate(),
                        updatedDelivery.getTime());
                if (!deliveryToUpdate.equals(deliveryExistsAtDateTime)) {
                    throw new DeliveryExistsException("Already have delivery record at " +
                            deliveryExistsAtDateTime.displayTime() +
                            " on " + deliveryExistsAtDateTime.getDate().format(DateTimeFormatter.ofPattern("MMM dd yyyy")));
                }
                deliveryToUpdate.update(updatedDelivery);
                deliveryRepository.save(deliveryToUpdate);
            } else {
                deliveryToUpdate = null;
            }
        }
        return deliveryToUpdate;
    }

}
