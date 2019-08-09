package com.gasis.rts.logic.object.unit.movement;

import com.gasis.rts.logic.object.unit.Unit;

/**
 * Handles movement requests that are coming from units themselves
 */
public interface MovementRequestHandler {

    /**
     * Handles a unit's movement request
     *
     * @param unit the unit that requested to be moved
     * @param x destination x
     * @param y destination y
     */
    void handleMovementRequest(Unit unit, short x, short y);
}
