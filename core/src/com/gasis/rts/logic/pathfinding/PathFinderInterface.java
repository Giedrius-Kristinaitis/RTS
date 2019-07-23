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
     * @param objects objects to find paths for
     * @param x destination x (in block map coordinates)
     * @param y destination y (in block map coordinates)
     */
    void findPathsToObjects(Iterable<Unit> objects, short x, short y);

    /**
     *
     * @param object object to find the path for
     * @param x destination x (in block map coordinates)
     * @param y destination y (in block map coordinates)
     */
    void findPathToObject(Unit object, short x, short y);

    /**
     * Re-finds the given object's path to it's destination
     *
     * @param object object to find the path for
     * @param maxObstacleDistance the maximum distance the algorithm can cover when avoiding an obstacle
     */
    void refindPathToObject(Unit object, float maxObstacleDistance);

    /**
     * Creates a new path group
     *
     * @param units units that will be a part of the group
     */
    void newGroup(Iterable<Unit> units);

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

    /**
     * Removes the path associated with the given unit
     *
     * @param unit unit associated with a path
     */
    void removePathForObject(Unit unit);

    /**
     * Clears found path list
     */
    void clearAllPaths();
}
