package com.gasis.rts.logic.map.blockmap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.resources.Resources;

/**
 * A normal map block without background, foreground, or content in it by default
 */
public abstract class NormalBlock implements Block {

    // block coordinates
    protected short x;
    protected short y;

    // what currently is on this block
    protected Object occupyingObject;

    /**
     * Checks if the block has something on it
     *
     * @return true if the block is occupied
     */
    public boolean isOccupied() {
        return occupyingObject != null;
    }

    /**
     * Gets the object that is occupying this block
     *
     * @return occupying object
     */
    public Object getOccupyingObject() {
        return occupyingObject;
    }

    /**
     * Occupies this block with an object
     *
     * @param object the object that occupies the block now
     */
    public void setOccupyingObject(Object object) {
        occupyingObject = object;
    }

    /**
     * Renders the block to the screen
     *
     * @param batch sprite batch to draw the map to
     * @param res object used to access assets
     * @param delta time elapsed since the last render
     */
    @Override
    public abstract void render(SpriteBatch batch, Resources res, float delta);
}
