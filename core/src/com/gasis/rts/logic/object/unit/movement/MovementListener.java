package com.gasis.rts.logic.object.unit.movement;

import com.gasis.rts.logic.object.unit.Unit;

/**
 * Listens for unit's movement events
 */
public interface MovementListener {

    /**
     * Called when a unit starts moving
     *
     * @param unit the unit that just started moving
     */
    void startedMoving(Unit unit);

    /**
     * Called when a unit reaches it's destination
     *
     * @param unit the unit that just arrived at it's destination
     */
    void destinationReached(Unit unit);
}
