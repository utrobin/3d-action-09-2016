package ru.javajava.mechanics.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.javajava.mechanics.GameMechanics;
import ru.javajava.mechanics.base.UserSnap;
import ru.javajava.websocket.HandleException;
import ru.javajava.websocket.MessageHandler;
import ru.javajava.websocket.MessageHandlerContainer;

import javax.annotation.PostConstruct;

/**
 * Created by ivan on 15.11.16.
 */
@Component
public class ClientSnapHandler extends MessageHandler<UserSnap> {
    private GameMechanics gameMechanics;
    private final MessageHandlerContainer messageHandlerContainer;

    public ClientSnapHandler(GameMechanics gameMechanics, MessageHandlerContainer messageHandlerContainer) {
        super(UserSnap.class);
        this.gameMechanics = gameMechanics;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(UserSnap.class, this);
    }

    @Override
    public void handle(UserSnap message, long forUser) throws HandleException {
        gameMechanics.addClientSnapshot(forUser, message);
    }
}
