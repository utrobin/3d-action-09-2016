package ru.javajava.mechanics.avatar;

import ru.javajava.mechanics.base.Coords;

import java.util.List;

/**
 * Created by ivan on 15.11.16.
 */
public class Sphere {
    Coords coords;

    public Sphere() {
    }

    public Sphere(Coords coords) {
        this.coords = coords;
    }

    public Coords getCoords() {
        return coords;
    }

    public void setCoords(Coords coords) {
        this.coords = coords;
    }
}

