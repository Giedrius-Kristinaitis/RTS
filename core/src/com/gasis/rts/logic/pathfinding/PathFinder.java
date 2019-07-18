package com.gasis.rts.logic.pathfinding;

import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.math.Point;

import java.util.*;

/**
 * Finds paths on a block map
 */
public class PathFinder implements PathFinderInterface {

    // the game's map
    protected BlockMap map;

    // paths for the searched objects
    protected Map<GameObject, Stack<Point>> foundPaths = new HashMap<GameObject, Stack<Point>>();

    /**
     * Default class constructor
     *
     * @param map
     */
    public PathFinder(BlockMap map) {
        this.map = map;
    }

    /**
     * Finds paths from all given objects to the destination point
     *
     * @param objects objects to find paths from
     * @param x       destination x (in block map coordinates)
     * @param y       destination y (in block map coordinates)
     */
    @Override
    public void findPathsToObjects(List<GameObject> objects, short x, short y) {

    }

    /**
     * Performs a greedy depth-first search algorithm between the given points
     * and saves the found path
     *
     * @param object the object for which the path will be found
     * @param x destination x
     * @param y destination y
     */
    protected void depthFirst(GameObject object, short x, short y) {
        Point startPoint = null;
        Point processedPoint = null;
        Set<Point> visitedPoints = new TreeSet<Point>();

        
    }
}
