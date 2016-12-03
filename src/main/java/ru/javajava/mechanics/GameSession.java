package ru.javajava.mechanics;

import ru.javajava.mechanics.avatar.GameUser;
import ru.javajava.model.UserProfile;

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

   public static final int MAX_PLAYERS = 2;

    public GameSession() {
        this.sessionId = ID_GENERATOR.getAndIncrement();
    }

    public GameSession(Iterable<UserProfile> players) {
        this.sessionId = ID_GENERATOR.getAndIncrement();
        for (UserProfile player: players) {
            this.players.add(new GameUser(player));
        }
    }

    public boolean isFull() {
        return  players.size() == MAX_PLAYERS;
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public void addPlayer (UserProfile player) {
        if (players.size() < MAX_PLAYERS) {
            players.add(new GameUser(player));
        }
    }

    public void removePlayer (UserProfile player) {
        final GameUser gameUser = new GameUser(player);
        players.remove(gameUser);
    }

    public void removePlayer (long userId) {
        for (GameUser player: players) {
            if (player.getId() == userId) {
                players.remove(player);
                break;
            }
        }
    }

    public List<GameUser> getPlayers() {
        return players;
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
