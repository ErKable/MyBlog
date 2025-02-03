package it.cgmconsulting.myblog.dto.response;

public record JwtAuthenticationResponse (
        String token,
        String message
) {
}