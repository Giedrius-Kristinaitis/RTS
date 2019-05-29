package com.gasis.rts.logic.object.unit;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.animation.Animation;
import com.gasis.rts.logic.animation.AnimationFinishListener;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

import java.util.List;

/**
 * Represents a single unit on a map
 */
public class Unit extends GameObject implements AnimationFinishListener {

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
    // defined above
    protected List<Short> movementAnimationIds;

    // is siege mode available for this unit
    protected boolean siegeModeAvailable;

    // is the unit in siege mode or not
    protected boolean inSiegeMode;

    // the ids of siege mode transition animations
    // animation indexes must match the values of facing directions defined above
    // if the length of this list is not 8, then the 0-th element is
    // used as the transition animation and the unit can only transition to siege
    // mode when facing east
    protected List<Short> siegeModeTransitionAnimationIds;

    // the animation played when the unit transitions into siege mode
    protected FrameAnimation siegeModeTransitionAnimation;

    /**
     * Notifies the observer that the animation has finished
     *
     * @param animation the animation that just finished
     */
    @Override
    public void finished(Animation animation) {
        if (animation == siegeModeTransitionAnimation) {
            siegeModeTransitionAnimation = null;
        }
    }

    /**
     * Checks if the unit is currently in siege mode
     * @return
     */
    public boolean isInSiegeMode() {
        return inSiegeMode;
    }

    /**
     * Toggles unit's siege mode
     *
     * @param inSiegeMode is the unit in siege mode now
     */
    public void setInSiegeMode(boolean inSiegeMode) {
        this.inSiegeMode = inSiegeMode;

        if (siegeModeAvailable && inSiegeMode) {
            // enter siege mode
            createSiegeModeTransitionAnimation();
        } else if (!inSiegeMode) {
            // get out of siege mode

        }
    }

    /**
     * Creates a new instance of the correct siege mode transition animation
     */
    protected void createSiegeModeTransitionAnimation() {
        short animationId = siegeModeTransitionAnimationIds.size() == 8 ? siegeModeTransitionAnimationIds.get(facingDirection) : 0;

        siegeModeTransitionAnimation = FrameAnimationFactory.getInstance().create(
                animationId,
                x,
                y,
                x,
                y,
                false
        );

        siegeModeTransitionAnimation.setWidth(width);
        siegeModeTransitionAnimation.setHeight(height);
    }

    /**
     * Sets the animation ids for siege mode transition animations
     *
     * @param siegeModeTransitionAnimationIds new animation ids
     */
    public void setSiegeModeTransitionAnimationIds(List<Short> siegeModeTransitionAnimationIds) {
        this.siegeModeTransitionAnimationIds = siegeModeTransitionAnimationIds;
    }

    /**
     * Gets the animation ids for siege mode transitions
     * @return
     */
    public Iterable<Short> getSiegeModeTransitionAnimationIds() {
        return siegeModeTransitionAnimationIds;
    }

    /**
     * Checks if siege mode is available for the unit
     * @return
     */
    public boolean isSiegeModeAvailable() {
        return siegeModeAvailable;
    }

    /**
     * Sets the siege mode availability for the unit
     *
     * @param siegeModeAvailable is the siege mode available or not
     */
    public void setSiegeModeAvailable(boolean siegeModeAvailable) {
        this.siegeModeAvailable = siegeModeAvailable;
    }

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

        createMovementAnimation();
    }

    /**
     * Creates a new instance of the movement animation if movement animation ids
     * are present
     */
    protected void createMovementAnimation() {
        if (moving && movementAnimationIds != null) {
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

        createMovementAnimation();
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
        if (siegeModeTransitionAnimation != null) {
            siegeModeTransitionAnimation.update(delta);
            return;
        }

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
        if (siegeModeTransitionAnimation != null) {
            siegeModeTransitionAnimation.render(batch, resources);
            return;
        }

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
