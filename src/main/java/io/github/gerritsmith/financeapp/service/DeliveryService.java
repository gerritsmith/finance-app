package io.github.gerritsmith.financeapp.service;

import io.github.gerritsmith.financeapp.data.DeliveryRepository;
import io.github.gerritsmith.financeapp.exception.DeliveryExistsException;
import io.github.gerritsmith.financeapp.exception.DeliveryWithoutShiftException;
import io.github.gerritsmith.financeapp.model.Delivery;
import io.github.gerritsmith.financeapp.model.DeliveryLeg;
import io.github.gerritsmith.financeapp.model.Shift;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryService {

    private DeliveryRepository deliveryRepository;
    private DeliveryLegService deliveryLegService;
    private ShiftService shiftService;

    // Constructors
    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository,
                           DeliveryLegService deliveryLegService,
                           ShiftService shiftService) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryLegService = deliveryLegService;
        this.shiftService = shiftService;
    }

    // Read
    public Delivery findByUserAndDateAndTime(User user, LocalDate date, LocalTime time) {
        return deliveryRepository.findByUserAndDateAndTime(user, date, time);
    }

    public List<Delivery> findAllByUserInTemporal(User user, Temporal temporal) {
        if (temporal.getClass().equals(LocalDate.class)) {
            return deliveryRepository.findByUserAndDate(user, (LocalDate) temporal);
        } else if (temporal.getClass().equals(YearMonth.class)) {
            return deliveryRepository.findAllByUser(user)
                    .stream()
                    .filter(d -> YearMonth.from(d.getDate()).equals(temporal))
                    .collect(Collectors.toList());
        } else if (temporal.getClass().equals(Year.class)) {
            return deliveryRepository.findAllByUser(user)
                    .stream()
                    .filter(d -> Year.from(d.getDate()).equals(temporal))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public List<Delivery> findAllDeliveriesByUser(User user) {
        return deliveryRepository.findAllByUser(user);
    }

    public Delivery findByIdAsUser(long id, User user) {
        return deliveryRepository.findByIdAndUser(id, user);
    }

    // Create
    @Transactional
    @PreAuthorize("hasAuthority('USER')")
    public Delivery addDelivery(Delivery delivery) throws DeliveryExistsException,
                                                          DeliveryWithoutShiftException {
        delivery.setShift(findShiftForDelivery(delivery));
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
    @PreAuthorize("hasAuthority('USER')")
    public Delivery updateDelivery(long deliveryId,
                                   Delivery updatedDelivery) throws DeliveryExistsException,
                                                                    DeliveryWithoutShiftException {
        Delivery deliveryToUpdate = findByIdAsUser(deliveryId, updatedDelivery.getUser());
        Delivery deliveryExistsAtDateTime = findByUserAndDateAndTime(updatedDelivery.getUser(),
                updatedDelivery.getDate(),
                updatedDelivery.getTime());
        if (deliveryExistsAtDateTime != null && !deliveryToUpdate.equals(deliveryExistsAtDateTime)) {
            throw new DeliveryExistsException("Already have delivery record at " +
                    deliveryExistsAtDateTime.displayTime() +
                    " on " + deliveryExistsAtDateTime.getDate().format(DateTimeFormatter.ofPattern("MMM dd yyyy")));
        }
        updatedDelivery.setShift(findShiftForDelivery(updatedDelivery));
        transferDeliveryFields(deliveryToUpdate, updatedDelivery);
        return deliveryRepository.save(deliveryToUpdate);
    }

    // Methods
    private void transferDeliveryFields(Delivery deliveryToUpdate, Delivery updatedDelivery) {
        deliveryToUpdate.setDate(updatedDelivery.getDate());
        deliveryToUpdate.setTime(updatedDelivery.getTime());
        deliveryToUpdate.setTotal(updatedDelivery.getTotal());
        deliveryToUpdate.setAppMiles(updatedDelivery.getAppMiles());
        deliveryToUpdate.setAppWaitTime(updatedDelivery.getAppWaitTime());
        deliveryToUpdate.setTotalMiles(updatedDelivery.getTotalMiles());
        deliveryToUpdate.setTotalTime(updatedDelivery.getTotalTime());
        deliveryToUpdate.setBasePay(updatedDelivery.getBasePay());
        deliveryToUpdate.setAdjustments(updatedDelivery.getAdjustments());
        int originalSize = deliveryToUpdate.getLegs().size();
        int newSize = updatedDelivery.getLegs().size();
        for (int i = 0; i < Math.min(originalSize, newSize); i++) {
            deliveryToUpdate.getLegs().get(i).update(updatedDelivery.getLegs().get(i));
        }
        for (int i = originalSize; i < newSize; i++) {
            DeliveryLeg deliveryLeg = updatedDelivery.getLegs().get(i);
            deliveryLeg.setDelivery(deliveryToUpdate);
            deliveryToUpdate.getLegs().add(deliveryLeg);
        }
        for (int i = newSize; i < originalSize; i++) {
            int lastIndex = deliveryToUpdate.getLegs().size() - 1;
            deliveryLegService.deleteDeliveryLeg(deliveryToUpdate.getLegs().remove(lastIndex));
        }
        deliveryToUpdate.setShift(updatedDelivery.getShift());
    }

    private Shift findShiftForDelivery(Delivery delivery) throws DeliveryWithoutShiftException {
        List<Shift> shiftsOnDate = shiftService.findAllByUserInTemporal(delivery.getUser(), delivery.getDate());
        for (Shift shift : shiftsOnDate) {
            if (delivery.getTime().compareTo(shift.getStartTime()) >= 0 &&
                    delivery.getTime().compareTo(shift.getEndTime()) <= 0) {
                return shift;
            }
        }
        throw new DeliveryWithoutShiftException("Delivery doesn't occur within a shift");
    }

}
