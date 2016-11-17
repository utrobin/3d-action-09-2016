package ru.javajava.mechanics;

import ru.javajava.mechanics.avatar.GameUser;
import ru.javajava.model.UserProfile;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by ivan on 14.11.16.
 */
public class GameSession {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    private final Long sessionId;
    private final List<GameUser> players = new ArrayList<>();

    public GameSession(Iterable<UserProfile> players) {
        this.sessionId = ID_GENERATOR.getAndIncrement();
        for (UserProfile player: players) {
            this.players.add(new GameUser(player));
        }
    }

    public GameUser getSelf(long userId) {
        for (GameUser player: players) {
            if (player.getId() == userId) {
                return player;
            }
        }
        return null;
    }

    public List<GameUser> getPlayers() {
        return players;
    }

    public void addPlayer (UserProfile player) {
        players.add(new GameUser(player));
    }

    public void removePlayer (UserProfile player) {
        final GameUser gameUser = new GameUser(player);
        players.remove(gameUser);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final GameSession other = (GameSession) o;

        return sessionId.equals(other.sessionId);
    }

    @Override
    public int hashCode() {
        return sessionId.hashCode();
    }
}
