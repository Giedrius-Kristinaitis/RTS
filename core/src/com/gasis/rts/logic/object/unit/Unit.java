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
import com.gasis.rts.logic.object.building.Landmine;
import com.gasis.rts.logic.object.combat.*;
import com.gasis.rts.logic.object.unit.movement.Movable;
import com.gasis.rts.logic.object.unit.movement.MovementListener;
import com.gasis.rts.logic.object.unit.movement.MovementRequestHandler;
import com.gasis.rts.logic.object.unit.movement.PathInfoProvider;
import com.gasis.rts.logic.player.Player;
import com.gasis.rts.logic.render.RenderQueueInterface;
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
@SuppressWarnings("Duplicates")
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

    // the secondary target object the unit can fire at while moving to it's main target
    protected GameObject secondaryTargetObject;

    // the secondary target's point
    protected Point secondaryTarget;

    // siege mode listeners
    protected Set<SiegeModeListener> siegeModeListeners = new HashSet<SiegeModeListener>();

    // is the unit rotating to it's target or because it is moving
    protected boolean rotatingToTarget = false;

    // is the unit moving to it's target or not
    protected boolean movingToTarget = false;

    // used to make movement requests from the unit itself
    protected MovementRequestHandler movementRequestHandler;

    // the point the unit goes to after it destroys it's target and leaves siege mode
    protected Point pointToGoToAfterTargetDestroyed;

    // the point the unit goes to after it leaves siege mode
    protected Point pointToGoToAfterLeavingSiegeMode;

    // does the unit have to leave siege mode after it's target is destroyed
    protected boolean leaveSiegeModeAfterTargetDestroyed;

    // provides path info
    protected PathInfoProvider pathInfoProvider;

    // was the unit ordered to 'attack move'
    protected boolean attackMove;

    // the point to which the unit was ordered to 'attack move'
    protected Point attackMoveDestination;

    // the tech needed for siege mode to work
    protected String siegeModeRequiredTechId;

    // the last time the unit's path was found
    protected long lastPathFindingTimestamp;

    // is auto siege mode supported
    protected boolean autoSiegeModeSupported = true;

    /**
     * Default class constructor
     *
     * @param map
     */
    public Unit(BlockMap map) {
        super(map);
    }

    /**
     * Gets the timestamp at which the unit's path was last found
     *
     * @return
     */
    public long getLastPathFindingTimestamp() {
        return lastPathFindingTimestamp;
    }

    /**
     * Sets the timestamp of the last moment when the unit's path was found
     *
     * @param lastPathFindingTimestamp last timestamp
     */
    public void setLastPathFindingTimestamp(long lastPathFindingTimestamp) {
        this.lastPathFindingTimestamp = lastPathFindingTimestamp;
    }

    /**
     * Sets the tech that is required in order for siege mode to work
     *
     * @param siegeModeRequiredTechId siege mode tech
     */
    public void setSiegeModeRequiredTechId(String siegeModeRequiredTechId) {
        this.siegeModeRequiredTechId = siegeModeRequiredTechId;
    }

    /**
     * Sets the unit's attack move flag
     *
     * @param attackMove was the unit ordered to attack move
     */
    public void setAttackMove(boolean attackMove) {
        this.attackMove = attackMove;

        if (attackMove) {
            removeTarget();
            notifyTargetRemovalListeners();
        }
    }

    /**
     * Checks if the unit is 'attack-moving'
     *
     * @return
     */
    public boolean isAttackMove() {
        return attackMove;
    }

    /**
     * Sets the destination point of the unit's attack move
     *
     * @param attackMoveDestination attack move destination
     */
    public void setAttackMoveDestination(Point attackMoveDestination) {
        this.attackMoveDestination = attackMoveDestination;
    }

    /**
     * Sets the point to which the unit has to move when it leaves siege mode
     *
     * @param pointToGoToAfterLeavingSiegeMode point to go to
     */
    public void setPointToGoToAfterLeavingSiegeMode(Point pointToGoToAfterLeavingSiegeMode) {
        this.pointToGoToAfterLeavingSiegeMode = pointToGoToAfterLeavingSiegeMode;
    }

    /**
     * Sets the  path info provider
     *
     * @param pathInfoProvider new path info provider
     */
    public void setPathInfoProvider(PathInfoProvider pathInfoProvider) {
        this.pathInfoProvider = pathInfoProvider;
    }

    /**
     * Requests the unit to make a movement request
     *
     * @param x destination x
     * @param y destination y
     */
    public void requestToMove(short x, short y) {
        movementRequestHandler.handleMovementRequest(this, x, y);
    }

    /**
     * Sets the movement request handler that the unit will use
     *
     * @param movementRequestHandler new movement request handler
     */
    public void setMovementRequestHandler(MovementRequestHandler movementRequestHandler) {
        this.movementRequestHandler = movementRequestHandler;
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
        for (SiegeModeListener listener : siegeModeListeners) {
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
        for (TargetRemovalListener listener : targetRemovalListeners) {
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
            rotatingToTarget = false;
        } else {
            if (!moveOneBlockForward()) {
                return;
            }
        }
    }

    /**
     * Checks if the block in front of the unit is available
     *
     * @return
     */
    protected boolean destinationAvailable() {
        Point destination = getDestinationBlock();

        if ((map.isBlockOccupied((short) destination.x, (short) destination.y) && !map.getOccupyingObject((short) destination.x, (short) destination.y).isPassable())
                || !map.isBlockPassable((short) destination.x, (short) destination.y)) {
            return false;
        }

        return true;
    }

    /**
     * Checks if the block in front of the unit is out of the map's bounds
     *
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
        pointToGoToAfterTargetDestroyed = null;

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
     *
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

            if (MathUtils.distance(getCenterX(), startingCenterX, getCenterY(), startingCenterY) >= MathUtils.distance(finalCenterX, startingCenterX, finalCenterY, startingCenterY)) {
                moving = false;
                notifyDestinationListeners();

                if (enterSiegeModeWhenFinishedMoving) {
                    handleSiegeModeTransition();
                    notifyUnableToMoveListeners();
                    enterSiegeModeWhenFinishedMoving = false;
                }

                handleLandmine();
            }
        }
    }

    /**
     * Handles going over a landmine
     */
    protected void handleLandmine() {
        GameObject object = map.getOccupyingPassableObject((short) occupiedBlock.x, (short) occupiedBlock.y);

        if (object instanceof Landmine && !owner.isAllied(object.getOwner())) {
            ((Landmine) object).detonate();
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
        for (MovementListener listener : movementListeners) {
            listener.startedMoving(this);
        }
    }

    /**
     * Notifies movement listeners that the unit is unable to move in it's current state
     */
    protected void notifyUnableToMoveListeners() {
        for (MovementListener listener : movementListeners) {
            listener.unableToMoveInCurrentState(this);
        }
    }

    /**
     * Notifies unit's movement listeners that the unit has reached it's destination
     */
    protected void notifyDestinationListeners() {
        for (MovementListener listener : movementListeners) {
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

        if (attackMove) {
            notifyUnableToMoveListeners();
        }
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
     * Checks if the object has a target object
     *
     * @return
     */
    @Override
    public boolean hasTargetObject() {
        return targetObject != null;
    }

    /**
     * Gets target x
     *
     * @return
     */
    @Override
    public float getTargetX() {
        return target != null ? target.x : (targetObject != null ? targetObject.getCenterX() : (secondaryTarget != null ? secondaryTarget.x : (secondaryTargetObject != null ? secondaryTargetObject.getCenterX() : 0)));
    }

    /**
     * Gets target y
     *
     * @return
     */
    @Override
    public float getTargetY() {
        return target != null ? target.y : (targetObject != null ? targetObject.getCenterY() : (secondaryTarget != null ? secondaryTarget.y : (secondaryTargetObject != null ? secondaryTargetObject.getCenterY() : 0)));
    }

    /**
     * Checks if the unit has a secondary target
     *
     * @return
     */
    public boolean hasSecondaryTarget() {
        return secondaryTargetObject != null;
    }

    /**
     * Sets the unit's secondary target
     *
     * @param secondaryTargetObject new secondary target
     */
    public void setSecondaryTargetObject(GameObject secondaryTargetObject) {
        this.secondaryTargetObject = secondaryTargetObject;
    }

    /**
     * Removes the current target
     */
    @Override
    public void removeTarget() {
        target = null;
        targetObject = null;

        secondaryTarget = null;
        secondaryTargetObject = null;
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
     *
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
        firingLogic.setOwner(this);
        this.firingLogic = firingLogic;
    }

    /**
     * Gets the firing logic of the unit
     *
     * @return
     */
    public FiringLogic getFiringLogic() {
        return firingLogic;
    }

    /**
     * Adds a new fire source to the unit
     *
     * @param name   name used to identify the source
     * @param source fire source to add
     */
    public void addFireSource(String name, FireSource source) {
        firingLogic.addFireSource(name, source);
    }

    /**
     * Gets the duration of the unit's firing texture usage after firing a shot
     *
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
     *
     * @return
     */
    public Iterable<String> getFiringTextures() {
        return firingTextures;
    }

    /**
     * Gets the direction the unit is facing when in siege mode
     *
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

            if (inSiegeMode && leaveSiegeModeAfterTargetDestroyed) {
                if ((targetObject == null && target == null) || (targetObject != null && targetObject.isDestroyed())) {
                    handleLeavingAutoSiegeMode();
                }
            } else if (!inSiegeMode) {
                leaveSiegeModeAfterTargetDestroyed = false;

                if (pointToGoToAfterLeavingSiegeMode != null) {
                    requestToMove((short) pointToGoToAfterLeavingSiegeMode.x, (short) pointToGoToAfterLeavingSiegeMode.y);
                    pointToGoToAfterLeavingSiegeMode = null;
                    movingToTarget = false;
                }
            }
        }
    }

    /**
     * Checks if the unit is currently in siege mode
     *
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
        if (!siegeModeAvailable || (rotatingToDirection != NONE && enterSiegeModeWhenFinishedRotating) || siegeModeTransitionAnimation != null || (siegeModeRequiredTechId != null && !owner.isTechResearched(siegeModeRequiredTechId))) {
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
            siegeModeToggleValue = newSiegeModeValue;
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
     *
     * @return
     */
    public Iterable<String> getSiegeModeTransitionAnimationNames() {
        return siegeModeTransitionAnimationNames;
    }

    /**
     * Checks if siege mode is available for the unit
     *
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
     *
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
     *
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
     * Sets the unit's movement cause
     *
     * @param movingToTarget is the unit moving to it's target or not
     */
    public void setMovingToTarget(boolean movingToTarget) {
        this.movingToTarget = movingToTarget;
    }

    /**
     * Gets the direction the unit is facing
     *
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
     *
     * @return
     */
    public Iterable<String> getStillTextures() {
        return stillTextures;
    }

    /**
     * Checks if the unit is currently rotating
     *
     * @return
     */
    public boolean isRotating() {
        return rotatingToDirection != NONE;
    }

    /**
     * Gets the x coordinate of it's occupied block (used for targeting the object)
     *
     * @return
     */
    @Override
    public float getOccupiedBlockX() {
        if (occupiedBlock == null) {
            return x;
        }

        return occupiedBlock.x * Block.BLOCK_WIDTH;
    }

    /**
     * Gets the y coordinate of it's occupied block (used for targeting the object)
     *
     * @return
     */
    @Override
    public float getOccupiedBlockY() {
        if (occupiedBlock == null) {
            return y;
        }

        return occupiedBlock.y * Block.BLOCK_HEIGHT;
    }

    /**
     * Gets the occupied block
     *
     * @return
     */
    public Point getOccupiedBlock() {
        return occupiedBlock;
    }

    /**
     * Sets the occupied block
     *
     * @param block occupied block
     */
    public void setOccupiedBlock(Point block) {
        this.occupiedBlock = block;
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
            updateHealing(delta);

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
            updateMainTarget();
        }

        if (!inSiegeMode) {
            handleEnteringAutoSiegeMode();
        }

        if (attackMove) {
            updateAttackMove();
        }

        if (target != null) {
            updateSecondaryTarget();

            if (firingLogic != null) {
                updateRotationToTarget();
                updateMainTargetShooting();
                updateSecondaryTargetShooting();
            }

            manageMainTarget();

            if (secondaryTargetObject != null && (!isSecondaryTargetReachable() || isMainTargetReachable())) {
                secondaryTargetObject = null;
                secondaryTarget = null;
            }
        }
    }

    /**
     * Updates the unit's 'attack-move' logic
     */
    protected void updateAttackMove() {
        if (!moving && rotatingToDirection == NONE && attackMoveDestination != null && !inSiegeMode && targetObject == null) {
            requestToMove((short) attackMoveDestination.x, (short) attackMoveDestination.y);
        }
    }

    /**
     * Manages the main target
     */
    protected void manageMainTarget() {
        if (!moving && rotatingToDirection == NONE && !isMainTargetReachable()) {
            if (!inSiegeMode) {
                moveCloserToTarget();
            } else {
                removeTarget();
                notifyTargetRemovalListeners();

                handleLeavingAutoSiegeMode();
            }
        } else if (!isMainTargetReachable() && !movingToTarget) {
            removeTarget();
            notifyTargetRemovalListeners();

            if (inSiegeMode) {
                handleLeavingAutoSiegeMode();
            }
        } else if (isMainTargetReachable() && movingToTarget) {
            notifyUnableToMoveListeners();
        }
    }

    /**
     * Updates the shooting of the secondary target
     */
    protected void updateSecondaryTargetShooting() {
        if (!isMainTargetReachable() && secondaryTarget != null && isSecondaryTargetReachable()) {
            if (facingDirection == CombatUtils.getFacingDirection(getCenterX(), getCenterY(), secondaryTarget.x, secondaryTarget.y)) {
                if (rotatingToDirection == NONE && inSiegeMode) {
                    if (MathUtils.distance(getCenterX() / Block.BLOCK_WIDTH, secondaryTarget.x / Block.BLOCK_WIDTH, getCenterY() / Block.BLOCK_HEIGHT, secondaryTarget.y / Block.BLOCK_HEIGHT) <= offensiveSpecs.getSiegeModeAttackRange()) {
                        firingLogic.target.x = secondaryTarget.x;
                        firingLogic.target.y = secondaryTarget.y;
                        firingLogic.enqueueShots(inSiegeMode);
                    } else {
                        firingLogic.removeEnqueuedShots();
                        secondaryTarget = null;
                        secondaryTargetObject = null;
                    }
                } else if (rotatingToDirection == NONE && !inSiegeMode) {
                    if (MathUtils.distance(getCenterX() / Block.BLOCK_WIDTH, secondaryTarget.x / Block.BLOCK_WIDTH, getCenterY() / Block.BLOCK_HEIGHT, secondaryTarget.y / Block.BLOCK_HEIGHT) <= offensiveSpecs.getAttackRange()) {
                        firingLogic.target.x = secondaryTarget.x;
                        firingLogic.target.y = secondaryTarget.y;
                        firingLogic.enqueueShots(inSiegeMode);
                    } else {
                        firingLogic.removeEnqueuedShots();
                    }
                }
            } else {
                firingLogic.removeEnqueuedShots();
            }
        }
    }

    /**
     * Updates the shooting of the main target
     */
    protected void updateMainTargetShooting() {
        if (facingDirection == CombatUtils.getFacingDirection(getCenterX(), getCenterY(), target.x, target.y)) {
            if (rotatingToDirection == NONE && inSiegeMode) {
                if (MathUtils.distance(getCenterX() / Block.BLOCK_WIDTH, target.x / Block.BLOCK_WIDTH, getCenterY() / Block.BLOCK_HEIGHT, target.y / Block.BLOCK_HEIGHT) <= offensiveSpecs.getSiegeModeAttackRange()) {
                    firingLogic.target.x = target.x;
                    firingLogic.target.y = target.y;
                    firingLogic.enqueueShots(inSiegeMode);
                } else {
                    firingLogic.removeEnqueuedShots();
                    removeTarget();
                    notifyTargetRemovalListeners();
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
        } else {
            firingLogic.removeEnqueuedShots();
        }
    }

    /**
     * Updates the unit's rotation to target
     */
    protected void updateRotationToTarget() {
        if (!moving && (rotatingToDirection == NONE || rotatingToTarget)) {
            rotateToDirection(CombatUtils.getFacingDirection(getCenterX(), getCenterY(), target.x, target.y));
            rotatingToTarget = true;
        }
    }

    /**
     * Updates the unit's main target
     */
    protected void updateMainTarget() {
        if (!targetObject.isDestroyed()) {
            if (target != null) {
                target.x = targetObject.getOccupiedBlockX() + Block.BLOCK_WIDTH / 2f;
                target.y = targetObject.getOccupiedBlockY() + Block.BLOCK_HEIGHT / 2f;
            } else {
                target = new Point(targetObject.getCenterX(), targetObject.getCenterY());
            }
        } else {
            target = null;
            targetObject = null;
            notifyTargetRemovalListeners();

            if (movingToTarget) {
                notifyUnableToMoveListeners();
            }

            if (target == null && targetObject == null) {
                if (firingLogic != null) {
                    firingLogic.removeEnqueuedShots();
                }

                handleLeavingAutoSiegeMode();
            }

            if (attackMove && attackMoveDestination != null && targetObject == null) {
                requestToMove((short) attackMoveDestination.x, (short) attackMoveDestination.y);
            }
        }
    }

    /**
     * Updates unit's secondary target
     */
    protected void updateSecondaryTarget() {
        if (!isMainTargetReachable() && secondaryTargetObject != null && isSecondaryTargetReachable()) {
            if (!secondaryTargetObject.isDestroyed()) {
                if (secondaryTarget != null) {
                    secondaryTarget.x = secondaryTargetObject.getOccupiedBlockX() + Block.BLOCK_WIDTH / 2f;
                    secondaryTarget.y = secondaryTargetObject.getOccupiedBlockY() + Block.BLOCK_HEIGHT / 2f;
                } else {
                    secondaryTarget = new Point(secondaryTargetObject.getCenterX(), secondaryTargetObject.getCenterY());
                }
            } else {
                secondaryTarget = null;
                secondaryTargetObject = null;

                notifyTargetRemovalListeners();

                if (firingLogic != null) {
                    firingLogic.removeEnqueuedShots();
                }
            }
        }
    }

    /**
     * Sets auto siege mode flag
     *
     * @param autoSiegeModeSupported auto siege mode flag
     */
    public void setAutoSiegeModeSupported(boolean autoSiegeModeSupported) {
        this.autoSiegeModeSupported = autoSiegeModeSupported;
    }

    /**
     * Handles unit's automatic siege mode entering when there is a target
     */
    protected void handleEnteringAutoSiegeMode() {
        if (siegeModeRequiredTechId != null && !owner.isTechResearched(siegeModeRequiredTechId) || !autoSiegeModeSupported) {
            return;
        }

        if (siegeModeAvailable && !inSiegeMode && target != null && isMainTargetReachable() && (attackMove || movingToTarget || (!moving && rotatingToDirection == NONE && pathInfoProvider.getFinalDestination(this) == null))) {
            if (moving) {
                pointToGoToAfterTargetDestroyed = pathInfoProvider.getFinalDestination(this);
            }

            setInSiegeMode(true);
            leaveSiegeModeAfterTargetDestroyed = true;
        }
    }

    /**
     * Handles leaving auto siege mode method
     */
    protected void handleLeavingAutoSiegeMode() {
        if (leaveSiegeModeAfterTargetDestroyed) {
            setInSiegeMode(false);

            if (pointToGoToAfterTargetDestroyed != null) {
                requestToMove((short) pointToGoToAfterTargetDestroyed.x,
                        (short) pointToGoToAfterTargetDestroyed.y);

                pointToGoToAfterTargetDestroyed = null;
            }
        }
    }

    /**
     * Checks if the unit can reach it's main target
     *
     * @return
     */
    public boolean isMainTargetReachable() {
        if (firingLogic == null || !anyFireSourceEnabled() || (target == null && targetObject == null)) {
            return true;
        } else {
            if (target != null) {
                if (!inSiegeMode) {
                    return MathUtils.distance(getCenterX() / Block.BLOCK_WIDTH, target.x / Block.BLOCK_WIDTH, getCenterY() / Block.BLOCK_HEIGHT, target.y / Block.BLOCK_HEIGHT) <= offensiveSpecs.getAttackRange();
                } else {
                    return MathUtils.distance(getCenterX() / Block.BLOCK_WIDTH, target.x / Block.BLOCK_WIDTH, getCenterY() / Block.BLOCK_HEIGHT, target.y / Block.BLOCK_HEIGHT) <= offensiveSpecs.getSiegeModeAttackRange();
                }
            } else {
                if (!inSiegeMode) {
                    return MathUtils.distance(getCenterX() / Block.BLOCK_WIDTH, targetObject.getCenterX() / Block.BLOCK_WIDTH, getCenterY() / Block.BLOCK_HEIGHT, targetObject.getCenterY() / Block.BLOCK_HEIGHT) <= offensiveSpecs.getAttackRange();
                } else {
                    return MathUtils.distance(getCenterX() / Block.BLOCK_WIDTH, targetObject.getCenterX() / Block.BLOCK_WIDTH, getCenterY() / Block.BLOCK_HEIGHT, targetObject.getCenterY() / Block.BLOCK_HEIGHT) <= offensiveSpecs.getSiegeModeAttackRange();
                }
            }
        }
    }

    /**
     * Checks if any fire source is enabled
     *
     * @return
     */
    protected boolean anyFireSourceEnabled() {
        boolean anySourceEnabled = false;

        if (firingLogic != null) {
            for (FireSource source : firingLogic.getFireSources()) {
                if (source.isEnabled()) {
                    anySourceEnabled = true;
                }
            }
        }

        return anySourceEnabled;
    }

    /**
     * Checks if the unit can reach it's secondary target
     *
     * @return
     */
    public boolean isSecondaryTargetReachable() {
        if (firingLogic == null || !anyFireSourceEnabled() || (secondaryTarget == null && secondaryTargetObject == null)) {
            return false;
        } else {
            if (secondaryTarget != null) {
                if (!inSiegeMode) {
                    return MathUtils.distance(getCenterX() / Block.BLOCK_WIDTH, secondaryTarget.x / Block.BLOCK_WIDTH, getCenterY() / Block.BLOCK_HEIGHT, secondaryTarget.y / Block.BLOCK_HEIGHT) <= offensiveSpecs.getAttackRange();
                } else {
                    return MathUtils.distance(getCenterX() / Block.BLOCK_WIDTH, secondaryTarget.x / Block.BLOCK_WIDTH, getCenterY() / Block.BLOCK_HEIGHT, secondaryTarget.y / Block.BLOCK_HEIGHT) <= offensiveSpecs.getSiegeModeAttackRange();
                }
            } else {
                if (!inSiegeMode) {
                    return MathUtils.distance(getCenterX() / Block.BLOCK_WIDTH, secondaryTargetObject.getCenterX() / Block.BLOCK_WIDTH, getCenterY() / Block.BLOCK_HEIGHT, secondaryTargetObject.getCenterY() / Block.BLOCK_HEIGHT) <= offensiveSpecs.getAttackRange();
                } else {
                    return MathUtils.distance(getCenterX() / Block.BLOCK_WIDTH, secondaryTargetObject.getCenterX() / Block.BLOCK_WIDTH, getCenterY() / Block.BLOCK_HEIGHT, secondaryTargetObject.getCenterY() / Block.BLOCK_HEIGHT) <= offensiveSpecs.getSiegeModeAttackRange();
                }
            }
        }
    }

    /**
     * Moves the unit to a point in which the unit can range it's target
     */
    protected void moveCloserToTarget() {
        if (target == null) {
            return;
        }

        Point rangePoint = getPointToRangeTarget();

        if (rangePoint != null) {
            movementRequestHandler.handleMovementRequest(this, (short) rangePoint.x, (short) rangePoint.y);
        } else {
            removeTarget();
            notifyTargetRemovalListeners();
        }
    }

    /**
     * Gets the maximum valid range from which the unit can currently attack
     *
     * @return
     */
    protected float getMaximumValidAttackRange() {
        if (!anyFireSourceEnabled()) {
            return -1;
        }

        return inSiegeMode ? offensiveSpecs.getSiegeModeAttackRange() : offensiveSpecs.getAttackRange();
    }

    /**
     * Gets the point from which the unit can range it's target
     *
     * @return
     */
    protected Point getPointToRangeTarget() {
        Point point = null;

        float range = getMaximumValidAttackRange();
        int closestDistanceToUnit = Integer.MAX_VALUE;

        int startX = (int) (target.x / Block.BLOCK_WIDTH - range);
        int startY = (int) (target.y / Block.BLOCK_HEIGHT - range);
        int endX = (int) (target.x / Block.BLOCK_WIDTH + range);
        int endY = (int) (target.y / Block.BLOCK_HEIGHT + range);

        for (int x = startX + 1; x <= endX - 1; x++) {
            for (int y = startY + 1; y <= endY - 1; y++) {
                int distance = (int) MathUtils.distance(x, target.x / Block.BLOCK_WIDTH, y, target.y / Block.BLOCK_HEIGHT);
                int distanceToUnit = (int) MathUtils.distance(x, getCenterX() / Block.BLOCK_WIDTH, y, getCenterY() / Block.BLOCK_HEIGHT);

                if (distance == (int) range - 1 && map.isBlockPassable((short) x, (short) y) && !map.isBlockOccupied((short) x, (short) y)) {
                    if (distanceToUnit < closestDistanceToUnit) {
                        closestDistanceToUnit = distanceToUnit;

                        if (point == null) {
                            point = new Point(x, y);
                        } else {
                            point.x = x;
                            point.y = y;
                        }
                    }
                }
            }
        }

        if (point == null) {
            point = new Point(target.x / Block.BLOCK_WIDTH, target.y / Block.BLOCK_HEIGHT);
        }

        return point;
    }

    /**
     * Updates the facing direction of the unit
     *
     * @param delta time elapsed since the last update
     */
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
    public void render(SpriteBatch batch, Resources resources, RenderQueueInterface renderQueue) {
        if (!destroyed) {
            renderSelectionCircle(batch, resources);

            // render the siege mode transition animation if present
            if (siegeModeTransitionAnimation != null) {
                siegeModeTransitionAnimation.render(batch, resources, renderQueue);
                renderStillUnit = false;
            }

            if (siegeModeTransitionAnimation == null && moving && movementAnimation != null) {
                // render the moving animation
                movementAnimation.render(batch, resources, renderQueue);
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
            firingLogic.render(batch, resources, renderQueue);
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
     * @param batch     sprite batch to draw to
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

    /**
     * Called when a tech gets researched
     *
     * @param player the player the tech was applied to
     * @param tech   the researched tech
     */
    @Override
    public void techResearched(Player player, String tech) {
        if (firingLogic != null) {
            for (FireSource source : firingLogic.getFireSources()) {
                if (source.getRequiredTechId() != null && source.getRequiredTechId().equalsIgnoreCase(tech)) {
                    source.setEnabled(true);
                }
            }
        }
    }

    /**
     * Gets the unit's final center x
     *
     * @return
     */
    public float getFinalCenterX() {
        return finalCenterX;
    }

    /**
     * Gets the unit's final center y
     *
     * @return
     */
    public float getFinalCenterY() {
        return finalCenterY;
    }
}
