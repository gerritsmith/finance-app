package io.github.gerritsmith.financeapp.dto;

import java.time.LocalDate;

public class DayReportDTO extends AbstractReportDTO {

    private LocalDate date;

    // Constructors
    public DayReportDTO() {}

    // Getters and Builder Setters
    public LocalDate getDate() {
        return date;
    }

    public DayReportDTO setDate(LocalDate date) {
        this.date = date;
        return this;
    }

}
