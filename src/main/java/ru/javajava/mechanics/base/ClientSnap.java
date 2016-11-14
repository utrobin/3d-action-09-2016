package ru.javajava.mechanics.base;


import org.jetbrains.annotations.NotNull;

/**
 * Created by ivan on 14.11.16.
 */
public class ClientSnap {
    private Coords position;
    private long frameTime;

    public Coords getPosition() {
        return position;
    }

    public void setPosition(@NotNull Coords position) {
        this.position = position;
    }

    public long getFrameTime() {
        return frameTime;
    }

    public void setFrameTime(long frameTime) {
        this.frameTime = frameTime;
    }
}