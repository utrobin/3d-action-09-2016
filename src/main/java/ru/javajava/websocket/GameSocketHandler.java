package ru.javajava.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.javajava.exceptions.AlreadyExistsException;
import ru.javajava.mechanics.base.UserSnap;
import ru.javajava.mechanics.requests.Disconnect;
import ru.javajava.mechanics.requests.JoinGame;
import ru.javajava.model.UserProfile;
import ru.javajava.services.AccountService;

import javax.naming.AuthenticationException;
import java.util.Random;
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

    private final AtomicLong ID_GENERATOR;
    int userCount;






    public GameSocketHandler(MessageHandlerContainer messageHandlerContainer, /*PingService pingService, */
                             AccountService accountService, RemotePointService remotePointService) {
        this.messageHandlerContainer = messageHandlerContainer;
    //    this.pingService = pingService;
        this.accountService = accountService;
        this.remotePointService = remotePointService;
        Random generator = new Random();
        ID_GENERATOR = new AtomicLong(generator.nextInt());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws AuthenticationException {
        final long userNumber = ID_GENERATOR.getAndIncrement(); //(Long) webSocketSession.getAttributes().get("userId");
//        if (id == null || accountService.getUserById(id) == null) {
//            throw new AuthenticationException("Only authenticated users allowed to play a game")

//        }



        final UserProfile user;
        try {
            user = accountService.addUser("User #" + userNumber, "1", "park@mail.ru" + userNumber);
        }
        catch (AlreadyExistsException e) {
            LOGGER.error("User already exists!");
            return;
        }
        LOGGER.info("Added User #{}", ++userCount);
        remotePointService.registerUser(user.getId(), webSocketSession);
        //pingService.refreshPing(userId);

        sendIdToClient(webSocketSession, user.getId());


        // Регистрация юзера в JoinGameHandler
        final Message message = new Message(JoinGame.Request.class, "{}");
        try {
            messageHandlerContainer.handle(message, user.getId());
        }
        catch (HandleException e) {
            LOGGER.error("Can't handle message while handshaking");
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws AuthenticationException {

//        final Long userId = (Long) session.getAttributes().get("userId");
//        final UserProfile user;
//        if (userId == null || (user = accountService.getUserById(userId)) == null) {
//            throw new AuthenticationException("Only authenticated users allowed to play a game");
//        }
        Long userId = remotePointService.get(session);
        final Message message = new Message(UserSnap.class.getName(), textMessage.getPayload());
        try {
            messageHandlerContainer.handle(message, userId);
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
//        final Long userId = (Long) webSocketSession.getAttributes().get("userId");
//        if (userId == null) {
//            LOGGER.warn("User disconnected but his session was not found (closeStatus=" + closeStatus + ')');
//            return;
//        }

        Long userId = remotePointService.get(webSocketSession);
        final Message message = new Message(Disconnect.Request.class, "{}");
        try {
            if (userId != null) {
                messageHandlerContainer.handle(message, userId);
                accountService.removeUser(userId);
            }
        }
        catch (HandleException e) {
            LOGGER.error("Can't remove user from game");
        }





        LOGGER.info("User has disconnected");
        remotePointService.removeUser(webSocketSession);
        userCount--;
    }

    private void sendIdToClient(WebSocketSession session, long id) {
        final Message message = new Message(Message.INITIALIZE_USER, String.valueOf(id));
        try {
            final String json = objectMapper.writeValueAsString(message);
	    LOGGER.info(json);
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


