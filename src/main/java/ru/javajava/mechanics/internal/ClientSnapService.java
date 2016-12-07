package ru.javajava.mechanics.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.javajava.mechanics.GameSession;
import ru.javajava.mechanics.avatar.GameUser;
import ru.javajava.mechanics.base.CameraDirection;
import ru.javajava.mechanics.base.MyVector;
import ru.javajava.mechanics.base.UserSnap;
import ru.javajava.mechanics.base.Coords;

import java.util.*;

/**
 * Created by ivan on 15.11.16.
 */
@Service
public class ClientSnapService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientSnapService.class);

    // User и его последний Snapshot
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

                final GameUser murdered = processFiring (snap, players);
                if (murdered != null) { // Если игрок в кого-то попал
                    murdered.markShot();
                    LOGGER.info("--------------------------------------------------------");
                    LOGGER.info("{} was shot by {}!", murdered.getUserProfile().getLogin(), player.getUserProfile().getLogin());
                }
                else {
                    LOGGER.info("Missed");
                }
            }
        }
    }


    // Функция возвращает игрока, в которого попали или null в случае промаха
    private GameUser processFiring(UserSnap snap, Iterable<GameUser> players) {
        final Coords position = snap.getPosition();
        final CameraDirection cameraDirection  = snap.getCamera();
        final double horizAngle = cameraDirection.getHorizontalAngle();
        final double verticAngle = cameraDirection.getVerticalAngle();
                                                                    // Вектор текущего выстрела
        final MyVector currentShot = new MyVector(-Math.sin(horizAngle), Math.sin(verticAngle), -Math.cos(horizAngle));


        for (GameUser player: players) {
            if (player.getId() == snap.getId()) {
                continue; // Это ты и есть
            }
            final Coords enemyCoords = player.getPosition();
            if (enemyCoords == null) {
                continue;
            }

            final MyVector idealShot = new MyVector(enemyCoords.subtract(position));    // Вектор идеального выстрела в центр врага

            final double distance = enemyCoords.getDistanceBetween(position);   // Расстояние до врага
            final double hypotenuse = Math.sqrt(distance*distance + RADIUS*RADIUS);
            final double maxCos = distance / hypotenuse;   // Косинус МАКСИМАЛЬНО возможного угла между идеальным вектором и существующим


            final double cos = currentShot.getCos(idealShot);
            LOGGER.info("Ideal shot: ({}, {}, {})", idealShot.getX(), idealShot.getY(), idealShot.getZ());
            LOGGER.info("My shot: ({}, {}, {})", currentShot.getX(), currentShot.getY(), currentShot.getZ());
            LOGGER.info("MaxCos = {}, cos = {}", maxCos, cos);
            if (cos >= maxCos) {
                return player;      // Игрок попал
            }
        }

        return null;
    }


    private boolean isCollinear(Coords first, Coords second) {
        double a = (double) Math.round(first.x / second.x * 100) / 100;
        double b = (double) Math.round(first.y / second.y * 100) / 100;
        double c = (double) Math.round(first.z / second.z * 100) / 100;
//        LOGGER.info("---------------------");
//
//        LOGGER.info("---------------------");
        return true;
    }










    public void clear() {
        userToSnaps.clear();
    }

}

