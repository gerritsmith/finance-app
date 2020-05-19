package io.github.gerritsmith.financeapp.controller;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import io.github.gerritsmith.financeapp.dto.upload.DeliveryCSVRow;
import io.github.gerritsmith.financeapp.dto.upload.ShiftCSVRow;
import io.github.gerritsmith.financeapp.exception.ShiftExistsException;
import io.github.gerritsmith.financeapp.model.Shift;
import io.github.gerritsmith.financeapp.model.User;
import io.github.gerritsmith.financeapp.service.ShiftService;
import io.github.gerritsmith.financeapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Stream;

@Controller
public class UploadController {

    @Autowired
    UserService userService;

    @Autowired
    ShiftService shiftService;

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
                        .peek(row -> {
                            try {
                                Shift shift = new Shift(user, row);
                                shiftService.addShift(shift);
                                row.setAddedToDatabase(true);
                            } catch (ShiftExistsException e) {
                                row.addError(e);
                            }
                        })
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

    @PostMapping("/upload/deliveries")
    public String uploadDeliveryCSVFile(@RequestParam MultipartFile file, Model model) {
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
                // TODO: create delivery, delivery leg, and location entities and save to database
                model.addAttribute("rows", rows);
                model.addAttribute("isSuccessful", true);
            } catch (IOException e) {
                model.addAttribute("errorMessage", "An error occurred while processing the CSV file.");
                model.addAttribute("isSuccessful", false);
            }
        }
        return "/upload/deliveries";
    }

}
