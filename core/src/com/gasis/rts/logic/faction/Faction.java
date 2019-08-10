package com.gasis.rts.logic.faction;

import com.gasis.rts.logic.object.building.BuildingLoader;
import com.gasis.rts.logic.object.unit.UnitLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A playable faction
 */
public class Faction {

    // name of the faction
    protected String name;

    // unit loaders to load and create units for this player
    protected Map<String, UnitLoader> unitLoaders = new HashMap<String, UnitLoader>();

    // building loaders to load and create buildings for this player
    protected Map<String, BuildingLoader> buildingLoaders = new HashMap<String, BuildingLoader>();

    // the name of the default control context for this faction
    protected String defaultControlContextName;

    // factions music
    protected List<String> soundtrack = new ArrayList<String>();

    /**
     * Gets the name of the default control context
     * @return
     */
    public String getDefaultControlContextName() {
        return defaultControlContextName;
    }

    /**
     * Sets the name of the default control context
     *
     * @param defaultControlContextName default context name
     */
    public void setDefaultControlContextName(String defaultControlContextName) {
        this.defaultControlContextName = defaultControlContextName;
    }

    /**
     * Sets the unit loaders for the faction
     *
     * @param unitLoaders unit loader list
     */
    public void setUnitLoaders(Map<String, UnitLoader> unitLoaders) {
        this.unitLoaders = unitLoaders;
    }

    /**
     * Sets the building loaders for the faction
     *
     * @param buildingLoaders building loader list
     */
    public void setBuildingLoaders(Map<String, BuildingLoader> buildingLoaders) {
        this.buildingLoaders = buildingLoaders;
    }

    /**
     * Gets the unit loaders of the faction
     * @return
     */
    public Map<String, UnitLoader> getUnitLoaders() {
        return unitLoaders;
    }

    /**
     * Gets the building loaders of the faction
     * @return
     */
    public Map<String, BuildingLoader> getBuildingLoaders() {
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

    /**
     * Gets the faction's soundtract
     * @return
     */
    public List<String> getSoundtrack() {
        return soundtrack;
    }

    /**
     * Sets the faction's soundtrack
     *
     * @param soundtrack new soundtrack
     */
    public void setSoundtrack(List<String> soundtrack) {
        this.soundtrack = soundtrack;
    }
}
