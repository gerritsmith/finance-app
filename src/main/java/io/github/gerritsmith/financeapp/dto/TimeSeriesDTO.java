package io.github.gerritsmith.financeapp.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TimeSeriesDTO {

    private List<DataPoint> data = new ArrayList<>();
    private String name;

    public void addDataPoint(LocalDate date, Double value) {
        data.add(new DataPoint(date, value));
    }

    public List<DataPoint> getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private class DataPoint {
        private LocalDate date;
        private Double value;

        public DataPoint(LocalDate date, Double value) {
            this.date = date;
            this.value = value;
        }

        public LocalDate getDate() {
            return date;
        }

        public Double getValue() {
            return value;
        }
    }

}
