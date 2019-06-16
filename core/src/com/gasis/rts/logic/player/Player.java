package com.gasis.rts.logic.player;

import com.gasis.rts.logic.object.building.Building;
import com.gasis.rts.logic.object.building.BuildingLoader;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.logic.object.unit.UnitLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents any player of the game: human or AI
 */
public abstract class Player {

    // unique identifier
    protected long id;

    // the current state of the player
    protected PlayerState state = new PlayerState();

    // all of the units the player currently owns
    protected List<Unit> units = new ArrayList<Unit>();

    // all of the buildings the player currently owns
    protected List<Building> buildings = new ArrayList<Building>();

    // unit loaders to load and create units for this player
    protected List<UnitLoader> unitLoaders = new ArrayList<UnitLoader>();

    // building loaders to load and create buildings for this player
    protected List<BuildingLoader> buildingLoaders = new ArrayList<BuildingLoader>();

    /**
     * Adds a new unit to the player's owned units
     *
     * @param code code of the unit
     */
    public void addUnit(String code) {

    }

    /**
     * Adds a new building to the player's owned buildings
     *
     * @param code code of the building
     */
    public void addBuilding(String code) {

    }

    /**
     * Gets the unit loaders of the player
     * @return
     */
    public Iterable<UnitLoader> getUnitLoaders() {
        return unitLoaders;
    }

    /**
     * Gets the building loaders of the player
     * @return
     */
    public Iterable<BuildingLoader> getBuildingLoaders() {
        return buildingLoaders;
    }

    /**
     * Gets the current state
     * @return
     */
    public PlayerState getState() {
        return state;
    }

    /**
     * Sets the current state
     *
     * @param state new state
     */
    public void setState(PlayerState state) {
        this.state = state;
    }

    /**
     * Gets the player's id
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the player's id
     *
     * @param id new id
     */
    public void setId(long id) {
        this.id = id;
    }
}
