package com.gasis.rts.logic.pathfinding;

import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.math.MathUtils;

import java.util.*;

/**
 * Finds paths on a block map
 */
public class PathFinder implements PathFinderInterface {

    // the game's map
    protected BlockMap map;

    // paths for the searched objects
    protected Map<Unit, Deque<Point>> foundPaths = new HashMap<Unit, Deque<Point>>();

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
    public void findPathsToObjects(List<Unit> objects, short x, short y) {
        for (Unit unit: objects) {
            depthFirst(unit, x, y);
        }
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
            Point next = getBestNotVisitedNeighbour(visitedPoints, neighbours, processedPoint, destination);

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
                    next = getBestNotVisitedNeighbour(visitedPoints, neighbours, processedPoint.lastPoint, destination);

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

        foundPaths.put(object, path);
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
     * @param visitedPoints points that have been visited so far
     * @param neighbours instance of a list that will be used to store neighbours (just to avoid memory leaking)
     * @param point current point in the algorithm
     * @param destination algorithm's destination point
     * @return
     */
    protected Point getBestNotVisitedNeighbour(Set<Point> visitedPoints, List<Point> neighbours, Point point, Point destination) {
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
            if (map.isBlockPassable((short) neighbor.x, (short) neighbor.y) && !map.isBlockOccupied((short) neighbor.x, (short) neighbor.y) && !visitedPoints.contains(neighbor)) {
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
        foundPaths.remove(unit);
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
            return foundPaths.get(object).peek();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Removes the next path point from the path of the given unit
     *
     * @param object unit for which to shorten the path
     */
    @Override
    public void removeNextPathPoint(Unit object) {
        try {
            foundPaths.get(object).pop();
        } catch (Exception ex) { }
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
}
