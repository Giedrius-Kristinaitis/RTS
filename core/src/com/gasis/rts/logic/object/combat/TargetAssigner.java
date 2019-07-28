package com.gasis.rts.logic.object.combat;

import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.building.Building;
import com.gasis.rts.logic.object.building.BuildingConstructionListener;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.logic.object.unit.movement.MovementAdapter;
import com.gasis.rts.logic.player.controls.BuildingPlacementListener;

/**
 * Finds and assigns targets to offensive game objects
 */
public class TargetAssigner extends MovementAdapter implements BuildingPlacementListener, BuildingConstructionListener, TargetRemovalListener {

    /**
     * Called when a building gets placed
     *
     * @param building the building that was just placed
     */
    @Override
    public void buildingPlaced(Building building) {
        
    }

    /**
     * Called when a building gets constructed
     *
     * @param building the building that just got constructed
     */
    @Override
    public void buildingConstructed(Building building) {

    }

    /**
     * Called when a game object gets it's target removed
     *
     * @param object object that got the target removed
     */
    @Override
    public void targetRemoved(GameObject object) {

    }

    /**
     * Called when a unit starts moving
     *
     * @param unit the unit that just started moving
     */
    @Override
    public void startedMoving(Unit unit) {

    }
}
