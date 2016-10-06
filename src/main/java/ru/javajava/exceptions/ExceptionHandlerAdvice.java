package ru.javajava.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.javajava.main.RegistrationController;

/**
 * Created by ivan on 05.10.16.
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity handle(Exception e) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new RegistrationController.ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
}

