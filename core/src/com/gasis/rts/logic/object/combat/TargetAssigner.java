package com.gasis.rts.logic.object.combat;

import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.map.blockmap.BlockMap;
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
public class TargetAssigner extends MovementAdapter implements BuildingPlacementListener, BuildingConstructionListener, TargetRemovalListener, UnitProductionListener, SiegeModeListener {

    // all players in the game
    protected List<Player> players;

    // the game's map
    protected BlockMap map;

    /**
     * Sets the game players
     */
    public void setPlayers(List<Player> players, BlockMap map) {
        this.players = players;
        this.map = map;
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
     * Called when a unit toggles siege mode
     *
     * @param unit the unit that just toggled siege mode
     */
    @Override
    public void siegeModeToggled(Unit unit) {
        assignTargetForObject(unit);
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
            assignTargetToUnit(unit, object);
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

            assignTargetToBuilding((OffensiveBuilding) building, object);
        }
    }

    /**
     * Assigns a target to a unit
     *
     * @param unit unit to assign the target to
     * @param target the target
     */
    protected void assignTargetToUnit(Unit unit, GameObject target) {
        if (((!unit.isInSiegeMode() && MathUtils.distance(unit.getCenterX() / Block.BLOCK_WIDTH, target.getCenterX() / Block.BLOCK_WIDTH, unit.getCenterY() / Block.BLOCK_HEIGHT, target.getCenterY() / Block.BLOCK_HEIGHT) <=
                unit.getDefensiveSpecs().getSightRange()) ||
                (unit.isInSiegeMode() && MathUtils.distance(unit.getCenterX() / Block.BLOCK_WIDTH, target.getCenterX() / Block.BLOCK_WIDTH, unit.getCenterY() / Block.BLOCK_HEIGHT, target.getCenterY() / Block.BLOCK_HEIGHT) <=
                        unit.getOffensiveSpecs().getSiegeModeAttackRange()))) {

            if (!unit.hasTarget() && !unit.hasTargetObject()) {
                unit.aimAt(target);
            } else if (!unit.hasSecondaryTarget() && !unit.isMainTargetReachable()) {
                unit.setSecondaryTargetObject(target);
            }
        }
    }

    /**
     * Assigns a target to a building
     *
     * @param building building to assign the target to
     * @param target the target
     */
    protected void assignTargetToBuilding(OffensiveBuilding building, GameObject target) {
        if (!building.hasTarget() && !building.hasTargetObject() && MathUtils.distance(building.getCenterX() / Block.BLOCK_WIDTH, target.getCenterX() / Block.BLOCK_WIDTH, building.getCenterY() / Block.BLOCK_HEIGHT, target.getCenterY() / Block.BLOCK_HEIGHT) <=
                building.getOffensiveSpecs().getAttackRange()) {

            building.aimAt(target);
        }
    }

    /**
     * Tries to find and assign a target to the given object
     *
     * @param object object to find target for
     */
    @SuppressWarnings("Duplicates")
    protected void assignTargetForObject(GameObject object) {
        if (hasTarget(object) && (!(object instanceof Unit) || ((Unit) object).isMainTargetReachable())) {
            return;
        }

        short centerBlockX = (short) (object.getCenterX() / Block.BLOCK_WIDTH);
        short centerBlockY = (short) (object.getCenterY() / Block.BLOCK_HEIGHT);

        short sightRangeX = (short) (object.getDefensiveSpecs().getSightRange() / Block.BLOCK_WIDTH);
        short sightRangeY = (short) (object.getDefensiveSpecs().getSightRange() / Block.BLOCK_HEIGHT);

        if (object instanceof Unit && ((Unit) object).isInSiegeMode()) {
            sightRangeX = (short) (object.getDefensiveSpecs().getSiegeModeSightRange() / Block.BLOCK_WIDTH);
            sightRangeY = (short) (object.getDefensiveSpecs().getSiegeModeSightRange() / Block.BLOCK_HEIGHT);
        }

        /*
         *   WARNING: the following code might appear unpleasant to some viewers and
         *   cause brain damage.
         *
         *   VIEWER DISCRETION IS ADVISED
         */

        for (int distance = 1; distance <= Math.max(sightRangeX, sightRangeY); distance++) {
            if (hasTarget(object) && (!(object instanceof Unit) || ((Unit) object).hasSecondaryTarget())) {
                break;
            }

            for (int x = centerBlockX - distance; x <= centerBlockX + distance; x++) {
                if (hasTarget(object) && (!(object instanceof Unit) || ((Unit) object).hasSecondaryTarget())) {
                    break;
                }

                GameObject occupyingObject = map.getOccupyingObject((short) x, (short) (centerBlockY - distance));

                if (occupyingObject != null && occupyingObject != object && occupyingObject.getOwner() != object.getOwner() && !object.getOwner().isAllied(occupyingObject.getOwner())) {
                    assignTargetToObject(object, occupyingObject);
                }

                occupyingObject = map.getOccupyingObject((short) x, (short) (centerBlockY + distance));

                if (occupyingObject != null && occupyingObject != object && occupyingObject.getOwner() != object.getOwner() && !object.getOwner().isAllied(occupyingObject.getOwner())) {
                    assignTargetToObject(object, occupyingObject);
                }
            }

            if (hasTarget(object) && (!(object instanceof Unit) || ((Unit) object).hasSecondaryTarget())) {
                break;
            }

            for (int y = centerBlockY - distance; y <= centerBlockY + distance; y++) {
                if (hasTarget(object) && (!(object instanceof Unit) || ((Unit) object).hasSecondaryTarget())) {
                    break;
                }

                GameObject occupyingObject = map.getOccupyingObject((short) (centerBlockX - distance), (short) y);

                if (occupyingObject != null && occupyingObject != object && occupyingObject.getOwner() != object.getOwner() && !object.getOwner().isAllied(occupyingObject.getOwner())) {
                    assignTargetToObject(object, occupyingObject);
                }

                occupyingObject = map.getOccupyingObject((short) (centerBlockX + distance), (short) y);

                if (occupyingObject != null && occupyingObject != object && occupyingObject.getOwner() != object.getOwner() && !object.getOwner().isAllied(occupyingObject.getOwner())) {
                    assignTargetToObject(object, occupyingObject);
                }
            }
        }
    }

    /**
     * Checks if the specified object has a target
     *
     * @param object object to check
     * @return
     */
    protected boolean hasTarget(GameObject object) {
        if (object instanceof Unit) {
            return ((Unit) object).hasTarget();
        } else if (object instanceof OffensiveBuilding) {
            return ((OffensiveBuilding) object).hasTarget();
        }

        return false;
    }

    /**
     * Tries to assign a target to the specified object
     *
     * @param object object to assign the target to
     * @param target the target

     */
    protected void assignTargetToObject(GameObject object, GameObject target) {
        if (object instanceof Unit) {
            assignTargetToUnit((Unit) object, target);
        } else if (object instanceof OffensiveBuilding) {
            assignTargetToBuilding((OffensiveBuilding) object, target);
        }
    }
}
