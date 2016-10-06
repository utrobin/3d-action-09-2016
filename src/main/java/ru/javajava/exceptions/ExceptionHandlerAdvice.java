package ru.javajava.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Created by ivan on 05.10.16.
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity handle(CustomException e) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ErrorResponse(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handle(Exception e) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    private static final class ErrorResponse {
        private final HttpStatus code;
        private final String reason;

        private ErrorResponse(HttpStatus code, String reason) {
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
