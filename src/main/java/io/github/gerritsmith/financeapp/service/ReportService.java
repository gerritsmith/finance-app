package io.github.gerritsmith.financeapp.service;

import io.github.gerritsmith.financeapp.dto.DayReportDTO;
import io.github.gerritsmith.financeapp.dto.ReportByDayDTO;
import io.github.gerritsmith.financeapp.model.Delivery;
import io.github.gerritsmith.financeapp.model.Expense;
import io.github.gerritsmith.financeapp.model.Shift;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
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
                .setTotalExpenses(statsService.sumDoubles(expenses.stream().map(Expense::getAmount)));
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

}