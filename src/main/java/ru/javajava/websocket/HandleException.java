package ru.javajava.websocket;

/**
 * Created by ivan on 14.11.16.
 */
public class HandleException extends Exception {
    public HandleException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandleException(String message) {
        super(message);
    }
}