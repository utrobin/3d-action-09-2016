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
//            for (ClientSnap snap : playerSnaps) {
//                processMovement(player, snap.getDirection(), snap.getFrameTime());
//            }
//            final ClientSnap lastSnap = playerSnaps.get(playerSnaps.size() - 1);
//            processMouseMove(player, lastSnap.getMouse());

            //TODO:Firing
        }
    }


//
//    private void moveSquareBy(@NotNull Square square, double dx, double dy) {
//        final PositionPart positionPart = square.claimPart(PositionPart.class);
//        final Coords lastDesirablePoint = positionPart.getLastDesirablePoint();
//        final double newX = Math.min(Config.PLAYGROUND_WIDTH - Config.SQUARE_SIZE, lastDesirablePoint.x + dx);
//        final double newY = Math.min(Config.PLAYGROUND_HEIGHT - Config.SQUARE_SIZE, lastDesirablePoint.y + dy);
//        positionPart.addDesirableCoords(new Coords(newX, newY));
//        movementService.registerObjectToMove(square);
//    }
//
//    private void processMouseMove(@NotNull GameUser gameUser, @NotNull Coords mouse) {
//        gameUser.getSquare().claimPart(MousePart.class).setMouse(mouse);
//    }


    public void clear() {
        userToSnap.clear();
    }

}

