package com.gasis.rts.logic.player.controls;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.building.Building;
import com.gasis.rts.logic.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Building selecting logic
 */
public class BuildingSelector extends Selector {

    // selected building (if any)
    protected Building selectedBuilding;

    // all listeners that listen for building selection
    protected List<BuildingSelectionListener> listeners = new ArrayList<BuildingSelectionListener>();

    /**
     * Default class constructor
     * @param player
     */
    public BuildingSelector(BlockMap map, Player player) {
        super(map, player);
    }

    /**
     * Adds a building selection listener
     *
     * @param listener listener to add
     */
    public void addBuildingSelectionListener(BuildingSelectionListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a building selection listener
     *
     * @param listener listener to remove
     */
    public void removeBuildingSelectionListener(BuildingSelectionListener listener) {
        listeners.remove(listener);
    }

    /**
     * Called when the screen was touched or a mouse button was pressed
     *
     * @param x x coordinate relative to the bottom left map corner
     * @param y y coordinate relative to the bottom left map corner
     * @param pointer the pointer for the event.
     * @param button  the button
     */
    @Override
    public void touchDown(float x, float y, int pointer, int button) {
        deselectBuilding();

        GameObject occupyingObject = map.getOccupyingObject((short) (x / Block.BLOCK_WIDTH), (short) (y / Block.BLOCK_HEIGHT));

        if (occupyingObject instanceof Building) {
            selectBuilding((Building) occupyingObject);
        }
    }

    /**
     * Selects the specified building if it belongs to the controlled player
     *
     * @param building building to select
     */
    protected void selectBuilding(Building building) {
        if (player.getBuildings().contains(building)) {
            selectedBuilding = building;
            selectedBuilding.setRenderHp(true);

            notifySelectionListeners();
        }
    }

    /**
     * Notifies all building selection listeners that a building was selected
     */
    protected void notifySelectionListeners() {
        for (BuildingSelectionListener listener: listeners) {
            listener.buildingSelected(selectedBuilding);
        }
    }

    /**
     * Notifies all building selection listeners that a building was deselected
     */
    protected void notifyDeselectionListeners() {
        for (BuildingSelectionListener listener: listeners) {
            listener.buildingDeselected();
        }
    }

    /**
     * Deselects currently selected building
     */
    public void deselectBuilding() {
        if (selectedBuilding != null) {
            selectedBuilding.setRenderHp(false);
            selectedBuilding = null;
            notifyDeselectionListeners();
        }
    }

    /**
     * Gets currently selected building
     * @return
     */
    public Building getSelectedBuilding() {
        return selectedBuilding;
    }

    /**
     * Renders texture-less shapes
     *
     * @param shapeRenderer renderer to draw shapes to
     */
    @Override
    public void render(ShapeRenderer shapeRenderer) {
        super.render(shapeRenderer);

        if (selectedBuilding != null) {
            renderSelectionCorners(shapeRenderer, selectedBuilding);
        }
    }
}
