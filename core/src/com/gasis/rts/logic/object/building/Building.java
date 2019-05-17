package com.gasis.rts.logic.object.building;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.resources.Resources;

/**
 * A building on the map
 */
public abstract class Building extends GameObject {

    /**
     * Renders the object to the screen
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     * @param delta     time elapsed since the last render
     */
    @Override
    public void render(SpriteBatch batch, Resources resources, float delta) {

    }
}
