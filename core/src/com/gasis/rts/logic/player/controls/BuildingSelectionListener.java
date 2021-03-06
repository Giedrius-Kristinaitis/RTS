package com.gasis.rts.logic.player.controls;

import com.gasis.rts.logic.object.building.Building;

/**
 * Listens for building selection events
 */
public interface BuildingSelectionListener {

    /**
     * Called when a building gets selected
     *
     * @param building the selected building
     */
    void buildingSelected(Building building);

    /**
     * Called when the selected building gets deselected
     */
    void buildingDeselected();
}
