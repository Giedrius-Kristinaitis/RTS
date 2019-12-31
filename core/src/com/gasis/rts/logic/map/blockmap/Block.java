package com.gasis.rts.logic.map.blockmap;

import com.gasis.rts.logic.object.GameObject;

/**
 * A normal map block without background, foreground, or content in it by default
 */
public class Block {

    // how big the block is on the map
    public static final float BLOCK_WIDTH = 1.2f;
    public static final float BLOCK_HEIGHT = 0.8f;

    // block coordinates
    protected short x;
    protected short y;

    // what currently is on this block
    protected GameObject occupyingObject;

    // the second passable object occupying the block
    protected GameObject passableObject;

    // is the block passable or not (can units and buildings be on it or not)
    protected boolean passable;

    /**
     * Checks if the block has something on it
     *
     * @return true if the block is occupied
     */
    public boolean isOccupied() {
        return occupyingObject != null;
    }

    /**
     * Checks if the block is passable or not
     *
     * @return
     */
    public boolean isPassable() {
        return passable;
    }

    /**
     * Sets the passable value for this block
     *
     * @param passable new passable value
     */
    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    /**
     * Gets the object that is occupying this block
     *
     * @return occupying object
     */
    public GameObject getOccupyingObject() {
        if (occupyingObject != null) {
            return occupyingObject;
        } else {
            return passableObject;
        }
    }

    /**
     * Gets the block's passable object
     *
     * @return
     */
    public GameObject getPassableObject() {
        return passableObject;
    }

    /**
     * Occupies this block with an object
     *
     * @param object the object that occupies the block now
     */
    public void setOccupyingObject(GameObject object) {
        occupyingObject = object;
    }

    /**
     * Sets the block's passable object
     *
     * @param object passable object
     */
    public void setPassableObject(GameObject object) {
        passableObject = object;
    }

    /**
     * Gets the x coordinate of the block
     *
     * @return x coordinate
     */
    public short getX() {
        return x;
    }

    /**
     * Gets the y coordinate of the block
     *
     * @return y coordinate
     */
    public short getY() {
        return y;
    }

    /**
     * Sets the x coordinate of the block
     *
     * @param x new x coordinate
     */
    public void setX(short x) {
        this.x = x;
    }

    /**
     * Sets the y coordinate of the block
     *
     * @param y new y coordinate of the block
     */
    public void setY(short y) {
        this.y = y;
    }
}
