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
        double deliveryTotalRevenue = 0;
        for (Delivery delivery : deliveries) {
            deliveryTotalRevenue += delivery.getTotal();
        }
        List<Shift> shifts = new ArrayList<>((Collection<Shift>) shiftService.findAllShiftsByUser(user));
        Duration shiftTotalDuration = Duration.ofHours(0);
        for (Shift shift : shifts) {
            shiftTotalDuration = shiftTotalDuration.plus(Duration.between(shift.getStartTime(), shift.getEndTime()));
        }
        UserStatsDTO userStatsDTO = new UserStatsDTO();
        userStatsDTO.setDeliveryCount(deliveries.size());
        userStatsDTO.setDeliveryTotalRevenue(deliveryTotalRevenue);
        userStatsDTO.setRevenuePerDelivery(deliveryTotalRevenue/deliveries.size());
        userStatsDTO.setShiftCount(shifts.size());
        userStatsDTO.setShiftTotalDuration(shiftTotalDuration);
        double decimalTotalHours = shiftTotalDuration.toHours() + shiftTotalDuration.toMinutesPart()/60.0;
        userStatsDTO.setRevenuePerHour(deliveryTotalRevenue/decimalTotalHours);
        return userStatsDTO;
    }

}
