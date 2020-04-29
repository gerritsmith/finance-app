package io.github.gerritsmith.financeapp.service;

import io.github.gerritsmith.financeapp.dto.UserStatsDTO;
import io.github.gerritsmith.financeapp.model.Delivery;
import io.github.gerritsmith.financeapp.model.Shift;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.DoubleSummaryStatistics;
import java.util.List;

@Service
public class StatsService {

    DeliveryService deliveryService;
    ShiftService shiftService;

    // Constructors
    @Autowired
    public StatsService(DeliveryService deliveryService, ShiftService shiftService) {
        this.deliveryService = deliveryService;
        this.shiftService = shiftService;
    }

    // Methods
    public UserStatsDTO getUserStats(User user) {
        List<Delivery> deliveries = new ArrayList<>((Collection<Delivery>) deliveryService.findAllDeliveriesByUser(user));
        DoubleSummaryStatistics deliveryTotalStats = deliveries.stream()
                .mapToDouble(Delivery::getTotal)
                .summaryStatistics();

        List<Shift> shifts = new ArrayList<>((Collection<Shift>) shiftService.findAllShiftsByUser(user));
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

}
