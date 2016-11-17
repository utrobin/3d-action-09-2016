package ru.javajava.websocket;

/**
 * Created by ivan on 14.11.16.
 */
public interface MessageHandlerContainer {

    void handle(Message message, long forUser) throws HandleException;

    <T> void registerHandler(Class<T> clazz, MessageHandler<T> handler);
}
