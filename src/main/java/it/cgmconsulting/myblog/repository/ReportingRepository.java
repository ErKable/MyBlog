package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.dto.response.ReportingResponse;
import it.cgmconsulting.myblog.entity.Reporting;
import it.cgmconsulting.myblog.entity.ReportingId;
import it.cgmconsulting.myblog.entity.ReportingReason;
import it.cgmconsulting.myblog.entity.enumerated.ReportingStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportingRepository extends JpaRepository<Reporting, ReportingId> {

    @Query("SELECT new it.cgmconsulting.myblog.dto.response.ReportingResponse(" +
            "r.reportingId.comment.id, " +
            "r.reportingId.comment.comment, " +
            "COUNT(r.reportingId.comment.id) " +
            ") FROM Reporting r " +
            "WHERE r.reportingStatus = :reportingStatus " +
            "GROUP BY r.reportingId.comment.id, r.reportingId.comment.comment")
    List<ReportingResponse> getReportinsByStatus(ReportingStatus reportingStatus);

    boolean existsByReportingIdCommentIdAndReportingStatus(int commentId, ReportingStatus status);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Reporting r SET r.reportingStatus = :status WHERE r.reportingId.comment.id = :commentId")
    void setInProgress(int commentId, ReportingStatus status);

    List<Reporting> findByReportingIdCommentIdAndReportingStatusIsNot(int commentId, ReportingStatus reportingStatus);
    List<Reporting> findByReportingIdCommentIdAndReportingStatusIs(int commentId, ReportingStatus reportingStatus);
    boolean existsByReportingIdCommentIdAndReportingStatusIsNot(int commentId, ReportingStatus status);
    @Modifying @Transactional
    @Query(value="UPDATE Reporting r " +
            "SET r.reportingStatus = :status, " +
            "r.reportingReason = :reason, " +
            "r.updatedAt = :now " +
            "WHERE r.reportingId.comment.id = :commentId")
    void setClosed(int commentId, ReportingStatus status, ReportingReason reason, LocalDateTime now);

}
