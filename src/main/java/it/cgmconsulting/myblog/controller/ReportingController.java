package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.dto.ResponseHandler;
import it.cgmconsulting.myblog.entity.enumerated.ReportingStatus;
import it.cgmconsulting.myblog.service.ReportingService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reporting")
public class ReportingController {
    private final ReportingService reportingService;
    @PostMapping("/{commentId}")
    @PreAuthorize("hasAuthority('MEMBER')")
    public ResponseEntity<?> createReporting(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @NotBlank @Size(min = 2, max=20) String reason,
            @PathVariable @Min(1) int commentId
            ){
        return ResponseHandler.generateResponse(HttpStatus.OK, reportingService.createReporting(userDetails, reason, commentId));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> getReportings(@RequestParam @NotNull ReportingStatus reportingStatus){
        return ResponseHandler.generateResponse(HttpStatus.OK, reportingService.getReportings(reportingStatus));
    }

    @PatchMapping("/{commentId}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> inProgress(@PathVariable int commentId){
        String msg = reportingService.inProgress(commentId);
        if(msg != null)
            return ResponseHandler.generateResponse(HttpStatus.OK, msg);
        else
            return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, msg);
    }

    @PatchMapping("/closeReport/{commentId}")
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> closeReporting(@PathVariable int commentId, @RequestParam ReportingStatus status,
                                            @RequestParam(defaultValue = "TROLL") String reason){
        //closed without ban
        //cambiare lo status di tutte le segnalazioni riferite al commento
        //da in progress a closed without ban
        //aggiornare la motivazione per tutti

        //closed with ban
        /*cambiare la status da new a closed with ban
        * settare enable a false sull'autore
        * settare censore a true nel commento
        * aggiornare la motivazione della segnalazione che deve essere identica
        * per tutte
        * */

        return ResponseHandler.generateResponse(HttpStatus.OK, reportingService.closeReportingAdelchi(commentId, status,reason));
    }
}
