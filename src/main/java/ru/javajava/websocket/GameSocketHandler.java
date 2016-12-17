package ru.javajava.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.javajava.mechanics.MechanicsExecutor;
import ru.javajava.mechanics.requests.Disconnect;
import ru.javajava.mechanics.requests.JoinGame;
import ru.javajava.model.UserProfile;
import ru.javajava.services.AccountService;

import javax.naming.AuthenticationException;
import java.io.IOException;

/**
 * Created by ivan on 14.11.16.
 */
@SuppressWarnings("ALL")
public class GameSocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSocketHandler.class.getName());

    private final AccountService accountService;

    private final MessageHandlerContainer messageHandlerContainer;

    private final RemotePointService remotePointService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    public GameSocketHandler(MessageHandlerContainer messageHandlerContainer,
                             AccountService accountService, RemotePointService remotePointService) {
        this.messageHandlerContainer = messageHandlerContainer;
        this.accountService = accountService;
        this.remotePointService = remotePointService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws AuthenticationException {
        final Long id = (Long) webSocketSession.getAttributes().get("userId");

        final UserProfile player;

        if (id == null || (player = accountService.getUserById(id)) == null) {
            LOGGER.error("Only authenticated users allowed to play a game!");
            return;
        }

        if (remotePointService.get(id) != null) {
            LOGGER.error("You are already playing, go away!");
            return;
        }


        LOGGER.info("--------------------------------------------------------");
        LOGGER.info("New player {} #{}", player.getLogin(), player.getId());


        remotePointService.registerUser(player.getId(), webSocketSession);

        sendIdToClient(webSocketSession, player.getId());


        final Message message = new Message(JoinGame.Request.class, "{}");
        try {
            messageHandlerContainer.handle(message, player.getId());
        }
        catch (HandleException e) {
            LOGGER.error("Can't handle message while handshaking");
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws AuthenticationException {

        final Long userId = (Long) session.getAttributes().get("userId");
        final UserProfile user;
        if (userId == null || (user = accountService.getUserById(userId)) == null) {
            return;
        }

        try {
            final Message message = objectMapper.readValue(textMessage.getPayload(), Message.class);
            messageHandlerContainer.handle(message, userId);
        } catch (HandleException | IOException e) {
            LOGGER.error("Can't handle message from user with ID={}, reason: {}", userId, e.getMessage());
        }
    }




    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        LOGGER.warn("Websocket transport problem", throwable);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        final Long userId = (Long) webSocketSession.getAttributes().get("userId");
        if (userId == null) {
            LOGGER.warn("User has disconnected but his session was not found");
            return;
        }

        if (remotePointService.contains(webSocketSession)) {
            remotePointService.removeUser(userId);

            final Message message = new Message(Disconnect.Request.class, "{}");
            try {
                messageHandlerContainer.handle(message, userId);
            } catch (HandleException e) {
                LOGGER.error("Can't remove user from game");
            }
        }
    }

    private void sendIdToClient(WebSocketSession session, long id) {
        final Message message = new Message(Message.INITIALIZE_USER, String.valueOf(id));
        try {
            final String json = objectMapper.writeValueAsString(message);
            session.sendMessage(new TextMessage(json));
        }
        catch (Exception e) {
            LOGGER.error("Sending ID to user FAILED");
        }

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}


