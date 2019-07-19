package com.gasis.rts.math;

/**
 * Math utility functions
 */
public class MathUtils {

    /**
     * Calculates the angle between 2 points in degrees
     *
     * @param x1 x of the first point
     * @param y1 y of the first point
     * @param x2 x of the second point
     * @param y2 y of the second point
     *
     * @return angle in degrees
     */
    public static float angle(float x1, float y1, float x2, float y2) {
        float xDiff = x2 + 0.0001f - x1;
        float yDiff = y2 + 0.0001f - y1;

        // calculate the tangent of the angle
        float tan = yDiff / xDiff;

        // get the angle from the tangent value
        float angle = (float) Math.atan(tan);

        // convert the angle from radians to degrees
        angle = (float) (angle * (180 / Math.PI));

        if (xDiff > 0) {
            return (360 + angle) % 360;
        } else {
            return 180 + angle;
        }
    }

    /**
     * Returns the distance between 2 points
     *
     * @return positive value of the distance
     */
    public static float distance(float x1, float x2, float y1, float y2) {
        return (float) Math.abs(Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)));
    }
}
