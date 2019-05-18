package com.gasis.rts.logic.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.resources.Resources;

/**
 * An animation
 */
public interface Animation {

    /**
     * Updates the animation
     *
     * @param delta time elapsed since the last render
     */
    void update(float delta);

    /**
     * Renders the animation
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    void render(SpriteBatch batch, Resources resources);
}
