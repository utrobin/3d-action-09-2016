package ru.javajava.mechanics.base;

/**
 * Created by ivan on 08.12.16.
 */
public class MyVector {
    private Coords coords;

    public MyVector() {
    }

    public MyVector(Coords coords) {
        this.coords = coords;
    }

    public MyVector(double x, double y, double z) {
        this.coords = new Coords(x, y, z);
    }

    public double getCos (MyVector other) {
        return getScalarMultipl(other) / (getModule() * other.getModule());
    }

    public double getScalarMultipl (MyVector other) {
        return this.coords.x * other.getX() + this.coords.y * other.getY() + this.coords.z * other.getZ();
    }

    public double getModule () {
        final double sum = this.coords.x*this.coords.x + this.coords.y*this.coords.y + this.coords.z*this.coords.z;
        return Math.sqrt(sum);
    }

    public Coords getCoords() {
        return coords;
    }

    public void setCoords(Coords coords) {
        this.coords = coords;
    }

    public double getX() {
        return coords.x;
    }
    public double getY() {
        return coords.y;
    }
    public double getZ() {
        return coords.z;
    }
}
