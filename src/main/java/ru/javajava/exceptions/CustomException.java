package ru.javajava.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created by ivan on 06.10.16.
 */
public class CustomException extends Exception {
    private final HttpStatus code;

    public CustomException(HttpStatus code, String message) {
        super(message);
        this.code = code;
    }

    public HttpStatus getCode() {
        return code;
    }
}
