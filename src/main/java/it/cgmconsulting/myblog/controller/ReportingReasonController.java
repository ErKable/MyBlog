package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.dto.ResponseHandler;
import it.cgmconsulting.myblog.dto.request.ReportingReasonRequest;
import it.cgmconsulting.myblog.service.ReportingReasonService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reporting-reason")
@RequiredArgsConstructor
@Validated
public class ReportingReasonController {
    private final ReportingReasonService reportingReasonService;

    @PostMapping
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> addReportingReason(@RequestBody @Valid ReportingReasonRequest request){
        String msg = reportingReasonService.addReportingReason(request);
        return ResponseHandler.generateResponse(HttpStatus.CREATED, msg);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> updateReason(@RequestParam  @NotBlank @Size(max = 20, min = 2) String reason,
                                          @RequestParam @Min(-1) @Max(120) byte newSeverity){
        String msg = reportingReasonService.updateReason(reason, newSeverity);
        if(msg != null)
            return ResponseHandler.generateResponse(HttpStatus.OK);

        else
            return ResponseHandler.generateResponse(HttpStatus.CONFLICT);

    }

    @PatchMapping
    @PreAuthorize("hasAuthority('MODERATOR')")
    public ResponseEntity<?> reasonInvalidation(@RequestParam  @NotBlank @Size(max = 20, min = 2) String reason){
        String msg = reportingReasonService.reasonInvalidation(reason);
        if(msg != null)
            return ResponseHandler.generateResponse(HttpStatus.OK, msg);
        else
            return ResponseHandler.generateResponse(HttpStatus.CONFLICT);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('MODERATOR') or hasAuthority('MEMBER')")
    public ResponseEntity<?> getValidReasons(){
        return ResponseHandler.generateResponse(HttpStatus.OK, reportingReasonService.getValidReasons());
    }
}
