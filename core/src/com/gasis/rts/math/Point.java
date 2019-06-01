package com.gasis.rts.math;

/**
 * Represents a 2D point
 */
public class Point {

    // coordinates of the point
    public float x;
    public float y;

    /**
     * Default class constructor
     */
    public Point() {}

    /**
     * Constructor with arguments
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
