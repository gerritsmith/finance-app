package io.github.gerritsmith.financeapp.dto;

import java.time.YearMonth;

public class MonthReportDTO extends AbstractReportDTO {

    private YearMonth yearMonth;

    // Constructors
    public MonthReportDTO() {}

    // Getters and Builder Setters
    public YearMonth getYearMonth() {
        return yearMonth;
    }

    public MonthReportDTO setYearMonth(YearMonth yearMonth) {
        this.yearMonth = yearMonth;
        return this;
    }

}
