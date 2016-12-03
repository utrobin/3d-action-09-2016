package ru.javajava.mechanics.internal;

import org.springframework.stereotype.Service;
import ru.javajava.mechanics.GameSession;
import ru.javajava.mechanics.avatar.GameUser;
import ru.javajava.mechanics.base.UserSnap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivan on 15.11.16.
 */
@Service
public class ClientSnapService {

    // User и его последний Snapshot
    private final Map<Long, UserSnap> userToSnap = new HashMap<>();

    public void pushClientSnap(long user, UserSnap snap) {
        userToSnap.put(user, snap);
    }

    public UserSnap getSnapForUser(long user) {
        return userToSnap.get(user);
    }

    public void processSnapshotsFor(GameSession gameSession) {
        final Collection<GameUser> players = gameSession.getPlayers();
        for (GameUser player : players) {
            final UserSnap playerSnap = getSnapForUser(player.getId());
            if (playerSnap == null) {
                continue;
            }
            player.setCoords(playerSnap.getCoords());
        }
    }

    public void clear() {
        userToSnap.clear();
    }

}

