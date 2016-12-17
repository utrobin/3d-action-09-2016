package ru.javajava.mechanics.handlers;

import org.springframework.stereotype.Component;
import ru.javajava.mechanics.GameMechanics;
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
    private final GameMechanics gameMechanics;
    private final MessageHandlerContainer messageHandlerContainer;

    public JoinGameHandler(GameMechanics gameMechanics, MessageHandlerContainer messageHandlerContainer) {
        super(JoinGame.Request.class);
        this.gameMechanics = gameMechanics;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(JoinGame.Request.class, this);
    }

    @Override
    public void handle(JoinGame.Request message, long forUser) throws HandleException {
        gameMechanics.addUser(forUser);
    }
}