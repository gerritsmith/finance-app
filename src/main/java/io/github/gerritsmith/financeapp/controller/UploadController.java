package io.github.gerritsmith.financeapp.controller;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import io.github.gerritsmith.financeapp.dto.upload.DeliveryCSVRow;
import io.github.gerritsmith.financeapp.dto.upload.ShiftCSVRow;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Controller
public class UploadController {

    @GetMapping("/upload")
    public String displayUploadHome() {
        return "upload/home";
    }

    @PostMapping("/upload/shifts")
    public String uploadShiftCSVFile(@RequestParam MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("errorMessage", "File is empty!");
            model.addAttribute("isSuccessful", false);
        } else {
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                CsvToBean<ShiftCSVRow> csvToBean = new CsvToBeanBuilder<ShiftCSVRow>(reader)
                        .withType(ShiftCSVRow.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();
                List<ShiftCSVRow> rows = csvToBean.parse();
                // TODO: create shift entities and save to database
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
