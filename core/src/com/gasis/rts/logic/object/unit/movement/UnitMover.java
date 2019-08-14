package com.gasis.rts.logic.object.unit.movement;

import com.gasis.rts.logic.Updatable;
import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.combat.CombatUtils;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.logic.pathfinding.PathFinderInterface;
import com.gasis.rts.math.MathUtils;
import com.gasis.rts.math.Point;

import java.util.*;

/**
 * Handles unit path finding and movement
 */
public class UnitMover implements Updatable, MovementListener, MovementRequestHandler, PathInfoProvider {

    // the unit groups that are being moved
    protected Set<UnitGroup> groups = new HashSet<UnitGroup>();

    // the units that have arrived at their destination and need to be removed from the unit
    // list (done to avoid concurrent modification exception)
    protected List<Unit> unitsToRemove = new ArrayList<Unit>();

    // the unit groups in which all units have stopped moving and the group needs
    // to be removed (done to avoid concurrent modification exception)
    protected List<UnitGroup> groupsToRemove = new ArrayList<UnitGroup>();

    // used to find paths for units
    protected PathFinderInterface pathFinder;

    // units' movement states, if the value is true that means the unit is moving
    protected Map<Unit, Boolean> movementStates = new HashMap<Unit, Boolean>();

    // the game's map
    protected BlockMap map;

    // has any unit in a group of units moved
    protected boolean anyGroupUnitMoved;

    // used to temporarily store units' distances to the destination point
    protected TreeSet<UnitDistance> unitDistances = new TreeSet<UnitDistance>();

    /**
     * Default class constructor
     */
    public UnitMover(BlockMap map, PathFinderInterface pathFinder) {
        this.map = map;
        this.pathFinder = pathFinder;
    }

    /**
     * Gets unit's final destination
     *
     * @param unit unit to get the destination for
     * @return
     */
    @Override
    public Point getFinalDestination(Unit unit) {
        return pathFinder.getFinalDestination(unit);
    }

    /**
     * Gets unit's next path point
     *
     * @param unit unit to get the next point for
     * @return
     */
    @Override
    public Point getNextPathPoint(Unit unit) {
        return pathFinder.getNextPathPointForObject(unit);
    }

    /**
     * Handles a unit's movement request
     *
     * @param unit the unit that requested to be moved
     * @param x    destination x
     * @param y    destination y
     */
    @Override
    public void handleMovementRequest(Unit unit, short x, short y) {
        Set<Unit> units = new HashSet<Unit>();
        units.add(unit);

        moveUnits(units, x, y);
    }

    /**
     * Moves the given units to the specified block on the map
     *
     * @param units units to move
     * @param x x of the block in block map coordinates
     * @param y y of the block in block map coordinates
     */
    public void moveUnits(Set<Unit> units, short x, short y) {
        UnitGroup group = createUnitGroup(units, x, y);

        this.groups.add(group);

        if (!group.units.isEmpty()) {
            pathFinder.newGroup(group.units);
        }

        calculateDestinationsAndMoveUnits(group, x, y);

        addMovementListeners(units);
        initializeMovementStates(units);
    }

    /**
     * Calculates each group unit's individual destination point and moves all units
     *
     * @param group unit group to move
     * @param destX destination x
     * @param destY destination y
     */
    protected void calculateDestinationsAndMoveUnits(UnitGroup group, short destX, short destY) {
        if (group.units.isEmpty()) {
            return;
        }

        calculateUnitDistances(group.units, destX, destY);

        short currentIterationSize = 3;

        /*
         * The following code might appear ugly to some people,
         *
         * READER DISCRETION IS ADVISED
         */

        pathFinder.findPathToObject(unitDistances.pollFirst().unit, destX, destY);

        while (!unitDistances.isEmpty()) {
            // loop through the top side
            for (int x = destX - (short) (currentIterationSize / 2); x <= destX + (short) (currentIterationSize / 2); x++) {
                if (!unitDistances.isEmpty()) {
                    pathFinder.findPathToObject(unitDistances.pollFirst().unit, (short) x, (short) (destY + (short) (currentIterationSize / 2)));
                } else {
                    break;
                }
            }

            // loop through the bottom side
            for (int x = destX - (short) (currentIterationSize / 2); x <= destX + (short) (currentIterationSize / 2); x++) {
                if (!unitDistances.isEmpty()) {
                    pathFinder.findPathToObject(unitDistances.pollFirst().unit, (short) x, (short) (destY - (short) (currentIterationSize / 2)));
                } else {
                    break;
                }
            }

            // loop through the left side
            for (int y = destY - (short) ((currentIterationSize - 1) / 2); y <= destY + (short) ((currentIterationSize - 1) / 2); y++) {
                if (!unitDistances.isEmpty()) {
                    pathFinder.findPathToObject(unitDistances.pollFirst().unit, (short) (destX - (short) (currentIterationSize / 2)), (short) y);
                } else {
                    break;
                }
            }

            // loop through the right side
            for (int y = destY - (short) ((currentIterationSize - 1) / 2); y <= destY + (short) ((currentIterationSize - 1) / 2); y++) {
                if (!unitDistances.isEmpty()) {
                    pathFinder.findPathToObject(unitDistances.pollFirst().unit, (short) (destX + (short) (currentIterationSize / 2)), (short) y);
                } else {
                    break;
                }
            }

            currentIterationSize += 2;
        }
    }

    /**
     * Calculates each unit's distance to the destination point
     *
     * @param units units to calculate distance for
     * @param x destination x
     * @param y destination y
     */
    protected void calculateUnitDistances(Set<Unit> units, short x, short y) {
        for (Unit unit: units) {
            UnitDistance distance = new UnitDistance();
            distance.unit = unit;
            distance.distance = MathUtils.distance((short) (unit.getCenterX() / Block.BLOCK_WIDTH), x, (short) (unit.getCenterY() / Block.BLOCK_HEIGHT), y);
            unitDistances.add(distance);
        }
    }

    /**
     * Creates a unit group from the given units
     *
     * @param units units to form the group from
     * @param x destination x
     * @param y destination y
     * @return
     */
    protected UnitGroup createUnitGroup(Set<Unit> units, short x, short y) {
        UnitGroup group = new UnitGroup();

        for (Unit unit: units) {
            removeUnitFromAllGroups(unit);

            if (!unit.isInSiegeMode()) {
                group.units.add(unit);
            } else {
                unit.setInSiegeMode(false);
                unit.setPointToGoToAfterLeavingSiegeMode(new Point(x, y));
            }
        }

        return group;
    }

    /**
     * Removes the given unit from all unit groups that it may be a part of
     *
     * @param unit unit to remove
     */
    protected void removeUnitFromAllGroups(Unit unit) {
        for (UnitGroup group: groups) {
            if (group.units.contains(unit)) {
                group.units.remove(unit);
                pathFinder.removePathForObject(unit);
                movementStates.remove(unit);
            }
        }
    }

    /**
     * Initializes units' movement states
     *
     * @param units units for which the state will be initialized
     */
    protected void initializeMovementStates(Set<Unit> units) {
        for (Unit unit: units) {
            if (!movementStates.containsKey(unit)) {
                movementStates.put(unit, false);
            }
        }
    }

    /**
     * Adds movement listener to all units
     *
     * @param units units to which the listeners will be added
     */
    protected void addMovementListeners(Set<Unit> units) {
        for (Unit unit: units) {
            unit.addMovementListener(this);
        }
    }

    /**
     * Called when a unit starts moving
     *
     * @param unit the unit that just started moving
     */
    @Override
    public void startedMoving(Unit unit) {
        movementStates.put(unit, true);
        pathFinder.removeNextPathPoint(unit);
    }

    /**
     * Called when a unit is unable to move anymore in it's current state (for example,
     * the unit has entered siege mode before reaching it's destination)
     *
     * @param unit unit that is unable to move
     */
    @Override
    public void unableToMoveInCurrentState(Unit unit) {
        stopUnit(unit);
    }

    /**
     * Stops a unit's movement
     *
     * @param unit unit to stop
     */
    public void stopUnit(Unit unit) {
        for (UnitGroup group: groups) {
            if (group.units.contains(unit)) {
                pathFinder.removePathForObject(unit);
                movementStates.remove(unit);
            }
        }
    }

    /**
     * Called when a unit reaches it's destination
     *
     * @param unit the unit that just arrived at it's destination
     */
    @Override
    public void stoppedMoving(Unit unit) {
        movementStates.put(unit, false);
    }

    /**
     * Updates the state of the object
     *
     * @param delta time elapsed since the last update
     */
    @Override
    public void update(float delta) {
        for (UnitGroup group: groups) {
            anyGroupUnitMoved = false;

            for (Unit unit : group.units) {
                if (movementStates.containsKey(unit)) {
                    if (!movementStates.get(unit)) {
                        Point nextPathPoint = pathFinder.getNextPathPointForObject(unit);

                        if (nextPathPoint != null && !unit.isRotating()) {
                            GameObject occupyingObject = map.getOccupyingObject((short) nextPathPoint.x, (short) nextPathPoint.y);
                            Unit occupyingUnit = occupyingObject instanceof Unit ? (Unit) occupyingObject : null;

                            if (occupyingObject == null || occupyingUnit == unit) {
                                unit.move(CombatUtils.getFacingDirection(unit.getCenterX(), unit.getCenterY(), nextPathPoint.x * Block.BLOCK_WIDTH + Block.BLOCK_WIDTH / 2f, nextPathPoint.y * Block.BLOCK_HEIGHT + Block.BLOCK_HEIGHT / 2f));
                            } else if (occupyingUnit == null || (!occupyingUnit.isMoving() && !group.units.contains(occupyingUnit))) {
                                pathFinder.refindPathToObject(unit);
                                anyGroupUnitMoved = true;
                            }
                        } else if (nextPathPoint == null) {
                            // the unit has arrived at it's destination and needs to be removed
                            unitsToRemove.add(unit);
                        }

                        if (unit.isRotating() || unit.isMoving()) {
                            anyGroupUnitMoved = true;
                        }
                    } else {
                        anyGroupUnitMoved = true;
                    }
                }
            }

            if (unitsToRemove.size() > 0) {
                for (Unit unit : unitsToRemove) {
                    group.units.remove(unit);
                    unit.removeMovementListener(this);
                    movementStates.remove(unit);
                    pathFinder.removePathForObject(unit);
                }

                unitsToRemove.clear();
            }

            if (!anyGroupUnitMoved && group.units.size() > 1) {
                groupsToRemove.add(group);
            } else if (group.units.isEmpty()) {
                groupsToRemove.add(group);
            }
        }

        if (groupsToRemove.size() > 0) {
            for (UnitGroup group: groupsToRemove) {
                for (Unit unit: group.units) {
                    pathFinder.removePathForObject(unit);
                    movementStates.remove(unit);
                    unit.removeMovementListener(this);
                }

                group.units.clear();
                groups.remove(group);
            }

            groupsToRemove.clear();
        }
    }

    /**
     * A group of moving units
     */
    protected class UnitGroup {

        protected Set<Unit> units = new HashSet<Unit>();
    }

    /**
     * Holds units and their distance to the destination point
     */
    protected class UnitDistance implements Comparable<UnitDistance> {

        protected Unit unit;
        protected float distance;

        @Override
        public int compareTo(UnitDistance other) {
            if (unit == other.unit) {
                return 0;
            } else {
                if (distance < other.distance) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
    }
}
