package io.github.gerritsmith.financeapp.controller;

import io.github.gerritsmith.financeapp.dto.DayReportDTO;
import io.github.gerritsmith.financeapp.model.User;
import io.github.gerritsmith.financeapp.service.ReportService;
import io.github.gerritsmith.financeapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;

@Controller
public class ReportController {

    @Autowired
    UserService userService;

    @Autowired
    ReportService reportService;

    @GetMapping("/reports")
    public String displayReportsHome() {
        return "report/home";
    }

    @GetMapping("/reports/by-day")
    public String displayDayReport(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")
                                               LocalDate date,
                                   Model model,
                                   Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        DayReportDTO dayReportDTO = reportService.getDayReport(user, date);
        model.addAttribute("date", date);
        model.addAttribute("dayReportDTO", dayReportDTO);
        return "report/day";
    }

}
