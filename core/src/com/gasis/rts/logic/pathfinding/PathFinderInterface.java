package com.gasis.rts.logic.pathfinding;

import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.math.Point;

import java.util.List;

/**
 * Finds paths on a block map
 */
public interface PathFinderInterface {

    /**
     * Finds paths from all given objects to the destination point
     *
     * @param objects objects to find paths from
     * @param x destination x (in block map coordinates)
     * @param y destination y (in block map coordinates)
     */
    void findPathsToObjects(List<Unit> objects, short x, short y);

    /**
     * Gets the next point from the given object's path
     *
     * @param object object to get the next point for
     * @return
     */
    Point getNextPathPointForObject(Unit object);

    /**
     * Removes the next path point from the path of the given unit
     *
     * @param object unit for which to shorten the path
     */
    void removeNextPathPoint(Unit object);
}
