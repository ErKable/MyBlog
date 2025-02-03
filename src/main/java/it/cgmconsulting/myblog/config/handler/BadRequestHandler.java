package it.cgmconsulting.myblog.config.handler;

import it.cgmconsulting.myblog.dto.ResponseHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice //annotazione che serve a verificare se un'ecezione viene gestita
//dal controller
public class BadRequestHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    //questa annotazione serve ad intercettare l'eccezione sollevvata dal controller
    //e all'interno del metodo la gestiamo
    public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException ex) {

        BindingResult bindingResult = ex.getBindingResult();

        Map<String, String> fieldErrors = new HashMap<>();
        //facciamo un for su tutte le eccezioni raccolte e torna i valori in una mappa
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String field = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            fieldErrors.put(field, errorMessage);
        }

        return ResponseHandler.generateErrorResponse(HttpStatus.BAD_REQUEST, fieldErrors);
    }
}