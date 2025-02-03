package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.dto.response.ReportingResponse;
import it.cgmconsulting.myblog.entity.*;
import it.cgmconsulting.myblog.entity.enumerated.ReportingStatus;
import it.cgmconsulting.myblog.repository.ReportingReasonRepository;
import it.cgmconsulting.myblog.repository.ReportingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportingService {
    private final ReportingReasonRepository reportingReasonRepository;
    private final ReportingRepository reportingRepository;
    private final CommentService commentService;
    private final ReportingReasonService reportingReasonService;

    public String createReporting(UserDetails userDetails, String reason, int commentId){
/*        if(!reportingRepository.existsByReportingIdCommentIdAndReportingStatus(commentId, ReportingStatus.NEW))
            return "Already in progress";*/
        //verificare esistenza Comment
        Comment comment = commentService.findById(commentId);
        if(comment.isCensored())
            return "Already censored";
        //verifico che l'utente abbia già segnalato
        List<Reporting> reportings = reportingRepository.findByReportingIdCommentIdAndReportingStatusIsNot(commentId, ReportingStatus.NEW);
        if(!reportings.isEmpty())
            for(Reporting rep : reportings){
                if(!rep.getReportingStatus().equals(ReportingStatus.NEW))
                    return "Already in charge";
            }


        ReportingId rId = new ReportingId(
                (User) userDetails,
                comment
        );
        if(reportingRepository.existsById(rId))
            return "You have already reported this comment";
        //cerco reason in corso di validità
        Optional<ReportingReason> rr = reportingReasonRepository.findByReportingReasonIdReasonAndEndDateIsNull(reason);
        if(rr.isEmpty())
            return "Invalid reporting reason";

        Reporting reporting = new Reporting(
                rId,
                rr.get()
        );
        reportingRepository.save(reporting);
        return "New reporting has been created";
    }

    public List<ReportingResponse> getReportings(ReportingStatus reportingStatus) {
            return reportingRepository.getReportinsByStatus(reportingStatus);
    }

    public String inProgress(int commentId) {
        String msg = "Status has been updated";
        if(reportingRepository.existsByReportingIdCommentIdAndReportingStatus(commentId, ReportingStatus.NEW))
            reportingRepository.setInProgress(commentId, ReportingStatus.IN_PROGRESS);
        else
            msg = null;
        return msg;
    }
    @Transactional //fare query di update per i report
    //spizza come ha fatto Adelchi
    public String closeReporting(int commentId, ReportingStatus status, String reason) {
        List<Reporting> reporting = reportingRepository.findByReportingIdCommentIdAndReportingStatusIs(commentId, ReportingStatus.IN_PROGRESS);
        Optional<ReportingReason> rr = reportingReasonRepository.findByReportingReasonIdReasonAndEndDateIsNull(reason);
        String msg = "";
        if(!rr.isEmpty()) {
            if(status.equals(ReportingStatus.CLOSED_WITHOUT_BAN)) {
                    for (Reporting r : reporting) {
                        r.setReportingStatus(status);
                        r.setReportingReason(rr.get());
                    }
                    msg ="Report closed without ban";
            } else if(status.equals(ReportingStatus.CLOSED_WITH_BAN)){
                Comment c = commentService.findById(commentId);
                c.setCensored(true);
                User u = c.getUser();
                u.setEnabled(false);
                for(Reporting r : reporting){
                    r.setReportingStatus(status);
                    r.setReportingReason(rr.get());
                }
                msg = "Report closed with ban";
            } else msg = "You have to close with or without ban";
        } else
            msg = "Insert a valid reporting reason";
        return msg;
    }

    @Transactional
    public String closeReportingAdelchi(int commentId, ReportingStatus status, String reason) {

        if(reportingRepository.existsByReportingIdCommentIdAndReportingStatusIsNot(commentId, ReportingStatus.IN_PROGRESS))
            return "Status for this reporting is not IN_PROGRESS";

        ReportingReason reportingReason = reportingReasonService.findByReportingReasonIdReasonAndEndDateIsNull(reason);

        if(status.equals(ReportingStatus.CLOSED_WITH_BAN)){
            Comment comment = commentService.findById(commentId);
            comment.setCensored(true);
            comment.getUser().setEnabled(false);
            if(comment.getUser().getBannedUntil() == null)
                comment.getUser().setBannedUntil(LocalDateTime.now().plusDays(reportingReason.getSeverity()));
            else
                comment.getUser().setBannedUntil(comment.getUser().getBannedUntil().plusDays(reportingReason.getSeverity()));
        }



        reportingRepository.setClosed(commentId, status, reportingReason, LocalDateTime.now());

        return "All reporting about comment "+commentId+" have been updated to "+status.name();
    }
}
