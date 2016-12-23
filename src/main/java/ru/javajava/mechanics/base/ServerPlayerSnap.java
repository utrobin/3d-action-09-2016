package ru.javajava.mechanics.base;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by ivan on 15.11.16.
 */
public class ServerPlayerSnap {
    @JsonProperty("id")
    private long userId;
    private String login;
    private Coords position;
    private int hp;
    private final Set<VictimModel> victims = new HashSet<>();
    private int kills;
    private int deaths;


    public Coords getPosition() {
        return position;
    }

    public void setPosition(Coords position) {
        this.position = position;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public Set<VictimModel> getVictims() {
        return victims;
    }

    public void setVictims(Collection<VictimModel> victims) {
        this.victims.addAll(victims);
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
