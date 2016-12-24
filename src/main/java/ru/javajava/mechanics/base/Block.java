package ru.javajava.mechanics.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ivan on 23.12.16.
 */
public class Block {
    private static final Logger LOGGER = LoggerFactory.getLogger(Block.class);
    private Coords center;
    private double xLength;
    private double yLength;
    private double zLength;

    private double xRadius;
    private double yRadius;
    private double zRadius;

    public Block(Coords center) {
        this.center = center;
    }

    public Coords getCenter() {
        return center;
    }

    public void setCenter(Coords center) {
        this.center = center;
    }

    public double getxLength() {
        return xLength;
    }

    public void setxLength(double xLength) {
        this.xLength = xLength;
        xRadius = xLength / 2;
    }

    public double getyLength() {
        return yLength;
    }

    public void setyLength(double yLength) {
        this.yLength = yLength;
        yRadius = yLength / 2;
    }

    public double getzLength() {
        return zLength;
    }

    public void setzLength(double zLength) {
        this.zLength = zLength;
        zRadius = zLength / 2;
    }

    public double getxRadius() {
        return xRadius;
    }

    public double getyRadius() {
        return yRadius;
    }

    public double getzRadius() {
        return zRadius;
    }

    private double getClosestXPlane(Coords point) {
        final double first = center.x + xRadius;
        final double second = center.x - xRadius;
        if (Math.abs(point.x - first) < Math.abs(point.x - second)) {
            return first;
        }
        else {
            return second;
        }
    }

    private double getYPlane() {
        return center.y + yRadius;
    }

    private double getClosestZPlane(Coords point) {
        final double first = center.z + zRadius;
        final double second = center.z - zRadius;
        if (Math.abs(point.z - first) < Math.abs(point.z - second)) {
            return first;
        }
        else {
            return second;
        }
    }

    private Coords getIntersectionPointWithX(Ray ray) {
        MyVector vector = ray.getVector();
        Coords linePoint = ray.getPoint();
        double plane = getClosestXPlane(ray.getPoint());  // Уравн. плоскости
        double t = (plane - linePoint.x) / vector.getX();

        double x = plane;
        double y = vector.getY() * t + linePoint.y;
        double z = vector.getZ() * t + linePoint.z;

        if (vector.getX() >= 0 && x < linePoint.x || vector.getX() < 0 && x > linePoint.x) {
            return null;
        }

        return new Coords(x, y, z);
    }

    private Coords getIntersectionPointWithY(Ray ray) {
        MyVector vector = ray.getVector();
        Coords linePoint = ray.getPoint();
        double plane = getYPlane();  // Уравн. плоскости
        double t = (plane - linePoint.y) / vector.getY();

        double y = plane;
        double x = vector.getX() * t + linePoint.x;
        double z = vector.getZ() * t + linePoint.z;
        return new Coords(x, y, z);
    }

    private Coords getIntersectionPointWithZ(Ray ray) {
        MyVector vector = ray.getVector();
        Coords linePoint = ray.getPoint();
        double plane = getClosestZPlane(ray.getPoint());
        double t = (plane - linePoint.z) / vector.getZ();

        double z = plane;
        double y = vector.getY() * t + linePoint.y;
        double x = vector.getX() * t + linePoint.x;


        if (vector.getZ() >= 0 && z < linePoint.z || vector.getZ() < 0 && z > linePoint.z) {
            return null;
        }

        return new Coords(x, y, z);
    }



    private boolean isInside (Coords point) {
        if (point == null) {
            return false;
        }
        boolean xOK = point.x >= center.x - xRadius && point.x <= center.x + xRadius;
        boolean yOK = point.y >= center.y - yRadius && point.y <= center.y + yRadius;
        boolean zOK = point.z >= center.z - zRadius && point.z <= center.z + zRadius;
        return xOK && yOK && zOK;
    }

    public Double isOnTheWay (Ray ray) {
        Coords intersectionX = getIntersectionPointWithX(ray);
        if (intersectionX != null) {
            LOGGER.info("X point: ({}, {}, {})", intersectionX.x, intersectionX.y, intersectionX.z);
        }
        Coords intersectionY = getIntersectionPointWithY(ray);
        Coords intersectionZ = getIntersectionPointWithZ(ray);
        boolean xInside = isInside(intersectionX);
        boolean yInside = isInside(intersectionY);
        boolean zInside = isInside(intersectionZ);

        if (!(xInside || yInside || zInside)) {
            return null;
        }
        double min = 100000;
        if (intersectionX != null && xInside && intersectionX.getDistanceBetween(ray.getPoint()) < min) {
            min = intersectionX.getDistanceBetween(ray.getPoint());
        }

        if (intersectionY != null && yInside && intersectionY.getDistanceBetween(ray.getPoint()) < min) {
            min = intersectionY.getDistanceBetween(ray.getPoint());
        }

        if (intersectionZ != null && zInside && intersectionZ.getDistanceBetween(ray.getPoint()) < min) {
            min = intersectionZ.getDistanceBetween(ray.getPoint());
        }
        return min;
    }
}
