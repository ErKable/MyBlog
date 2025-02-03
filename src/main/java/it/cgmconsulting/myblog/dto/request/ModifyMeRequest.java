package it.cgmconsulting.myblog.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ModifyMeRequest(
        @NotBlank @Email String email,
        @Size(min = 2, max = 50) String firstName,
        @Size(min = 2, max = 50) String lastName,
        @Size(min = 2, max = 255) String bio
) {}
