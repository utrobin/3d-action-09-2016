package ru.javajava.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by ivan on 14.11.16.
 */
public abstract class MessageHandler<T> {
    private final Class<T> clazz;

    public MessageHandler(Class<T> clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    public void handleMessage(Message message, Long forUser) throws HandleException {
        try {
            final Object data = new ObjectMapper().readValue(message.getContent(), clazz);

            handle(clazz.cast(data), forUser);
        } catch (IOException | ClassCastException ex) {
            throw new HandleException("Can't read incoming message of type " + message.getType() + " with content: " + message.getContent(), ex);
        }
    }

    public abstract void handle(T message, long forUser) throws HandleException;
}
