package ru.javajava.mechanics.base;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

/**
 * Created by ivan on 14.11.16.
 */
public class UserSnap {
    @JsonProperty("id")
    private long id;
    @JsonProperty("position")
    private Coords coords;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Coords getCoords() {
        return coords;
    }

    public void setCoords(Coords coords) {
        this.coords = coords;
    }
}
