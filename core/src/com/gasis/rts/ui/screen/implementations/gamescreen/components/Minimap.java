package com.gasis.rts.ui.screen.implementations.gamescreen.components;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.utils.Constants;

/**
 * Minimap showing map elements
 */
public class Minimap extends AbstractComponent {

    // minimap's opacity
    protected final float OPACITY = 0.75f;

    // block size
    protected float blockWidth;
    protected float blockHeight;

    /**
     * Updates the minimap
     *
     * @param delta time elapsed since the last update
     */
    @Override
    public void act(float delta) {
        super.act(delta);


    }

    /**
     * Draws the minimap
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.setColor(1, 1, 1, OPACITY);
        renderContents(batch);
        batch.setColor(1, 1, 1, 1);
    }

    /**
     * Renders the minimap's contents
     *
     * @param batch batch to draw to
     */
    protected void renderContents(Batch batch) {
        for (short x = 0; x < game.getMap().getWidth(); x++) {
            for (short y = 0; y < game.getMap().getHeight(); y++) {
                if (!game.getExplorationData().isExplored(x, y)) {
                    batch.draw(
                            game.getResources().atlas(Constants.FOLDER_ATLASES + Constants.MINIMAP_ATLAS).findRegion(Constants.MINIMAP_BLOCK_UNEXPLORED),
                            getX() + x * blockWidth,
                            getY() + y * blockHeight,
                            blockWidth,
                            blockHeight
                    );
                } else {
                    if (!game.getExplorationData().isVisible(x, y)) {
                        if (game.getMap().isBlockPassable(x, y)) {
                            batch.draw(
                                    game.getResources().atlas(Constants.FOLDER_ATLASES + Constants.MINIMAP_ATLAS).findRegion(Constants.MINIMAP_BLOCK_EXPLORED_INVISIBLE),
                                    getX() + x * blockWidth,
                                    getY() + y * blockHeight,
                                    blockWidth,
                                    blockHeight
                            );
                        } else {
                            batch.draw(
                                    game.getResources().atlas(Constants.FOLDER_ATLASES + Constants.MINIMAP_ATLAS).findRegion(Constants.MINIMAP_BLOCK_VISIBLE),
                                    getX() + x * blockWidth,
                                    getY() + y * blockHeight,
                                    blockWidth,
                                    blockHeight
                            );
                        }
                    } else {
                        if (!game.getMap().isBlockPassable(x, y)) {
                            batch.draw(
                                    game.getResources().atlas(Constants.FOLDER_ATLASES + Constants.MINIMAP_ATLAS).findRegion(Constants.MINIMAP_BLOCK_TERRAIN_OBJECT),
                                    getX() + x * blockWidth,
                                    getY() + y * blockHeight,
                                    blockWidth,
                                    blockHeight
                            );
                        } else {
                            batch.draw(
                                    game.getResources().atlas(Constants.FOLDER_ATLASES + Constants.MINIMAP_ATLAS).findRegion(Constants.MINIMAP_BLOCK_VISIBLE),
                                    getX() + x * blockWidth,
                                    getY() + y * blockHeight,
                                    blockWidth,
                                    blockHeight
                            );

                            GameObject occupyingObject = game.getMap().getOccupyingObject(x, y);

                            if (occupyingObject != null) {
                                if (occupyingObject instanceof Unit) {
                                    batch.draw(
                                            game.getResources().atlas(Constants.FOLDER_ATLASES + Constants.MINIMAP_ATLAS).findRegion(Constants.MINIMAP_HEAVY_UNIT_PREFIX + occupyingObject.getOwner().getColor()),
                                            getX() + x * blockWidth,
                                            getY() + y * blockHeight,
                                            blockWidth,
                                            blockHeight
                                    );
                                } else {
                                    batch.draw(
                                            game.getResources().atlas(Constants.FOLDER_ATLASES + Constants.MINIMAP_ATLAS).findRegion(Constants.MINIMAP_BLOCK_OBJECT_PREFIX + occupyingObject.getOwner().getColor()),
                                            getX() + x * blockWidth,
                                            getY() + y * blockHeight,
                                            blockWidth,
                                            blockHeight
                                    );
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Called when the stage resizes
     *
     * @param width  stage width
     * @param height stage height
     */
    @Override
    public void resize(int width, int height) {
        blockWidth = getWidth() / game.getMap().getWidth();
        blockHeight = getHeight() / game.getMap().getHeight();
    }
}
