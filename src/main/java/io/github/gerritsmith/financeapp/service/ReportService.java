package io.github.gerritsmith.financeapp.service;

import io.github.gerritsmith.financeapp.dto.*;
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
    public TemporalReportDTO getDayReport(User user, LocalDate date) {
        List<Delivery> deliveries = deliveryService.findByUserAndDate(user, date);
        List<Shift> shifts = shiftService.findByUserAndDate(user, date);
        List<Expense> expenses = expenseService.findByUserAndDate(user, date);

        int deliveryCount = statsService.countDeliveries(deliveries);

        TemporalReportDTO dayReportDTO = new TemporalReportDTO();
        dayReportDTO.setTemporal(date)
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

    public ReportByTemporalDTO getReportByDay(User user) {
        List<Shift> shifts = shiftService.findAllShiftsByUser(user);
        List<LocalDate> dates = shifts.stream()
                .map(Shift::getDate)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        ReportByTemporalDTO reportByDayDTO = new ReportByTemporalDTO();
        for (LocalDate date : dates) {
            reportByDayDTO.addTemporalReport(getDayReport(user, date));
        }
        return reportByDayDTO;
    }

    public TemporalReportDTO getMonthReport(User user, YearMonth yearMonth) {
        List<Delivery> deliveries = deliveryService.findByUserAndYearMonth(user, yearMonth);
        List<Shift> shifts = shiftService.findByUserAndYearMonth(user, yearMonth);
        List<Expense> expenses = expenseService.findByUserAndYearMonth(user, yearMonth);

        int deliveryCount = statsService.countDeliveries(deliveries);

        TemporalReportDTO monthReportDTO = new TemporalReportDTO();
        monthReportDTO.setTemporal(yearMonth)
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

    public ReportByTemporalDTO getReportByMonth(User user) {
        List<Shift> shifts = shiftService.findAllShiftsByUser(user);
        List<YearMonth> yearMonths = shifts.stream()
                .map(Shift::getDate)
                .map(d -> YearMonth.of(d.getYear(), d.getMonth()))
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        ReportByTemporalDTO reportByMonthDTO = new ReportByTemporalDTO();
        for (YearMonth yearMonth : yearMonths) {
            reportByMonthDTO.addTemporalReport(getMonthReport(user, yearMonth));
        }
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
        if (yearsFound.size() == 0) {
            years.add(LocalDate.now().getYear());
            return years;
        }
        for (int year = yearsFound.get(yearsFound.size() - 1); yearsFound.get(0) <= year; year--) {
            years.add(year);
        }
        return years;
    }

}
