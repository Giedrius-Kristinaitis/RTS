package com.gasis.rts.logic.object.unit.movement;

/**
 * Anything that can move
 */
public interface Movable {

    /**
     * Orders the object one block in the given direction
     *
     * @param direction movement direction
     */
    void move(byte direction);
}
