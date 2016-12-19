package ru.javajava.mechanics.handlers;

import org.springframework.stereotype.Component;
import ru.javajava.mechanics.MechanicsExecutor;
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
    private final MechanicsExecutor mechanicsExecutor;
    private final MessageHandlerContainer messageHandlerContainer;

    public ClientSnapHandler(MechanicsExecutor mechanicsExecutor, MessageHandlerContainer messageHandlerContainer) {
        super(UserSnap.class);
        this.mechanicsExecutor = mechanicsExecutor;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(UserSnap.class, this);
    }

    @Override
    public void handle(UserSnap message, long forUser) throws HandleException {
        mechanicsExecutor.addClientSnapshot(forUser, message);
    }
}
