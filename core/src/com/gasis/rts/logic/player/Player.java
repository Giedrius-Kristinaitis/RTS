package com.gasis.rts.logic.player;

import com.badlogic.gdx.files.FileHandle;
import com.gasis.rts.logic.Updatable;
import com.gasis.rts.logic.faction.Faction;
import com.gasis.rts.logic.faction.FactionLoader;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.building.Building;
import com.gasis.rts.logic.object.building.ElectricityListener;
import com.gasis.rts.logic.object.building.Landmine;
import com.gasis.rts.logic.object.building.OffensiveBuilding;
import com.gasis.rts.logic.object.combat.DestructionHandler;
import com.gasis.rts.logic.object.combat.DestructionListener;
import com.gasis.rts.logic.object.combat.TargetAssigner;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.logic.object.unit.movement.UnitMover;
import com.gasis.rts.logic.pathfinding.PathFinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents any player of the game: human or AI
 */
public class Player implements DestructionListener, Updatable, ElectricityListener {

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

    // ids of allied players
    protected List<Player> allies = new ArrayList<Player>();

    // assigns targets to offensive objects
    protected TargetAssigner targetAssigner;

    // moves units
    protected UnitMover unitMover;

    // all techs researched by the player
    protected Set<String> researchedTechs = new HashSet<String>();

    /**
     * Default class constructor
     */
    public Player(DestructionHandler destructionHandler, TargetAssigner targetAssigner, BlockMap map) {
        this.destructionHandler = destructionHandler;
        this.targetAssigner = targetAssigner;

        unitMover = new UnitMover(map, new PathFinder(map));
    }

    /**
     * Adds a researched tech
     *
     * @param techId tech id
     */
    public void addResearchedTech(String techId) {
        researchedTechs.add(techId);

        for (Unit unit: units) {
            unit.techResearched(this, techId);
        }

        for (Building building: buildings) {
            building.techResearched(this, techId);
        }
    }

    /**
     * Checks if a tech is researched
     *
     * @param id tech id
     * @return
     */
    public boolean isTechResearched(String id) {
        return researchedTechs.contains(id);
    }

    /**
     * Gets the unit mover used by the player
     * @return
     */
    public UnitMover getUnitMover() {
        return unitMover;
    }

    /**
     * Adds an ally to the player
     *
     * @param player new ally
     */
    public void addAlly(Player player) {
        if (!allies.contains(player)) {
            allies.add(player);
        }
    }

    /**
     * Removes an ally from the player
     *
     * @param player ally to remove
     */
    public void removeAlly(Player player) {
        allies.remove(player);
    }

    /**
     * Checks if the player is allied to another player
     *
     * @param player player to check for alliance
     * @return
     */
    public boolean isAllied(Player player) {
        if (player == this) {
            return true;
        }

        return allies.contains(player);
    }

    /**
     * Called when a game object gets destroyed
     *
     * @param object the destroyed object
     */
    @Override
    public void objectDestroyed(GameObject object) {
        if (object instanceof Unit) {
            if (!unitsToRemove.contains(object)) {
                unitsToRemove.add((Unit) object);
            }
        } else if (object instanceof Building) {
            if (!buildingsToRemove.contains(object)) {
                buildingsToRemove.add((Building) object);
            }
        }

        object.deoccupyBlocks();
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
        unit.addMovementListener(targetAssigner);
        unit.addTargetRemovalListener(targetAssigner);
        unit.addSiegeModeListener(targetAssigner);
        unit.setMovementRequestHandler(unitMover);
        unit.setPathInfoProvider(unitMover);

        for (String tech: researchedTechs) {
            unit.techResearched(this, tech);
        }

        units.add(unit);

        state.units++;
    }

    /**
     * Adds a new building to the player's owned buildings
     *
     * @param building the building to add
     */
    public void addBuilding(Building building) {
        if (building instanceof OffensiveBuilding) {
            ((OffensiveBuilding) building).addTargetReachedListener(destructionHandler);
            ((OffensiveBuilding) building).addTargetRemovalListener(targetAssigner);

            if (building instanceof Landmine) {
                ((Landmine) building).addListener(destructionHandler);
            }
        }

        building.addDestructionListener(this);
        building.addConstructionListener(targetAssigner);
        building.addUnitProductionListener(targetAssigner);

        for (String tech: researchedTechs) {
            building.techResearched(this, tech);
        }

        buildings.add(building);

        state.buildings++;
        state.requiredElectricity += building.getElectricityRequirement();

        electricityGained(0);
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
                units.remove(unitsToRemove.get(i));
                unitsToRemove.remove(i--);
                state.units--;
            }
        }
    }

    /**
     * Removes buildings that need to be removed
     */
    protected void removeBuildings() {
        for (int i = 0; i < buildingsToRemove.size(); i++) {
            if (buildingsToRemove.get(i).canBeRemoved()) {
                state.buildings--;
                state.requiredElectricity -= buildingsToRemove.get(i).getElectricityRequirement();

                if (buildingsToRemove.get(i).isElectricityAvailable()) {
                    state.usedElectricity -= buildingsToRemove.get(i).getElectricityRequirement();
                    buildingsToRemove.get(i).setElectricityAvailable(false);
                }

                buildings.remove(buildingsToRemove.get(i));
                buildingsToRemove.get(i).removeDestructionListener(this);
                buildingsToRemove.remove(i);
                i--;

                electricityGained(0);
            }
        }
    }

    /**
     * Called when electricity is gained
     *
     * @param amount amount of gained electricity
     */
    @Override
    public void electricityGained(int amount) {
        for (Building building: buildings) {
            if (!building.isElectricityAvailable() && state.availableElectricity - state.usedElectricity >= building.getElectricityRequirement()) {
                state.usedElectricity += building.getElectricityRequirement();
                building.setElectricityAvailable(true);
            }
        }
    }

    /**
     * Called when electricity is lost
     *
     * @param amount amount of lost electricity
     */
    @Override
    public void electricityLost(int amount) {
        for (Building building: buildings) {
            if (building.isElectricityAvailable() && state.availableElectricity < state.usedElectricity) {
                state.usedElectricity -= building.getElectricityRequirement();
                building.setElectricityAvailable(false);
            }
        }
    }
}
