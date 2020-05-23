package io.github.gerritsmith.financeapp.dto;

import java.time.temporal.Temporal;

public class TemporalReportDTO extends AbstractReportDTO {

    private Temporal temporal;

    // Constructors
    public TemporalReportDTO() {}

    // Getters and Builder Setters
    public Temporal getTemporal() {
        return temporal;
    }

    public TemporalReportDTO setTemporal(Temporal temporal) {
        this.temporal = temporal;
        return this;
    }

}
