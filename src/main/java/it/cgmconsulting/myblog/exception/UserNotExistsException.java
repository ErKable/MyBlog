package it.cgmconsulting.myblog.exception;

import org.springframework.security.core.AuthenticationException;

public class UserNotExistsException extends AuthenticationException {
    public UserNotExistsException(String msg) {
        super(msg);
    }
}
