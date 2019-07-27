package com.gasis.rts.logic.object.unit.movement;

import com.gasis.rts.logic.object.unit.Unit;

/**
 * An adapter class for movement listener
 */
public class MovementAdapter implements MovementListener {

    /**
     * Called when a unit starts moving
     *
     * @param unit the unit that just started moving
     */
    @Override
    public void startedMoving(Unit unit) {

    }

    /**
     * Called when a unit reaches it's destination or stops moving for other reasons
     *
     * @param unit the unit that just arrived at it's destination
     */
    @Override
    public void stoppedMoving(Unit unit) {

    }

    /**
     * Called when a unit is unable to move anymore in it's current state (for example,
     * the unit has entered siege mode before reaching it's destination)
     *
     * @param unit unit that is unable to move
     */
    @Override
    public void unableToMoveInCurrentState(Unit unit) {

    }
}
