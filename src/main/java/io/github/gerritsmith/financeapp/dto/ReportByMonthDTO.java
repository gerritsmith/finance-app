package io.github.gerritsmith.financeapp.dto;

import java.util.List;

public class ReportByMonthDTO {

    private List<MonthReportDTO> monthlyReports;

    public List<MonthReportDTO> getMonthlyReports() {
        return monthlyReports;
    }

    public void setMonthlyReports(List<MonthReportDTO> monthlyReports) {
        this.monthlyReports = monthlyReports;
    }

}
