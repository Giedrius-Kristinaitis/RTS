package com.gasis.rts.logic.player.controls;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.building.BuildingLoader;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

/**
 * Building placement logic
 */
public class BuildingPlacer implements Renderable {

    // opacity of the building and it's bounds
    protected final float textureOpacity = 0.5f;

    // the texture of the building that is currently being placed
    protected String buildingAtlas;
    protected String buildingTexture;

    // is any building being placed right now
    protected boolean placing = false;

    // building's coordinates
    protected float buildingX;
    protected float buildingY;

    // building's dimensions
    protected float buildingWidth;
    protected float buildingHeight;

    // the game's map
    protected BlockMap map;

    /**
     * Default class constructor
     * @param map
     */
    public BuildingPlacer(BlockMap map) {
        this.map = map;
    }

    /**
     * Starts the process of building placement
     *
     * @param loader building's loader
     */
    public void initiateBuildingPlacement(BuildingLoader loader) {
        buildingWidth = loader.getWidth();
        buildingHeight = loader.getHeight();

        buildingTexture = loader.getPlacementTexture();
        buildingAtlas = loader.getAtlas();

        placing = true;
    }

    /**
     * Called when the mouse is moved
     *
     * @param x mouse x relative to map's bottom left
     * @param y mouse y relative to map's bottom left
     */
    public void mouseMoved(float x, float y) {
        buildingX = (int) (x / Block.BLOCK_WIDTH - buildingWidth / 2f) * Block.BLOCK_WIDTH;
        buildingY = (int) (y / Block.BLOCK_HEIGHT - buildingHeight / 2f) * Block.BLOCK_HEIGHT;
    }

    /**
     * Checks if any building is being placed right now
     * @return
     */
    public boolean isPlacing() {
        return placing;
    }

    /**
     * Calcels the current building's placement process
     */
    public void cancelPlacement() {
        placing = false;
    }

    /**
     * Attempts to finish the current building's placement process
     */
    public void finishPlacement() {
        if (placing) {

            placing = false;
        }
    }

    /**
     * Renders the object to the screen
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {
        if (placing) {
            batch.setColor(1, 1, 1, textureOpacity);
            batch.draw(resources.atlas(Constants.FOLDER_ATLASES + buildingAtlas).findRegion(buildingTexture), buildingX, buildingY, buildingWidth, buildingHeight);
            batch.setColor(1, 1, 1, 1);
        }
    }
}
