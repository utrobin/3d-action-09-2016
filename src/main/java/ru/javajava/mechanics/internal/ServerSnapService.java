package ru.javajava.mechanics.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import ru.javajava.mechanics.GameSession;
import ru.javajava.mechanics.avatar.GameUser;
import ru.javajava.mechanics.base.ServerPlayerSnap;
import ru.javajava.mechanics.base.ServerSnap;
import ru.javajava.websocket.Message;
import ru.javajava.websocket.RemotePointService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ivan on 15.11.16.
 */
@Service
public class ServerSnapService {
    private final RemotePointService remotePointService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ServerSnapService(RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public void sendSnapshotsFor(GameSession gameSession) {
        final Collection<GameUser> players = gameSession.getPlayers();
        final List<ServerPlayerSnap> playersSnaps = new ArrayList<>();
        for (GameUser player : players) {
            playersSnaps.add(player.generateSnap());
        }
        final ServerSnap snap = new ServerSnap();

        if (playersSnaps.isEmpty()) {
            throw new RuntimeException("No snapshots for this session - aborting");
        }

        snap.setPlayers(playersSnaps);
        try {
            final WebSocketMessage<String> message = new TextMessage(objectMapper.writeValueAsString(snap));
            for (GameUser player : players) {
                remotePointService.sendMessageToUser(player.getId(), message);
            }
        } catch (IOException e) {
        }

    }
}

