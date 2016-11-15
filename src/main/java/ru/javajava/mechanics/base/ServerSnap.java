package ru.javajava.mechanics.base;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by ivan on 15.11.16.
 */
public class ServerSnap {

    List<ServerPlayerSnap> players;


    public List<ServerPlayerSnap> getPlayers() {
        return players;
    }

    public void setPlayers(List<ServerPlayerSnap> players) {
        this.players = players;
    }

}