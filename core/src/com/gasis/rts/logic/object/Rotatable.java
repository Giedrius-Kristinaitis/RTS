package com.gasis.rts.logic.object;

/**
 * A thing that can change it's facing direction
 */
public interface Rotatable {

    /**
     * Rotates to face the specified direction
     *
     * @param facingDirection direction to face (must be one of defined directions in the
     *                        Unit class)
     */
    void rotateToDirection(byte facingDirection);
}
