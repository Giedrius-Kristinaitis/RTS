package com.gasis.rts.logic.object.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.Updatable;
import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.Rotatable;
import com.gasis.rts.math.MathUtils;
import com.gasis.rts.math.Point;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

import java.util.List;

import static com.gasis.rts.logic.object.unit.Unit.*;

/**
 * Represents any gun that rotates around a fixed point (tank gun, rocket launcher...)
 */
@SuppressWarnings("Duplicates")
public class RotatingGun implements Updatable, Renderable, Rotatable, Aimable, DamageValueProvider {

    // texture atlas that holds the textures of the gun
    protected String atlas;

    // textures of the rotating gun
    // texture indexes must match the values of facing directions
    // defined in the Unit class
    protected List<String> textures;

    // coordinates of the point which the gun rotates around
    protected float x;
    protected float y;

    // the offset of the gun (used when applying recoil force)
    protected float xOffset;
    protected float yOffset;

    // speed at which the offset grows (per second) (used when applying recoil force)
    protected float xOffsetSpeed;
    protected float yOffsetSpeed;

    // gun's dimensions
    protected float width;
    protected float height;

    // the resistance of the recoil force between 0 (doesn't resist at all)
    // and 1 (fully resists)
    protected float recoilResistance;

    // the direction which the gun is pointed to
    // must have one of the facing direction values defined in the Unit class
    protected byte facingDirection = EAST;

    // the direction the gun is currently rotating to
    protected byte rotatingToDirection = NONE;

    // how much time has elapsed since the last rotation
    protected float timeSinceLastRotation;

    // the speed at which the gun rotates (expressed by rotations per seconds)
    protected float rotationSpeed;

    // the gun's firing logic
    protected FiringLogic firingLogic;

    // is the gun in siege mode
    protected boolean inSiegeMode;

    // the strength of the recoil generated when it fires
    protected float recoil;

    // coordinates relative to it's holder's center for every facing direction
    protected List<Float> relativeX;
    protected List<Float> relativeY;

    // target of the gun
    protected Point target;

    // offensive specs of the gun
    protected OffensiveSpecs offensiveSpecs;

    // is the gun present in these conditions
    protected boolean presentInSiegeMode;
    protected boolean presentOutOfSiegeMode;

    // is the gun currently present or not
    protected boolean currentlyPresent;

    // is the gun destroyed or not
    protected boolean destroyed = false;

    // the object the gun is aiming at
    protected GameObject targetObject;

    // the secondary target object the unit can fire at while moving to it's main target
    protected GameObject secondaryTargetObject;

    // the secondary target's point
    protected Point secondaryTarget;

    // the range of the gun that overrides the one specified in offensive specs
    protected float individualRange;

    // the gun's individual reload speed that overrides the one specified in offensive specs
    protected float individualReloadSpeed;

    /**
     * Gets the gun's individual reload speed
     * @return
     */
    public float getIndividualReloadSpeed() {
        return individualReloadSpeed;
    }

    /**
     * Sets the gun's individual reload speed
     *
     * @param individualReloadSpeed individual reload speed
     */
    public void setIndividualReloadSpeed(float individualReloadSpeed) {
        this.individualReloadSpeed = individualReloadSpeed;

        if (firingLogic != null && individualReloadSpeed > 0) {
            firingLogic.setReloadSpeed(individualReloadSpeed);
            firingLogic.setSiegeModeReloadSpeed(individualReloadSpeed);
        }
    }

    /**
     * Sets the individual range of the gun
     *
     * @param individualRange new individual range
     */
    public void setIndividualRange(float individualRange) {
        this.individualRange = individualRange;
    }

    /**
     * Gets the gun's individual range
     * @return
     */
    public float getIndividualRange() {
        return individualRange;
    }

    /**
     * Sets the gun's secondary target
     *
     * @param secondaryTargetObject secondary target object
     */
    public void setSecondaryTargetObject(GameObject secondaryTargetObject) {
        this.secondaryTargetObject = secondaryTargetObject;
    }

    /**
     * Checks if the gun has a secondary target
     * @return
     */
    public boolean hasSecondaryTarget() {
        return secondaryTargetObject != null;
    }

    /**
     * Sets the gun's destroyed value
     *
     * @param destroyed is the gun destroyed
     */
    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;

        firingLogic.removeEnqueuedShots();
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
     * Checks if the gun can be removed
     * @return
     */
    public boolean canBeRemoved() {
        if (firingLogic != null) {
            return firingLogic.canBeRemoved();
        }

        return true;
    }

    /**
     * Sets the current gun's presence
     *
     * @param currentlyPresent new presence value
     */
    public void setCurrentlyPresent(boolean currentlyPresent) {
        this.currentlyPresent = currentlyPresent;

        firingLogic.reset();
    }

    /**
     * Checks if the gun is currently present
     * @return
     */
    public boolean isCurrentlyPresent() {
        return currentlyPresent;
    }

    /**
     * Checks if the gun is present when the holder in siege mode
     * @return
     */
    public boolean isPresentInSiegeMode() {
        return presentInSiegeMode;
    }

    /**
     * Sets the guns presence in is siege mode
     *
     * @param presentInSiegeMode new presence value
     */
    public void setPresentInSiegeMode(boolean presentInSiegeMode) {
        this.presentInSiegeMode = presentInSiegeMode;
    }

    /**
     * Checks if the gun is present when the holder is not in siege mode
     * @return
     */
    public boolean isPresentOutOfSiegeMode() {
        return presentOutOfSiegeMode;
    }

    /**
     * Sets the gun's presence when not in siege mode
     *
     * @param presentOutOfSiegeMode new presence value
     */
    public void setPresentOutOfSiegeMode(boolean presentOutOfSiegeMode) {
        this.presentOutOfSiegeMode = presentOutOfSiegeMode;
    }

    /**
     * Gets the name of the texture atlas used by the gun
     * @return
     */
    public String getAtlas() {
        return atlas;
    }

    /**
     * Sets the name of the texture atlas to be used by the gun
     *
     * @param atlas new name of the atlas
     */
    public void setAtlas(String atlas) {
        this.atlas = atlas;
    }

    /**
     * Gets the offensive specs of the object
     * @return
     */
    public OffensiveSpecs getOffensiveSpecs() {
        return offensiveSpecs;
    }

    /**
     * Sets the offensive specs of the object
     *
     * @param offensiveSpecs new offensive specs
     */
    public void setOffensiveSpecs(OffensiveSpecs offensiveSpecs) {
        this.offensiveSpecs = offensiveSpecs;
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
     * Checks if the object has a target object
     *
     * @return
     */
    @Override
    public boolean hasTargetObject() {
        return targetObject != null;
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
     * Gets the relative x coordinates of the gun
     * @return
     */
    public List<Float> getRelativeX() {
        return relativeX;
    }

    /**
     * Gets the relative y coordinates of the gun
     * @return
     */
    public List<Float> getRelativeY() {
        return relativeY;
    }

    /**
     * Sets the relative x coordinates of the gun
     *
     * @param relativeX new x
     */
    public void setRelativeX(List<Float> relativeX) {
        this.relativeX = relativeX;
    }

    /**
     * Sets the relative y coordinates of the gun
     *
     * @param relativeY new y
     */
    public void setRelativeY(List<Float> relativeY) {
        this.relativeY = relativeY;
    }

    /**
     * Gets the recoil generated when firing
     * @return
     */
    public float getRecoil() {
        return recoil;
    }

    /**
     * Sets the recoil generated when firing
     *
     * @param recoil new recoil value
     */
    public void setRecoil(float recoil) {
        this.recoil = recoil;
    }

    /**
     * Checks if the gun is in siege mode
     * @return
     */
    public boolean isInSiegeMode() {
        return inSiegeMode;
    }

    /**
     * Toggles the gun's siege mode
     *
     * @param inSiegeMode is the gun in siege mode
     */
    public void setInSiegeMode(boolean inSiegeMode) {
        if (this.inSiegeMode != inSiegeMode) {
            if (firingLogic.hasEnqueuedShots()) {
                firingLogic.removeEnqueuedShots();
            }

            this.inSiegeMode = inSiegeMode;
        }
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
     * Sets the firing logic for the gun
     *
     * @param firingLogic new firing logic
     */
    public void setFiringLogic(FiringLogic firingLogic) {
        firingLogic.setDamageProvider(this);
        this.firingLogic = firingLogic;
    }

    /**
     * Gets the firing logic of the gun
     * @return
     */
    public FiringLogic getFiringLogic() {
        return firingLogic;
    }

    /**
     * Sets the textures of the gun
     *
     * @param textures new texture list
     */
    public void setTextures(List<String> textures) {
        this.textures = textures;
    }

    /**
     * Gets the textures of the gun
     * @return
     */
    public List<String> getTextures() {
        return textures;
    }

    /**
     * Adds a new fire source to the gun
     *
     * @param name name used to identify the source
     * @param source fire source to add
     */
    public void addFireSource(String name, FireSource source) {
        firingLogic.addFireSource(name, source);
    }

    /**
     * Gets the resistance of the recoil force
     * @return
     */
    public float getRecoilResistance() {
        return recoilResistance;
    }

    /**
     * Sets the resistance of the recoil force
     *
     * @param recoilResistance new resistance between 0 and 1
     */
    public void setRecoilResistance(float recoilResistance) {
        this.recoilResistance = recoilResistance;
    }

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
     * Applies recoil force to the gun
     *
     * @param recoil strength of the recoil
     */
    protected void applyRecoil(float recoil) {
        if (recoil <= 0) {
            return;
        }

        switch (facingDirection) {
            case NORTH:
                xOffsetSpeed = 0;
                yOffsetSpeed = -recoil;
                break;
            case NORTH_EAST:
                xOffsetSpeed = -recoil;
                yOffsetSpeed = -recoil;
                break;
            case EAST:
                xOffsetSpeed = -recoil;
                yOffsetSpeed = 0;
                break;
            case SOUTH_EAST:
                xOffsetSpeed = -recoil;
                yOffsetSpeed = recoil;
                break;
            case SOUTH:
                xOffsetSpeed = 0;
                yOffsetSpeed = recoil;
                break;
            case SOUTH_WEST:
                xOffsetSpeed = recoil;
                yOffsetSpeed = recoil;
                break;
            case WEST:
                xOffsetSpeed = recoil;
                yOffsetSpeed = 0;
                break;
            case NORTH_WEST:
                xOffsetSpeed = recoil;
                yOffsetSpeed = -recoil;
                break;
            default:
                xOffsetSpeed = 0;
                yOffsetSpeed = 0;
                break;
        }
    }

    /**
     * Orders the gun to rotate to face the given direction
     *
     * @param direction the direction the gun should face
     */
    @Override
    public void rotateToDirection(byte direction) {
        if (rotatingToDirection != direction && facingDirection != direction) {
            rotatingToDirection = direction;
            timeSinceLastRotation = 0;
        }
    }

    /**
     * Updates the state of the gun
     *
     * @param delta time elapsed since the last update
     */
    @Override
    public void update(float delta) {
        if (!currentlyPresent) {
            return;
        }

        updateFacingDirection(delta);
        updateOffset(delta);
        updateTarget();
    }

    /**
     * Updates the state of the gun
     *
     * @param togglingSiegeMode is the holder switching between siege mode right now
     * @param delta time elapsed since the last update
     * @param updateTarget should the target also be updated
     */
    public void update(boolean togglingSiegeMode, float delta, boolean updateTarget) {
        if (!currentlyPresent) {
            return;
        }

        if (!destroyed) {
            updateFacingDirection(delta);
            updateOffset(delta);

            if (updateTarget) {
                updateTarget();
            } else {
                firingLogic.removeEnqueuedShots();
            }
        }

        if (firingLogic != null && firingLogic.update(togglingSiegeMode, inSiegeMode, facingDirection, delta, x + xOffset, y + yOffset) && rotatingToDirection == NONE) {
            applyRecoil(recoil);
        }
    }

    /**
     * Updates the target logic
     */
    protected void updateTarget() {
        if (targetObject != null) {
            updateMainTarget();
        }

        if (target != null) {
            updateSecondaryTarget();
            updateRotationToTarget();
            updateMainTargetShooting();
            updateSecondaryTargetShooting();

            if (secondaryTargetObject != null && (!isSecondaryTargetReachable() || isMainTargetReachable())) {
                secondaryTargetObject = null;
                secondaryTarget = null;
            }
        }
    }

    /**
     * Updates the shooting at the secondary target
     */
    protected void updateSecondaryTargetShooting() {
        if (!isMainTargetReachable() && secondaryTarget != null && isSecondaryTargetReachable()) {
            if (rotatingToDirection == NONE && inSiegeMode) {
                if (MathUtils.distance(x / Block.BLOCK_WIDTH, secondaryTarget.x / Block.BLOCK_WIDTH, y / Block.BLOCK_HEIGHT, secondaryTarget.y / Block.BLOCK_HEIGHT) <= (individualRange > 0 ? individualRange : offensiveSpecs.getSiegeModeAttackRange())) {
                    firingLogic.target.x = secondaryTarget.x;
                    firingLogic.target.y = secondaryTarget.y;
                    firingLogic.enqueueShots(inSiegeMode);
                } else {
                    firingLogic.removeEnqueuedShots();
                }
            } else if (rotatingToDirection == NONE && !inSiegeMode) {
                if (MathUtils.distance(x / Block.BLOCK_WIDTH, secondaryTarget.x / Block.BLOCK_WIDTH, y / Block.BLOCK_HEIGHT, secondaryTarget.y / Block.BLOCK_HEIGHT) <= (individualRange > 0 ? individualRange : offensiveSpecs.getAttackRange())) {
                    firingLogic.target.x = secondaryTarget.x;
                    firingLogic.target.y = secondaryTarget.y;
                    firingLogic.enqueueShots(inSiegeMode);
                } else {
                    removeEnqueuedShots();
                }
            }
        }
    }

    /**
     * Updates the shooting at the main target
     */
    protected void updateMainTargetShooting() {
        if (rotatingToDirection == NONE && inSiegeMode) {
            if (MathUtils.distance(x / Block.BLOCK_WIDTH, target.x / Block.BLOCK_WIDTH, y / Block.BLOCK_HEIGHT, target.y / Block.BLOCK_HEIGHT) <= (individualRange > 0 ? individualRange : offensiveSpecs.getSiegeModeAttackRange())) {
                firingLogic.target.x = target.x;
                firingLogic.target.y = target.y;
                firingLogic.enqueueShots(inSiegeMode);
            } else {
                firingLogic.removeEnqueuedShots();
            }
        } else if (rotatingToDirection == NONE && !inSiegeMode) {
            if (MathUtils.distance(x / Block.BLOCK_WIDTH, target.x / Block.BLOCK_WIDTH, y / Block.BLOCK_HEIGHT, target.y / Block.BLOCK_HEIGHT) <= (individualRange > 0 ? individualRange : offensiveSpecs.getAttackRange())) {
                firingLogic.target.x = target.x;
                firingLogic.target.y = target.y;
                firingLogic.enqueueShots(inSiegeMode);
            } else {
                removeEnqueuedShots();
            }
        }
    }

    /**
     * Updates the gun's rotation to it's target
     */
    protected void updateRotationToTarget() {
        if (secondaryTargetObject == null || isMainTargetReachable()) {
            rotateToDirection(CombatUtils.getFacingDirection(x, y, target.x, target.y));

            if (isMainTargetReachable() && secondaryTargetObject != null) {
                setSecondaryTargetObject(null);
                secondaryTarget = null;
            }
        } else if (secondaryTarget != null) {
            rotateToDirection(CombatUtils.getFacingDirection(x, y, secondaryTarget.x, secondaryTarget.y));
        }
    }

    /**
     * Updates the gun's secondary target
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

                if (firingLogic != null) {
                    firingLogic.removeEnqueuedShots();
                }
            }
        }
    }

    /**
     * Updates the gun's main target
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

            if (firingLogic != null) {
                firingLogic.removeEnqueuedShots();
            }
        }
    }

    /**
     * Checks if the gun can reach it's main target or not
     * @return
     */
    public boolean isMainTargetReachable() {
        if (firingLogic == null || (target == null && targetObject == null)) {
            return true;
        }

        if (target != null) {
            if (!inSiegeMode) {
                return MathUtils.distance(x / Block.BLOCK_WIDTH, target.x / Block.BLOCK_WIDTH, y / Block.BLOCK_HEIGHT, target.y / Block.BLOCK_HEIGHT) <= (individualRange > 0 ? individualRange : offensiveSpecs.getAttackRange());
            } else {
                return MathUtils.distance(x / Block.BLOCK_WIDTH, target.x / Block.BLOCK_WIDTH, y / Block.BLOCK_HEIGHT, target.y / Block.BLOCK_HEIGHT) <= (individualRange > 0 ? individualRange : offensiveSpecs.getSiegeModeAttackRange());
            }
        } else {
            if (!inSiegeMode) {
                return MathUtils.distance(x / Block.BLOCK_WIDTH, targetObject.getCenterX() / Block.BLOCK_WIDTH, y / Block.BLOCK_HEIGHT, targetObject.getCenterY() / Block.BLOCK_HEIGHT) <= (individualRange > 0 ? individualRange : offensiveSpecs.getAttackRange());
            } else {
                return MathUtils.distance(x / Block.BLOCK_WIDTH, targetObject.getCenterX() / Block.BLOCK_WIDTH, y / Block.BLOCK_HEIGHT, targetObject.getCenterY() / Block.BLOCK_HEIGHT) <= (individualRange > 0 ? individualRange : offensiveSpecs.getSiegeModeAttackRange());
            }
        }
    }

    /**
     * Checks if the gun can reach it's secondary target or not
     * @return
     */
    public boolean isSecondaryTargetReachable() {
        if (firingLogic == null || (secondaryTarget == null && secondaryTargetObject == null)) {
            return false;
        }

        if (secondaryTarget != null) {
            if (!inSiegeMode) {
                return MathUtils.distance(x / Block.BLOCK_WIDTH, secondaryTarget.x / Block.BLOCK_WIDTH, y / Block.BLOCK_HEIGHT, secondaryTarget.y / Block.BLOCK_HEIGHT) <= (individualRange > 0 ? individualRange : offensiveSpecs.getAttackRange());
            } else {
                return MathUtils.distance(x / Block.BLOCK_WIDTH, secondaryTarget.x / Block.BLOCK_WIDTH, y / Block.BLOCK_HEIGHT, secondaryTarget.y / Block.BLOCK_HEIGHT) <= (individualRange > 0 ? individualRange : offensiveSpecs.getSiegeModeAttackRange());
            }
        } else {
            if (!inSiegeMode) {
                return MathUtils.distance(x / Block.BLOCK_WIDTH, secondaryTargetObject.getCenterX() / Block.BLOCK_WIDTH, y / Block.BLOCK_HEIGHT, secondaryTargetObject.getCenterY() / Block.BLOCK_HEIGHT) <= (individualRange > 0 ? individualRange : offensiveSpecs.getAttackRange());
            } else {
                return MathUtils.distance(x / Block.BLOCK_WIDTH, secondaryTargetObject.getCenterX() / Block.BLOCK_WIDTH, y / Block.BLOCK_HEIGHT, secondaryTargetObject.getCenterY() / Block.BLOCK_HEIGHT) <= (individualRange > 0 ? individualRange : offensiveSpecs.getSiegeModeAttackRange());
            }
        }
    }

    /**
     * Updates the offset of the gun
     *
     * @param delta time elapsed since the last update
     */
    protected void updateOffset(float delta) {
        xOffset += xOffsetSpeed * delta;
        yOffset += yOffsetSpeed * delta;

        // apply recoil resistance
        xOffsetSpeed *= (1 - recoilResistance * delta * 10);
        yOffsetSpeed *= (1 - recoilResistance * delta * 10);

        // make the offset approach 0
        xOffset *= (1 - recoilResistance * delta * 10);
        yOffset *= (1 - recoilResistance * delta * 10);
    }

    /**
     * Checks if the gun is currently rotating
     * @return
     */
    public boolean isRotating() {
        return rotatingToDirection != NONE;
    }

    /**
     * Updates the facing direction of the gun
     *
     * @param delta time elapsed since the last update
     */
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
        if (!currentlyPresent) {
            return;
        }

        if (!destroyed) {
            batch.draw(
                    resources.atlas(Constants.FOLDER_ATLASES + atlas).findRegion(textures.get(facingDirection)),
                    x + xOffset - width / 2f,
                    y + yOffset - height / 2f,
                    width,
                    height
            );
        }

        if (firingLogic != null) {
            firingLogic.render(batch, resources);
        }
    }
}
