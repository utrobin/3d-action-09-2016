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
        return result;
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

