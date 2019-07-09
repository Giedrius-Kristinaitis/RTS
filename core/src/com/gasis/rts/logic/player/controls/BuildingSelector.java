package com.gasis.rts.logic.player.controls;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.building.Building;
import com.gasis.rts.logic.player.Player;
import com.gasis.rts.resources.Resources;

/**
 * Building selecting logic
 */
public class BuildingSelector extends Selector implements Renderable {

    // selected building (if any)
    protected Building selectedBuilding;

    /**
     * Default class constructor
     * @param player
     */
    public BuildingSelector(BlockMap map, ShapeRenderer shapeRenderer, Player player) {
        super(map, shapeRenderer, player);
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
            Building building = (Building) occupyingObject;

            if (player.getBuildings().contains(building)) {
                selectedBuilding = building;
                selectedBuilding.setRenderHp(true);
            }
        }
    }

    /**
     * Deselects currently selected building
     */
    public void deselectBuilding() {
        if (selectedBuilding != null) {
            selectedBuilding.setRenderHp(false);
            selectedBuilding = null;
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
     * Renders the object to the screen
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {
        if (selectedBuilding != null) {
            renderSelectionCorners(selectedBuilding);
        }
    }
}
