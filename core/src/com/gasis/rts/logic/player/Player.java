package com.gasis.rts.logic.player;

import com.badlogic.gdx.Gdx;
import com.gasis.rts.logic.faction.Faction;
import com.gasis.rts.logic.faction.FactionLoader;
import com.gasis.rts.logic.object.building.Building;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.utils.Constants;

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

    /**
     * Initializes the player's data with the given faction
     *
     * @param factionName name of the faction file
     */
    public void initialize(String factionName) {
        FactionLoader loader = new FactionLoader();
        loader.load(Gdx.files.internal(Constants.FOLDER_FACTIONS + factionName));

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
    public Iterable<Unit> getUnits() {
        return units;
    }

    /**
     * Gets the buildings that the player owns
     * @return
     */
    public Iterable<Building> getBuildings() {
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
