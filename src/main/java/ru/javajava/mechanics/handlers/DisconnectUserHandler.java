package ru.javajava.mechanics.handlers;

import org.springframework.stereotype.Component;
import ru.javajava.mechanics.MechanicsExecutor;
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
    private final MechanicsExecutor mechanicExecutor;
    private final MessageHandlerContainer messageHandlerContainer;

    public DisconnectUserHandler(MechanicsExecutor mechanicExecutor, MessageHandlerContainer messageHandlerContainer) {
        super(Disconnect.Request.class);
        this.mechanicExecutor = mechanicExecutor;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(Disconnect.Request.class, this);
    }

    @Override
    public void handle(Disconnect.Request message, long forUser) throws HandleException {
        mechanicExecutor.removeUser(forUser);
    }
}

