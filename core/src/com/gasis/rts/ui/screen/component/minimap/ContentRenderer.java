package com.gasis.rts.ui.screen.component.minimap;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.logic.player.Player;
import com.gasis.rts.logic.player.exploration.ExplorationDataInterface;
import com.gasis.rts.logic.render.SimpleRenderable;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

/**
 * Renders contents of the minimap
 */
public class ContentRenderer implements SimpleRenderable {

    // provides dimensions
    protected MinimapDimensionsProvider dimensionsProvider;

    // provides current render bounds
    protected BoundsProvider boundsProvider;

    // game's map
    protected BlockMap map;

    // player's exploration data
    protected ExplorationDataInterface explorationData;

    // player the minimap is rendered for
    protected Player player;

    /**
     * Renders the object
     *
     * @param batch     batch to draw to
     * @param resources game's assets
     */
    @Override
    public void render(Batch batch, Resources resources) {
        renderContents(batch, resources);
        renderRenderBounds(batch, resources);
    }

    /**
     * Renders the minimap's contents
     *
     * @param batch     batch to draw to
     * @param resources game's assets
     */
    protected void renderContents(Batch batch, Resources resources) {
        for (short x = 0; x < map.getWidth(); x++) {
            for (short y = 0; y < map.getHeight(); y++) {
                if (!explorationData.isExplored(x, y)) {
                    batch.draw(
                            resources.atlas(Constants.FOLDER_ATLASES + Constants.MINIMAP_ATLAS).findRegion(Constants.MINIMAP_BLOCK_UNEXPLORED),
                            dimensionsProvider.getMinimapX() + x * dimensionsProvider.getBlockWidth(),
                            dimensionsProvider.getMinimapY() + y * dimensionsProvider.getBlockHeight(),
                            dimensionsProvider.getBlockWidth(),
                            dimensionsProvider.getBlockHeight()
                    );
                } else {
                    if (!explorationData.isVisible(x, y)) {
                        if (map.isBlockPassable(x, y)) {
                            batch.draw(
                                    resources.atlas(Constants.FOLDER_ATLASES + Constants.MINIMAP_ATLAS).findRegion(Constants.MINIMAP_BLOCK_EXPLORED_INVISIBLE),
                                    dimensionsProvider.getMinimapX() + x * dimensionsProvider.getBlockWidth(),
                                    dimensionsProvider.getMinimapY() + y * dimensionsProvider.getBlockHeight(),
                                    dimensionsProvider.getBlockWidth(),
                                    dimensionsProvider.getBlockHeight()
                            );

                            renderObject(batch, resources, x, y, false);
                        } else {
                            batch.draw(
                                    resources.atlas(Constants.FOLDER_ATLASES + Constants.MINIMAP_ATLAS).findRegion(Constants.MINIMAP_BLOCK_VISIBLE),
                                    dimensionsProvider.getMinimapX() + x * dimensionsProvider.getBlockWidth(),
                                    dimensionsProvider.getMinimapY() + y * dimensionsProvider.getBlockHeight(),
                                    dimensionsProvider.getBlockWidth(),
                                    dimensionsProvider.getBlockHeight()
                            );
                        }
                    } else {
                        if (!map.isBlockPassable(x, y)) {
                            batch.draw(
                                    resources.atlas(Constants.FOLDER_ATLASES + Constants.MINIMAP_ATLAS).findRegion(Constants.MINIMAP_BLOCK_TERRAIN_OBJECT),
                                    dimensionsProvider.getMinimapX() + x * dimensionsProvider.getBlockWidth(),
                                    dimensionsProvider.getMinimapY() + y * dimensionsProvider.getBlockHeight(),
                                    dimensionsProvider.getBlockWidth(),
                                    dimensionsProvider.getBlockHeight()
                            );
                        } else {
                            batch.draw(
                                    resources.atlas(Constants.FOLDER_ATLASES + Constants.MINIMAP_ATLAS).findRegion(Constants.MINIMAP_BLOCK_VISIBLE),
                                    dimensionsProvider.getMinimapX() + x * dimensionsProvider.getBlockWidth(),
                                    dimensionsProvider.getMinimapY() + y * dimensionsProvider.getBlockHeight(),
                                    dimensionsProvider.getBlockWidth(),
                                    dimensionsProvider.getBlockHeight()
                            );

                            renderObject(batch, resources, x, y, true);
                        }
                    }
                }
            }
        }
    }

    /**
     * Renders a game object
     *
     * @param batch
     * @param resources
     * @param x
     * @param y
     * @param renderEnemy
     */
    protected void renderObject(Batch batch, Resources resources, short x, short y, boolean renderEnemy) {
        GameObject occupyingObject = map.getOccupyingObject(x, y);

        if (occupyingObject != null) {
            if (occupyingObject instanceof Unit && (renderEnemy || occupyingObject.getOwner().isAllied(player))) {
                batch.draw(
                        resources.atlas(Constants.FOLDER_ATLASES + Constants.MINIMAP_ATLAS).findRegion(Constants.MINIMAP_HEAVY_UNIT_PREFIX + occupyingObject.getOwner().getColor()),
                        dimensionsProvider.getMinimapX() + x * dimensionsProvider.getBlockWidth(),
                        dimensionsProvider.getMinimapY() + y * dimensionsProvider.getBlockHeight(),
                        dimensionsProvider.getBlockWidth(),
                        dimensionsProvider.getBlockHeight()
                );
            } else {
                batch.draw(
                        resources.atlas(Constants.FOLDER_ATLASES + Constants.MINIMAP_ATLAS).findRegion(Constants.MINIMAP_BLOCK_OBJECT_PREFIX + occupyingObject.getOwner().getColor()),
                        dimensionsProvider.getMinimapX() + x * dimensionsProvider.getBlockWidth(),
                        dimensionsProvider.getMinimapY() + y * dimensionsProvider.getBlockHeight(),
                        dimensionsProvider.getBlockWidth(),
                        dimensionsProvider.getBlockHeight()
                );
            }
        }
    }

    /**
     * Renders the current render bounds
     *
     * @param batch     batch to draw to
     * @param resources game's assets
     */
    protected void renderRenderBounds(Batch batch, Resources resources) {
        batch.draw(
                resources.atlas(Constants.FOLDER_ATLASES + Constants.MINIMAP_ATLAS).findRegion(Constants.MINIMAP_BOUNDS),
                boundsProvider.getRenderBounds().start.x,
                boundsProvider.getRenderBounds().start.y,
                boundsProvider.getRenderBounds().end.x - boundsProvider.getRenderBounds().start.x,
                boundsProvider.getRenderBounds().end.y - boundsProvider.getRenderBounds().start.y
        );
    }

    /**
     * Sets the dimension provider
     *
     * @param dimensionsProvider
     */
    public void setDimensionsProvider(MinimapDimensionsProvider dimensionsProvider) {
        this.dimensionsProvider = dimensionsProvider;
    }

    /**
     * Sets the bounds provider
     *
     * @param boundsProvider
     */
    public void setBoundsProvider(BoundsProvider boundsProvider) {
        this.boundsProvider = boundsProvider;
    }

    /**
     * Sets the game map
     *
     * @param map
     */
    public void setMap(BlockMap map) {
        this.map = map;
    }

    /**
     * Sets the player's exploration data
     *
     * @param explorationData
     */
    public void setExplorationData(ExplorationDataInterface explorationData) {
        this.explorationData = explorationData;
    }

    /**
     * Sets the player
     *
     * @param player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }
}
