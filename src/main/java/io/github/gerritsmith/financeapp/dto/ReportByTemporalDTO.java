package io.github.gerritsmith.financeapp.dto;

import java.util.List;

public class ReportByTemporalDTO {

    private List<TemporalReportDTO> temporalReports;

    public List<TemporalReportDTO> getTemporalReports() {
        return temporalReports;
    }

    public void setTemporalReports(List<TemporalReportDTO> temporalReports) {
        this.temporalReports = temporalReports;
    }

}
