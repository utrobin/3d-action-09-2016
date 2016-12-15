package ru.javajava.mechanics.handlers;

import org.springframework.stereotype.Component;
import ru.javajava.mechanics.GameMechanics;
import ru.javajava.mechanics.MechanicsExecutor;
import ru.javajava.mechanics.requests.JoinGame;
import ru.javajava.websocket.HandleException;
import ru.javajava.websocket.MessageHandler;
import ru.javajava.websocket.MessageHandlerContainer;

import javax.annotation.PostConstruct;

/**
 * Created by ivan on 15.11.16.
 */
@Component
public class JoinGameHandler extends MessageHandler<JoinGame.Request> {
    private final MechanicsExecutor mechanicsExecutor;
    private final MessageHandlerContainer messageHandlerContainer;

    public JoinGameHandler(MechanicsExecutor mechanicsExecutor, MessageHandlerContainer messageHandlerContainer) {
        super(JoinGame.Request.class);
        this.mechanicsExecutor = mechanicsExecutor;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(JoinGame.Request.class, this);
    }

    @Override
    public void handle(JoinGame.Request message, long forUser) throws HandleException {
        mechanicsExecutor.addUser(forUser);
    }
}