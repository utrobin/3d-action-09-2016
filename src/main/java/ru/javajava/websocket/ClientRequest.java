package ru.javajava.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.javajava.mechanics.base.Coords;

/**
 * Created by ivan on 15.11.16.
 */
public class ClientRequest {
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