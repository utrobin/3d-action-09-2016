package ru.javajava.exceptions;

/**
 * Created by ivan on 27.10.16.
 */
public class AlreadyExistsException extends Exception {
    public AlreadyExistsException() {
    }

    public AlreadyExistsException(String message) {
        super(message);
    }
}
