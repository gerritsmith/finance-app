package io.github.gerritsmith.financeapp.dto;

import java.util.ArrayList;
import java.util.List;

public class ReportByTemporalDTO {

    private List<TemporalReportDTO> temporalReports = new ArrayList<>();

    public void addTemporalReport(TemporalReportDTO temporalReport) {
        temporalReports.add(temporalReport);
    }

    public List<TemporalReportDTO> getTemporalReports() {
        return temporalReports;
    }

    public void setTemporalReports(List<TemporalReportDTO> temporalReports) {
        this.temporalReports = temporalReports;
    }

}
