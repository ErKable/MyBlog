package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.ReportingReason;
import it.cgmconsulting.myblog.entity.ReportingReasonId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReportingReasonRepository extends JpaRepository<ReportingReason, ReportingReasonId> {

    boolean existsByReportingReasonIdReasonAndEndDateIsNull(String reason);
    //boolean existsByReportingReasonIdReasonAndEndDateIsNull(String reason, LocalDateTime now);

    Optional<ReportingReason> findByReportingReasonIdReasonAndEndDateIsNull(String reason);
    @Query("SELECT r.reportingReasonId.reason " +
            "FROM ReportingReason r " +
            "WHERE r.reportingReasonId.reason = :reason " +
            "AND r.endDate IS NULL")
    Optional<ReportingReason> getReportingReason(String reason);

    //List<ReportingReason> findByEndDateIsNullOrderByReportingReasonIdReason();
    @Query("SELECT rr.reportingReasonId.reason FROM ReportingReason rr " +
            "WHERE rr.endDate IS NULL ORDER BY rr.reportingReasonId.reason")
    List<String> getValidReason();
}
