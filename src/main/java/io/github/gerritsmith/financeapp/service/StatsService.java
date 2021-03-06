package io.github.gerritsmith.financeapp.service;

import io.github.gerritsmith.financeapp.dto.DeliveryStatsDTO;
import io.github.gerritsmith.financeapp.dto.UserStatsDTO;
import io.github.gerritsmith.financeapp.model.Delivery;
import io.github.gerritsmith.financeapp.model.DeliveryLeg;
import io.github.gerritsmith.financeapp.model.Shift;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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

        int deliveryCount = countDeliveries(deliveries);

        List<Shift> shifts = shiftService.findAllShiftsByUser(user);
        Duration shiftTotalDuration = shifts.stream()
                .map(s -> Duration.between(s.getStartTime(), s.getEndTime()))
                .reduce(Duration.ZERO, Duration::plus);
        double decimalTotalHours = shiftTotalDuration.toHours() + shiftTotalDuration.toMinutesPart()/60.0;

        UserStatsDTO userStatsDTO = new UserStatsDTO();
        userStatsDTO.setDeliveryCount(deliveryCount)
                .setDeliveryTotalRevenue(deliveryTotalStats.getSum())
                .setRevenuePerDelivery(deliveryTotalStats.getSum()/deliveryCount)
                .setShiftCount(shifts.size())
                .setShiftTotalDuration(shiftTotalDuration)
                .setRevenuePerHour(deliveryTotalStats.getSum()/decimalTotalHours);
        return userStatsDTO;
    }

    public DeliveryStatsDTO getDeliveryStats(List<Delivery> deliveries) {
         return deliveries.stream().reduce(
                 new DeliveryStatsDTO(),
                 (s, d) -> DeliveryStatsDTO.sum(
                        s,
                        new DeliveryStatsDTO(
                                d.getLegs().size(),
                                d.getAppMiles(),
                                d.getAppWaitTime(),
                                d.getTotalMiles(),
                                d.getTotalTime(),
                                d.getBasePay(),
                                d.getLegs().stream()
                                        .map(DeliveryLeg::getTip)
                                        .filter(Objects::nonNull)
                                        .reduce(0.0, Double::sum),
                                d.getLegs().stream()
                                        .map(DeliveryLeg::getCash)
                                        .filter(Objects::nonNull)
                                        .reduce(0.0, Double::sum)
                        )
                 ),
                 DeliveryStatsDTO::sum
         );
    }

    public int countDeliveries(List<Delivery> deliveries) {
        int deliveryCount = 0;
        for (Delivery delivery : deliveries) {
            deliveryCount += delivery.getLegs().size();
        }
        return deliveryCount;
    }

    public Double sumDoubles(Stream<Double> numbers) {
        return numbers.filter(Objects::nonNull).reduce(0.0, Double::sum);
    }

    public Duration sumDurations(Stream<Duration> durations) {
        return durations.reduce(Duration.ZERO, Duration::plus);
    }

}
