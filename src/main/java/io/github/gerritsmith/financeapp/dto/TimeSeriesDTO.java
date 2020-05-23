package io.github.gerritsmith.financeapp.dto;

import java.time.temporal.Temporal;
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
            "totalExpenses",
            "totalDeliveryTime",
            "totalDeliveryMiles",
            "totalAppMiles",
            "totalAppWaitTime",
            "totalBasePay",
            "totalTip",
            "totalCash"
    ));
    private final Map<String, String> columnDisplayNames = new HashMap<>(Map.ofEntries(
            entry("deliveryCount", "Deliveries"),
            entry("deliveryGroupCount", "Delivery Groups"),
            entry("totalRevenue", "Revenue"),
            entry("totalShiftHours", "Shift Hours"),
            entry("totalShiftMiles", "Shift Miles"),
            entry("totalExpenses", "Expenses"),
            entry("totalDeliveryTime", "Delivery Time (min)"),
            entry("totalDeliveryMiles", "Delivery Miles"),
            entry("totalAppMiles", "App Miles"),
            entry("totalAppWaitTime", "App Wait Time (min)"),
            entry("totalBasePay", "Base Pay"),
            entry("totalTip", "Tips"),
            entry("totalCash", "Cash Tips")
    ));
    private final List<String> denominatorNames = new ArrayList<>(Arrays.asList(
            "one",
            "deliveryCount",
            "totalRevenue",
            "deliveryGroupCount",
            "totalShiftHours",
            "totalShiftMiles",
            "totalDeliveryTime",
            "totalDeliveryMiles",
            "totalAppMiles",
            "totalAppWaitTime",
            "totalBasePay",
            "totalTip"
    ));
    private final Map<String, String> denominatorDisplayNames = new HashMap<>(Map.ofEntries(
            entry("one", "1 (i.e. just plot total)"),
            entry("deliveryCount", "Delivery"),
            entry("deliveryGroupCount", "Delivery Group"),
            entry("totalRevenue", "Revenue Dollar"),
            entry("totalShiftHours", "Shift Hour"),
            entry("totalShiftMiles", "Shift Mile"),
            entry("totalDeliveryTime", "Delivery Minute"),
            entry("totalDeliveryMiles", "Delivery Mile"),
            entry("totalAppMiles", "App Mile"),
            entry("totalAppWaitTime", "App Wait Minute"),
            entry("totalBasePay", "Base Pay Dollar"),
            entry("totalTip", "Tip Dollar")
    ));

    // Constructors
    public TimeSeriesDTO() {}

    public TimeSeriesDTO(ReportByTemporalDTO reportByTemporalDTO) {
        for (TemporalReportDTO temporalReport : reportByTemporalDTO.getTemporalReports()) {
            addDataPoint(temporalReport.getTemporal(),
                    temporalReport.getDeliveryStatsDTO().getDeliveryCount(),
                    temporalReport.getDeliveryGroupCount(),
                    temporalReport.getTotalRevenue(),
                    temporalReport.getTotalShiftHoursAsDecimal(),
                    temporalReport.getTotalShiftMiles(),
                    temporalReport.getTotalExpenses(),
                    temporalReport.getDeliveryStatsDTO().getTotalDeliveryTime(),
                    temporalReport.getDeliveryStatsDTO().getTotalDeliveryMiles(),
                    temporalReport.getDeliveryStatsDTO().getTotalAppMiles(),
                    temporalReport.getDeliveryStatsDTO().getTotalAppWaitTime(),
                    temporalReport.getDeliveryStatsDTO().getTotalBasePay(),
                    temporalReport.getDeliveryStatsDTO().getTotalTip(),
                    temporalReport.getDeliveryStatsDTO().getTotalCash());
        }
    }


    // 'Setter' method
    public void addDataPoint(Temporal temporal,
                             int deliveryCount,
                             int deliveryGroupCount,
                             double totalRevenue,
                             double totalShiftHours,
                             double totalShiftMiles,
                             double totalExpenses,
                             double totalDeliveryTime,
                             double totalDeliveryMiles,
                             double totalAppMiles,
                             double totalAppWaitTime,
                             double totalBasePay,
                             double totalTip,
                             double totalCash) {
        data.add(new DataPoint(temporal,
                deliveryCount,
                deliveryGroupCount,
                totalRevenue,
                totalShiftHours,
                totalShiftMiles,
                totalExpenses,
                totalDeliveryTime,
                totalDeliveryMiles,
                totalAppMiles,
                totalAppWaitTime,
                totalBasePay,
                totalTip,
                totalCash
        ));
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
        private Temporal temporal;
        private int deliveryCount;
        private int deliveryGroupCount;
        private double totalRevenue;
        private double totalShiftHours;
        private double totalShiftMiles;
        private double totalExpenses;

        private double totalDeliveryTime;
        private double totalDeliveryMiles;
        private double totalAppMiles;
        private double totalAppWaitTime;
        private double totalBasePay;
        private double totalTip;
        private double totalCash;


        // Constructor
        public DataPoint(Temporal temporal,
                         int deliveryCount,
                         int deliveryGroupCount,
                         double totalRevenue,
                         double totalShiftHours,
                         double totalShiftMiles,
                         double totalExpenses,
                         double totalDeliveryTime,
                         double totalDeliveryMiles,
                         double totalAppMiles,
                         double totalAppWaitTime,
                         double totalBasePay,
                         double totalTip,
                         double totalCash) {
            this.temporal = temporal;
            this.deliveryCount = deliveryCount;
            this.deliveryGroupCount = deliveryGroupCount;
            this.totalRevenue = totalRevenue;
            this.totalShiftHours = totalShiftHours;
            this.totalShiftMiles = totalShiftMiles;
            this.totalExpenses = totalExpenses;
            this.totalDeliveryTime = totalDeliveryTime;
            this.totalDeliveryMiles = totalDeliveryMiles;
            this.totalAppMiles = totalAppMiles;
            this.totalAppWaitTime = totalAppWaitTime;
            this.totalBasePay = totalBasePay;
            this.totalTip = totalTip;
            this.totalCash = totalCash;
        }

        // Getters
        public Temporal getTemporal() {
            return temporal;
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

        public double getTotalDeliveryTime() {
            return totalDeliveryTime;
        }

        public double getTotalDeliveryMiles() {
            return totalDeliveryMiles;
        }

        public double getTotalAppMiles() {
            return totalAppMiles;
        }

        public double getTotalAppWaitTime() {
            return totalAppWaitTime;
        }

        public double getTotalBasePay() {
            return totalBasePay;
        }

        public double getTotalTip() {
            return totalTip;
        }

        public double getTotalCash() {
            return totalCash;
        }

    }

}
