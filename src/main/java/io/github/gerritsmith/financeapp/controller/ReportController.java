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
        model.addAttribute("years", reportService.getYearsSpanningRecords(user));
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
        ReportByTemporalDTO reportByDayDTO = reportService.getReportByDay(user);
        model.addAttribute("reportByDayDTO", reportByDayDTO);

        TimeSeriesDTO dataToPlot = new TimeSeriesDTO();
        for (TemporalReportDTO dayReport : reportByDayDTO.getTemporalReports()) {
            dataToPlot.addDataPoint(dayReport.getTemporal(),
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
        ReportByTemporalDTO reportByMonthDTO = reportService.getReportByMonth(user);
        model.addAttribute("reportByMonthDTO", reportByMonthDTO);

        TimeSeriesDTO dataToPlot = new TimeSeriesDTO();
        for (TemporalReportDTO monthReport : reportByMonthDTO.getTemporalReports()) {
            dataToPlot.addDataPoint(((YearMonth) monthReport.getTemporal()).atDay(1),
                    monthReport.getDeliveryCount(),
                    monthReport.getDeliveryGroupCount(),
                    monthReport.getTotalRevenue(),
                    monthReport.getTotalShiftHoursAsDecimal(),
                    monthReport.getTotalShiftMiles(),
                    monthReport.getTotalExpenses());
        }
        model.addAttribute("dataToPlot", dataToPlot);

        return "report/by-month";
    }

}
