package com.gasis.rts.logic.object.unit;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.animation.Animation;
import com.gasis.rts.logic.animation.AnimationFinishListener;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.logic.object.Fireable;
import com.gasis.rts.logic.object.OffensiveGameObject;
import com.gasis.rts.logic.object.Rotatable;
import com.gasis.rts.logic.object.combat.FireSource;
import com.gasis.rts.logic.object.combat.TargetReachListener;
import com.gasis.rts.math.Point;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a single unit on a map
 */
public class Unit extends OffensiveGameObject implements AnimationFinishListener, Fireable, Rotatable {

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

    // fire sources of the unit
    protected Map<String, FireSourceWithReloadAndFirePoints> fireSources = new HashMap<String, FireSourceWithReloadAndFirePoints>();

    // how many shots are fired in siege mode before reloading
    protected byte siegeModeShotCount = 1;

    // how often (in seconds) the shots are fired when in siege mode (only applied when
    // siegeModeShotCount > 1)
    protected float siegeModeShotInterval;

    // how much time has elapsed since the last siege mode shot
    protected float timeSinceLastShot;

    // how many shots are waiting to be fired
    protected byte enqueuedShots;

    // current target coordinates
    protected float targetX;
    protected float targetY;

    /**
     * Gets the siege mode shot count
     * @return
     */
    public byte getSiegeModeShotCount() {
        return siegeModeShotCount;
    }

    /**
     * Sets the siege mode shot count
     *
     * @param siegeModeShotCount new shot count
     */
    public void setSiegeModeShotCount(byte siegeModeShotCount) {
        this.siegeModeShotCount = siegeModeShotCount;
    }

    /**
     * Gets the siege mode shot interval
     * @return
     */
    public float getSiegeModeShotInterval() {
        return siegeModeShotInterval;
    }

    /**
     * Sets the shot interval for siege mode shots
     *
     * @param siegeModeShotInterval new shot interval
     */
    public void setSiegeModeShotInterval(float siegeModeShotInterval) {
        this.siegeModeShotInterval = siegeModeShotInterval;
    }

    /**
     * Creates a new fire source for the gun
     *
     * @param name name used to identify the source
     * @param gunCount how many guns are firing
     * @param type type of the projectile
     * @param scale scale of the projectile
     * @param flightTime projectile's flight time in seconds
     * @param firePoints from where the source fires in each facing direction
     * @param reloadSpeed reload speed in seconds
     * @param siegeModeReloadSpeed reload speed when in siege mode (in seconds)
     * @param listener listener that listens for the reach of the target
     */
    @SuppressWarnings("Duplicates")
    public void createFireSource(String name, byte gunCount, byte type, byte scale, float flightTime, List<Point> firePoints, float reloadSpeed, float siegeModeReloadSpeed, TargetReachListener listener) {
        FireSource source = new FireSource();
        source.setFireType(type);
        source.setProjectileScale(scale);
        source.setGunCount(gunCount);
        source.addTargetReachListener(listener);
        source.setFlightTime(flightTime);

        FireSourceWithReloadAndFirePoints combinedSource = new FireSourceWithReloadAndFirePoints(source, reloadSpeed, siegeModeReloadSpeed, firePoints);

        fireSources.put(name, combinedSource);
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
        }

        this.inSiegeMode = inSiegeMode;
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
     * Fires a shot at a target
     *
     * @param targetX x coordinate of the target
     * @param targetY y coordinate of the target
     */
    @Override
    public void fire(float targetX, float targetY) {
        if (inSiegeMode) {
            enqueuedShots = siegeModeShotCount;
        } else {
            enqueuedShots = 1;
        }

        timeSinceLastShot = siegeModeShotInterval;

        this.targetX = targetX;
        this.targetY = targetY;
    }

    /**
     * Updates the unit's firing
     *
     * @param delta time since the last update
     */
    protected void updateFiring(float delta) {
        if (enqueuedShots > 0 && timeSinceLastShot >= siegeModeShotInterval) {
            launchSingleShot();
            enqueuedShots--;
            timeSinceLastShot = 0;

            if (enqueuedShots == 0) {
                for (FireSourceWithReloadAndFirePoints source: fireSources.values()) {
                    source.timeSinceLastReload = 0;
                }
            }
        } else {
            timeSinceLastShot += delta;
        }
    }

    /**
     * Launches a single shot at the target
     */
    @SuppressWarnings("Duplicates")
    protected void launchSingleShot() {
        for (FireSourceWithReloadAndFirePoints source : fireSources.values()) {
            boolean fire = false;

            if ((inSiegeMode && source.timeSinceLastReload >= source.siegeModeReloadSpeed) || (!inSiegeMode && source.timeSinceLastReload >= source.reloadSpeed)) {
                fire = true;
            }

            if (fire) {
                source.fireSource.setX(source.firePoints.get(facingDirection).x);
                source.fireSource.setY(source.firePoints.get(facingDirection).y);
                source.fireSource.fire(facingDirection, targetX, targetY);
                firingTextureUsageDuration = 0;
            }
        }
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
        updateFireSources(delta);
        updateFiring(delta);

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
     * Updates fire sources
     *
     * @param delta time since the last update
     */
    protected void updateFireSources(float delta) {
        for (FireSourceWithReloadAndFirePoints source: fireSources.values()) {
            source.timeSinceLastReload += delta;
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
    }

    /**
     * Holds a fire source, it's reload speed and firing points
     */
    protected class FireSourceWithReloadAndFirePoints {

        // firing source
        protected FireSource fireSource;

        // reload speed (in seconds)
        protected float reloadSpeed;

        // reload speed when in siege mode (in seconds)
        protected float siegeModeReloadSpeed;

        // the coordinates the fire source fires from
        // indexes must match facing directions defined above
        protected List<Point> firePoints;

        // how much time has elapsed since the last reload (in seconds)
        protected float timeSinceLastReload;

        protected FireSourceWithReloadAndFirePoints(FireSource fireSource, float reloadSpeed, float siegeModeReloadSpeed, List<Point> firePoints) {
            this.fireSource = fireSource;
            this.reloadSpeed = reloadSpeed;
            this.firePoints = firePoints;
            this.siegeModeReloadSpeed = siegeModeReloadSpeed;
        }
    }
}
