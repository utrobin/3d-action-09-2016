package ru.javajava.mechanics.base;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by ivan on 07.12.16.
 */
public class CameraDirection {
    public CameraDirection() {
    }

    public CameraDirection(double verticalAngle, double horizontalAngle) {
        this.verticalAngle = verticalAngle;
        this.horizontalAngle = horizontalAngle;
    }

    @JsonProperty("x")
    private double verticalAngle;
    @JsonProperty("y")
    private double horizontalAngle;

    public double getHorizontalAngle() {
        return horizontalAngle;
    }
    public double getVerticalAngle() {
        return verticalAngle;
    }
}
