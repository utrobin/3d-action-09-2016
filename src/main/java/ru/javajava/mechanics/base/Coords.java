package ru.javajava.mechanics.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

/**
 * Created by ivan on 14.11.16.
 */
public class Coords {
    public Coords(@JsonProperty("x") double x, @JsonProperty("y") double y, @JsonProperty("z") double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public final double x;
    public final double y;
    public final double z;


    public Coords add(@NotNull Coords addition) {
        return new Coords(x + addition.x, y + addition.y, z + addition.z);
    }
}
