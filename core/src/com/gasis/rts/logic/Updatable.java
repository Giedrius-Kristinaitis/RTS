package com.gasis.rts.logic;

/**
 * Anything that can be updated
 */
public interface Updatable {

    /**
     * Updates the state of the object
     *
     * @param delta time elapsed since the last update
     */
    void update(float delta);
}
