package io.github.gerritsmith.financeapp.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimeSeriesDTO {

    private List<DataPoint> data = new ArrayList<>();
    private final List<String> columnNames = new ArrayList<>(Arrays.asList(
            "Number of Deliveries",
            "Number of Delivery Groups",
            "Total Revenue",
            "Total Shift Hours",
            "Total Shift Miles",
            "Total Expenses"
    ));

    // 'Setter' method
    public void addDataPoint(LocalDate date,
                             int deliveryCount,
                             int deliveryGroupCount,
                             double totalRevenue,
                             double totalShiftHours,
                             double totalShiftMiles,
                             double totalExpenses) {
        data.add(new DataPoint(date,
                               deliveryCount,
                               deliveryGroupCount,
                               totalRevenue,
                               totalShiftHours,
                               totalShiftMiles,
                               totalExpenses));
    }

    // Getters
    public List<DataPoint> getData() {
        return data;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    // Subclass for 'rows' of the data table
    private class DataPoint {
        private LocalDate date;
        private int deliveryCount;
        private int deliveryGroupCount;
        private double totalRevenue;
        private double totalShiftHours;
        private double totalShiftMiles;
        private double totalExpenses;

        // Constructor
        public DataPoint(LocalDate date,
                         int deliveryCount,
                         int deliveryGroupCount,
                         double totalRevenue,
                         double totalShiftHours,
                         double totalShiftMiles,
                         double totalExpenses) {
            this.date = date;
            this.deliveryCount = deliveryCount;
            this.deliveryGroupCount = deliveryGroupCount;
            this.totalRevenue = totalRevenue;
            this.totalShiftHours = totalShiftHours;
            this.totalShiftMiles = totalShiftMiles;
            this.totalExpenses = totalExpenses;
        }

        // Getters
        public LocalDate getDate() {
            return date;
        }

        public int getDeliveryCount() {
            return deliveryCount;
        }

        public int getDeliveryGroupCount() {
            return deliveryGroupCount;
        }

        public double getTotalRevenue() {
            return totalRevenue;
        }

        public double getTotalShiftHours() {
            return totalShiftHours;
        }

        public double getTotalShiftMiles() {
            return totalShiftMiles;
        }

        public double getTotalExpenses() {
            return totalExpenses;
        }
    }

}
