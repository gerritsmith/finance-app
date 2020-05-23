package io.github.gerritsmith.financeapp.service;

import io.github.gerritsmith.financeapp.dto.ReportByTemporalDTO;
import io.github.gerritsmith.financeapp.dto.TemporalReportDTO;
import io.github.gerritsmith.financeapp.model.Delivery;
import io.github.gerritsmith.financeapp.model.Expense;
import io.github.gerritsmith.financeapp.model.Shift;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.Temporal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public TemporalReportDTO getTemporalReport(User user, Temporal temporal) {
        List<Delivery> deliveries = deliveryService.findAllByUserInTemporal(user, temporal);
        List<Shift> shifts = shiftService.findAllByUserInTemporal(user, temporal);
        List<Expense> expenses = expenseService.findAllByUserInTemporal(user, temporal);

        TemporalReportDTO temporalReportDTO = new TemporalReportDTO();
        temporalReportDTO.setTemporal(temporal)
                .setDeliveries(deliveries)
                .setShifts(shifts)
                .setExpenses(expenses)
                .setTotalRevenue(statsService.sumDoubles(
                        deliveries.stream().map(Delivery::getTotal)))
                .setTotalShiftHours(statsService.sumDurations(
                        shifts.stream().map(s -> Duration.between(s.getStartTime(), s.getEndTime()))))
                .setTotalShiftMiles(statsService.sumDoubles(
                        shifts.stream().map(Shift::getMiles)))
                .setTotalExpenses(statsService.sumDoubles(
                        expenses.stream().map(Expense::getAmount)))
                .setDeliveryStatsDTO(statsService.getDeliveryStats(deliveries));
        return temporalReportDTO;
    }

    public ReportByTemporalDTO getReportByTemporal(User user,
                                                   Class<? extends Temporal> temporalClass) {
        List<Temporal> temporals = getTemporalsWithRecords(user, temporalClass);
        ReportByTemporalDTO reportByTemporalDTO = new ReportByTemporalDTO();
        for (Temporal temporal : temporals) {
            reportByTemporalDTO.addTemporalReport(getTemporalReport(user, temporal));
        }
        return reportByTemporalDTO;
    }

    public List<Temporal> getTemporalsWithRecords(User user,
                                                   Class<? extends Temporal> temporalClass) {
        Stream<LocalDate> dates = shiftService.findAllShiftsByUser(user).stream().map(Shift::getDate);
        if (temporalClass.equals(YearMonth.class)) {
            return dates.map(d -> YearMonth.of(d.getYear(), d.getMonth()))
                    .distinct()
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());
        } else if (temporalClass.equals(Year.class)) {
            return dates.map(d -> Year.of(d.getYear()))
                    .distinct()
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());
        } else {
            return dates.distinct()
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());
        }
    }

}
