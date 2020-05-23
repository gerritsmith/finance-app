package io.github.gerritsmith.financeapp.controller;

import io.github.gerritsmith.financeapp.dto.ReportByTemporalDTO;
import io.github.gerritsmith.financeapp.dto.TemporalReportDTO;
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

    private static final List<String> months = new ArrayList<>(Arrays.asList(
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
        model.addAttribute("years", reportService.getTemporalsWithRecords(user, Year.class));
        return "report/home";
    }

    @GetMapping("/reports/by-day")
    public String displayDayReport(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd")
                                               LocalDate date,
                                   Model model,
                                   @ModelAttribute User user) {
        TemporalReportDTO dayReportDTO = reportService.getTemporalReport(user, date);
        model.addAttribute("dayReportDTO", dayReportDTO);
        return "report/day";
    }

    @GetMapping("/report/by-day")
    public String displayReportByDay(Model model,
                                     @ModelAttribute User user) {
        ReportByTemporalDTO reportByDayDTO = reportService.getReportByTemporal(user, LocalDate.class);
        model.addAttribute("reportByDayDTO", reportByDayDTO);
        TimeSeriesDTO dataToPlot = new TimeSeriesDTO(reportByDayDTO);
        model.addAttribute("dataToPlot", dataToPlot);
        return "report/by-day";
    }

    @GetMapping("/reports/by-month")
    public String displayMonthReport(@RequestParam @DateTimeFormat(pattern = "yyyy-MM")
                                                 YearMonth month,
                                     Model model,
                                     @ModelAttribute User user) {
        TemporalReportDTO monthReportDTO = reportService.getTemporalReport(user, month);
        model.addAttribute("monthReportDTO", monthReportDTO);
        return "report/month";
    }

    @GetMapping("/report/by-month")
    public String displayReportByMonth(Model model,
                                       @ModelAttribute User user) {
        ReportByTemporalDTO reportByMonthDTO = reportService.getReportByTemporal(user, YearMonth.class);
        model.addAttribute("reportByMonthDTO", reportByMonthDTO);
        TimeSeriesDTO dataToPlot = new TimeSeriesDTO(reportByMonthDTO);
        model.addAttribute("dataToPlot", dataToPlot);
        return "report/by-month";
    }

    @GetMapping("/reports/by-year")
    public String displayYearReport(@RequestParam @DateTimeFormat(pattern = "yyyy")
                                                Year year,
                                     Model model,
                                     @ModelAttribute User user) {
        TemporalReportDTO yearReportDTO = reportService.getTemporalReport(user, year);
        model.addAttribute("yearReportDTO", yearReportDTO);
        return "report/year";
    }

    @GetMapping("/report/by-year")
    public String displayReportByYear(Model model,
                                      @ModelAttribute User user) {
        ReportByTemporalDTO reportByYearDTO = reportService.getReportByTemporal(user, Year.class);
        model.addAttribute("reportByYearDTO", reportByYearDTO);
        TimeSeriesDTO dataToPlot = new TimeSeriesDTO(reportByYearDTO);
        model.addAttribute("dataToPlot", dataToPlot);
        return "report/by-year";
    }

}
