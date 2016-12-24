package ru.javajava.mechanics.avatar;

import ru.javajava.mechanics.base.Coords;
import ru.javajava.mechanics.base.ServerPlayerSnap;
import ru.javajava.mechanics.base.VictimModel;
import ru.javajava.model.UserProfile;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ivan on 14.11.16.
 */
public class GameUser {
    private final UserProfile userProfile;
    private Coords position;
    private int hp = 100;
    public static final int SHOT_REDUCING = 35;
    private boolean wasShot;
    private final Set<VictimModel> victims = new HashSet<>();

    private int kills = 0;
    private int deaths = 0;

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

    public void resetForNextSnap() {
        wasShot = false;
        victims.clear();
        if (hp == 0) {
            setFullHealth();
        }
    }


    public ServerPlayerSnap generateSnap() {
        final ServerPlayerSnap result = new ServerPlayerSnap();
        result.setUserId(getId());
        result.setPosition(position);
        result.setHp(hp);
        result.setVictims(victims);
        result.setKills(kills);
        result.setDeaths(deaths);
        result.setLogin(userProfile.getLogin());
        victims.clear();
        return result;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void markShot(double coefficient) {
        wasShot = true;
        hp -= SHOT_REDUCING * coefficient;
        if (hp < 0) {
            hp = 0;
            deaths++;
        }
    }

    public void addVictim (VictimModel victim) {
        victims.add(victim);
        kills++;
    }

    public boolean getShot() {
        return wasShot;
    }

    public int getHp() {
        return hp;
    }

    private void setFullHealth() {
        hp = 100;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
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

