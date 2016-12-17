package ru.javajava.mechanics.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import ru.javajava.mechanics.GameSession;
import ru.javajava.mechanics.avatar.GameUser;
import ru.javajava.model.UserProfile;
import ru.javajava.websocket.RemotePointService;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by ivan on 15.11.16.
 */

public class GameSessionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSessionService.class);
    private final Map<Long, GameSession> usersMap = new HashMap<>();
    private final Set<GameSession> gameSessions = new LinkedHashSet<>();

    private final RemotePointService remotePointService;


    public GameSessionService(RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public Set<GameSession> getSessions() {
        return gameSessions;
    }

    public boolean hasFreeSlots() {
        for (GameSession session: gameSessions) {
            if (!session.isFull()) {
                return true;
            }
        }
        return false;
    }


    public GameSession getSessionForUser(long userId) {
        return usersMap.get(userId);
    }

    public boolean isPlaying(long userId) {
        return usersMap.containsKey(userId);
    }

    public void addNewPlayer (UserProfile user) {
        for (GameSession session: gameSessions) {
            if (!session.isFull()) {
                session.addPlayer(user);
                usersMap.put(user.getId(), session);
                LOGGER.info("--------------------------------------------------------");
                LOGGER.info("Added player #{} to room #{} (players here: {})", user.getId(), session.getId(), session.getPlayers().size());
                return;
            }
        }

        final GameSession newSession = new GameSession();
        newSession.addPlayer(user);
        gameSessions.add(newSession);
        usersMap.put(user.getId(), newSession);
        LOGGER.info("--------------------------------------------------------");
        LOGGER.info("Started new room #{}, total rooms: {}", newSession.getId(), gameSessions.size());
    }


    public void removePlayer (GameSession session, long userId) {
        if (!gameSessions.contains(session)) {
            throw new RuntimeException("Game session not found");
        }
        usersMap.remove(userId);
        session.removePlayer(userId);
        LOGGER.info("--------------------------------------------------------");
        LOGGER.info("Player #{} was removed from room #{} (players here: {})", userId, session.getId(), session.getPlayers().size());
        if (session.isEmpty()) {
            notifyGameIsOver(session);
        }
    }

    public void notifyGameIsOver(GameSession gameSession) {
        final boolean exists = gameSessions.remove(gameSession);
        final Set<GameUser> players = gameSession.getPlayers();
        for (GameUser player: players) {
            usersMap.remove(player.getId());
            if (exists) {
                remotePointService.cutDownConnection(player.getId(), CloseStatus.SERVER_ERROR);
            }
        }
        LOGGER.info("--------------------------------------------------------");
        LOGGER.info("Game #{} is over, total rooms: {}", gameSession.getId(), gameSessions.size());
    }
}