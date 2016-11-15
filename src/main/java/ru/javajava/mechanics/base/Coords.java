package ru.javajava.mechanics.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * Created by ivan on 14.11.16.
 */
public class Coords {

    public Coords() {
    }

    public Coords(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @JsonProperty("x")
    public  double x;
    @JsonProperty("y")
    public  double y;
    @JsonProperty("z")
    public  double z;


    public Coords add(@NotNull Coords addition) {
        return new Coords(x + addition.x, y + addition.y, z + addition.z);
    }
}
