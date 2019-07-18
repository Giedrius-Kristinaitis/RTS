package com.gasis.rts.math;

/**
 * Represents a 2D point
 */
public class Point implements Comparable<Point> {

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

    /**
     * Compares the point to another point
     *
     * @param p the other point
     * @return
     */
    @Override
    public int compareTo(Point p) {
        if (x + y < p.x + p.y) {
            return -1;
        } else if (x + y > p.x + p.y) {
            return 1;
        }

        return 0;
    }
}
