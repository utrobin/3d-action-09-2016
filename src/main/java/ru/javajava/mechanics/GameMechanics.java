package ru.javajava.mechanics;

import ru.javajava.mechanics.base.UserSnap;

/**
 * Created by ivan on 15.11.16.
 */
public interface GameMechanics {

    void addClientSnapshot(long userId, UserSnap userSnap);

    void addUser(long user);

    void gmStep(long frameTime);

    void reset();
}
