package it.cgmconsulting.myblog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReportingResponse {
    private int commentId;
    private String comment;
    private long totalReporting; // totale delle segnalazioni ricevute
}
