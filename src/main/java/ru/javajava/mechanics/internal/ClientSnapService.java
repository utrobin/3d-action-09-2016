package ru.javajava.mechanics.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.javajava.mechanics.GameSession;
import ru.javajava.mechanics.avatar.GameUser;
import ru.javajava.mechanics.base.CameraDirection;
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
                CameraDirection cameraDirection = snap.getCamera();
                processFiring (snap, players);
            }
        }
    }


    // Функция возвращает игрока, в которого попали или null в случае промаха
    private GameUser processFiring(UserSnap snap, Iterable<GameUser> players) {
        final Coords position = snap.getPosition();
        final CameraDirection cameraDirection  = snap.getCamera();
        double horizAngle = cameraDirection.getHorizontalAngle();
        final double verticAngle = cameraDirection.getVerticalAngle();
        horizAngle = horizAngle % Math.PI;

        final Coords currentShot = new Coords(Math.sin(horizAngle), Math.sin(verticAngle), Math.cos(horizAngle));


        // Debug
        for (GameUser player: players) {
            if (player.getId() == snap.getId()) {
                continue; // Это ты и есть
            }
            final Coords enemyCoords = player.getPosition();
            if (enemyCoords == null) {
                continue;
            }


            final Coords idealShot = enemyCoords.subtract(position);
            double x = (double) Math.round(idealShot.x * 100) / 100;
            double y = (double) Math.round(idealShot.y * 100) / 100;
            double z = (double) Math.round(idealShot.z * 100) / 100;




            final double distance = enemyCoords.getDistanceBetween(position);
            final double hypotenuse = Math.sqrt(distance*distance + RADIUS*RADIUS);
            final double maxCos = distance / hypotenuse;   // Косинус МАКСИМАЛЬНО возможного угла

            final double cos = getCos(idealShot, currentShot);

            if (cos >= maxCos) {    // Если угол между векторами МЕНЬШЕ максимально возможного
                LOGGER.info("SHOOTED!");
            }
            else {
                LOGGER.info("missed");
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


    private double getCos (Coords vec1, Coords vec2) {
        return getScalarMultipl(vec1, vec2) / (getModule(vec1) * getModule(vec2));
    }

    private double getScalarMultipl (Coords first, Coords second) {
        return first.x * second.x + first.y * second.y + first.z * second.z;
    }

    private double getModule (Coords vec) {
        final double sum = vec.x * vec.x + vec.y * vec.y + vec.z * vec.z;
        return Math.sqrt(sum);
    }







    public void clear() {
        userToSnaps.clear();
    }

}

