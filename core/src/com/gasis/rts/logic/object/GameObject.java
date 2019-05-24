package com.gasis.rts.logic.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.resources.Resources;

/**
 * Represents all game objects: units, buildings
 */
public abstract class GameObject implements Damageable {

    // the name of the texture atlas to which the object's texture(s) belongs
    protected String atlas;

    // position of the object
    protected float x;
    protected float y;

    // dimensions of the object
    protected float width;
    protected float height;

    protected float maxHp; // maximum number of hit-points the object can have
    protected float hp; // current number of hit-points the object has

    // defence stat of the object
    protected float defence;

    // is the object passable or not
    protected boolean passable;

    /**
     * Does damage to the object
     *
     * @param attack attack stat of the attacker,
     *               damage will be calculated based on the object's defence
     */
    @Override
    public void doDamage(float attack) {
        hp -= attack / defence;
    }

    /**
     * Checks if the object is passable
     * @return
     */
    public boolean isPassable() {
        return passable;
    }

    /**
     * Sets the passable value for this object
     *
     * @param passable is the object passable or not
     */
    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    /**
     * Sets the maximum amount of hp the object can have
     *
     * @param maxHp new max hp
     */
    public void setMaxHp(float maxHp) {
        this.maxHp = maxHp;
    }

    /**
     * Sets the hp of the object
     *
     * @param hp new hp
     */
    public void setHp(float hp) {
        this.hp = hp;
    }

    /**
     * Sets the defence stat of the object
     *
     * @param defence new defence
     */
    public void setDefence(float defence) {
        this.defence = defence;
    }

    /**
     * Gets the maximum amount of hp the object can have
     *
     * @return
     */
    public float getMaxHp() {
        return maxHp;
    }

    /**
     * Gets the current hp of the object
     * @return
     */
    public float getHp() {
        return hp;
    }

    /**
     * Gets the defence stat of the object
     * @return
     */
    public float getDefence() {
        return defence;
    }

    /**
     * Gets the name of the of the texture atlas
     * @return
     */
    public String getAtlas() {
        return atlas;
    }

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
     * Gets the x coordinate of the object's center point
     *
     * @return
     */
    public float getCenterX() {
        return x + width / 2f;
    }

    /**
     * Gets the y coordinate of the object's center point
     *
     * @return
     */
    public float getCenterY() {
        return y + height / 2f;
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
     * Sets the x coordinate of the object's center point
     *
     * @param x new center x
     */
    public void setCenterX(float x) {
        this.x = x - width / 2f;
    }

    /**
     * Sets the y coordinate of the object's center point
     *
     * @param y new center y
     */
    public void setCenterY(float y) {
        this.y = y - height / 2f;
    }

    /**
     * Sets the name of the texture atlas used by this object
     *
     * @param atlas new name of the atlas
     */
    public void setAtlas(String atlas) {
        this.atlas = atlas;
    }

    /**
     * Updates the game object
     *
     * @param delta time elapsed since the last render
     */
    public abstract void update(float delta);

    /**
     * Renders the object to the screen
     *
     * @param batch sprite batch to draw to
     * @param resources game assets
     */
    public abstract void render(SpriteBatch batch, Resources resources);
}
