package com.gasis.rts.logic.object.unit.movement;

import com.gasis.rts.logic.Updatable;
import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.combat.CombatUtils;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.logic.pathfinding.PathFinderInterface;
import com.gasis.rts.math.Point;

import java.util.*;

/**
 * Handles unit path finding and movement
 */
public class UnitMover implements Updatable, MovementListener {

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

    /**
     * Default class constructor
     */
    public UnitMover(BlockMap map, PathFinderInterface pathFinder) {
        this.map = map;
        this.pathFinder = pathFinder;
    }

    /**
     * Moves the given units to the specified block on the map
     *
     * @param units units to move
     * @param x x of the block in block map coordinates
     * @param y y of the block in block map coordinates
     */
    public void moveUnits(List<Unit> units, short x, short y) {
        removeSiegeModeUnits(units);

        this.groups.add(createUnitGroup(units));

        pathFinder.findPathsToObjects(units, x, y);

        addMovementListeners(units);
        initializeMovementStates(units);
    }

    /**
     * Removes units that are in siege mode from the list
     *
     * @param units units to check
     */
    protected void removeSiegeModeUnits(List<Unit> units) {
        for (int i = 0; i < units.size(); i++) {
            if (units.get(i).isInSiegeMode()) {
                units.remove(i);
                i--;
            }
        }
    }

    /**
     * Creates a unit group from the given units
     *
     * @param units units to form the group from
     * @return
     */
    protected UnitGroup createUnitGroup(List<Unit> units) {
        UnitGroup group = new UnitGroup();

        for (Unit unit: units) {
            removeUnitFromAllGroups(unit);
            group.units.add(unit);
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
            group.units.remove(unit);
        }
    }

    /**
     * Initializes units' movement states
     *
     * @param units units for which the state will be initialized
     */
    protected void initializeMovementStates(List<Unit> units) {
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
    protected void addMovementListeners(List<Unit> units) {
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
    }

    /**
     * Called when a unit is unable to move anymore in it's current state (for example,
     * the unit has entered siege mode before reaching it's destination)
     *
     * @param unit unit that is unable to move
     */
    @Override
    public void unableToMoveInCurrentState(Unit unit) {
        for (UnitGroup group: groups) {
            if (group.units.contains(unit)) {
                pathFinder.removePathForObject(unit);
                group.units.remove(unit);
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

                        if (nextPathPoint != null && !map.isBlockOccupied((short) nextPathPoint.x, (short) nextPathPoint.y)) {
                            unit.move(CombatUtils.getFacingDirection(unit.getCenterX(), unit.getCenterY(), nextPathPoint.x * Block.BLOCK_WIDTH + Block.BLOCK_WIDTH / 2f, nextPathPoint.y * Block.BLOCK_HEIGHT + Block.BLOCK_HEIGHT / 2f));
                            pathFinder.removeNextPathPoint(unit);
                            anyGroupUnitMoved = true;
                        } else if (nextPathPoint == null) {
                            // the unit has arrived at it's destination and needs to be removed
                            unitsToRemove.add(unit);
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

            if (!anyGroupUnitMoved) {
                groupsToRemove.add(group);
            }
        }

        if (groupsToRemove.size() > 0) {
            for (UnitGroup group: groupsToRemove) {
                for (Unit unit: group.units) {
                    pathFinder.removePathForObject(unit);
                    movementStates.remove(unit);
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
}
