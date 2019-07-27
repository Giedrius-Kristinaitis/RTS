package com.gasis.rts.logic.object.combat;

import com.gasis.rts.logic.object.building.Building;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.logic.object.unit.movement.MovementAdapter;
import com.gasis.rts.logic.player.controls.BuildingPlacementListener;

/**
 * Finds and assigns targets to offensive game objects
 */
public class TargetAssigner extends MovementAdapter implements BuildingPlacementListener {

    /**
     * Called when a building gets placed
     *
     * @param building the building that was just placed
     */
    @Override
    public void buildingPlaced(Building building) {

    }

    /**
     * Called when a unit starts moving
     *
     * @param unit the unit that just started moving
     */
    @Override
    public void startedMoving(Unit unit) {

    }

    /**
     * Called when a unit reaches it's destination or stops moving for other reasons
     *
     * @param unit the unit that just arrived at it's destination
     */
    @Override
    public void stoppedMoving(Unit unit) {

    }
}
