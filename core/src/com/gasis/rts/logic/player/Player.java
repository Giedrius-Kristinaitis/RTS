package com.gasis.rts.logic.player;

import com.gasis.rts.logic.object.building.Building;
import com.gasis.rts.logic.object.unit.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents any player of the game: human or AI
 */
public abstract class Player {

    // the current state of the player
    protected PlayerState state = new PlayerState();

    // all of the units the player currently owns
    protected List<Unit> units = new ArrayList<Unit>();

    // all of the buildings the player currently owns
    protected List<Building> buildings = new ArrayList<Building>();

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
}
