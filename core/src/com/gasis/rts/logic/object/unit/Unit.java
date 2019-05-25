package com.gasis.rts.logic.object.unit;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

import java.util.List;

/**
 * Represents a single unit on a map
 */
public abstract class Unit extends GameObject {

    // unit facing directions
    public static final byte NORTH = 0;
    public static final byte NORTH_EAST = 1;
    public static final byte EAST = 2;
    public static final byte SOUTH_EAST = 3;
    public static final byte SOUTH = 4;
    public static final byte SOUTH_WEST = 5;
    public static final byte WEST = 6;
    public static final byte NORTH_WEST = 7;

    // textures used by the unit (when standing still)
    // indexes of the textures must match the values of
    // the facing directions defined above
    protected List<String> stillTextures;

    // the index of the current texture (that is drawn when the unit is standing
    // still) in the texture list
    protected byte currentStillTexture;

    // the direction the unit is currently facing
    protected byte facingDirection;

    // is the unit moving or not
    protected boolean moving;

    // the animation that is played when the unit moves
    protected FrameAnimation movementAnimation;

    // the ids of the movement animations
    // indexes of animation ids must match the values of facing directions
    // defined in the Unit class
    protected List<Short> movementAnimationIds;

    /**
     * Checks if the unit is moving
     * @return
     */
    public boolean isMoving() {
        return moving;
    }

    /**
     * Sets the moving value of the unit
     *
     * @param moving is the unit moving or not
     */
    public void setMoving(boolean moving) {
        this.moving = moving;

        if (moving) {
            // create a new movement animation
            movementAnimation = FrameAnimationFactory.getInstance().create(
                    movementAnimationIds.get(facingDirection),
                    x,
                    y,
                    x,
                    y,
                    false
            );

            movementAnimation.setWidth(width);
            movementAnimation.setHeight(height);
        } else {
            movementAnimation = null;
        }
    }

    /**
     * Sets the ids of the movement animations
     *
     * @param animationIds list of animation ids
     */
    public void setMovementAnimationIds(List<Short> animationIds) {
        this.movementAnimationIds = animationIds;
    }

    /**
     * Gets the ids of the movement animations
     * @return
     */
    public Iterable<Short> getMovementAnimationIds() {
        return movementAnimationIds;
    }


    /**
     * Sets the direction the unit is facing
     *
     * @param facingDirection new direction the unit is facing
     */
    public void setFacingDirection(byte facingDirection) {
        this.facingDirection = facingDirection;

        currentStillTexture = facingDirection;
    }

    /**
     * Gets the direction the unit is facing
     * @return
     */
    public byte getFacingDirection() {
        return facingDirection;
    }

    /**
     * Sets the textures of the unit
     *
     * @param stillTextures new texture list
     */
    public void setStillTextures(List<String> stillTextures) {
        this.stillTextures = stillTextures;
    }

    /**
     * Gets the textures of the unit
     * @return
     */
    public Iterable<String> getStillTextures() {
        return stillTextures;
    }

    /**
     * Sets the current texture index
     *
     * @param currentStillTexture new texture index
     */
    public void setCurrentStillTexture(byte currentStillTexture) {
        this.currentStillTexture = currentStillTexture;
    }

    /**
     * Gets the index of the current unit's texture
     *
     * @return
     */
    public byte getCurrentStillTexture() {
        return currentStillTexture;
    }

    /**
     * Updates the game object
     *
     * @param delta time elapsed since the last render
     */
    @Override
    public void update(float delta) {
        if (moving && movementAnimation != null) {
            // update the movement animation
            movementAnimation.update(delta);

            movementAnimation.setCenterX(getCenterX());
            movementAnimation.setCenterY(getCenterY());
        }
    }

    /**
     * Renders the object to the screen
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {
        if (moving && movementAnimation != null) {
            // render the moving animation
            movementAnimation.render(batch, resources);
            return;
        }

        // render the still unit
        batch.draw(
                resources.atlas(Constants.FOLDER_ATLASES + atlas).findRegion(stillTextures.get(currentStillTexture)),
                x,
                y,
                width,
                height
        );
    }
}
