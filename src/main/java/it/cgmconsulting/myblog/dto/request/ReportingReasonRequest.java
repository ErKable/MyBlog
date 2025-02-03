package it.cgmconsulting.myblog.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ReportingReasonRequest {
    @NotBlank @Size(max = 20, min = 2)
    private String reason;
    @NotNull @FutureOrPresent
    private LocalDate startDate;
    //@Min(-1) @Max(120)
    private byte severity;
}
