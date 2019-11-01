package com.gasis.rts.logic.player.exploration;

import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.building.Building;
import com.gasis.rts.logic.object.building.BuildingConstructionListener;
import com.gasis.rts.logic.object.combat.DestructionListener;
import com.gasis.rts.logic.object.combat.SiegeModeListener;
import com.gasis.rts.logic.object.production.UnitProductionListener;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.logic.object.unit.movement.MovementAdapter;
import com.gasis.rts.math.MathUtils;

/**
 * Manages player's exploration data
 */
public class ExplorationDataManager extends MovementAdapter implements BuildingConstructionListener, UnitProductionListener, SiegeModeListener, DestructionListener {

    // exploration data to manage
    protected ExplorationDataInterface explorationData;

    /**
     * Sets exploration data to manage
     *
     * @param explorationData
     */
    public void setExplorationData(ExplorationDataInterface explorationData) {
        this.explorationData = explorationData;
    }

    /**
     * Called when a building gets constructed
     *
     * @param building the building that just got constructed
     */
    @Override
    public void buildingConstructed(Building building) {
        setAreaVisibility(building, true);
    }

    /**
     * Called when a unit gets produced
     *
     * @param unit the unit that was just produced
     */
    @Override
    public void unitProduced(Unit unit) {
        setAreaVisibility(unit, true);
    }

    /**
     * Called when a unit starts moving
     *
     * @param unit the unit that just started moving
     */
    @Override
    public void startedMoving(Unit unit) {
        setAreaVisibility(unit, true);
    }

    /**
     * Called when a game object gets destroyed
     *
     * @param object the destroyed object
     */
    @Override
    public void objectDestroyed(GameObject object) {
        setAreaVisibility(object, false);
    }

    /**
     * Called when a unit toggles siege mode
     *
     * @param unit the unit that just toggled siege mode
     */
    @Override
    public void siegeModeToggled(Unit unit) {
        setAreaVisibility(unit, true);
    }

    /**
     * Sets area visibility around the specified game object
     *
     * @param object     object to set visibility around
     * @param visibility area visibility
     */
    protected void setAreaVisibility(GameObject object, boolean visibility) {
        boolean siegeMode = object instanceof Unit && ((Unit) object).isInSiegeMode();

        short sightRange = (short) (siegeMode ? object.getDefensiveSpecs().getSiegeModeSightRange() : object.getDefensiveSpecs().getSightRange());

        short startX = (short) (object.getCenterX() / Block.BLOCK_WIDTH - sightRange);
        short endX = (short) (startX + sightRange * 2);
        short startY = (short) (object.getCenterY() / Block.BLOCK_HEIGHT - sightRange);
        short endY = (short) (startY + sightRange * 2);

        for (short x = startX; x <= endX; x++) {
            for (short y = startY; y <= endY; y++) {
                if (MathUtils.distance(x + 0.5f, object.getCenterX() / Block.BLOCK_WIDTH, y + 0.5f, object.getCenterY() / Block.BLOCK_HEIGHT) > sightRange) {
                    continue;
                }

                if (visibility) {
                    explorationData.setExplored(x, y, true);
                }

                explorationData.setVisible(x, y, visibility);
            }
        }
    }
}
