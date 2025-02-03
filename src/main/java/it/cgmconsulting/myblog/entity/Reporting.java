package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import it.cgmconsulting.myblog.entity.enumerated.ReportingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reporting extends CreationUpdate {
    @EmbeddedId
    private ReportingId reportingId;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportingStatus reportingStatus = ReportingStatus.NEW;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "reason", referencedColumnName = "reason", nullable = false),
            @JoinColumn(name = "start_date", referencedColumnName = "startDate", nullable = false)
    })
    private ReportingReason reportingReason;

    public Reporting(ReportingId reportingId, ReportingReason reportingReason) {
        this.reportingId = reportingId;
        this.reportingReason = reportingReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reporting reporting = (Reporting) o;
        return Objects.equals(reportingId, reporting.reportingId) && Objects.equals(reportingReason, reporting.reportingReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportingId, reportingReason);
    }

    @Override
    public String toString() {
        return "Reporting{" +
                "reportingId=" + reportingId +
                ", reportingStatus=" + reportingStatus +
                ", reportingReason=" + reportingReason +
                '}';
    }
}
