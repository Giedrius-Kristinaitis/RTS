package com.gasis.rts.logic.player;

import com.badlogic.gdx.files.FileHandle;
import com.gasis.rts.logic.faction.Faction;
import com.gasis.rts.logic.faction.FactionLoader;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.building.Building;
import com.gasis.rts.logic.object.unit.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents any player of the game: human or AI
 */
public class Player {

    // unique identifier
    protected Long id;

    // the current state of the player
    protected PlayerState state = new PlayerState();

    // all of the units the player currently owns
    protected List<Unit> units = new ArrayList<Unit>();

    // all of the buildings the player currently owns
    protected List<Building> buildings = new ArrayList<Building>();

    // faction the player is controlling
    protected Faction faction;

    // the building that is currently selected
    protected Building selectedBuilding;

    // the units that are currently selected
    protected List<Unit> selectedUnits;

    /**
     * Sets the currently selected building
     *
     * @param building selected building
     */
    public void setSelectedBuilding(Building building) {
        this.selectedBuilding = building;
    }

    /**
     * Sets the currently selected units
     *
     * @param units selected units
     */
    public void setSelectedUnits(List<Unit> units) {
        this.selectedUnits = units;
    }

    /**
     * Gets the building that is currently selected
     * @return
     */
    public Building getSelectedBuilding() {
        return selectedBuilding;
    }

    /**
     * Gets the units that are currently selected
     * @return
     */
    public List<Unit> getSelectedUnits() {
        return selectedUnits;
    }

    /**
     * Initializes the player's data with the given faction
     *
     * @param factionFile the faction file
     * @param map the game's map
     */
    public void initialize(FileHandle factionFile, BlockMap map) {
        FactionLoader loader = new FactionLoader();
        loader.load(factionFile, map);

        faction = loader.createInstance();
    }

    /**
     * Adds a new unit to the player's owned units
     *
     * @param unit the unit to add
     */
    public void addUnit(Unit unit) {
        units.add(unit);
    }

    /**
     * Adds a new building to the player's owned buildings
     *
     * @param building the building to add
     */
    public void addBuilding(Building building) {
        buildings.add(building);
    }

    /**
     * Gets the units that the player owns
     * @return
     */
    public List<Unit> getUnits() {
        return units;
    }

    /**
     * Gets the buildings that the player owns
     * @return
     */
    public List<Building> getBuildings() {
        return buildings;
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
    public Long getId() {
        return id;
    }

    /**
     * Sets the player's id
     *
     * @param id new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the player's faction
     * @return
     */
    public Faction getFaction() {
        return faction;
    }
}
