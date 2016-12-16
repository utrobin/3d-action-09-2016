package ru.javajava.mechanics.base;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ivan on 14.11.16.
 */
public class UserSnap {
    @JsonProperty("id")
    private long id;
    @JsonProperty("position")
    private Coords position;

    @JsonProperty("camera")
    private Coords camera;

    @JsonProperty("firing")
    private boolean firing;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Coords getPosition() {
        return position;
    }

    public void setPosition(Coords position) {
        this.position = position;
    }

    public Coords getCamera() {
        return camera;
    }

    public void setCamera(Coords camera) {
        this.camera = camera;
    }

    public boolean isFiring() {
        return firing;
    }

    public void setFiring(boolean firing) {
        this.firing = firing;
    }

}
