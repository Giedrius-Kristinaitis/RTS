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
        if (x == p.x && y == p.y) {
            return 0;
        } else if (x < p.x) {
            return -1;
        } else {
            return 1;
        }
    }

    /**
     * Checks if the point is equal to another object
     *
     * @param obj object to compare to
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point)) {
            return false;
        }

        return compareTo((Point) obj) == 0;
    }

    /**
     * Gets the hash code of the point
     * @return
     */
    @Override
    public int hashCode() {
        return (int) (x * y);
    }
}
