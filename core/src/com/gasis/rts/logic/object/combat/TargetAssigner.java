package com.gasis.rts.logic.object.combat;

import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.building.Building;
import com.gasis.rts.logic.object.building.BuildingConstructionListener;
import com.gasis.rts.logic.object.building.OffensiveBuilding;
import com.gasis.rts.logic.object.production.UnitProductionListener;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.logic.object.unit.movement.MovementAdapter;
import com.gasis.rts.logic.player.Player;
import com.gasis.rts.logic.player.controls.BuildingPlacementListener;
import com.gasis.rts.math.MathUtils;

import java.util.List;

/**
 * Finds and assigns targets to offensive game objects
 */
public class TargetAssigner extends MovementAdapter implements BuildingPlacementListener, BuildingConstructionListener, TargetRemovalListener, UnitProductionListener {

    // all players in the game
    protected List<Player> players;

    /**
     * Sets the game players
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    /**
     * Called when a building gets placed
     *
     * @param building the building that was just placed
     */
    @Override
    public void buildingPlaced(Building building) {
        notifyEnemiesAboutExistence(building);
    }

    /**
     * Called when a building gets constructed
     *
     * @param building the building that just got constructed
     */
    @Override
    public void buildingConstructed(Building building) {
        if (building instanceof OffensiveBuilding) {
            assignTargetForObject(building);
        }
    }

    /**
     * Called when a unit gets produced
     *
     * @param unit the unit that was just produced
     */
    @Override
    public void unitProduced(Unit unit) {
        assignTargetForObject(unit);
        notifyEnemiesAboutExistence(unit);
    }

    /**
     * Called when a game object gets it's target removed
     *
     * @param object object that got the target removed
     */
    @Override
    public void targetRemoved(GameObject object) {
        assignTargetForObject(object);
    }

    /**
     * Called when a unit starts moving
     *
     * @param unit the unit that just started moving
     */
    @Override
    public void startedMoving(Unit unit) {
        assignTargetForObject(unit);
        notifyEnemiesAboutExistence(unit);
    }

    /**
     * Notifies all enemies of the given object that it exists
     *
     * @param object object to notify about
     */
    protected void notifyEnemiesAboutExistence(GameObject object) {
        for (Player player: players) {
            if (player.isAllied(object.getOwner()) || player == object.getOwner()) {
                continue;
            }

            assignTargetToEnemyUnits(player, object);
            assignTargetToEnemyBuildings(player, object);
        }
    }

    /**
     * Sets the target object for the object's enemy units if they have no target and the
     * object is in range
     *
     * @param enemy enemy player
     * @param object target object
     */
    protected void assignTargetToEnemyUnits(Player enemy, GameObject object) {
        for (Unit unit: enemy.getUnits()) {
            if (!unit.hasTarget() && ((!unit.isInSiegeMode() && MathUtils.distance(unit.getCenterX(), object.getCenterX(), unit.getCenterY(), object.getCenterY()) <=
                unit.getDefensiveSpecs().getSightRange()) ||
                (unit.isInSiegeMode() && MathUtils.distance(unit.getCenterX(), object.getCenterX(), unit.getCenterY(), object.getCenterY()) <=
                            unit.getOffensiveSpecs().getSiegeModeAttackRange()))) {

                unit.aimAt(object);
            }
        }
    }

    /**
     * Sets the target object for the object's enemy buildings if they have no target and the
     * object is in range
     *
     * @param enemy enemy player
     * @param object target object
     */
    protected void assignTargetToEnemyBuildings(Player enemy, GameObject object) {
        for (Building building: enemy.getBuildings()) {
            if (!(building instanceof OffensiveBuilding) || building.isBeingConstructed()) {
                continue;
            }

            OffensiveBuilding offensiveBuilding = (OffensiveBuilding) building;

            if (!offensiveBuilding.hasTarget() && MathUtils.distance(offensiveBuilding.getCenterX(), object.getCenterX(), offensiveBuilding.getCenterY(), object.getCenterY()) <=
                    offensiveBuilding.getOffensiveSpecs().getAttackRange()) {

                offensiveBuilding.aimAt(object);
            }
        }
    }

    /**
     * Tries to find and assign a target to the given object
     *
     * @param object object to find target for
     */
    protected void assignTargetForObject(GameObject object) {
        
    }
}
