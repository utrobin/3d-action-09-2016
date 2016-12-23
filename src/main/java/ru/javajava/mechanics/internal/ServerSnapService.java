package ru.javajava.mechanics.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSnapService.class);
    private final RemotePointService remotePointService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ServerSnapService(RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public void sendSnapshotsFor(GameSession gameSession) {
        final Collection<GameUser> players = gameSession.getPlayers();
        final List<ServerPlayerSnap> playersSnaps = new ArrayList<>();
        for (GameUser player : players) {
            final ServerPlayerSnap serverPlayerSnap = player.generateSnap();
            playersSnaps.add(serverPlayerSnap);
        }

        if (playersSnaps.isEmpty()) {
            throw new RuntimeException("No players snaps");
        }

        final ServerSnap snap = new ServerSnap();
        snap.setPlayers(playersSnaps);
        try {
            final Message message = new Message();
            message.setType(Message.SNAPSHOT);
            for (GameUser player : players) {
                final boolean wasShot = player.getShot();
                snap.setShot(wasShot);
                final int hp = player.getHp();
                snap.setHp(hp);
                message.setData(objectMapper.writeValueAsString(snap));
                remotePointService.sendMessageToUser(player.getId(), message);
                player.resetForNextSnap();
            }
        } catch (IOException e) {
            LOGGER.error("Error sending server snap! {}", e.getMessage());
        }

    }
}

