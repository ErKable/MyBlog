package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.dto.request.ReportingReasonRequest;
import it.cgmconsulting.myblog.entity.ReportingReason;
import it.cgmconsulting.myblog.entity.ReportingReasonId;
import it.cgmconsulting.myblog.repository.ReportingReasonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportingReasonService {
    private final ReportingReasonRepository reportingReasonRepository;

    public String addReportingReason(ReportingReasonRequest request) {
        Optional<ReportingReason> foundReportingReason = reportingReasonRepository.getReportingReason(request.getReason());
        ReportingReasonId reportingReasonId = null;
        ReportingReason reportingReason = null;
        if(foundReportingReason.isEmpty()){
            reportingReasonId = new ReportingReasonId(request.getReason().toUpperCase(), request.getStartDate());
            reportingReason = new ReportingReason(reportingReasonId, request.getSeverity());
            reportingReasonRepository.save(reportingReason);
            return "New reporting reason added";
        }
        else
            return "Reporting reason already declared";
    }

    public ReportingReason findByReportingReasonIdReasonAndEndDateIsNull(String reason){
        return reportingReasonRepository.findByReportingReasonIdReasonAndEndDateIsNull(reason).orElseThrow();
    }

    @Transactional
    public String updateReason(String reason, byte newSeverity) {
        String msg = reason+"'s Severity updated to "+newSeverity+" days";
        //recuperare la reason con endDate null
        Optional<ReportingReason> rr = reportingReasonRepository.findByReportingReasonIdReasonAndEndDateIsNull(reason);
        if(rr.isEmpty()) {
            msg = null;
            return msg;
        }
        //settare l'end date con today
        ReportingReason rrToUpdate = rr.get();
        if(rrToUpdate.getSeverity() == newSeverity || newSeverity == 0) {
            msg = "Same severity or new severity == 0";
            return msg;
        }
        rrToUpdate.setEndDate(LocalDate.now());
        //creare una nuovca reason con startDate domani
        ReportingReason rrToAdd = new ReportingReason(
                new ReportingReasonId(reason, LocalDate.now().plusDays(1)),
                newSeverity
        );
        reportingReasonRepository.save(rrToAdd);
        return msg;
    }
    @Transactional
    public String reasonInvalidation(String reason) {
        String msg = "Reason "+reason+" has been invalidated";
        Optional<ReportingReason> rr = reportingReasonRepository.findByReportingReasonIdReasonAndEndDateIsNull(reason);
        if(rr.isEmpty()) {
            msg = null;
            return msg;
        }
        rr.get().setEndDate(LocalDate.now());
        return msg;
    }

    public List<String> getValidReasons() {
        return reportingReasonRepository.getValidReason();
    }
}
