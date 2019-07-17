package com.gasis.rts.logic.player.controls;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.building.Building;
import com.gasis.rts.logic.object.building.BuildingLoader;
import com.gasis.rts.logic.player.Player;
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
    protected float buildingCenterX;
    protected float buildingX;
    protected float buildingY;

    // building's dimensions
    protected float buildingWidth;
    protected float buildingHeight;

    // the last position of the mouse before initiating building placement
    protected float lastMouseXBeforePlacing;
    protected float lastMouseYBeforePlacing;

    // the game's map
    protected BlockMap map;

    // the placed building's loader
    protected BuildingLoader loader;

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
        this.loader = loader;

        buildingWidth = loader.getWidth();
        buildingHeight = loader.getHeight();

        buildingTexture = loader.getPlacementTexture();
        buildingAtlas = loader.getAtlas();

        placing = true;

        calculateBuildingCoordinates(lastMouseXBeforePlacing, lastMouseYBeforePlacing);
    }

    /**
     * Called when the mouse is moved
     *
     * @param x mouse x relative to map's bottom left
     * @param y mouse y relative to map's bottom left
     */
    public void mouseMoved(float x, float y) {
        if (placing) {
            calculateBuildingCoordinates(x, y);
        } else {
            lastMouseXBeforePlacing = x;
            lastMouseYBeforePlacing = y;
        }
    }

    /**
     * Calculates the building's coordinates
     *
     * @param mouseX mouse pointer x
     * @param mouseY mouse pointer y
     */
    protected void calculateBuildingCoordinates(float mouseX, float mouseY) {
        buildingX = (int) (mouseX / Block.BLOCK_WIDTH - buildingWidth / 2f) * Block.BLOCK_WIDTH;
        buildingY = (int) (mouseY / Block.BLOCK_HEIGHT - buildingHeight / 2f) * Block.BLOCK_HEIGHT;
        buildingCenterX = buildingX + loader.getWidthInBlocks() * Block.BLOCK_WIDTH / 2f;
    }

    /**
     * Checks if any building is being placed right now
     * @return
     */
    public boolean isPlacing() {
        return placing;
    }

    /**
     * Cancels the current building's placement process
     */
    public void cancelPlacement() {
        placing = false;
    }

    /**
     * Attempts to finish the current building's placement process
     */
    public void finishPlacement(Player player) {
        if (placing && canPlaceInCurrentPosition()) {
            Building building = loader.newInstance();

            for (short x = (short) (buildingX / Block.BLOCK_WIDTH); x < buildingX / Block.BLOCK_WIDTH + building.getWidthInBlocks(); x++) {
                for (short y = (short) (buildingY / Block.BLOCK_HEIGHT); y < buildingY / Block.BLOCK_HEIGHT + building.getHeightInBlocks(); y++) {
                    map.occupyBlock(x, y, building);
                }
            }

            building.setCenterX(buildingCenterX);
            building.setY(buildingY);
            building.setXInBlocks((short) (buildingX / Block.BLOCK_WIDTH));
            building.setYInBlocks((short) (buildingY / Block.BLOCK_HEIGHT));
            building.initializeAnimations();

            player.addBuilding(building);
            building.setOwner(player);

            building.setBeingConstructed(true);

            placing = false;
        }
    }

    /**
     * Checks if the building can be placed where it currently is positioned
     *
     * @return
     */
    protected boolean canPlaceInCurrentPosition() {
        if (buildingX / Block.BLOCK_WIDTH < 0 || buildingX / Block.BLOCK_WIDTH + loader.getWidthInBlocks() > map.getWidth() || buildingY / Block.BLOCK_HEIGHT < 0 || buildingY / Block.BLOCK_HEIGHT + loader.getHeightInBlocks() > map.getHeight()) {
            return false;
        }

        for (short x = (short) (buildingX / Block.BLOCK_WIDTH); x < buildingX / Block.BLOCK_WIDTH + loader.getWidthInBlocks(); x++) {
            for (short y = (short) (buildingY / Block.BLOCK_HEIGHT); y < buildingY / Block.BLOCK_HEIGHT + loader.getHeightInBlocks(); y++) {
                if (map.isBlockOccupied(x, y)) {
                    return false;
                }
            }
        }

        return true;
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
            batch.draw(resources.atlas(Constants.FOLDER_ATLASES + buildingAtlas).findRegion(buildingTexture), buildingCenterX - buildingWidth / 2f, buildingY, buildingWidth, buildingHeight);
            batch.setColor(1, 1, 1, 1);
        }
    }
}
