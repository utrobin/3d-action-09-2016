package ru.javajava.mechanics.internal;

import org.springframework.stereotype.Service;
import ru.javajava.mechanics.GameSession;
import ru.javajava.mechanics.avatar.GameUser;

import ru.javajava.mechanics.base.Coords;

import ru.javajava.mechanics.base.MyVector;
import ru.javajava.mechanics.base.UserSnap;

import java.util.*;

/**
 * Created by ivan on 15.11.16.
 */
@Service
public class ClientSnapService {
    private final Map<Long, List<UserSnap>> userToSnaps = new HashMap<>();

    private static final int RADIUS = 3;

    public void pushClientSnap(long user, UserSnap snap) {
        userToSnaps.putIfAbsent(user, new ArrayList<>());
        final List<UserSnap> userSnaps = userToSnaps.get(user);
        userSnaps.add(snap);
    }

    public List<UserSnap> getSnapsForUser(long user) {
        return userToSnaps.get(user);
    }

    public void processSnapshotsFor(GameSession gameSession) {
        final Collection<GameUser> players = gameSession.getPlayers();
        for (GameUser player : players) {
            final List<UserSnap> playerSnaps = getSnapsForUser(player.getId());
            if (playerSnaps == null || playerSnaps.isEmpty()) {
                continue;
            }
            final UserSnap lastSnap = playerSnaps.get(playerSnaps.size() - 1);
            player.setPosition(lastSnap.getPosition());

            for (UserSnap snap: playerSnaps) {
                if (!snap.isFiring()) {
                    continue;
                }
                final GameUser victim = processFiring (snap, players);
                if (victim != null) {
                    victim.markShot();
                    if (!victim.isAlive()) {
                        player.addVictim(victim.getId());
                    }
                }
            }
        }
    }



    private GameUser processFiring(UserSnap snap, Iterable<GameUser> players) {
        final Coords position = snap.getPosition();

        final Coords cameraDirection  = snap.getCamera();
        final MyVector currentShot = new MyVector(cameraDirection);

        for (GameUser player: players) {
            if (player.getId() == snap.getId()) {
                continue;
            }
            final Coords enemyCoords = player.getPosition();
            if (enemyCoords == null) {
                continue;
            }

            final MyVector idealShot = new MyVector(enemyCoords.subtract(position));


            final double distance = enemyCoords.getDistanceBetween(position);
            final double hypotenuse = Math.sqrt(distance*distance + RADIUS*RADIUS);

            final double maxCos = distance / hypotenuse;

            final double cos = currentShot.getCos(idealShot);
            if (cos >= maxCos) {
                return player;
            }
        }
        return null;
    }

    public void clear() {
        userToSnaps.clear();
    }
}

