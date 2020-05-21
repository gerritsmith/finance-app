package io.github.gerritsmith.financeapp.service;

import io.github.gerritsmith.financeapp.dto.DayReportDTO;
import io.github.gerritsmith.financeapp.dto.MonthReportDTO;
import io.github.gerritsmith.financeapp.dto.ReportByDayDTO;
import io.github.gerritsmith.financeapp.dto.ReportByMonthDTO;
import io.github.gerritsmith.financeapp.model.Delivery;
import io.github.gerritsmith.financeapp.model.Expense;
import io.github.gerritsmith.financeapp.model.Shift;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    DeliveryService deliveryService;
    ShiftService shiftService;
    ExpenseService expenseService;

    StatsService statsService;

    // Constructors
    @Autowired
    public ReportService(DeliveryService deliveryService,
                         ShiftService shiftService,
                         ExpenseService expenseService,
                         StatsService statsService) {
        this.deliveryService = deliveryService;
        this.shiftService = shiftService;
        this.expenseService = expenseService;
        this.statsService = statsService;
    }

    // Methods
    public DayReportDTO getDayReport(User user, LocalDate date) {
        List<Delivery> deliveries = deliveryService.findByUserAndDate(user, date);
        List<Shift> shifts = shiftService.findByUserAndDate(user, date);
        List<Expense> expenses = expenseService.findByUserAndDate(user, date);

        int deliveryCount = 0;
        for (Delivery delivery : deliveries) {
            deliveryCount += delivery.getLegs().size();
        }

        DayReportDTO dayReportDTO = new DayReportDTO();
        dayReportDTO.setDate(date)
                .setDeliveries(deliveries)
                .setShifts(shifts)
                .setExpenses(expenses)
                .setDeliveryCount(deliveryCount)
                .setTotalRevenue(statsService.sumDoubles(
                        deliveries.stream().map(Delivery::getTotal)))
                .setTotalShiftHours(statsService.sumDurations(
                        shifts.stream().map(s -> Duration.between(s.getStartTime(), s.getEndTime()))))
                .setTotalShiftMiles(statsService.sumDoubles(
                        shifts.stream().map(Shift::getMiles)))
                .setTotalExpenses(statsService.sumDoubles(
                        expenses.stream().map(Expense::getAmount)));
        return dayReportDTO;
    }

    public ReportByDayDTO getReportByDay(User user) {
        List<Shift> shifts = shiftService.findAllShiftsByUser(user);
        List<LocalDate> dates = shifts.stream().map(Shift::getDate).distinct().collect(Collectors.toList());
        List<DayReportDTO> dailyReports = new ArrayList<>();
        for (LocalDate date : dates) {
            dailyReports.add(getDayReport(user, date));
        }
        dailyReports.sort((o1, o2) -> {
            if (o1.getDate().isAfter(o2.getDate())) {
                return -1;
            } else if (o1.getDate().equals(o2.getDate())) {
                return 0;
            }
            return 1;
        });
        ReportByDayDTO reportByDayDTO = new ReportByDayDTO();
        reportByDayDTO.setDailyReports(dailyReports);
        return reportByDayDTO;
    }

    public MonthReportDTO getMonthReport(User user, YearMonth yearMonth) {
        List<Delivery> deliveries = deliveryService.findByUserAndYearMonth(user, yearMonth);
        List<Shift> shifts = shiftService.findByUserAndYearMonth(user, yearMonth);
        List<Expense> expenses = expenseService.findByUserAndYearMonth(user, yearMonth);

        int deliveryCount = 0;
        for (Delivery delivery : deliveries) {
            deliveryCount += delivery.getLegs().size();
        }

        MonthReportDTO monthReportDTO = new MonthReportDTO();
        monthReportDTO.setYearMonth(yearMonth)
                .setDeliveries(deliveries)
                .setShifts(shifts)
                .setExpenses(expenses)
                .setDeliveryCount(deliveryCount)
                .setTotalRevenue(statsService.sumDoubles(
                        deliveries.stream().map(Delivery::getTotal)))
                .setTotalShiftHours(statsService.sumDurations(
                        shifts.stream().map(s -> Duration.between(s.getStartTime(), s.getEndTime()))))
                .setTotalShiftMiles(statsService.sumDoubles(
                        shifts.stream().map(Shift::getMiles)))
                .setTotalExpenses(statsService.sumDoubles(
                        expenses.stream().map(Expense::getAmount)));
        return monthReportDTO;
    }

    public ReportByMonthDTO getReportByMonth(User user) {
        List<Shift> shifts = shiftService.findAllShiftsByUser(user);
        List<YearMonth> yearMonths = shifts.stream()
                .map(Shift::getDate)
                .map(d -> YearMonth.of(d.getYear(), d.getMonth()))
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        List<MonthReportDTO> monthlyReports = new ArrayList<>();
        for (YearMonth yearMonth : yearMonths) {
            monthlyReports.add(getMonthReport(user, yearMonth));
        }
        ReportByMonthDTO reportByMonthDTO = new ReportByMonthDTO();
        reportByMonthDTO.setMonthlyReports(monthlyReports);
        return reportByMonthDTO;
    }

    public List<Integer> getYearsSpanningRecords(User user) {
        List<Shift> shifts = shiftService.findAllShiftsByUser(user);
        List<Integer> yearsFound = shifts.stream()
                .map(Shift::getDate)
                .map(LocalDate::getYear)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        List<Integer> years = new ArrayList<>();
        for (int year = yearsFound.get(yearsFound.size() - 1); yearsFound.get(0) <= year; year--) {
            years.add(year);
        }
        return years;
    }

}
