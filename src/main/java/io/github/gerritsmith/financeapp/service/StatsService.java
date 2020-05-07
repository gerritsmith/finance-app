package io.github.gerritsmith.financeapp.service;

import io.github.gerritsmith.financeapp.dto.StatsForDayDTO;
import io.github.gerritsmith.financeapp.dto.UserStatsDTO;
import io.github.gerritsmith.financeapp.model.Delivery;
import io.github.gerritsmith.financeapp.model.Shift;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.DoubleSummaryStatistics;
import java.util.List;

@Service
public class StatsService {

    DeliveryService deliveryService;
    ShiftService shiftService;
    ExpenseService expenseService;

    // Constructors
    @Autowired
    public StatsService(DeliveryService deliveryService,
                        ShiftService shiftService,
                        ExpenseService expenseService) {
        this.deliveryService = deliveryService;
        this.shiftService = shiftService;
        this.expenseService = expenseService;
    }

    // Methods
    public UserStatsDTO getUserStats(User user) {
        List<Delivery> deliveries = deliveryService.findAllDeliveriesByUser(user);
        DoubleSummaryStatistics deliveryTotalStats = deliveries.stream()
                .mapToDouble(Delivery::getTotal)
                .summaryStatistics();

        List<Shift> shifts = shiftService.findAllShiftsByUser(user);
        Duration shiftTotalDuration = shifts.stream()
                .map(s -> Duration.between(s.getStartTime(), s.getEndTime()))
                .reduce(Duration.ZERO, Duration::plus);
        double decimalTotalHours = shiftTotalDuration.toHours() + shiftTotalDuration.toMinutesPart()/60.0;

        UserStatsDTO userStatsDTO = new UserStatsDTO();
        userStatsDTO.setDeliveryCount(deliveryTotalStats.getCount())
                .setDeliveryTotalRevenue(deliveryTotalStats.getSum())
                .setRevenuePerDelivery(deliveryTotalStats.getSum()/deliveryTotalStats.getCount())
                .setShiftCount(shifts.size())
                .setShiftTotalDuration(shiftTotalDuration)
                .setRevenuePerHour(deliveryTotalStats.getSum()/decimalTotalHours);
        return userStatsDTO;
    }

    public StatsForDayDTO getUserStatsForDay(User user, LocalDate date) {
        List<Delivery> deliveries = deliveryService.findByUserAndDate(user, date);

        StatsForDayDTO statsForDayDTO = new StatsForDayDTO();
        statsForDayDTO.setDeliveries(deliveries);
        return statsForDayDTO;
    }

}
