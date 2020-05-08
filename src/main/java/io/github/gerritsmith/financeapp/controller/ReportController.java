package io.github.gerritsmith.financeapp.controller;

import io.github.gerritsmith.financeapp.dto.DayReportDTO;
import io.github.gerritsmith.financeapp.dto.ReportByDayDTO;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        model.addAttribute("dayReportDTO", dayReportDTO);
        return "report/day";
    }

    @GetMapping("/report/by-day")
    public String displayReportByDay(Model model,
                                     Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        ReportByDayDTO reportByDayDTO = reportService.getReportByDay(user);
        model.addAttribute("reportByDayDTO", reportByDayDTO);

        List<LocalDate> dates = reportByDayDTO.getDailyReports()
                .stream()
                .map(DayReportDTO::getDate)
                .collect(Collectors.toList());
        List<Double> totals = reportByDayDTO.getDailyReports()
                .stream()
                .map(DayReportDTO::getTotalRevenue)
                .collect(Collectors.toList());
        model.addAttribute("dates", dates);
        model.addAttribute("totals", totals);
        return "report/by-day";
    }

}
