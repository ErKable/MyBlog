package it.cgmconsulting.myblog.dto.response;

import it.cgmconsulting.myblog.entity.enumerated.Role;

public record ResourceResponse(Role role, String message) {}