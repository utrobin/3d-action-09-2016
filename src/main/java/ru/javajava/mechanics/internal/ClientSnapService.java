package ru.javajava.mechanics.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javajava.mechanics.GameSession;
import ru.javajava.mechanics.avatar.GameUser;
import ru.javajava.mechanics.base.*;
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
    @Autowired
    private BlockService blockService;

    private static final int RADIUS = 3;
    public static final int SCORES_FOR_SHOT = 2;
    public static final int SCORES_FOR_KILL = 10;
    public static final double DAMAGE_COEFF_MIN = 0.5;


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
        final Coords myPosition = snap.getPosition();

        final Coords cameraDirection  = snap.getCamera();
        cameraDirection.y *= -1; // Из-за непоняток с вертикальной коодинатой
        
        final MyVector currentShot = new MyVector(cameraDirection);

        for (GameUser player: players) {
            if (player.getId() == snap.getId()) {
                continue;
            }
            final Coords enemyPosition = player.getPosition();
            if (enemyPosition == null) {
                continue;
            }

            final MyVector idealShot = new MyVector(enemyPosition.subtract(myPosition));
            LOGGER.info("------------------------------");
            LOGGER.info("Ideal shot: ({}, {}, {})", idealShot.getX(), idealShot.getY(), idealShot.getZ());
            LOGGER.info("My shot: ({}, {}, {})", currentShot.getX(), currentShot.getY(), currentShot.getZ());

            final double distance = enemyPosition.getDistanceBetween(myPosition);
            final double hypotenuse = Math.sqrt(distance*distance + RADIUS*RADIUS);

            final double maxCos = distance / hypotenuse;

            final double cos = currentShot.getCos(idealShot);
            if (cos >= maxCos) {
                if (!noWallsBetween(myPosition, enemyPosition, currentShot)) {
                    continue;
                }
                LOGGER.info("Shot in target!");
                final double shotLenght = distance / cos;
                final double distanceFromEnemyCenter =
                        Math.sqrt(shotLenght*shotLenght - distance*distance);
                
                damageCoeff = (RADIUS - distanceFromEnemyCenter) / RADIUS;
                if (damageCoeff < DAMAGE_COEFF_MIN) {
                    damageCoeff = DAMAGE_COEFF_MIN;
                }
                return player;
            }
        }
        return null;
    }

    private boolean noWallsBetween(Coords killer, Coords enemy, MyVector camera) {
        final Set<Block> blocks = blockService.getBlocks();
        for (Block block: blocks) {
            Ray shotRay = new Ray(camera, killer);
            Double distanceToBlock = block.isOnTheWay(shotRay);
            if (distanceToBlock != null) {
                final double distanceToEnemy = killer.getDistanceBetween(enemy);
                if (distanceToBlock < distanceToEnemy) {
                    LOGGER.info("Shot in wall!!");
                    return false;
                }
            }
        }
        return true;
    }

    public void clear() {
        userToSnaps.clear();
    }
}

