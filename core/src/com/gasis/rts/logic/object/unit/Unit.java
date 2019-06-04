package com.gasis.rts.logic.object.unit;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.animation.Animation;
import com.gasis.rts.logic.animation.AnimationFinishListener;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.logic.object.OffensiveGameObject;
import com.gasis.rts.logic.object.Rotatable;
import com.gasis.rts.logic.object.combat.Aimable;
import com.gasis.rts.logic.object.combat.FireSource;
import com.gasis.rts.logic.object.combat.FiringLogic;
import com.gasis.rts.math.MathUtils;
import com.gasis.rts.math.Point;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

import java.util.List;

/**
 * Represents a single unit on a map
 */
public class Unit extends OffensiveGameObject implements AnimationFinishListener, Rotatable, Aimable {

    // unit facing directions
    public static final byte NONE = -1;
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

    // the direction the unit is currently facing
    protected byte facingDirection = EAST;

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

    // the direction the unit is facing when in siege mode (only used when the unit
    // can face one direction when in siege mode)
    protected byte siegeModeFacingDirection = EAST;

    // the ids of siege mode transition animations
    // animation indexes must match the values of facing directions defined above
    // if the length of this list is not 8, then the 0-th element is
    // used as the transition animation and the unit can only transition to siege
    // mode when facing east
    protected List<Short> siegeModeTransitionAnimationIds;

    // the animation played when the unit transitions into siege mode
    protected FrameAnimation siegeModeTransitionAnimation;

    // the facing direction the unit is currently rotating to achieve
    protected byte rotatingToDirection = NONE;

    // how much time in seconds has elapsed since the last unit's rotation
    protected float timeSinceLastRotation = 0;

    // should the unit switch to siege mode when it's body rotates to the correct
    // facing direction
    protected boolean enterSiegeModeWhenFinishedRotating = false;

    // textures used when the unit fires
    // indexes of the textures must match the values of facing directions
    // defined above
    protected List<String> firingTextures;

    // does the unit use the firing texture for it's facing direction
    // when reloading
    protected boolean stayInFiringTextureWhenReloading = false;

    // how long the unit stays in the firing texture when firing (in seconds)
    protected float firingTextureUsageDuration;

    // how long has the firing texture been used so far
    protected float firingTextureTime;

    // the gun's firing logic
    protected FiringLogic firingLogic;

    // target of the unit
    protected Point target;

    /**
     * Aims at the specified target coordinates
     *
     * @param targetX x of the target
     * @param targetY y of the target
     */
    @Override
    public void aimAt(float targetX, float targetY) {
        target = new Point(targetX, targetY);
    }

    /**
     * Checks if this object has a target
     *
     * @return
     */
    @Override
    public boolean hasTarget() {
        return target != null;
    }

    /**
     * Sets the firing logic for the unit
     *
     * @param firingLogic new firing logic
     */
    public void setFiringLogic(FiringLogic firingLogic) {
        this.firingLogic = firingLogic;
    }

    /**
     * Gets the firing logic of the unit
     * @return
     */
    public FiringLogic getFiringLogic() {
        return firingLogic;
    }

    /**
     * Adds a new fire source to the unit
     *
     * @param name name used to identify the source
     * @param source fire source to add
     */
    public void addFireSource(String name, FireSource source) {
        firingLogic.addFireSource(name, source);
    }

    /**
     * Gets the duration of the unit's firing texture usage after firing a shot
     * @return
     */
    public float getFiringTextureUsageDuration() {
        return firingTextureUsageDuration;
    }

    /**
     * Sets the duration of the unit's firing texture usage after firing a shot
     *
     * @param firingTextureUsageDuration usage duration in seconds
     */
    public void setFiringTextureUsageDuration(float firingTextureUsageDuration) {
        this.firingTextureUsageDuration = firingTextureUsageDuration;
    }

    /**
     * Checks if the unit stays in it's firing texture when reloading
     */
    public boolean isStayInFiringTextureWhenReloading() {
        return stayInFiringTextureWhenReloading;
    }

    /**
     * Makes the unit stay/not stay in it's firing texture when reloading
     *
     * @param stayInFiringTextureWhenReloading new stay value
     */
    public void setStayInFiringTextureWhenReloading(boolean stayInFiringTextureWhenReloading) {
        this.stayInFiringTextureWhenReloading = stayInFiringTextureWhenReloading;
    }

    /**
     * Sets the firing textures for the unit
     *
     * @param firingTextures new firing textures
     */
    public void setFiringTextures(List<String> firingTextures) {
        this.firingTextures = firingTextures;
    }

    /**
     * Gets the firing textures of the unit
     * @return
     */
    public Iterable<String> getFiringTextures() {
        return firingTextures;
    }

    /**
     * Gets the direction the unit is facing when in siege mode
     * @return
     */
    public byte getSiegeModeFacingDirection() {
        return siegeModeFacingDirection;
    }

    /**
     * Sets the unit's facing direction when in siege mode
     *
     * @param siegeModeFacingDirection new facing direction
     */
    public void setSiegeModeFacingDirection(byte siegeModeFacingDirection) {
        this.siegeModeFacingDirection = siegeModeFacingDirection;
    }

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
        if (!siegeModeAvailable || (rotatingToDirection != NONE && enterSiegeModeWhenFinishedRotating) || siegeModeTransitionAnimation != null) {
            return;
        }

        if (this.inSiegeMode != inSiegeMode) {
            // enter or leave siege mode
            if (siegeModeTransitionAnimationIds.size() != 8) {
                if (facingDirection == siegeModeFacingDirection) {
                    createSiegeModeTransitionAnimation(!inSiegeMode);
                } else {
                    rotateToDirection(siegeModeFacingDirection);
                    enterSiegeModeWhenFinishedRotating = true;
                }
            } else {
                createSiegeModeTransitionAnimation(!inSiegeMode);
            }

            if (firingLogic.hasEnqueuedShots()) {
                firingLogic.removeEnqueuedShots();
            }

            this.inSiegeMode = inSiegeMode;
        }
    }

    /**
     * Rotates the unit if required in order for it to face the specified direction
     *
     * @param facingDirection new facing direction
     */
    @Override
    public void rotateToDirection(byte facingDirection) {
        if (this.facingDirection != facingDirection) {
            rotatingToDirection = facingDirection;
            timeSinceLastRotation = 0;
        } else {
            rotatingToDirection = NONE;
        }
    }

    /**
     * Creates a new instance of the correct siege mode transition animation
     *
     * @param reverse is the siege mode transition animation reversed or not
     */
    protected void createSiegeModeTransitionAnimation(boolean reverse) {
        if (!siegeModeAvailable) {
            return;
        }

        short animationId = siegeModeTransitionAnimationIds.size() == 8 ? siegeModeTransitionAnimationIds.get(facingDirection) : siegeModeTransitionAnimationIds.get(0);

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
        siegeModeTransitionAnimation.addFinishListener(this);
        siegeModeTransitionAnimation.setReversed(reverse);
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
    protected void setFacingDirection(byte facingDirection) {
        this.facingDirection = facingDirection;

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
     * Updates the game object
     *
     * @param delta time elapsed since the last render
     */
    @Override
    public void update(float delta) {
        // if there is an instance of the siege mode transition animation update it and do nothing else
        if (siegeModeTransitionAnimation != null) {
            siegeModeTransitionAnimation.update(delta);
            return;
        }

        updateBodyFacingDirection(delta);
        updateTarget();

        if (firingLogic.update(inSiegeMode, facingDirection, delta, getCenterX(), getCenterY())) {
            // reset the firing texture's usage time
            firingTextureTime = 0;
        }

        // update fire texture's time
        firingTextureTime += delta;

        // update the movement animation
        if (moving && movementAnimation != null) {
            movementAnimation.update(delta);

            movementAnimation.setCenterX(getCenterX());
            movementAnimation.setCenterY(getCenterY());
        }
    }

    /**
     * Updates the target logic
     */
    @SuppressWarnings("Duplicates")
    protected void updateTarget() {
        if (target != null) {
            if (inSiegeMode && MathUtils.distance(getCenterX(), target.x, getCenterY(), target.y) <= offensiveSpecs.getSiegeModeAttackRange()) {
                firingLogic.target = target;
                firingLogic.enqueueShots(inSiegeMode);
            } else if (!inSiegeMode && MathUtils.distance(getCenterX(), target.x, getCenterY(), target.y) <= offensiveSpecs.getAttackRange()) {
                firingLogic.target = target;
                firingLogic.enqueueShots(inSiegeMode);
            }
        }
    }

    /**
     * Updates the facing direction of the unit
     *
     * @param delta time elapsed since the last update
     */
    @SuppressWarnings("Duplicates")
    protected void updateBodyFacingDirection(float delta) {
        // update unit's rotation if it is currently rotating
        if (rotatingToDirection != NONE && timeSinceLastRotation >= 1f / offensiveSpecs.getSpeed()) {
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

                // enter siege mode if required
                if (enterSiegeModeWhenFinishedRotating) {
                    createSiegeModeTransitionAnimation(!inSiegeMode);
                    enterSiegeModeWhenFinishedRotating = false;
                }
            }
        } else {
            timeSinceLastRotation += delta;
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
        // render the siege mode transition animation if present
        if (siegeModeTransitionAnimation != null) {
            siegeModeTransitionAnimation.render(batch, resources);
            return;
        }

        if (moving && movementAnimation != null) {
            // render the moving animation
            movementAnimation.render(batch, resources);
            return;
        }

        // render the firing texture if is being used
        if (firingTextures != null && firingTextureTime <= firingTextureUsageDuration) {
            batch.draw(
                    resources.atlas(Constants.FOLDER_ATLASES + atlas).findRegion(firingTextures.get(facingDirection)),
                    x,
                    y,
                    width,
                    height
            );
            return;
        }

        // render the still unit
        batch.draw(
                resources.atlas(Constants.FOLDER_ATLASES + atlas).findRegion(stillTextures.get(facingDirection)),
                x,
                y,
                width,
                height
        );

        firingLogic.render(batch, resources);
    }
}
