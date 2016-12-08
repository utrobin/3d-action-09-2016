package ru.javajava.mechanics.base;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Created by ivan on 15.11.16.
 */
public class ServerPlayerSnap {
    @JsonProperty("id")
    private long userId;
    @JsonProperty("position")
    private Coords position;
    @JsonProperty("hp")
    private int hp;

    @JsonProperty ("shot")
    private boolean shot;

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

    public boolean isShot() {
        return shot;
    }

    public void setShot(boolean shot) {
        this.shot = shot;
    }
}
