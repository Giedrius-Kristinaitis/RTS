package com.gasis.rts.logic.player.controls;

import com.gasis.rts.logic.object.building.Building;

/**
 * Listens for building placement events
 */
public interface BuildingPlacementListener {

    /**
     * Called when a building gets placed
     *
     * @param building the building that was just placed
     */
    void buildingPlaced(Building building);
}
