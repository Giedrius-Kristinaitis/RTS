package com.gasis.rts.logic.map.blockmap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.resources.Resources;

/**
 * A single block on the map
 */
public interface Block {

    // how big a single block is on the map
    float BLOCK_SIZE = 1;

    /**
     * Renders the block to the screen
     *
     * @param batch sprite batch to draw the map to
     * @param res object used to access assets
     * @param delta time elapsed since the last render
     */
    void render(SpriteBatch batch, Resources res, float delta);
}
