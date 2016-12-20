package ru.javajava.mechanics.base;

import org.jetbrains.annotations.NotNull;

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

    public  double x;
    public  double y;
    public  double z;


    public Coords add(@NotNull Coords addition) {
        return new Coords(x + addition.x, y + addition.y, z + addition.z);
    }

    public Coords subtract(@NotNull Coords subtractor) {
        return new Coords(x - subtractor.x, y - subtractor.y, z - subtractor.z);
    }

    public double getDistanceBetween (Coords other) {
        double a = (this.x - other.x) * (this.x - other.x);
        double b = (this.y - other.y) * (this.y - other.y);
        double c = (this.z - other.z) * (this.z - other.z);
        return Math.sqrt(a + b + c);
    }
}
