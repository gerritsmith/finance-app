package io.github.gerritsmith.financeapp.service;

import io.github.gerritsmith.financeapp.data.ShiftRepository;
import io.github.gerritsmith.financeapp.exception.ShiftExistsException;
import io.github.gerritsmith.financeapp.model.Shift;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ShiftService {

    private ShiftRepository shiftRepository;

    // Constructors
    @Autowired
    public ShiftService(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    // Read
    public Iterable<Shift> findByUserAndDate(User user, LocalDate date) {
        return shiftRepository.findByUserAndDate(user, date);
    }

    // Create
    @Transactional
    public Shift addShift(Shift shift) throws ShiftExistsException {
        Iterable<Shift> shiftsOnDate = findByUserAndDate(shift.getUser(), shift.getDate());
        if (shiftsOnDate != null) {
            for (Shift existingShift : shiftsOnDate) {
                if (shift.getStartTime().isAfter(existingShift.getStartTime()) &&
                        shift.getStartTime().isBefore(existingShift.getEndTime())) {
                    throw new ShiftExistsException("This overlaps an existing shift on " +
                            existingShift.getDate().format(DateTimeFormatter.ofPattern("MMM dd yyyy")) +
                            " from " + existingShift.getStartTime().format(DateTimeFormatter.ofPattern("h:mm a")) +
                            " to " + existingShift.getEndTime().format(DateTimeFormatter.ofPattern("h:mm a")));
                }
            }
        }
        return shiftRepository.save(shift);
    }

}
