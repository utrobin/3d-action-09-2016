package ru.javajava.mechanics.avatar;

import ru.javajava.mechanics.base.Coords;
import ru.javajava.mechanics.base.ServerPlayerSnap;
import ru.javajava.model.UserProfile;

/**
 * Created by ivan on 14.11.16.
 */
public class GameUser {
    private final UserProfile userProfile;
    private Coords position;
    private int hp = 100;
    public static final int SHOT_REDUCING = 35;

    public GameUser(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setPosition(Coords coords) {
        this.position = coords;
    }

    public Coords getPosition() {
        return position;
    }

    public long getId() {
        return userProfile.getId();
    }

    public ServerPlayerSnap generateSnap() {
        final ServerPlayerSnap result = new ServerPlayerSnap();
        result.setUserId(getId());
        result.setPosition(position);
        result.setHp(hp);
        return result;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void setHp(byte hp) {
        this.hp = hp;
    }

    public void markShot() {
        hp -= SHOT_REDUCING;
        if (hp < 0) {
            hp = 0;
        }
    }

    public void markСomplHealthy() {
        hp = 100;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final GameUser other = (GameUser) o;
        return this.getId() == other.getId();
    }

    @Override
    public int hashCode() {
        final long id = userProfile.getId();
        return (int) (id ^ (id >>> 32));
    }
}

