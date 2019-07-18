package com.gasis.rts.logic.object.unit.movement;

import com.gasis.rts.logic.Updatable;
import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.object.combat.CombatUtils;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.logic.pathfinding.PathFinderInterface;
import com.gasis.rts.math.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles unit path finding and movement
 */
public class UnitMover implements Updatable, MovementListener {

    // the units that are being moved
    protected List<Unit> units = new ArrayList<Unit>();

    // the units that have arrived at their destination and need to be removed from the unit
    // list above (done to avoid concurrent modification exception)
    protected List<Unit> unitsToRemove = new ArrayList<Unit>();

    // the destination of the moved units
    protected short destinationX;
    protected short destinationY;

    // used to find paths for units
    protected PathFinderInterface pathFinder;

    // units' movement states, if the value is true that means the unit is moving
    protected Map<Unit, Boolean> movementStates = new HashMap<Unit, Boolean>();

    /**
     * Default class constructor
     *
     * @param pathFinder
     */
    public UnitMover(PathFinderInterface pathFinder) {
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
        this.units.addAll(units);

        destinationX = x;
        destinationY = y;

        pathFinder.findPathsToObjects(units, x, y);

        addMovementListeners(units);
        initializeMovementStates(units);
    }

    /**
     * Initializes units' movement states
     *
     * @param units units for which the state will be initialized
     */
    protected void initializeMovementStates(List<Unit> units) {
        for (Unit unit: units) {
            movementStates.put(unit, false);
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
     * Stops moving units
     */
    public void stopMovingUnits() {
        units = null;
        movementStates.clear();
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
     * Called when a unit reaches it's destination
     *
     * @param unit the unit that just arrived at it's destination
     */
    @Override
    public void destinationReached(Unit unit) {
        movementStates.put(unit, false);
    }

    /**
     * Updates the state of the object
     *
     * @param delta time elapsed since the last update
     */
    @Override
    public void update(float delta) {
        if (units != null) {
            for (Unit unit: units) {
                if (movementStates.containsKey(unit)) {
                    if (!movementStates.get(unit)) {
                        Point nextPathPoint = pathFinder.getNextPathPointForObject(unit);

                        if (nextPathPoint != null) {
                            unit.move(CombatUtils.getFacingDirection(unit.getCenterX(), unit.getCenterY(), nextPathPoint.x * Block.BLOCK_WIDTH + Block.BLOCK_WIDTH / 2f, nextPathPoint.y * Block.BLOCK_HEIGHT + Block.BLOCK_HEIGHT / 2f));
                        } else {
                            // the unit has arrived at it's destination and needs to be removed
                            unitsToRemove.add(unit);
                        }
                    }
                }
            }

            if (unitsToRemove.size() > 0) {
                for (Unit unit : unitsToRemove) {
                    units.remove(unit);
                    unit.removeMovementListener(this);
                    movementStates.remove(unit);
                }

                unitsToRemove.clear();
            }
        }
    }
}
