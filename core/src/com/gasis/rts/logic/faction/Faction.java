package com.gasis.rts.logic.faction;

import com.gasis.rts.logic.object.building.BuildingLoader;
import com.gasis.rts.logic.object.unit.UnitLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * A playable faction
 */
public class Faction {

    // name of the faction
    protected String name;

    // unit loaders to load and create units for this player
    protected List<UnitLoader> unitLoaders = new ArrayList<UnitLoader>();

    // building loaders to load and create buildings for this player
    protected List<BuildingLoader> buildingLoaders = new ArrayList<BuildingLoader>();

    /**
     * Sets the unit loaders for the faction
     *
     * @param unitLoaders unit loader list
     */
    public void setUnitLoaders(List<UnitLoader> unitLoaders) {
        this.unitLoaders = unitLoaders;
    }

    /**
     * Sets the building loaders for the faction
     *
     * @param buildingLoaders building loader list
     */
    public void setBuildingLoaders(List<BuildingLoader> buildingLoaders) {
        this.buildingLoaders = buildingLoaders;
    }

    /**
     * Gets the unit loaders of the faction
     * @return
     */
    public Iterable<UnitLoader> getUnitLoaders() {
        return unitLoaders;
    }

    /**
     * Gets the building loaders of the faction
     * @return
     */
    public Iterable<BuildingLoader> getBuildingLoaders() {
        return buildingLoaders;
    }

    /**
     * Sets the name of the faction
     *
     * @param name faction's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the faction
     * @return
     */
    public String getName() {
        return name;
    }
}
