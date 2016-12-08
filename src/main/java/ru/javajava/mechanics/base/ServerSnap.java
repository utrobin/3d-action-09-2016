package ru.javajava.mechanics.base;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by ivan on 15.11.16.
 */
public class ServerSnap {

    @JsonProperty("shot")
    boolean shot;
    List<ServerPlayerSnap> players;

    public List<ServerPlayerSnap> getPlayers() {
        return players;
    }

    public void setPlayers(List<ServerPlayerSnap> players) {
        this.players = players;
    }

    public boolean isShot() {
        return shot;
    }

    public void setShot(boolean shot) {
        this.shot = shot;
    }
}