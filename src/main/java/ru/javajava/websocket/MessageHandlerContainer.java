package ru.javajava.websocket;

import org.jetbrains.annotations.NotNull;

/**
 * Created by ivan on 14.11.16.
 */
public interface MessageHandlerContainer {

    void handle(@NotNull Message message, long forUser) throws HandleException;

    <T> void registerHandler(@NotNull Class<T> clazz, MessageHandler<T> handler);
}
