package ru.javajava.mechanics.base;

import java.util.List;

/**
 * Created by ivan on 15.11.16.
 */
public class ServerSnap {

    private boolean shot;
    private int hp;
    private List<ServerPlayerSnap> players;

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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}