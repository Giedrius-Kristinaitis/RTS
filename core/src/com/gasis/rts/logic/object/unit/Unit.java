package com.gasis.rts.logic.object.unit;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.animation.Animation;
import com.gasis.rts.logic.animation.AnimationFinishListener;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.OffensiveGameObject;
import com.gasis.rts.logic.object.Rotatable;
import com.gasis.rts.logic.object.combat.*;
import com.gasis.rts.logic.object.unit.movement.Movable;
import com.gasis.rts.logic.object.unit.movement.MovementListener;
import com.gasis.rts.math.MathUtils;
import com.gasis.rts.math.Point;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a single unit on a map
 */
public class Unit extends OffensiveGameObject implements AnimationFinishListener, Rotatable, Aimable, Movable, DamageValueProvider {

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

    // should the unit move one block in the facing direction when finished rotating
    protected boolean moveWhenFinishedRotating;

    // the animation that is played when the unit moves
    protected FrameAnimation movementAnimation;

    // the names of the movement animations
    // indexes of animation names must match the values of facing directions
    // defined above
    protected List<String> movementAnimationNames;

    // is siege mode available for this unit
    protected boolean siegeModeAvailable;

    // is the unit in siege mode or not
    protected boolean inSiegeMode;

    // the direction the unit is facing when in siege mode (only used when the unit
    // can face one direction when in siege mode)
    protected byte siegeModeFacingDirection = EAST;

    // the names of siege mode transition animations
    // animation indexes must match the values of facing directions defined above
    // if the length of this list is not 8, then the 0-th element is
    // used as the transition animation and the unit can only transition to siege
    // mode when facing east
    protected List<String> siegeModeTransitionAnimationNames;

    // the animation played when the unit transitions into siege mode
    protected FrameAnimation siegeModeTransitionAnimation;

    // the facing direction the unit is currently rotating to achieve
    protected byte rotatingToDirection = NONE;

    // how much time in seconds has elapsed since the last unit's rotation
    protected float timeSinceLastRotation = 0;

    // should the unit switch to siege mode when it's body rotates to the correct
    // facing direction
    protected boolean enterSiegeModeWhenFinishedRotating = false;

    // should the unit switch to siege mode when it stops moving
    protected boolean enterSiegeModeWhenFinishedMoving = false;

    // will the unit be in siege mode after toggling or not
    protected boolean siegeModeToggleValue;

    // the new siege mode value after calling setInSiegeMode
    protected boolean newSiegeModeValue = false;

    // textures used when the unit fires
    // indexes of the textures must match the values of facing directions
    // defined above
    protected List<String> firingTextures;

    // textures used when the unit is in siege mode
    // indexes of the textures must match the values of facing directions
    // defined above
    protected List<String> siegeModeTextures;

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

    // should the still texture be rendered
    private boolean renderStillUnit = true;

    // should the unit's selection circle be rendered or not
    protected boolean renderSelectionCircle;

    // unit's movement listeners
    protected Set<MovementListener> movementListeners = new HashSet<MovementListener>();

    // unit's coordinates before moving
    protected float startingCenterX;
    protected float startingCenterY;

    // unit's coordinates after moving
    protected float finalCenterX;
    protected float finalCenterY;

    // the block the unit has occupied
    protected Point occupiedBlock;

    // target removal listeners
    protected Set<TargetRemovalListener> targetRemovalListeners = new HashSet<TargetRemovalListener>();

    // the object the unit is currently aiming at
    protected GameObject targetObject;

    // siege mode listeners
    protected Set<SiegeModeListener> siegeModeListeners = new HashSet<SiegeModeListener>();

    /**
     * Default class constructor
     * @param map
     */
    public Unit(BlockMap map) {
        super(map);
    }

    /**
     * Adds a siege mode listener
     *
     * @param listener listener to add
     */
    public void addSiegeModeListener(SiegeModeListener listener) {
        siegeModeListeners.add(listener);
    }

    /**
     * Removes a siege mode listener
     *
     * @param listener listener to remove
     */
    public void removeSiegeModeListeners(SiegeModeListener listener) {
        siegeModeListeners.remove(listener);
    }

    /**
     * Notifies siege mode listeners that the unit has just toggled siege mode
     */
    public void notifySiegeModeListeners() {
        for (SiegeModeListener listener: siegeModeListeners) {
            listener.siegeModeToggled(this);
        }
    }

    /**
     * Adds a target removal listener
     *
     * @param listener listener to add
     */
    public void addTargetRemovalListener(TargetRemovalListener listener) {
        targetRemovalListeners.add(listener);
    }

    /**
     * Removes a target removal listener
     *
     * @param listener listener to remove
     */
    public void removeTargetRemovalListener(TargetRemovalListener listener) {
        targetRemovalListeners.remove(listener);
    }

    /**
     * Notifies target removal listeners that the object's target has been removed
     */
    protected void notifyTargetRemovalListeners() {
        for (TargetRemovalListener listener: targetRemovalListeners) {
            listener.targetRemoved(this);
        }
    }

    /**
     * De-occupies the unit's occupied block
     */
    @Override
    public void deoccupyBlocks() {
        if (!moving) {
            short blockX = (short) (getCenterX() / Block.BLOCK_WIDTH);
            short blockY = (short) (getCenterY() / Block.BLOCK_HEIGHT);

            map.occupyBlock(blockX, blockY, null);
        } else {
            map.occupyBlock((short) occupiedBlock.x, (short) occupiedBlock.y, null);
        }
    }

    /**
     * Does damage to the object
     *
     * @param attack attack stat of the attacker,
     *               damage will be calculated based on the object's defence
     */
    @Override
    public void doDamage(float attack) {
        super.doDamage(attack);

        if (destroyed) {
            notifyUnableToMoveListeners();
        }
    }

    /**
     * Checks if the object can be safely removed from object list
     *
     * @return
     */
    @Override
    public boolean canBeRemoved() {
        if (firingLogic != null) {
            return firingLogic.canBeRemoved();
        }

        return true;
    }

    /**
     * Gets damage value
     *
     * @return
     */
    @Override
    public float getDamage() {
        return inSiegeMode ? offensiveSpecs.getSiegeModeAttack() : offensiveSpecs.getAttack();
    }

    /**
     * Orders the object one block in the given direction
     *
     * @param direction movement direction
     */
    @Override
    public void move(byte direction) {
        if (moving || inSiegeMode || destroyed) {
            return;
        }

        if (direction != facingDirection) {
            rotateToDirection(direction);
            moveWhenFinishedRotating = true;
        } else {
            if (!moveOneBlockForward()) {
                return;
            }
        }
    }

    /**
     * Checks if the block in front of the unit is available
     * @return
     */
    protected boolean destinationAvailable() {
        Point destination = getDestinationBlock();

        if (map.isBlockOccupied((short) destination.x, (short) destination.y)
            || !map.isBlockPassable((short) destination.x, (short) destination.y)) {
            return false;
        }

        return true;
    }

    /**
     * Checks if the block in front of the unit is out of the map's bounds
     * @return
     */
    protected boolean forwardBlockOutOfMapBounds() {
        Point destination = getDestinationBlock();

        if (destination.x < 0 || destination.x >= map.getHeight() || destination.y < 0 || destination.y >= map.getHeight()) {
            return true;
        }

        return false;
    }

    /**
     * Moves the unit one block forward
     *
     * @return true if the unit has started moving
     */
    protected boolean moveOneBlockForward() {
        if (forwardBlockOutOfMapBounds() || !destinationAvailable() || destroyed) {
            notifyDestinationListeners();
            return false;
        }

        initializeMovementCoordinates();
        changeOccupiedBlock();
        moving = true;
        notifyMovementStartListeners();

        return true;
    }

    /**
     * Changes the block the unit occupies
     */
    protected void changeOccupiedBlock() {
        map.occupyBlock((short) (startingCenterX / Block.BLOCK_WIDTH), (short) (startingCenterY / Block.BLOCK_HEIGHT), null);

        Point destination = getDestinationBlock();

        map.occupyBlock((short) destination.x, (short) destination.y, this);
        occupiedBlock = destination;
    }

    /**
     * Initializes the unit's movement coordinates: the starting and end point
     */
    protected void initializeMovementCoordinates() {
        startingCenterX = getCenterX();
        startingCenterY = getCenterY();

        Point destination = getDestinationBlock();

        finalCenterX = destination.x * Block.BLOCK_WIDTH + Block.BLOCK_WIDTH / 2f;
        finalCenterY = destination.y * Block.BLOCK_HEIGHT + Block.BLOCK_HEIGHT / 2f;
    }

    /**
     * Gets the unit's destination block
     * @return
     */
    protected Point getDestinationBlock() {
        Point destination = new Point();

        short blockX = (short) (getCenterX() / Block.BLOCK_WIDTH);
        short blockY = (short) (getCenterY() / Block.BLOCK_HEIGHT);

        switch (facingDirection) {
            case NORTH:
                destination.x = blockX;
                destination.y = blockY + 1;
                break;
            case NORTH_EAST:
                destination.x = blockX + 1;
                destination.y = blockY + 1;
                break;
            case EAST:
                destination.x = blockX + 1;
                destination.y = blockY;
                break;
            case SOUTH_EAST:
                destination.x = blockX + 1;
                destination.y = blockY - 1;
                break;
            case SOUTH:
                destination.x = blockX;
                destination.y = blockY - 1;
                break;
            case SOUTH_WEST:
                destination.x = blockX - 1;
                destination.y = blockY - 1;
                break;
            case WEST:
                destination.x = blockX - 1;
                destination.y = blockY;
                break;
            case NORTH_WEST:
                destination.x = blockX - 1;
                destination.y = blockY + 1;
                break;
        }

        return destination;
    }

    /**
     * Updates the unit's movement
     *
     * @param delta time elapsed since the last update
     */
    protected void updateMovement(float delta) {
        if (moving) {
            setCenterX(getCenterX() + (finalCenterX - startingCenterX) / 4 * offensiveSpecs.getSpeed() * delta);
            setCenterY(getCenterY() + (finalCenterY - startingCenterY) / 4 * offensiveSpecs.getSpeed() * delta);

            if (Math.abs(getCenterX() - finalCenterX) < 0.05f && Math.abs(getCenterY() - finalCenterY) < 0.05f) {
                moving = false;
                notifyDestinationListeners();

                if (enterSiegeModeWhenFinishedMoving) {
                    handleSiegeModeTransition();
                    notifyUnableToMoveListeners();
                    enterSiegeModeWhenFinishedMoving = false;
                }
            }
        }
    }

    /**
     * Adds a movement listener
     *
     * @param listener listener to add
     */
    public void addMovementListener(MovementListener listener) {
        movementListeners.add(listener);
    }

    /**
     * Removes a movement listener
     *
     * @param listener listener to remove
     */
    public void removeMovementListener(MovementListener listener) {
        movementListeners.remove(listener);
    }

    /**
     * Notifies unit's movement listeners that the unit has started moving
     */
    protected void notifyMovementStartListeners() {
        for (MovementListener listener: movementListeners) {
            listener.startedMoving(this);
        }
    }

    /**
     * Notifies movement listeners that the unit is unable to move in it's current state
     */
    protected void notifyUnableToMoveListeners() {
        for (MovementListener listener: movementListeners) {
            listener.unableToMoveInCurrentState(this);
        }
    }

    /**
     * Notifies unit's movement listeners that the unit has reached it's destination
     */
    protected void notifyDestinationListeners() {
        for (MovementListener listener: movementListeners) {
            listener.stoppedMoving(this);
        }
    }

    /**
     * Aims at the specified target coordinates
     *
     * @param targetX x of the target
     * @param targetY y of the target
     */
    @Override
    public void aimAt(float targetX, float targetY) {
        target = new Point(targetX, targetY);
        targetObject = null;
    }

    /**
     * Aims at the specified object
     *
     * @param target object to aim at
     */
    @Override
    public void aimAt(GameObject target) {
        targetObject = target;
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
     * Removes the current target
     */
    @Override
    public void removeTarget() {
        target = null;
        targetObject = null;

        notifyTargetRemovalListeners();
    }

    /**
     * Removes all enqueued shots
     */
    @Override
    public void removeEnqueuedShots() {
        if (firingLogic != null) {
            firingLogic.removeEnqueuedShots();
        }
    }

    /**
     * Checks if the object is aimed at the ground or not
     *
     * @return
     */
    @Override
    public boolean aimedAtGround() {
        return target != null && targetObject == null;
    }

    /**
     * Adds a target reach listener
     *
     * @param listener listener to add
     */
    public void addTargetReachedListener(TargetReachListener listener) {
        if (firingLogic != null) {
            firingLogic.addTargetReachedListener(listener);
        }
    }

    /**
     * Removes a target reach listener
     *
     * @param listener listener to remove
     */
    public void removeTargetReachListener(TargetReachListener listener) {
        if (firingLogic != null) {
            firingLogic.removeTargetReachListener(listener);
        }
    }

    /**
     * Sets the textures used by the unit when it's in siege mode
     *
     * @param siegeModeTextures names of siege mode textures
     */
    public void setSiegeModeTextures(List<String> siegeModeTextures) {
        this.siegeModeTextures = siegeModeTextures;
    }

    /**
     * Gets the siege mode textures the unit is using
     * @return
     */
    public Iterable<String> getSiegeModeTextures() {
        return siegeModeTextures;
    }

    /**
     * Sets the firing logic for the unit
     *
     * @param firingLogic new firing logic
     */
    public void setFiringLogic(FiringLogic firingLogic) {
        firingLogic.setDamageProvider(this);
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
            notifySiegeModeListeners();
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
            newSiegeModeValue = inSiegeMode;

            if (moving) {
                enterSiegeModeWhenFinishedMoving = true;
            } else {
                handleSiegeModeTransition();
            }
        }
    }

    /**
     * Handles the logic of transitioning to siege mode
     */
    protected void handleSiegeModeTransition() {
        if (siegeModeTransitionAnimationNames.size() != 8) {
            if (facingDirection == siegeModeFacingDirection) {
                rotatingToDirection = NONE;
                enterSiegeModeWhenFinishedRotating = false;
                siegeModeToggleValue = newSiegeModeValue;
                toggleSiegeMode();
            } else if (newSiegeModeValue) {
                rotateToDirection(siegeModeFacingDirection);
                enterSiegeModeWhenFinishedRotating = true;
                siegeModeToggleValue = true;
            }
        } else {
            toggleSiegeMode();
        }

        if (firingLogic != null && firingLogic.hasEnqueuedShots()) {
            firingLogic.removeEnqueuedShots();
        }
    }

    /**
     * Toggles siege mode
     */
    protected void toggleSiegeMode() {
        if (inSiegeMode == siegeModeToggleValue) {
            return;
        }

        inSiegeMode = siegeModeToggleValue;

        if (inSiegeMode) {
            facingDirection = siegeModeTextures.size() != 8 ? siegeModeFacingDirection : facingDirection;
        }

        createSiegeModeTransitionAnimation(!inSiegeMode);
    }

    /**
     * Rotates the unit if required in order for it to face the specified direction
     *
     * @param facingDirection new facing direction
     */
    @Override
    public void rotateToDirection(byte facingDirection) {
        if ((rotatingToDirection != NONE && enterSiegeModeWhenFinishedRotating) || inSiegeMode && siegeModeTextures.size() == 1) {
            return;
        }

        if (rotatingToDirection != facingDirection && this.facingDirection != facingDirection) {
            rotatingToDirection = facingDirection;
            timeSinceLastRotation = 0;
        }
    }

    /**
     * Creates a new instance of the correct siege mode transition animation
     *
     * @param reverse is the siege mode transition animation reversed or not
     */
    protected void createSiegeModeTransitionAnimation(boolean reverse) {
        if (!siegeModeAvailable || siegeModeTransitionAnimation != null) {
            return;
        }

        String animationName = siegeModeTransitionAnimationNames.size() == 8 ? siegeModeTransitionAnimationNames.get(facingDirection) : siegeModeTransitionAnimationNames.get(0);

        siegeModeTransitionAnimation = FrameAnimationFactory.getInstance().create(
                animationName,
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
     * Sets the animation names for siege mode transition animations
     *
     * @param siegeModeTransitionAnimationNames new animation names
     */
    public void setSiegeModeTransitionAnimationNames(List<String> siegeModeTransitionAnimationNames) {
        this.siegeModeTransitionAnimationNames = siegeModeTransitionAnimationNames;
    }

    /**
     * Gets the animation names for siege mode transitions
     * @return
     */
    public Iterable<String> getSiegeModeTransitionAnimationNames() {
        return siegeModeTransitionAnimationNames;
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

        if (moving) {
            createMovementAnimation();
        } else {
            movementAnimation = null;
        }
    }

    /**
     * Creates a new instance of the movement animation if movement animation ids
     * are present
     */
    protected void createMovementAnimation() {
        if (moving && movementAnimationNames != null) {
            // create a new movement animation
            movementAnimation = FrameAnimationFactory.getInstance().create(
                    movementAnimationNames.get(facingDirection),
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
     * Sets the names of the movement animations
     *
     * @param animationIds list of animation names
     */
    public void setMovementAnimationNames(List<String> animationIds) {
        this.movementAnimationNames = animationIds;
    }

    /**
     * Gets the names of the movement animations
     * @return
     */
    public Iterable<String> getMovementAnimationNames() {
        return movementAnimationNames;
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
     * Checks if the unit is currently rotating
     * @return
     */
    public boolean isRotating() {
        return rotatingToDirection != NONE;
    }

    /**
     * Updates the game object
     *
     * @param delta time elapsed since the last render
     */
    @Override
    public void update(float delta) {
        if (firingLogic != null && firingLogic.update(siegeModeTransitionAnimation != null, inSiegeMode, facingDirection, delta, getCenterX(), getCenterY()) && rotatingToDirection == NONE) {
            // reset the firing texture's usage time
            firingTextureTime = 0;
        }

        if (!destroyed) {
            // if there is an instance of the siege mode transition animation update it and do nothing else
            if (siegeModeTransitionAnimation != null) {
                siegeModeTransitionAnimation.update(delta);
                return;
            }

            updateBodyFacingDirection(delta);
            updateTarget();
            updateMovement(delta);

            // update fire texture's time
            firingTextureTime += delta;

            // update the movement animation
            if (moving && movementAnimation != null) {
                movementAnimation.update(delta);

                movementAnimation.setCenterX(getCenterX());
                movementAnimation.setCenterY(getCenterY());
            }
        }
    }

    /**
     * Updates the target logic
     */
    @SuppressWarnings("Duplicates")
    protected void updateTarget() {
        if (targetObject != null) {
            if (!targetObject.isDestroyed()) {
                if (target != null) {
                    target.x = targetObject.getCenterX();
                    target.y = targetObject.getCenterY();
                } else {
                    target = new Point(targetObject.getCenterX(), targetObject.getCenterY());
                }
            } else {
                target = null;
                targetObject = null;
                notifyTargetRemovalListeners();

                if (firingLogic != null && target == null && targetObject == null) {
                    firingLogic.removeEnqueuedShots();
                }
            }
        }

        if (firingLogic != null && target != null && !moving) {
            rotateToDirection(CombatUtils.getFacingDirection(getCenterX(), getCenterY(), target.x, target.y));

            if (rotatingToDirection == NONE && inSiegeMode) {
                if (MathUtils.distance(getCenterX() / Block.BLOCK_WIDTH, target.x / Block.BLOCK_WIDTH, getCenterY() / Block.BLOCK_HEIGHT, target.y / Block.BLOCK_HEIGHT) <= offensiveSpecs.getSiegeModeAttackRange()) {
                    firingLogic.target.x = target.x;
                    firingLogic.target.y = target.y;
                    firingLogic.enqueueShots(inSiegeMode);
                } else {
                    firingLogic.removeEnqueuedShots();
                }
            } else if (rotatingToDirection == NONE && !inSiegeMode) {
                if (MathUtils.distance(getCenterX() / Block.BLOCK_WIDTH, target.x / Block.BLOCK_WIDTH, getCenterY() / Block.BLOCK_HEIGHT, target.y / Block.BLOCK_HEIGHT) <= offensiveSpecs.getAttackRange()) {
                    firingLogic.target.x = target.x;
                    firingLogic.target.y = target.y;
                    firingLogic.enqueueShots(inSiegeMode);
                } else {
                    firingLogic.removeEnqueuedShots();
                }
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
                    enterSiegeModeWhenFinishedRotating = false;
                    toggleSiegeMode();
                }

                // move if required
                else if (moveWhenFinishedRotating) {
                    moveWhenFinishedRotating = false;
                    moveOneBlockForward();
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
        if (!destroyed) {
            renderSelectionCircle(batch, resources);

            // render the siege mode transition animation if present
            if (siegeModeTransitionAnimation != null) {
                siegeModeTransitionAnimation.render(batch, resources);
                renderStillUnit = false;
            }

            if (siegeModeTransitionAnimation == null && moving && movementAnimation != null) {
                // render the moving animation
                movementAnimation.render(batch, resources);
                renderStillUnit = false;
            }

            // render the firing texture if is being used
            if (siegeModeTransitionAnimation == null && movementAnimation == null && firingTextures != null && firingTextureTime <= firingTextureUsageDuration) {
                batch.draw(
                        resources.atlas(Constants.FOLDER_ATLASES + atlas).findRegion(firingTextures.get(facingDirection)),
                        x,
                        y,
                        width,
                        height
                );
                renderStillUnit = false;
            }

            // render the still unit
            if (renderStillUnit) {
                if (!inSiegeMode || rotatingToDirection != NONE) {
                    batch.draw(
                            resources.atlas(Constants.FOLDER_ATLASES + atlas).findRegion(stillTextures.get(facingDirection)),
                            x,
                            y,
                            width,
                            height
                    );
                } else {
                    batch.draw(
                            resources.atlas(Constants.FOLDER_ATLASES + atlas).findRegion(siegeModeTextures.size() != 8 ? siegeModeTextures.get(0) : siegeModeTextures.get(facingDirection)),
                            x,
                            y,
                            width,
                            height
                    );
                }
            }
        }

        if (firingLogic != null) {
            firingLogic.render(batch, resources);
        }

        renderStillUnit = true;

        // render unit's hp
        if (!destroyed) {
            renderHp(batch, resources);
        }
    }

    /**
     * Renders the unit's selection circle if required
     *
     * @param batch sprite batch to draw to
     * @param resources game's assets
     */
    protected void renderSelectionCircle(SpriteBatch batch, Resources resources) {
        if (renderSelectionCircle) {
            batch.draw(resources.atlas(Constants.GENERAL_TEXTURE_ATLAS).findRegion(Constants.UNIT_SELECTION_CIRCLE_TEXTURE),
                    x, y, width, height);
        }
    }

    /**
     * Toggles unit's selection circle's rendering
     *
     * @param renderSelectionCircle should the selection circle be rendered or not
     */
    public void setRenderSelectionCircle(boolean renderSelectionCircle) {
        this.renderSelectionCircle = renderSelectionCircle;
    }
}
