package ru.javajava.mechanics.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javajava.mechanics.GameSession;
import ru.javajava.mechanics.avatar.GameUser;
import ru.javajava.mechanics.base.Coords;
import ru.javajava.mechanics.base.MyVector;
import ru.javajava.mechanics.base.UserSnap;
import ru.javajava.mechanics.base.VictimModel;
import ru.javajava.services.AccountService;

import java.util.*;

/**
 * Created by ivan on 15.11.16.
 */
@Service
public class ClientSnapService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSnapService.class);
    private final Map<Long, List<UserSnap>> userToSnaps = new HashMap<>();
    private double damageCoeff;

    @Autowired
    private AccountService accountService;

    private static final int RADIUS = 3;
    public static final int SCORES_FOR_SHOT = 2;
    public static final int SCORES_FOR_KILL = 10;


    public synchronized void pushClientSnap(long user, UserSnap snap) {
        final List<UserSnap> userSnaps = userToSnaps.computeIfAbsent(user, u -> new ArrayList<>());
        userSnaps.add(snap);
    }

    public synchronized List<UserSnap> getSnapsForUser(long user) {
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
                    accountService.incrementRating(player.getId(), SCORES_FOR_SHOT);
                    LOGGER.info("Damage: {}", damageCoeff * GameUser.SHOT_REDUCING);

                    victim.markShot(damageCoeff);
                    if (!victim.isAlive()) {
                        final VictimModel model =
                                new VictimModel(victim.getId(), victim.getUserProfile().getLogin());
                        player.addVictim(model);
                        accountService.incrementRating(player.getId(), SCORES_FOR_KILL);
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
                double shotLenght = distance / cos;
                double distanceFromEnemyCenter = Math.sqrt(shotLenght*shotLenght - distance*distance);
                LOGGER.info("SHOOTED! Distance from center enemy: {}", distanceFromEnemyCenter);
                damageCoeff = distanceFromEnemyCenter / RADIUS;
                return player;
            }
        }
        return null;
    }

    public void clear() {
        userToSnaps.clear();
    }
}

