package com.gasis.rts.logic.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.resources.Resources;

/**
 * Anything that can be rendered to the screen
 */
public interface Renderable {

    /**
     * Renders the object to the screen
     *
     * @param batch sprite batch to draw to
     * @param resources game assets
     */
    void render(SpriteBatch batch, Resources resources);
}
