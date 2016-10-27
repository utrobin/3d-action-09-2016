package ru.javajava.exceptions;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by ivan on 05.10.16.
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {



    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity handle(EmptyResultDataAccessException e) {
        return ResponseEntity.ok(new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    @ExceptionHandler (AlreadyExistsException.class)
    public ResponseEntity handle(AlreadyExistsException e) {
        return ResponseEntity.ok(new ErrorResponse(
                HttpStatus.CONFLICT, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handle(Exception e) {
        return ResponseEntity.ok(new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    public static final class ErrorResponse {
        private final HttpStatus code;
        private final String reason;

        public ErrorResponse(HttpStatus code, String reason) {
            this.code = code;
            this.reason = reason;
        }

        @SuppressWarnings("unused")
        public int getCode() {
            return code.value();
        }

        @SuppressWarnings("unused")
        public String getReason() {
            return reason;
        }
    }
}

