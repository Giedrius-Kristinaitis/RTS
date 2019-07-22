package com.gasis.rts.logic.pathfinding;

import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.math.MathUtils;

import java.util.*;

/**
 * Finds paths on a block map
 */
public class PathFinder implements PathFinderInterface {

    // the game's map
    protected BlockMap map;

    // all existing path groups
    protected Set<PathGroup> groups = new HashSet<PathGroup>();

    // the newest created path group to which new paths will be put
    protected PathGroup newestGroup;

    // path groups to be removed from the group set
    protected List<PathGroup> groupsToRemove = new ArrayList<PathGroup>();

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
    public void findPathsToObjects(Iterable<Unit> objects, short x, short y) {
        for (Unit unit: objects) {
            depthFirst(unit, x, y);
        }
    }

    /**
     * @param object object to find the path for
     * @param x      destination x (in block map coordinates)
     * @param y      destination y (in block map coordinates)
     */
    @Override
    public void findPathToObject(Unit object, short x, short y) {
        depthFirst(object, x, y);
    }

    /**
     * Re-finds the given object's path to it's destination
     *
     * @param object object to find the path for
     */
    @Override
    public void refindPathToObject(Unit object) {
        for (PathGroup group: groups) {
            if (group.foundPaths.containsKey(object)) {
                depthFirst(object, (short) group.foundPaths.get(object).getLast().x, (short) group.foundPaths.get(object).getLast().y);
                break;
            }
        }
    }

    /**
     * Creates a new path group
     *
     * @param units units that will be a part of the group
     */
    @Override
    public void newGroup(Iterable<Unit> units) {
        PathGroup group = new PathGroup();

        for (Unit unit: units) {
            for (PathGroup group1: groups) {
                group1.foundPaths.remove(unit);
            }

            group.foundPaths.put(unit, null);
        }

        newestGroup = group;
        groups.add(group);
        removeEmptyGroups();
    }

    /**
     * Removes empty path groups to avoid memory leaking
     */
    protected void removeEmptyGroups() {
        for (PathGroup group: groups) {
            if (group.foundPaths.isEmpty()) {
                groupsToRemove.add(group);
            }
        }

        for (PathGroup group: groupsToRemove) {
            groups.remove(group);
        }

        groupsToRemove.clear();
    }

    /**
     * Performs a greedy depth-first search algorithm between the given points
     * and saves the found path
     *
     * @param object the object for which the path will be found
     * @param x destination x
     * @param y destination y
     */
    protected void depthFirst(Unit object, short x, short y) {
        if (newestGroup == null) {
            throw new IllegalStateException("No path groups created");
        }

        Point startPoint = getObjectCoordinates(object);
        Point destination = new Point(x, y);
        Point processedPoint = startPoint;
        Point firstDeadEnd = null;
        Set<Point> visitedPoints = new TreeSet<Point>();
        List<Point> neighbours = new ArrayList<Point>();

        while (true) {
            visitedPoints.add(processedPoint);

            if (processedPoint.equals(destination)) {
                break;
            }

            // get the point that has not been visited and is the closest to the destination
            Point next = getBestNotVisitedNeighbour(object, visitedPoints, neighbours, processedPoint, destination);

            // make the next point be processed in the next iteration
            if (next != null) {
                next.lastPoint = processedPoint;
                processedPoint = next;
            } else {
                if (firstDeadEnd == null) {
                    firstDeadEnd = processedPoint;
                }

                // backtrack the visited points and pick the one that has unvisited neighbours
                while (processedPoint.lastPoint != null) {
                    next = getBestNotVisitedNeighbour(object, visitedPoints, neighbours, processedPoint.lastPoint, destination);

                    if (next == null) {
                        processedPoint = processedPoint.lastPoint;
                    } else {
                        break;
                    }
                }

                if (next == null) {
                    break;
                } else {
                    processedPoint = next;
                }
            }
        }

        // form the path
        Deque<Point> path = null;

        if (processedPoint.equals(destination)) {
            path = formPath(processedPoint);
        } else {
            path = formPath(firstDeadEnd);
        }

        newestGroup.foundPaths.put(object, path);
    }

    /**
     * Forms a path between two points
     *
     * @param destination end point
     */
    protected Deque<Point> formPath(Point destination) {
        Deque<Point> path = new LinkedList<Point>();

        Point current = destination;

        while (current.lastPoint != null) {
            path.push(current);
            current = current.lastPoint;
        }

        return path;
    }

    /**
     * Gets point's neighbour that is not visited and is the closest to the destination
     *
     * @param unit the unit the algorithm is finding path for
     * @param visitedPoints points that have been visited so far
     * @param neighbours instance of a list that will be used to store neighbours (just to avoid memory leaking)
     * @param point current point in the algorithm
     * @param destination algorithm's destination point
     * @return
     */
    protected Point getBestNotVisitedNeighbour(Unit unit, Set<Point> visitedPoints, List<Point> neighbours, Point point, Point destination) {
        neighbours.clear();
        neighbours.add(new Point(point.x, point.y + 1));
        neighbours.add(new Point(point.x + 1, point.y + 1));
        neighbours.add(new Point(point.x + 1, point.y));
        neighbours.add(new Point(point.x + 1, point.y - 1));
        neighbours.add(new Point(point.x, point.y - 1));
        neighbours.add(new Point(point.x - 1, point.y - 1));
        neighbours.add(new Point(point.x - 1, point.y));
        neighbours.add(new Point(point.x - 1, point.y + 1));

        Point next = null;
        float minDistance = Float.MAX_VALUE;

        for (Point neighbor: neighbours) {
            if (blockAvailable(unit, visitedPoints, neighbor)) {
                float distance = MathUtils.distance(neighbor.x, destination.x, neighbor.y, destination.y);

                if (distance < minDistance) {
                    minDistance = distance;
                    next = neighbor;
                }
            }
        }

        return next;
    }

    /**
     * Checks if a point is available for a path
     *
     * @param unit the unit the algorithm is finding path for
     * @param visitedPoints points visited so far by the algorithm
     * @param point point to check
     * @return
     */
    protected boolean blockAvailable(Unit unit, Set<Point> visitedPoints, Point point) {
        if (point.x < 0 || point.y < 0 || point.x >= map.getWidth() || point.y >= map.getHeight()) {
            return false;
        }

        GameObject occupyingObject = map.getOccupyingObject((short) point.x, (short) point.y);
        Unit occupyingUnit = occupyingObject instanceof Unit ? (Unit) occupyingObject : null;

        return
                map.isBlockPassable((short) point.x, (short) point.y)
                        && (!map.isBlockOccupied((short) point.x, (short) point.y) || (occupyingUnit != null && (occupyingUnit.isMoving() || getGroup(unit).foundPaths.containsKey(occupyingUnit))))
                        && !visitedPoints.contains(point);
    }

    /**
     * Gets the specified unit's path group
     *
     * @param unit unit to get the group for
     * @return
     */
    protected PathGroup getGroup(Unit unit) {
        for (PathGroup group: groups) {
            if (group.foundPaths.containsKey(unit)) {
                return group;
            }
        }

        return null;
    }

    /**
     * Gets the object's coordinates in block map's system
     *
     * @param object
     * @return
     */
    protected Point getObjectCoordinates(Unit object) {
        return new Point(
                (short) (object.getCenterX() / Block.BLOCK_WIDTH),
                (short) (object.getCenterY() / Block.BLOCK_HEIGHT)
        );
    }

    /**
     * Removes the path associated with the given unit
     *
     * @param unit unit associated with a path
     */
    public void removePathForObject(Unit unit) {
        for (PathGroup group: groups) {
            group.foundPaths.remove(unit);
        }
    }

    /**
     * Gets the next point from the given object's path
     *
     * @param object object to get the next point for
     * @return
     */
    @Override
    public Point getNextPathPointForObject(Unit object) {
        try {
            for (PathGroup group: groups) {
                if (group.foundPaths.containsKey(object)) {
                    return group.foundPaths.get(object).peek();
                }
            }
        } catch (Exception ex) {
            return null;
        }

        return null;
    }

    /**
     * Removes the next path point from the path of the given unit
     *
     * @param object unit for which to shorten the path
     */
    @Override
    public void removeNextPathPoint(Unit object) {
        try {
            for (PathGroup group: groups) {
                if (group.foundPaths.containsKey(object)) {
                    group.foundPaths.get(object).pop();
                    break;
                }
            }
        } catch (Exception ex) { }
    }

    /**
     * Clears found path list
     */
    @Override
    public void clearAllPaths() {
        for (PathGroup group: groups) {
            group.foundPaths.clear();
        }

        groups.clear();
        newestGroup = null;
    }

    /**
     * A point extension that has the last visited point attached to it
     */
    protected class Point extends com.gasis.rts.math.Point {

        protected Point lastPoint;

        protected Point(float x, float y) {
            super(x, y);
        }
    }

    /**
     * A group of paths
     */
    protected class PathGroup {

        protected Map<Unit, Deque<Point>> foundPaths = new HashMap<Unit, Deque<Point>>();
    }
}
