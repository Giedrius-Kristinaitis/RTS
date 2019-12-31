package com.gasis.rts.logic.render;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.gasis.rts.resources.Resources;

/**
 * Simpler version of a renderable (cannot be added to a render queue)
 */
public interface SimpleRenderable {

    /**
     * Renders the object
     *
     * @param batch     batch to draw to
     * @param resources game's assets
     */
    void render(Batch batch, Resources resources);
}
