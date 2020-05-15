package io.github.gerritsmith.financeapp.dto;

import java.time.LocalDate;
import java.util.*;

import static java.util.Map.entry;

public class TimeSeriesDTO {

    private final List<DataPoint> data = new ArrayList<>();
    private final List<String> columnNames = new ArrayList<>(Arrays.asList(
            "deliveryCount",
            "deliveryGroupCount",
            "totalRevenue",
            "totalShiftHours",
            "totalShiftMiles",
            "totalExpenses"
    ));
    private final Map<String, String> columnDisplayNames = new HashMap<>(Map.ofEntries(
            entry("deliveryCount", "Deliveries"),
            entry("deliveryGroupCount", "Delivery Groups"),
            entry("totalRevenue", "Revenue"),
            entry("totalShiftHours", "Shift Hours"),
            entry("totalShiftMiles", "Shift Miles"),
            entry("totalExpenses", "Expenses")
    ));
    private final List<String> denominatorNames = new ArrayList<>(Arrays.asList(
            "one",
            "deliveryCount",
            "deliveryGroupCount",
            "totalShiftHours",
            "totalShiftMiles"
    ));
    private final Map<String, String> denominatorDisplayNames = new HashMap<>(Map.ofEntries(
            entry("one", "1 (i.e. just plot total)"),
            entry("deliveryCount", "Delivery"),
            entry("deliveryGroupCount", "Delivery Group"),
            entry("totalShiftHours", "Shift Hour"),
            entry("totalShiftMiles", "Shift Mile")
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

    public Map<String, String> getColumnDisplayNames() {
        return columnDisplayNames;
    }

    public List<String> getDenominatorNames() {
        return denominatorNames;
    }

    public Map<String, String> getDenominatorDisplayNames() {
        return denominatorDisplayNames;
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
