package ru.javajava.mechanics;

import ru.javajava.mechanics.base.UserSnap;

/**
 * Created by ivan on 15.11.16.
 */
public interface GameMechanics {

    void addClientSnapshot(long userId, UserSnap userSnap);

    void addUser(long user);

    boolean hasFreeSlots();

    boolean removeUser (long user);

    boolean isPlaying (long user);

    void gmStep(long frameTime);

    int getSessionsNum();

    GameSession getSessionForUser(long user);

    void reset();
}
