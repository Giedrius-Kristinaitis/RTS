package com.gasis.rts.logic.object.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.Updatable;
import com.gasis.rts.resources.Resources;

/**
 * Handles game object destruction
 */
public class DestructionHandler implements TargetReachListener, Renderable, Updatable {

    /**
     * Gets called when a projectile reaches it's target
     *
     * @param targetX   x coordinate of the target
     * @param targetY   y coordinate of the target
     * @param damage    the damage caused by the projectile
     * @param explosive is the projectile that reached the target explosive or not
     * @param scale     the scale of the projectile
     */
    @Override
    public void targetReached(float targetX, float targetY, float damage, boolean explosive, byte scale) {
        
    }

    /**
     * Updates the state of the object
     *
     * @param delta time elapsed since the last update
     */
    @Override
    public void update(float delta) {

    }

    /**
     * Renders the object to the screen
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {

    }
}
