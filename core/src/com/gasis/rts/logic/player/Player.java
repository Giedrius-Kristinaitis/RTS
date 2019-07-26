package com.gasis.rts.logic.player;

import com.badlogic.gdx.files.FileHandle;
import com.gasis.rts.logic.Updatable;
import com.gasis.rts.logic.faction.Faction;
import com.gasis.rts.logic.faction.FactionLoader;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.building.Building;
import com.gasis.rts.logic.object.building.OffensiveBuilding;
import com.gasis.rts.logic.object.combat.DestructionHandler;
import com.gasis.rts.logic.object.combat.DestructionListener;
import com.gasis.rts.logic.object.unit.Unit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents any player of the game: human or AI
 */
public class Player implements DestructionListener, Updatable {

    // unique identifier
    protected Long id;

    // the current state of the player
    protected PlayerState state = new PlayerState();

    // all of the units the player currently owns
    protected Set<Unit> units = new HashSet<Unit>();

    // all of the buildings the player currently owns
    protected Set<Building> buildings = new HashSet<Building>();

    // faction the player is controlling
    protected Faction faction;

    // the building that is currently selected
    protected Building selectedBuilding;

    // the units that are currently selected
    protected Set<Unit> selectedUnits;

    // handles game object destruction
    protected DestructionHandler destructionHandler;

    // all the units that have been destroyed and need to be removed
    protected List<Unit> unitsToRemove = new ArrayList<Unit>();

    // all the buildings that have been destroyed and need to be removed
    protected List<Building> buildingsToRemove = new ArrayList<Building>();

    /**
     * Default class constructor
     */
    public Player(DestructionHandler destructionHandler) {
        this.destructionHandler = destructionHandler;
    }

    /**
     * Called when a game object gets destroyed
     *
     * @param object the destroyed object
     */
    @Override
    public void objectDestroyed(GameObject object) {
        if (object instanceof Unit) {
            unitsToRemove.add((Unit) object);
        } else if (object instanceof Building) {
            buildingsToRemove.add((Building) object);
        }
    }

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
    public void setSelectedUnits(Set<Unit> units) {
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
    public Set<Unit> getSelectedUnits() {
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
        unit.addTargetReachedListener(destructionHandler);
        unit.addDestructionListener(this);
        units.add(unit);
    }

    /**
     * Adds a new building to the player's owned buildings
     *
     * @param building the building to add
     */
    public void addBuilding(Building building) {
        if (building instanceof OffensiveBuilding) {
            ((OffensiveBuilding) building).addTargetReachedListener(destructionHandler);
        }

        building.addDestructionListener(this);

        buildings.add(building);
    }

    /**
     * Gets the units that the player owns
     * @return
     */
    public Set<Unit> getUnits() {
        return units;
    }

    /**
     * Gets the buildings that the player owns
     * @return
     */
    public Set<Building> getBuildings() {
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

    /**
     * Updates the state of the object
     *
     * @param delta time elapsed since the last update
     */
    @Override
    public void update(float delta) {
        if (unitsToRemove.size() > 0) {
            removeUnits();
        }

        if (buildingsToRemove.size() > 0) {
            removeBuildings();
        }
    }

    /**
     * Removes units that need to be removed
     */
    protected void removeUnits() {
        for (int i = 0; i < unitsToRemove.size(); i++) {
            if (unitsToRemove.get(i).canBeRemoved()) {
                unitsToRemove.get(i).removeDestructionListener(this);
                unitsToRemove.remove(i--);
            }
        }
    }

    /**
     * Removes buildings that need to be removed
     */
    protected void removeBuildings() {
        for (int i = 0; i < buildingsToRemove.size(); i++) {
            if (buildingsToRemove.get(i).canBeRemoved()) {
                buildingsToRemove.get(i).removeDestructionListener(this);
                buildingsToRemove.remove(i);
                i--;
            }
        }
    }
}
