package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.dto.ResponseHandler;
import it.cgmconsulting.myblog.dto.request.SignInRequest;
import it.cgmconsulting.myblog.dto.request.SignUpRequest;
import it.cgmconsulting.myblog.exception.UserAlreadyExistsException;
import it.cgmconsulting.myblog.service.AuthenticationService;
import it.cgmconsulting.myblog.exception.EmailAlreadyExistsException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    /*
    * Classe per gestire registrazione e login di un utente
    *
    *
    * */
    @PostMapping("/signup")
    //la notation valid serve a far partire i controlli definiti nel signup request
    public ResponseEntity<Object> signup(@Valid @RequestBody SignUpRequest request) {
        try {
            //chiama il metodo signup di authenticationService
            return ResponseHandler.generateResponse(HttpStatus.CREATED, authenticationService.signup(request));
        } catch (UserAlreadyExistsException ex) {
            return ResponseHandler.generateErrorResponse(HttpStatus.CONFLICT, "Email already in use");
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> signin(@Valid @RequestBody SignInRequest request) {
        try {
            return ResponseHandler.generateResponse(authenticationService.signin(request));
        } catch (AuthenticationException ex) {
            return ResponseHandler.generateErrorResponse(HttpStatus.UNAUTHORIZED, "Email or password invalid");
        }
    }

    @PatchMapping("/confirm/{jwt}")
    public ResponseEntity<?> confirm(@PathVariable String jwt){
        if(authenticationService.confirm(jwt))
            return ResponseHandler.generateResponse(HttpStatus.OK, "Your account has been confirmed");
        else
            return ResponseHandler.generateErrorResponse(HttpStatus.CONFLICT, "Cannot confirm");
    }

}