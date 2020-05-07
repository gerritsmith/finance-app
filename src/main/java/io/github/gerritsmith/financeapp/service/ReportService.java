package io.github.gerritsmith.financeapp.service;

import io.github.gerritsmith.financeapp.dto.DayReportDTO;
import io.github.gerritsmith.financeapp.model.Delivery;
import io.github.gerritsmith.financeapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    DeliveryService deliveryService;
    ShiftService shiftService;
    ExpenseService expenseService;

    // Constructors
    @Autowired
    public ReportService(DeliveryService deliveryService,
                         ShiftService shiftService,
                         ExpenseService expenseService) {
        this.deliveryService = deliveryService;
        this.shiftService = shiftService;
        this.expenseService = expenseService;
    }

    // Methods
    public DayReportDTO getDayReport(User user, LocalDate date) {
        List<Delivery> deliveries = deliveryService.findByUserAndDate(user, date);

        DayReportDTO dayReportDTO = new DayReportDTO();
        dayReportDTO.setDeliveries(deliveries);
        return dayReportDTO;
    }

}
