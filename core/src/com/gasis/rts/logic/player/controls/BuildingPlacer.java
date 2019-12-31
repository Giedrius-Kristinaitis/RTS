package com.gasis.rts.logic.player.controls;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.building.Building;
import com.gasis.rts.logic.object.building.BuildingLoader;
import com.gasis.rts.logic.player.Player;
import com.gasis.rts.logic.render.RenderQueueInterface;
import com.gasis.rts.logic.render.Renderable;
import com.gasis.rts.math.Point;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    // blocks on which the building's placement currently is
    protected List<Point> blocks = new ArrayList<Point>();

    // building placement listeners
    protected Set<BuildingPlacementListener> placementListeners = new HashSet<BuildingPlacementListener>();

    /**
     * Default class constructor
     *
     * @param map
     */
    public BuildingPlacer(BlockMap map) {
        this.map = map;
    }

    /**
     * Adds a building placement listener
     *
     * @param listener listener to add
     */
    public void addPlacementListener(BuildingPlacementListener listener) {
        placementListeners.add(listener);
    }

    /**
     * Removes a building placement listener
     *
     * @param listener listener to remove
     */
    public void removeBuildingPlacementListener(BuildingPlacementListener listener) {
        placementListeners.remove(listener);
    }

    /**
     * Notifies placement listeners that a building has been placed
     *
     * @param building the building that was just placed
     */
    protected void notifyPlacementListeners(Building building) {
        for (BuildingPlacementListener listener : placementListeners) {
            listener.buildingPlaced(building);
        }
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

        findBlocks(lastMouseXBeforePlacing, lastMouseYBeforePlacing);
        calculateBuildingCoordinates(lastMouseXBeforePlacing, lastMouseYBeforePlacing);
    }

    /**
     * Re-initiates building placement to place the same building that was just placed
     */
    public void reinitiateBuildingPlacement() {
        initiateBuildingPlacement(loader);
    }

    /**
     * Called when the mouse is moved
     *
     * @param x mouse x relative to map's bottom left
     * @param y mouse y relative to map's bottom left
     */
    public void mouseMoved(float x, float y) {
        if (placing) {
            findBlocks(x, y);
            calculateBuildingCoordinates(x, y);
        } else {
            lastMouseXBeforePlacing = x;
            lastMouseYBeforePlacing = y;
        }
    }

    /**
     * Finds the blocks on which the building currently is
     *
     * @param mouseX mouse x in world coordinates
     * @param mouseY mouse y in world coordinates
     */
    protected void findBlocks(float mouseX, float mouseY) {
        blocks.clear();

        short centerBlockX = (short) (mouseX / Block.BLOCK_WIDTH);
        short blockY = (short) (mouseY / Block.BLOCK_HEIGHT - buildingHeight / 2f);

        int startingX = centerBlockX - loader.getWidthInBlocks() / 2;
        int endX = centerBlockX + loader.getWidthInBlocks() / 2;

        if (loader.getWidthInBlocks() % 2 != 0) {
            endX++;
        }

        for (int x = startingX; x < endX; x++) {
            for (int y = blockY; y < blockY + loader.getHeightInBlocks(); y++) {
                blocks.add(new Point(x, y));
            }
        }
    }

    /**
     * Calculates the building's coordinates
     *
     * @param mouseX mouse pointer x
     * @param mouseY mouse pointer y
     */
    protected void calculateBuildingCoordinates(float mouseX, float mouseY) {
        buildingCenterX = blocks.get(0).x * Block.BLOCK_WIDTH + ((blocks.get(blocks.size() - 1).x + 1) * Block.BLOCK_WIDTH - blocks.get(0).x * Block.BLOCK_WIDTH) / 2f;
        buildingX = buildingCenterX - buildingWidth / 2f;
        buildingY = blocks.get(0).y * Block.BLOCK_HEIGHT;
    }

    /**
     * Checks if any building is being placed right now
     *
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

            building.setCenterX(buildingCenterX);
            building.setY(buildingY);
            building.setXInBlocks((short) (buildingX / Block.BLOCK_WIDTH));
            building.setYInBlocks((short) (buildingY / Block.BLOCK_HEIGHT));
            building.occupyBlocks(new ArrayList<Point>(blocks));
            building.initializeAnimations();

            player.addBuilding(building);
            building.setOwner(player);

            building.setBeingConstructed(true);

            notifyPlacementListeners(building);

            placing = false;
        }
    }

    /**
     * Checks if the building can be placed where it currently is positioned
     *
     * @return
     */
    protected boolean canPlaceInCurrentPosition() {
        for (Point block : blocks) {
            if (map.isBlockOccupied((short) block.x, (short) block.y)
                    || !map.isBlockPassable((short) block.x, (short) block.y)
                    || block.x < 0 || block.y < 0 || block.x >= map.getWidth() || block.y >= map.getHeight()) {

                return false;
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
    public void render(SpriteBatch batch, Resources resources, RenderQueueInterface renderQueue) {
        if (placing) {
            batch.setColor(1, 1, 1, textureOpacity);
            batch.draw(resources.atlas(Constants.FOLDER_ATLASES + buildingAtlas).findRegion(buildingTexture), buildingCenterX - buildingWidth / 2f, buildingY, buildingWidth, buildingHeight);
            batch.setColor(1, 1, 1, 1);
        }
    }
}
