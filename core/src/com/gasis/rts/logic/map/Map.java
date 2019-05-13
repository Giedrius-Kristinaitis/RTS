package com.gasis.rts.logic.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.resources.Resources;

/**
 * Game map abstraction
 */
public interface Map {

    /**
     * Renders the map to the screen
     *
     * @param batch sprite batch to draw the map to
     * @param res object used to access assets
     * @param delta time elapsed since the last render
     */
    void render(SpriteBatch batch, Resources res, float delta);
}
