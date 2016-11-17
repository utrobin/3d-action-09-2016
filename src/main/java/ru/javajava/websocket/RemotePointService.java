package ru.javajava.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ivan on 14.11.16.
 */
@Service
public class RemotePointService {
    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<WebSocketSession, Long> sessionToId = new ConcurrentHashMap<>();

    public void registerUser(Long userId, WebSocketSession webSocketSession) {
        sessions.put(userId, webSocketSession);
        sessionToId.put(webSocketSession, userId);
    }

    public boolean isConnected(Long userId) {
        return sessions.containsKey(userId) && sessions.get(userId).isOpen();
    }

    public void removeUser(Long userId)
    {
        WebSocketSession session = sessions.get(userId);
        sessions.remove(userId);
        sessionToId.remove(session);
    }

    public void removeUser (WebSocketSession session) {
        Long userId = sessionToId.get(session);
        sessionToId.remove(session);
        if (userId != null) {
            sessions.remove(userId);
        }
    }

    public void cutDownConnection(Long userId, CloseStatus closeStatus) {
        final WebSocketSession webSocketSession = sessions.get(userId);
        if (webSocketSession != null && webSocketSession.isOpen()) {
            try {
                webSocketSession.close(closeStatus);
            } catch (IOException ignore) {
            }
        }
    }

    public void sendMessageToUser(Long userId, Message message) throws IOException {
        WebSocketMessage<String> webSocketMessage = new TextMessage(objectMapper.writeValueAsString(message));
        final WebSocketSession webSocketSession = sessions.get(userId);
        if (webSocketSession == null) {
            throw new IOException("no game websocket for user " + userId);
        }
        if (!webSocketSession.isOpen()) {
            throw new IOException("session is closed or not exists");
        }
        try {
            webSocketSession.sendMessage(webSocketMessage);
        } catch (JsonProcessingException | WebSocketException e) {
            throw new IOException("Unable to send message", e);
        }
    }

    public Long get (WebSocketSession session) {
        return sessionToId.get(session);
    }

    public WebSocketSession get (long userId) {
        return sessions.get(userId);
    }
}
