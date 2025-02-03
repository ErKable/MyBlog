package it.cgmconsulting.myblog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ReportingReason {
    @EmbeddedId
    private ReportingReasonId reportingReasonId;
    private LocalDate endDate;
    //@Column(nullable = false)
    // non serve nullable = false perche essendo un tipo primitivo
    // viene inizializzato al suo valore di default, infatti sul DB
    //verra riportato come campo obbligatorio
    private byte severity;

    public ReportingReason(ReportingReasonId reportingReasonId, byte severity) {
        this.reportingReasonId = reportingReasonId;
        this.severity = severity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportingReason that = (ReportingReason) o;
        return Objects.equals(reportingReasonId, that.reportingReasonId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportingReasonId);
    }
}
