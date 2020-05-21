package io.github.gerritsmith.financeapp.controller;

import io.github.gerritsmith.financeapp.dto.DayReportDTO;
import io.github.gerritsmith.financeapp.dto.MonthReportDTO;
import io.github.gerritsmith.financeapp.dto.ReportByDayDTO;
import io.github.gerritsmith.financeapp.dto.TimeSeriesDTO;
import io.github.gerritsmith.financeapp.model.User;
import io.github.gerritsmith.financeapp.service.ReportService;
import io.github.gerritsmith.financeapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class ReportController {

    @Autowired
    UserService userService;

    @Autowired
    ReportService reportService;

    private List<String> months = new ArrayList<>(Arrays.asList(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    ));

    @ModelAttribute
    public void addControllerWideModelAttributes(Model model, Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user);
    }

    @GetMapping("/reports")
    public String displayReportsHome(Model model, @ModelAttribute User user) {
        model.addAttribute("months", months);
        model.addAttribute("years", reportService.getYearsSpanningRecords(user));
        return "report/home";
    }

    @GetMapping("/reports/by-day")
    public String displayDayReport(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")
                                               LocalDate date,
                                   Model model,
                                   @ModelAttribute User user) {
        DayReportDTO dayReportDTO = reportService.getDayReport(user, date);
        model.addAttribute("dayReportDTO", dayReportDTO);
        return "report/day";
    }

    @GetMapping("/report/by-day")
    public String displayReportByDay(Model model,
                                     @ModelAttribute User user) {
        ReportByDayDTO reportByDayDTO = reportService.getReportByDay(user);
        model.addAttribute("reportByDayDTO", reportByDayDTO);

        TimeSeriesDTO dataToPlot = new TimeSeriesDTO();
        for (DayReportDTO dayReport : reportByDayDTO.getDailyReports()) {
            dataToPlot.addDataPoint(dayReport.getDate(),
                                    dayReport.getDeliveryCount(),
                                    dayReport.getDeliveryGroupCount(),
                                    dayReport.getTotalRevenue(),
                                    dayReport.getTotalShiftHoursAsDecimal(),
                                    dayReport.getTotalShiftMiles(),
                                    dayReport.getTotalExpenses());
        }
        model.addAttribute("dataToPlot", dataToPlot);

        return "report/by-day";
    }

    @GetMapping("/reports/by-month")
    public String displayMonthReport(@RequestParam int month,
                                     @RequestParam Year inYear,
                                     Model model,
                                     @ModelAttribute User user) {
        YearMonth yearMonth = inYear.atMonth(month);
        MonthReportDTO monthReportDTO = reportService.getMonthReport(user, yearMonth);
        model.addAttribute("monthReportDTO", monthReportDTO);
        return "report/month";
    }

}
