package com.gasis.rts.logic.object.unit;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.animation.Animation;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.combat.RotatingGun;
import com.gasis.rts.logic.object.combat.TargetReachListener;
import com.gasis.rts.resources.Resources;

import java.util.HashMap;
import java.util.Map;

/**
 * A unit that has one or more rotating guns
 */
public class RotatingGunUnit extends Unit {

    // rotating guns of the unit
    protected Map<String, RotatingGun> guns = new HashMap<String, RotatingGun>();

    /**
     * Default class constructor
     * @param map
     */
    public RotatingGunUnit(BlockMap map) {
        super(map);
    }

    /**
     * Adds a rotating gun to the unit
     *
     * @param name name used to identify the gun
     * @param gun gun to add
     */
    public void addGun(String name, RotatingGun gun) {
        gun.getFiringLogic().setOwner(this);
        guns.put(name, gun);
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
            for (RotatingGun gun: guns.values()) {
                gun.setDestroyed(true);
            }

            if (firingLogic != null) {
                firingLogic.removeEnqueuedShots();
            }
        }
    }

    /**
     * Checks if the object can be safely removed from object list
     *
     * @return
     */
    @Override
    public boolean canBeRemoved() {
        if (super.canBeRemoved()) {
            for (RotatingGun gun: guns.values()) {
                if (!gun.canBeRemoved()) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    /**
     * Rotates the unit if required in order for it to face the specified direction
     *
     * @param facingDirection new facing direction
     */
    @Override
    public void rotateToDirection(byte facingDirection) {
        super.rotateToDirection(facingDirection);

        // if there is no target, rotate the guns as well
        for (RotatingGun gun: guns.values()) {
            if (!gun.hasTarget()) {
                gun.rotateToDirection(facingDirection);
            }
        }
    }

    /**
     * Toggles siege mode
     */
    @Override
    protected void toggleSiegeMode() {
        super.toggleSiegeMode();

        for (RotatingGun gun : guns.values()) {
            gun.setInSiegeMode(inSiegeMode);

            // update gun's presence
            if (inSiegeMode && !gun.isPresentInSiegeMode() && gun.isCurrentlyPresent()) {
                gun.setCurrentlyPresent(false);
            } else if (inSiegeMode && !gun.isCurrentlyPresent()) {
                gun.setCurrentlyPresent(true);
            }
        }
    }

    /**
     * Notifies the observer that the animation has finished
     *
     * @param animation the animation that just finished
     */
    @Override
    public void finished(Animation animation) {
        if (animation == super.siegeModeTransitionAnimation && !inSiegeMode) {
            // update guns' presence
            for (RotatingGun gun : guns.values()) {
                if (!gun.isPresentOutOfSiegeMode() && gun.isCurrentlyPresent()) {
                    gun.setCurrentlyPresent(false);
                } else if (!gun.isCurrentlyPresent()) {
                    gun.setCurrentlyPresent(true);
                }
            }
        }

        super.finished(animation);
    }

    /**
     * Adds a target reach listener
     *
     * @param listener listener to add
     */
    @Override
    public void addTargetReachedListener(TargetReachListener listener) {
        super.addTargetReachedListener(listener);

        for (RotatingGun gun: guns.values()) {
            gun.addTargetReachedListener(listener);
        }
    }

    /**
     * Removes a target reach listener
     *
     * @param listener listener to remove
     */
    @Override
    public void removeTargetReachListener(TargetReachListener listener) {
        super.removeTargetReachListener(listener);

        for (RotatingGun gun: guns.values()) {
            gun.removeTargetReachListener(listener);
        }
    }

    /**
     * Updates the game object
     *
     * @param delta time elapsed since the last render
     */
    @Override
    public void update(float delta) {
        super.update(delta);

        // update the rotating guns
        for (RotatingGun gun: guns.values()) {
            gun.setRotationPointX(getCenterX() + gun.getRelativeX().get(facingDirection));
            gun.setRotationPointY(getCenterY() + gun.getRelativeY().get(facingDirection));
            gun.update(siegeModeTransitionAnimation != null, delta, true);

            if (!gun.hasTarget() && !inSiegeMode) {
                if (Math.abs(facingDirection - gun.getFacingDirection()) > 1) {
                    gun.rotateToDirection(facingDirection);
                } else {
                    gun.setFacingDirection(facingDirection);
                }
            }
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
        super.aimAt(targetX, targetY);

        for (RotatingGun gun: guns.values()) {
            gun.aimAt(targetX, targetY);
        }
    }

    /**
     * Aims at the specified object
     *
     * @param target object to aim at
     */
    @Override
    public void aimAt(GameObject target) {
        super.aimAt(target);

        for (RotatingGun gun: guns.values()) {
            gun.aimAt(target);
        }
    }

    /**
     * Removes the current target
     */
    @Override
    public void removeTarget() {
        super.removeTarget();

        for (RotatingGun gun: guns.values()) {
            gun.removeTarget();
        }
    }

    /**
     * Removes all enqueued shots
     */
    @Override
    public void removeEnqueuedShots() {
        super.removeEnqueuedShots();

        for (RotatingGun gun: guns.values()) {
            gun.removeEnqueuedShots();
        }
    }

    /**
     * Sets the unit's secondary target
     *
     * @param secondaryTargetObject new secondary target
     */
    @Override
    public void setSecondaryTargetObject(GameObject secondaryTargetObject) {
        super.setSecondaryTargetObject(secondaryTargetObject);

        for (RotatingGun gun: guns.values()) {
            if (!gun.hasSecondaryTarget()) {
                gun.setSecondaryTargetObject(secondaryTargetObject);
            }
        }
    }

    /**
     * Checks if the unit can reach it's target
     *
     * @return
     */
    @Override
    public boolean isMainTargetReachable() {
        if (super.isMainTargetReachable()) {
            for (RotatingGun gun: guns.values()) {
                if (!gun.isMainTargetReachable()) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the maximum valid range from which the unit can currently attack
     *
     * @return
     */
    @Override
    protected float getMaximumValidAttackRange() {
        return super.getMaximumValidAttackRange() - 1;
    }

    /**
     * Renders the object to the screen
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    @SuppressWarnings("Duplicates")
    public void render(SpriteBatch batch, Resources resources) {
        if (!destroyed) {
            if (renderHp) {
                renderHp = false;
                super.render(batch, resources);
                renderHp = true;
            } else {
                super.render(batch, resources);
            }
        }

        // render the rotating guns
        for (RotatingGun gun: guns.values()) {
            if (gun.isCurrentlyPresent()) {
                gun.render(batch, resources);
            }
        }

        if (!destroyed) {
            renderHp(batch, resources);
        }
    }
}
