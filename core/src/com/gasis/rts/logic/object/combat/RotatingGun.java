package com.gasis.rts.logic.object.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.Updatable;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

import java.util.List;

import static com.gasis.rts.logic.object.unit.Unit.*;

/**
 * Represents any gun that rotates around a fixed point (tank gun, rocket launcher...)
 */
public class RotatingGun implements Updatable, Renderable {

    // texture atlas that holds the textures of the gun
    protected String atlas;

    // textures of the rotating gun
    // texture indexes must match the values of facing directions
    // defined in the Unit class
    protected List<String> textures;

    // coordinates of the point which the gun rotates around
    protected float x;
    protected float y;

    // gun's dimensions
    protected float width;
    protected float height;

    // the direction which the gun is pointed to
    // must have one of the facing direction values defined in the Unit class
    protected byte facingDirection = EAST;

    // the direction the gun is currently rotating to
    protected byte rotatingToDirection;

    // how much time has elapsed since the last rotation
    protected float timeSinceLastRotation;

    // the speed at which the gun rotates (expressed by rotations per seconds)
    protected float rotationSpeed;

    /**
     * Gets the rotation speed of the gun
     * @return
     */
    public float getRotationSpeed() {
        return rotationSpeed;
    }

    /**
     * Sets the rotation speed of the gun
     *
     * @param rotationSpeed the speed at which the gun rotates (in rotations per second)
     */
    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    /**
     * Gets the facing direction of the gun
     * @return
     */
    public byte getFacingDirection() {
        return facingDirection;
    }

    /**
     * Sets the facing direction of the gun
     *
     * @param facingDirection
     */
    public void setFacingDirection(byte facingDirection) {
        this.facingDirection = facingDirection;
    }

    /**
     * Gets the width of the gun
     * @return
     */
    public float getWidth() {
        return width;
    }

    /**
     * Gets the height of the gun
     * @return
     */
    public float getHeight() {
        return height;
    }

    /**
     * Sets the width of the gun
     *
     * @param width new width
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * Sets the height of the gun
     *
     * @param height new height
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Sets the x coordinate of the rotation point
     *
     * @param x new x coordinate
     */
    public void setRotationPointX(float x) {
        this.x = x;
    }

    /**
     * Sets the y coordinate of the rotation point
     *
     * @param y new y coordinate
     */
    public void setRotationPointY(float y) {
        this.y = y;
    }

    /**
     * Gets the x coordinate of the rotation point
     * @return
     */
    public float getRotationPointX() {
        return x;
    }

    /**
     * Gets the y coordinate of the rotation point
     * @return
     */
    public float getRotationPointY() {
        return y;
    }

    /**
     * Orders the gun to rotate to face the given direction
     *
     * @param direction the direction the gun should face
     */
    public void rotateToDirection(byte direction) {
        this.rotatingToDirection = direction;
        timeSinceLastRotation = 0;
    }

    /**
     * Updates the state of the gun
     *
     * @param delta time elapsed since the last update
     */
    @Override
    public void update(float delta) {
        updateFacingDirection(delta);
    }

    /**
     * Updates the facing direction of the gun
     *
     * @param delta time elapsed since the last update
     */
    @SuppressWarnings("Duplicates")
    protected void updateFacingDirection(float delta) {
        // update gun's rotation if it is currently rotating
        if (rotatingToDirection != NONE && timeSinceLastRotation >= 1f / rotationSpeed) {
            byte directionDiff = (byte) Math.abs(facingDirection - rotatingToDirection);

            byte directionIncrement = (byte) (facingDirection - rotatingToDirection < 0 ? 1 : -1);

            if (directionDiff > 4) {
                directionIncrement *= -1;
            }

            facingDirection += directionIncrement;

            if (facingDirection < 0) {
                facingDirection = 7;
            } else if (facingDirection > 7) {
                facingDirection = 0;
            }

            timeSinceLastRotation = 0;

            if (facingDirection == rotatingToDirection) {
                rotatingToDirection = NONE;

                // make the gun be able to rotate again immediately
                timeSinceLastRotation = 1f / rotationSpeed;
            }
        } else {
            timeSinceLastRotation += delta;
        }
    }

    /**
     * Renders the gun to the screen
     *
     * @param batch sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {
        batch.draw(
                resources.atlas(Constants.FOLDER_ATLASES + atlas).findRegion(textures.get(facingDirection)),
                x - width / 2f,
                y - height / 2f,
                width,
                height
        );
    }
}
