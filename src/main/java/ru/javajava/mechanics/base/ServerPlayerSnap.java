package ru.javajava.mechanics.base;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Created by ivan on 15.11.16.
 */
public class ServerPlayerSnap {
    @JsonProperty("id")
    private long userId;
    @JsonProperty("position")
    private Coords playerCoords;

    public Coords getPlayerCoords() {
        return playerCoords;
    }

    public void setPlayerCoords(Coords playerCoords) {
        this.playerCoords = playerCoords;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
