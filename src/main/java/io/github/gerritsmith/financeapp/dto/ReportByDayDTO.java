package io.github.gerritsmith.financeapp.dto;

import java.util.List;

public class ReportByDayDTO {

    List<DayReportDTO> dailyReports;

    public List<DayReportDTO> getDailyReports() {
        return dailyReports;
    }

    public ReportByDayDTO setDailyReports(List<DayReportDTO> dailyReports) {
        this.dailyReports = dailyReports;
        return this;
    }
}
