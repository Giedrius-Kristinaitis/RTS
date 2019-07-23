package com.gasis.rts.logic.object.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.Updatable;
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

        if (firingLogic != null) {
            firingLogic.removeEnqueuedShots();
        }
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
     */
    public void update(boolean togglingSiegeMode, float delta) {
        if (!currentlyPresent) {
            return;
        }

        update(delta);

        if (firingLogic != null && firingLogic.update(togglingSiegeMode, inSiegeMode, facingDirection, delta, x + xOffset, y + yOffset) && rotatingToDirection == NONE) {
            applyRecoil(recoil);
        }
    }

    /**
     * Updates the target logic
     */
    @SuppressWarnings("Duplicates")
    protected void updateTarget() {
        if (target != null) {
            rotateToDirection(CombatUtils.getFacingDirection(x + width / 2f, y + height / 2f, target.x, target.y));

            if (rotatingToDirection == NONE && inSiegeMode && MathUtils.distance(x + width / 2f, target.x, y + height / 2f, target.y) <= offensiveSpecs.getSiegeModeAttackRange()) {
                firingLogic.target = target;
                firingLogic.enqueueShots(inSiegeMode);
            } else if (rotatingToDirection == NONE && !inSiegeMode && MathUtils.distance(x + width / 2f, target.x, y + height / 2f, target.y) <= offensiveSpecs.getAttackRange()) {
                firingLogic.target = target;
                firingLogic.enqueueShots(inSiegeMode);
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
        if (!currentlyPresent) {
            return;
        }

        batch.draw(
                resources.atlas(Constants.FOLDER_ATLASES + atlas).findRegion(textures.get(facingDirection)),
                x + xOffset - width / 2f,
                y + yOffset - height / 2f,
                width,
                height
        );

        if (firingLogic != null) {
            firingLogic.render(batch, resources);
        }
    }
}
