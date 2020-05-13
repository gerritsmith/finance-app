package io.github.gerritsmith.financeapp.service;

import io.github.gerritsmith.financeapp.data.ShiftRepository;
import io.github.gerritsmith.financeapp.exception.DeliveryWithoutShiftException;
import io.github.gerritsmith.financeapp.exception.ShiftExistsException;
import io.github.gerritsmith.financeapp.model.Delivery;
import io.github.gerritsmith.financeapp.model.Shift;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ShiftService {

    private ShiftRepository shiftRepository;

    // Constructors
    @Autowired
    public ShiftService(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    // Read
    public List<Shift> findByUserAndDate(User user, LocalDate date) {
        return shiftRepository.findByUserAndDate(user, date);
    }

    public List<Shift> findAllShiftsByUser(User user) {
        return shiftRepository.findAllByUser(user);
    }

    public Shift findByIdAsUser(long id, User user) {
        return shiftRepository.findByIdAndUser(id, user);
    }

    // Create
    @Transactional
    public Shift addShift(Shift shift) throws ShiftExistsException {
        List<Shift> shiftsOnDate = findByUserAndDate(shift.getUser(), shift.getDate());
        if (shiftsOnDate != null) {
            for (Shift existingShift : shiftsOnDate) {
                if (hasOverlap(shift, existingShift)) {
                    throw new ShiftExistsException("This overlaps an existing shift on " +
                            existingShift.getDate().format(DateTimeFormatter.ofPattern("MMM dd yyyy")) +
                            " from " + existingShift.getStartTime().format(DateTimeFormatter.ofPattern("h:mm a")) +
                            " to " + existingShift.getEndTime().format(DateTimeFormatter.ofPattern("h:mm a")));
                }
            }
        }
        return shiftRepository.save(shift);
    }

    // Update
    @Transactional
    public Shift updateShift(long shiftId,
                             Shift updatedShift) throws ShiftExistsException,
                                                        DeliveryWithoutShiftException {
        Shift shiftToUpdate = shiftRepository.findByIdAndUser(shiftId, updatedShift.getUser());
        List<Shift> shiftsExistingOnDate = findByUserAndDate(updatedShift.getUser(),
                updatedShift.getDate());
        for (Shift existingShift : shiftsExistingOnDate) {
            if (hasOverlap(updatedShift, existingShift) && !shiftToUpdate.equals(existingShift)) {
                throw new ShiftExistsException("Already have shift record on " +
                        existingShift.getDate().format(DateTimeFormatter.ofPattern("MMM dd yyyy")) +
                        " from " + existingShift.getStartTime().format(DateTimeFormatter.ofPattern("h:mm a")) +
                        " to " + existingShift.getEndTime().format(DateTimeFormatter.ofPattern("h:mm a")));
            }
        }
        for (Delivery delivery : shiftToUpdate.getDeliveries()) {
            if (!delivery.getDate().equals(updatedShift.getDate()) ||
                    delivery.getTime().compareTo(updatedShift.getStartTime()) < 0 ||
                    delivery.getTime().compareTo(updatedShift.getEndTime()) > 0) {
                throw new DeliveryWithoutShiftException("Shift edits which result in " +
                        "deliveries occurring outside of shift are not allowed.");
            }
        }
        shiftToUpdate.update(updatedShift);
        return shiftRepository.save(shiftToUpdate);
    }

    // Methods
    private boolean hasOverlap(Shift shift1, Shift shift2) {
        if (((shift1.getStartTime().compareTo(shift2.getStartTime()) >= 0) &&
                (shift1.getStartTime().compareTo(shift2.getEndTime()) <= 0 )) ||
                ((shift1.getEndTime().compareTo(shift2.getStartTime()) >= 0) &&
                        (shift1.getEndTime().compareTo(shift2.getEndTime()) <= 0 )) ) {
            return true;
        }
        if (((shift2.getStartTime().compareTo(shift1.getStartTime()) >= 0) &&
                (shift2.getStartTime().compareTo(shift1.getEndTime()) <= 0 )) ||
                ((shift2.getEndTime().compareTo(shift1.getStartTime()) >= 0) &&
                        (shift2.getEndTime().compareTo(shift1.getEndTime()) <= 0 )) ) {
            return true;
        }
        return false;
    }

}
