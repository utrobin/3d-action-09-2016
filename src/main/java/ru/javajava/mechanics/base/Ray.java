package ru.javajava.mechanics.base;

/**
 * Created by ivan on 24.12.16.
 */
public class Ray {
    private MyVector vector;
    private Coords point;

    public Ray(MyVector vector, Coords point) {
        this.vector = vector;
        this.point = point;
    }

    public MyVector getVector() {
        return vector;
    }

    public void setVector(MyVector vector) {
        this.vector = vector;
    }

    public Coords getPoint() {
        return point;
    }

    public void setPoint(Coords point) {
        this.point = point;
    }
}
