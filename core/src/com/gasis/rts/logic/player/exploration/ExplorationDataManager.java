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

import java.util.HashSet;
import java.util.Set;

/**
 * Manages player's exploration data
 */
public class ExplorationDataManager extends MovementAdapter implements BuildingConstructionListener, UnitProductionListener, SiegeModeListener, DestructionListener {

    // exploration data to manage
    protected ExplorationDataInterface explorationData;

    // visibility data by unit
    protected VisibilityEntry[][] visibilityData;

    // map dimensions
    protected short mapWidth;
    protected short mapHeight;

    /**
     * Sets exploration data to manage
     *
     * @param explorationData
     */
    public void setExplorationData(ExplorationDataInterface explorationData) {
        this.explorationData = explorationData;
    }

    /**
     * Initializes visibility data array
     *
     * @param mapWidth map width
     * @param mapHeight map height
     */
    public void initVisibilityData(short mapWidth, short mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        visibilityData = new VisibilityEntry[mapWidth][mapHeight];

        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                visibilityData[x][y] = new VisibilityEntry();
            }
        }
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
    public void stoppedMoving(Unit unit) {
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
        boolean siegeMode = object instanceof Unit && ((Unit) object).isSiegeModeAvailable();

        short areaRange = (short) (siegeMode ? object.getDefensiveSpecs().getSiegeModeSightRange() : object.getDefensiveSpecs().getSightRange());
        short sightRange = (short) (siegeMode && ((Unit) object).isInSiegeMode() ? object.getDefensiveSpecs().getSiegeModeSightRange() : object.getDefensiveSpecs().getSightRange());

        short startX = (short) ((object.getCenterX() / Block.BLOCK_WIDTH - areaRange) - 1);
        short endX = (short) ((startX + areaRange * 2) + 2);
        short startY = (short) ((object.getCenterY() / Block.BLOCK_HEIGHT - areaRange) - 1);
        short endY = (short) ((startY + areaRange * 2) + 2);

        for (short x = startX; x <= endX; x++) {
            for (short y = startY; y <= endY; y++) {
                if (MathUtils.distance(x + 0.5f, object.getCenterX() / Block.BLOCK_WIDTH, y + 0.5f, object.getCenterY() / Block.BLOCK_HEIGHT) > sightRange) {
                    if (x >= 0 && y >= 0 && x < mapWidth && y < mapHeight) {
                        visibilityData[x][y].objects.remove(object);

                        if (visibilityData[x][y].objects.size() == 0) {
                            visibilityData[x][y].visibility = false;
                            explorationData.setVisible(x, y, false);
                        }
                    }

                    continue;
                }

                if (visibility) {
                    explorationData.setExplored(x, y, true);
                }

                boolean visible = visibility;

                if (x >= 0 && y >= 0 && x < mapWidth && y < mapHeight) {
                    if (visibility) {
                        visibilityData[x][y].visibility = true;
                        visibilityData[x][y].objects.add(object);
                    } else {
                        visibilityData[x][y].objects.remove(object);

                        if (visibilityData[x][y].objects.size() == 0) {
                            visibilityData[x][y].visibility = false;
                        }
                    }

                    visible = visibilityData[x][y].visibility;
                }

                explorationData.setVisible(x, y, visible);
            }
        }
    }

    /**
     * Used to keep track of block visibility by unit
     */
    protected class VisibilityEntry {

        protected Set<GameObject> objects = new HashSet<GameObject>();
        protected boolean visibility;
    }
}
