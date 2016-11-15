package ru.javajava.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.javajava.MapForTwoUsers;
import ru.javajava.mechanics.base.Coords;
import ru.javajava.model.UserProfile;
import ru.javajava.services.AccountService;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by ivan on 14.11.16.
 */
public class GameSocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSocketHandler.class.getName());

    private final AccountService accountService;

    //private PingService pingService;

    private final MessageHandlerContainer messageHandlerContainer;

    private final RemotePointService remotePointService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    private static final Map<WebSocketSession, Long> sessionToId = new HashMap<>();




    public GameSocketHandler(@NotNull MessageHandlerContainer messageHandlerContainer, /*@NotNull PingService pingService, */
                             @NotNull AccountService accountService, @NotNull RemotePointService remotePointService) {
        this.messageHandlerContainer = messageHandlerContainer;
    //    this.pingService = pingService;
        this.accountService = accountService;
        this.remotePointService = remotePointService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws AuthenticationException {

//        if (accountService.getUserById(userId) == null) {
//            throw new AuthenticationException("Only authenticated users allowed to play a game");
//        }
        //remotePointService.registerUser(userId, webSocketSession);
        //pingService.refreshPing(userId);


        if (MapForTwoUsers.getFirst() != null) {
            sessionToId.put(webSocketSession, 2L);
            MapForTwoUsers.addSecond(webSocketSession);
            LOGGER.info("Connection established for second user");
        }
        else {
            sessionToId.put(webSocketSession, 1L);
            MapForTwoUsers.addFirst(webSocketSession);
            LOGGER.info("Connection established for first user");
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws AuthenticationException {
        final Long userId = sessionToId.get(session);  //(Long) session.getAttributes().get("userId");
        UserProfile user;


        String coords = textMessage.getPayload();
        if (MapForTwoUsers.getFirst().equals(session)) {
            WebSocketSession secondPlayer = MapForTwoUsers.getSecond();
            if (secondPlayer != null) {
                try {
                    secondPlayer.sendMessage(new TextMessage(coords));
                    LOGGER.info("Sending to SECOND");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        else {
            WebSocketSession firstPlayer = MapForTwoUsers.getFirst();
            if (firstPlayer != null) {
                try {
                    firstPlayer.sendMessage(new TextMessage(coords));
                    LOGGER.info("Sending to FIRST");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }






//        if ((user = accountService.getUserById(userId)) == null) {
//            throw new AuthenticationException("Only authenticated users allowed to play a game");
//        }
       // handleMessage(user, message);
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    private void handleMessage(UserProfile userProfile, TextMessage text) {

        final Message message;
        try {
            message = objectMapper.readValue(text.getPayload(), Message.class);
            LOGGER.info("New message from user with ID={}: {}, type: {}", userProfile.getId(), message.getContent(), message.getType());
        } catch (IOException ex) {
            LOGGER.error("wrong json format at ping response", ex);
            return;
        }
        try {
            messageHandlerContainer.handle(message, userProfile.getId());
        } catch (HandleException e) {
            LOGGER.error("Can't handle message of type " + message.getType() + " with content: " + message.getContent(), e);
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
            LOGGER.warn("User disconnected but his session was not found (closeStatus=" + closeStatus + ')');
            //return;
        }
        //remotePointService.removeUser(userId);

        // debug
        MapForTwoUsers.clear();
        sessionToId.clear();

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}

