package ru.javajava.mechanics.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import ru.javajava.mechanics.GameSession;
import ru.javajava.mechanics.avatar.GameUser;
import ru.javajava.model.UserProfile;
import ru.javajava.websocket.RemotePointService;

import java.util.*;

/**
 * Created by ivan on 15.11.16.
 */
@Service
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

    public GameSession getSessionForUser(long userId) {
        return usersMap.get(userId);
    }

    public boolean isPlaying(long userId) {
        return usersMap.containsKey(userId);
    }

    public void notifyGameIsOver(GameSession gameSession) {
        final boolean exists = gameSessions.remove(gameSession);
        final List<GameUser> players = gameSession.getPlayers();
        for (GameUser player: players) {
            usersMap.remove(player.getId());
            if (exists) {
                remotePointService.cutDownConnection(player.getId(), CloseStatus.SERVER_ERROR);
            }
        }
    }

    public GameSession startGame(List<UserProfile> players) {
        final GameSession gameSession = new GameSession(players);
        gameSessions.add(gameSession);
        for (UserProfile player: players) {
            usersMap.put(player.getId(), gameSession);
        }
        return gameSession;
    }

    public void addPlayer (GameSession session, UserProfile user) {
        if (!gameSessions.contains(session)) {
            throw new RuntimeException("Game session not found");
        }
        usersMap.put(user.getId(), session);
        session.addPlayer(user);
        LOGGER.info("Added player to session");
    }

    public void removePlayer (GameSession session, UserProfile user) {
        if (!gameSessions.contains(session)) {
            throw new RuntimeException("Game session not found");
        }
        usersMap.remove(user.getId());
        session.removePlayer(user);
        LOGGER.info("Removing player from session");
    }
}