package com.gasis.rts.logic.player.exploration;

import com.gasis.rts.logic.object.building.Building;
import com.gasis.rts.logic.object.building.BuildingConstructionListener;
import com.gasis.rts.logic.object.production.UnitProductionListener;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.logic.object.unit.movement.MovementAdapter;
import com.gasis.rts.logic.player.controls.BuildingPlacementListener;

/**
 * Manages player's exploration data
 */
public class ExplorationDataManager extends MovementAdapter implements BuildingConstructionListener, UnitProductionListener {

    // exploration data to manage
    protected ExplorationDataInterface explorationData;

    /**
     * Sets exploration data to manage
     *
     * @param explorationData
     */
    public void setExplorationData(ExplorationDataInterface explorationData) {
        this.explorationData = explorationData;
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
     * Called when a unit gets produced
     *
     * @param unit the unit that was just produced
     */
    @Override
    public void unitProduced(Unit unit) {

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
