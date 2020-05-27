package io.github.gerritsmith.financeapp.controller;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import io.github.gerritsmith.financeapp.dto.upload.DeliveryCSVRow;
import io.github.gerritsmith.financeapp.dto.upload.ShiftCSVRow;
import io.github.gerritsmith.financeapp.exception.DeliveryExistsException;
import io.github.gerritsmith.financeapp.exception.DeliveryWithoutShiftException;
import io.github.gerritsmith.financeapp.exception.LocationExistsException;
import io.github.gerritsmith.financeapp.exception.ShiftExistsException;
import io.github.gerritsmith.financeapp.model.*;
import io.github.gerritsmith.financeapp.service.DeliveryService;
import io.github.gerritsmith.financeapp.service.LocationService;
import io.github.gerritsmith.financeapp.service.ShiftService;
import io.github.gerritsmith.financeapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UploadController {

    @Autowired
    UserService userService;

    @Autowired
    ShiftService shiftService;

    @Autowired
    LocationService locationService;

    @Autowired
    DeliveryService deliveryService;

    @ModelAttribute
    public void addControllerWideModelAttributes(Model model, Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user);
    }

    @GetMapping("/upload")
    public String displayUploadHome() {
        return "upload/home";
    }

    @PostMapping("/upload/shifts")
    @PreAuthorize("hasAuthority('USER')")
    public String uploadShiftCSVFile(@RequestParam MultipartFile file,
                                     Model model,
                                     @ModelAttribute User user) {
        if (file.isEmpty()) {
            model.addAttribute("errorMessage", "File is empty!");
            model.addAttribute("isSuccessful", false);
        } else {
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                CsvToBean<ShiftCSVRow> csvToBean = new CsvToBeanBuilder<ShiftCSVRow>(reader)
                        .withType(ShiftCSVRow.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();
                List<ShiftCSVRow> rows = csvToBean.stream()
                        .peek(row -> processShiftCSVRow(row, user))
                        .collect(Collectors.toList());
                model.addAttribute("rows", rows);
                model.addAttribute("isSuccessful", true);
            } catch (IOException e) {
                model.addAttribute("errorMessage", "An error occurred while processing the CSV file.");
                model.addAttribute("isSuccessful", false);
            }
        }
        return "/upload/shifts";
    }

    private void processShiftCSVRow(ShiftCSVRow row, User user) {
        try {
            Shift shift = new Shift(user, row);
            shiftService.addShift(shift);
            row.setAddedToDatabase(true);
        } catch (ShiftExistsException e) {
            row.addError(e);
        }
    }

    @PostMapping("/upload/deliveries")
    @PreAuthorize("hasAuthority('USER')")
    public String uploadDeliveryCSVFile(@RequestParam MultipartFile file,
                                        @ModelAttribute User user,
                                        Model model) {
        if (file.isEmpty()) {
            model.addAttribute("errorMessage", "File is empty!");
            model.addAttribute("isSuccessful", false);
        } else {
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                CsvToBean<DeliveryCSVRow> csvToBean = new CsvToBeanBuilder<DeliveryCSVRow>(reader)
                        .withType(DeliveryCSVRow.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                List<DeliveryCSVRow> rows = csvToBean.parse();
                processDeliveryCSVUpload(rows, user);

                model.addAttribute("rows", rows);
                model.addAttribute("isSuccessful", true);
            } catch (IOException e) {
                model.addAttribute("errorMessage", "An error occurred while processing the CSV file.");
                model.addAttribute("isSuccessful", false);
            }
        }
        return "/upload/deliveries";
    }

    private void processDeliveryCSVUpload(List<DeliveryCSVRow> rows, User user) {
        int deliveryGroupStartIndex = 0;
        boolean inDeliveryGroup = false;
        for (int i = rows.size() - 1; -1 < i; i--) {
            // TODO: speed up this loop and make this 'progress bar' visible on the front
            System.out.println(i);

            DeliveryCSVRow row = rows.get(i);
            if (row.getTotal() != null && !inDeliveryGroup) {
                Delivery delivery = new Delivery(user, row);
                DeliveryLeg deliveryLeg = processDeliveryLegFromRow(user, row);
                deliveryLeg.setDelivery(delivery);
                delivery.addDeliveryLeg(deliveryLeg);
                try {
                    deliveryService.addDelivery(delivery);
                } catch (DeliveryWithoutShiftException | DeliveryExistsException e) {
                    row.addError(e);
                }
            }

            if (row.getTotal() == null && !inDeliveryGroup) {
                deliveryGroupStartIndex = i;
                inDeliveryGroup = true;
            }

            if (row.getTotal() != null && inDeliveryGroup) {
                inDeliveryGroup = false;
                Delivery delivery = new Delivery(user, row);
                for (int j = i; j <= deliveryGroupStartIndex; j++) {
                    DeliveryLeg deliveryLeg = processDeliveryLegFromRow(user, rows.get(j));
                    deliveryLeg.setDelivery(delivery);
                    delivery.addDeliveryLeg(deliveryLeg);
                }
                try {
                    deliveryService.addDelivery(delivery);
                } catch (DeliveryWithoutShiftException | DeliveryExistsException e) {
                    row.addError(e);
                }
            }

        }
    }

    private DeliveryLeg processDeliveryLegFromRow(User user, DeliveryCSVRow row) {
        Location pickupLocation = locationService
                .findByUserAndName(user, row.getPickupLocationName());
        if (pickupLocation == null) {
            pickupLocation = new Location(user, LocationType.PICKUP, row);
            saveLocationToDatabaseFromRow(pickupLocation, row);
        } else {
            row.addError(new Exception(row.getPickupLocationName() + " already in database"));
        }
        Location dropoffLocation = locationService
                .findByUserAndNameAndAddressAndApt(user, row.getDropoffName(),
                        row.getDropoffLocationAddress(), row.getApt());
        if (dropoffLocation == null) {
            dropoffLocation = new Location(user, LocationType.DROPOFF, row);
            saveLocationToDatabaseFromRow(dropoffLocation, row);
        } else {
            row.addError(new Exception(row.getDropoffLocationAddress() + " with name and apt # already in database"));
        }
        return new DeliveryLeg(row, pickupLocation, dropoffLocation);
    }

    private void saveLocationToDatabaseFromRow(Location location, DeliveryCSVRow row) {
        try {
            locationService.addLocation(location);
        } catch (LocationExistsException e) {
            row.addError(e);
        }
    }

}
