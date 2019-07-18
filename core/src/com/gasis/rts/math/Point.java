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
