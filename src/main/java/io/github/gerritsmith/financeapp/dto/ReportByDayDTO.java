package io.github.gerritsmith.financeapp.dto;

import java.util.List;

public class ReportByDayDTO {

    private List<DayReportDTO> dailyReports;

    public List<DayReportDTO> getDailyReports() {
        return dailyReports;
    }

    public void setDailyReports(List<DayReportDTO> dailyReports) {
        this.dailyReports = dailyReports;
    }

}
