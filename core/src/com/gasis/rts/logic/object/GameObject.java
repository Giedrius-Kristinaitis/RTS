package com.gasis.rts.logic.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.resources.Resources;

/**
 * Represents any game object: building, unit...
 */
public abstract class GameObject {

    // position of the object
    protected float x;
    protected float y;

    // dimensions of the object
    protected float width;
    protected float height;

    /**
     * Gets the x coordinate of the object
     * @return
     */
    public float getX() {
        return x;
    }

    /**
     * Gets the y coordinate of the object
     * @return
     */
    public float getY() {
        return y;
    }

    /**
     * Gets the width of the building
     * @return
     */
    public float getWidth() {
        return width;
    }

    /**
     * Gets the height of the building
     * @return
     */
    public float getHeight() {
        return height;
    }

    /**
     * Sets the x coordinate of the object
     *
     * @param x new x coordinate
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Sets the y coordinate of the object
     *
     * @param y new y coordinate
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Sets the width of the building
     *
     * @param width new width
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * Sets the height of the building
     *
     * @param height new height
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Renders the object to the screen
     *
     * @param batch sprite batch to draw to
     * @param resources game assets
     * @param delta time elapsed since the last render
     */
    public abstract void render(SpriteBatch batch, Resources resources, float delta);
}
