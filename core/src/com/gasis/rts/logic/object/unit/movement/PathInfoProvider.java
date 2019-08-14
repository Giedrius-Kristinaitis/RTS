package com.gasis.rts.logic.object.unit.movement;

import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.math.Point;

/**
 * Provides path information for units
 */
public interface PathInfoProvider {

    /**
     * Gets unit's final destination
     *
     * @param unit unit to get the destination for
     * @return
     */
    Point getFinalDestination(Unit unit);

    /**
     * Gets unit's next path point
     *
     * @param unit unit to get the next point for
     * @return
     */
    Point getNextPathPoint(Unit unit);
}
