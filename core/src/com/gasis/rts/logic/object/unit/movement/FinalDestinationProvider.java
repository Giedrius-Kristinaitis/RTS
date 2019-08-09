package com.gasis.rts.logic.object.unit.movement;

import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.math.Point;

/**
 * Provides final destinations for units
 */
public interface FinalDestinationProvider {

    /**
     * Gets unit's final destination
     *
     * @param unit unit to get the destination for
     * @return
     */
    Point getFinalDestination(Unit unit);
}
