package com.gasis.rts.logic.object;

/**
 * Any GameObject subclass than can fire at a target
 */
public interface Fireable {

    /**
     * Fires a shot at a target
     *
     * @param targetX x coordinate of the target
     * @param targetY y coordinate of the target
     *
     * @return has the shot been fired or not
     */
    boolean fire(float targetX, float targetY);
}
