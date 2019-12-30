package com.gasis.rts.logic.map;

import com.gasis.rts.math.Point;

import java.util.List;

/**
 * Game map abstraction
 */
public interface Map {

    /**
     * Gets the width of the map
     *
     * @return
     */
    float getWidth();

    /**
     * Gets the height of the map
     *
     * @return
     */
    float getHeight();

    /**
     * Gets all possible locations for a base
     *
     * @return
     */
    List<Point> getBaseLocations();
}
