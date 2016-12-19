package ru.javajava.mechanics;

import ru.javajava.mechanics.avatar.GameUser;
import ru.javajava.model.UserProfile;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by ivan on 14.11.16.
 */
public class GameSession {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    private final Long sessionId;
    private final Set<GameUser> players = new HashSet<>();

    public static final int MAX_PLAYERS = 16;

    public GameSession() {
        this.sessionId = ID_GENERATOR.getAndIncrement();
    }


    public boolean isFull() {
        return  players.size() == MAX_PLAYERS;
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public void addPlayer (UserProfile player) {
        if (players.size() >= MAX_PLAYERS) {
            throw new RuntimeException("No more players for this session!");
        }
        players.add(new GameUser(player));
    }


    public void removePlayer (long userId) {
        for (GameUser player: players) {
            if (player.getId() == userId) {
                players.remove(player);
                break;
            }
        }
    }

    public Set<GameUser> getPlayers() {
        return players;
    }

    public Long getId() {
        return sessionId;
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
