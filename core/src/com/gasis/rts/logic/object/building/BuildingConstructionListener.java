package com.gasis.rts.logic.object.building;

/**
 * Listens for building construction events
 */
public interface BuildingConstructionListener {

    /**
     * Called when a building gets constructed
     *
     * @param building the building that just got constructed
     */
    void buildingConstructed(Building building);
}
