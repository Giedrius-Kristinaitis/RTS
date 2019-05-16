package com.gasis.rts.logic.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.resources.Resources;

/**
 * A layer on a block map
 */
public interface MapLayer {

    /**
     * Renders the layer to the screen
     *
     * @param batch sprite batch to draw to
     * @param resources game assets
     * @param delta time elapsed since the last render
     */
    void render(SpriteBatch batch, Resources resources, float delta);
}
