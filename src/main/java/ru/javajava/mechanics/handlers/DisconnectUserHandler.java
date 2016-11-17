package ru.javajava.mechanics.handlers;

import org.springframework.stereotype.Component;
import ru.javajava.mechanics.GameMechanics;
import ru.javajava.mechanics.requests.Disconnect;
import ru.javajava.websocket.HandleException;
import ru.javajava.websocket.MessageHandler;
import ru.javajava.websocket.MessageHandlerContainer;

import javax.annotation.PostConstruct;

/**
 * Created by ivan on 17.11.16.
 */
@Component
public class DisconnectUserHandler extends MessageHandler<Disconnect.Request> {
    private final GameMechanics gameMechanics;
    private final MessageHandlerContainer messageHandlerContainer;

    public DisconnectUserHandler(GameMechanics gameMechanics, MessageHandlerContainer messageHandlerContainer) {
        super(Disconnect.Request.class);
        this.gameMechanics = gameMechanics;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(Disconnect.Request.class, this);
    }

    @Override
    public void handle(Disconnect.Request message, long forUser) throws HandleException {
        gameMechanics.removeUser(forUser);
    }
}

