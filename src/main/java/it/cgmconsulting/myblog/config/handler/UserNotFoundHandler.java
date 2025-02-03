package it.cgmconsulting.myblog.config.handler;

import it.cgmconsulting.myblog.dto.ResponseHandler;
import it.cgmconsulting.myblog.exception.UserNotExistsException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableWebMvc
@RestControllerAdvice
public class UserNotFoundHandler {
    @ExceptionHandler(UserNotExistsException.class)
    public ResponseEntity<Object> handleNotFound() {
        return ResponseHandler.generateErrorResponse(HttpStatus.NOT_FOUND, "User not found");
    }
}