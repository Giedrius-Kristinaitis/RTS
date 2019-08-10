package com.gasis.rts.logic.object.building;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.combat.*;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.math.MathUtils;
import com.gasis.rts.math.Point;
import com.gasis.rts.resources.Resources;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A building that attacks the enemy
 */
public class OffensiveBuilding extends Building implements Aimable, DamageValueProvider {

    // the firing logic of the building
    protected FiringLogic firingLogic;

    // the building's target
    protected Point target;

    // rotating guns of the building (if it has any)
    protected Map<String, RotatingGun> rotatingGuns = new HashMap<String, RotatingGun>();

    // offensive specs of the building
    protected OffensiveSpecs offensiveSpecs;

    // target removal listeners
    protected Set<TargetRemovalListener> targetRemovalListeners = new HashSet<TargetRemovalListener>();

    // the object the building is currently aiming at
    protected GameObject targetObject;

    /**
     * Default class constructor
     * @param map
     */
    public OffensiveBuilding(BlockMap map) {
        super(map);
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
            if (firingLogic != null) {
                firingLogic.removeEnqueuedShots();
            }

            for (RotatingGun gun: rotatingGuns.values()) {
                gun.setDestroyed(true);
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
        if (!super.canBeRemoved()) {
            return false;
        } else {
            for (RotatingGun gun: rotatingGuns.values()) {
                if (!gun.canBeRemoved()) {
                    return false;
                }
            }
        }

        return true;
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

        for (RotatingGun gun: rotatingGuns.values()) {
            gun.addTargetReachedListener(listener);
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

        for (RotatingGun gun: rotatingGuns.values()) {
            gun.removeTargetReachListener(listener);
        }
    }

    /**
     * Gets damage value
     *
     * @return
     */
    @Override
    public float getDamage() {
        return offensiveSpecs.getAttack();
    }

    /**
     * Sets the offensive specs for the building
     *
     * @param offensiveSpecs new offensive specs
     */
    public void setOffensiveSpecs(OffensiveSpecs offensiveSpecs) {
        this.offensiveSpecs = offensiveSpecs;
    }

    /**
     * Gets the offensive specs of the building
     * @return
     */
    public OffensiveSpecs getOffensiveSpecs() {
        return offensiveSpecs;
    }

    /**
     * Adds a rotating gun to the building
     *
     * @param name name used to identify the gun
     * @param gun the gun to add
     */
    public void addGun(String name, RotatingGun gun) {
        rotatingGuns.put(name, gun);
    }

    /**
     * Sets the firing logic for the building
     *
     * @param firingLogic logic used by the building
     */
    public void setFiringLogic(FiringLogic firingLogic) {
        firingLogic.setDamageProvider(this);
        this.firingLogic = firingLogic;
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
     * Aims at the specified target coordinates
     *
     * @param targetX x of the target
     * @param targetY y of the target
     */
    @Override
    public void aimAt(float targetX, float targetY) {
        target = new Point(targetX, targetY);
        targetObject = null;

        for (RotatingGun gun: rotatingGuns.values()) {
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
        targetObject = target;

        for (RotatingGun gun: rotatingGuns.values()) {
            gun.aimAt(target);
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

        for (RotatingGun gun: rotatingGuns.values()) {
            gun.removeEnqueuedShots();
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
     * Sets the x coordinate of the object
     *
     * @param x new x coordinate
     */
    @Override
    public void setX(float x) {
        super.setX(x);

        updateGunRotationPointX();
    }

    /**
     * Sets the y coordinate of the object
     *
     * @param y new y coordinate
     */
    @Override
    public void setY(float y) {
        super.setY(y);

        updateGunRotationPointY();
    }

    /**
     * Sets the x coordinate of the object's center point
     *
     * @param x new center x
     */
    @Override
    public void setCenterX(float x) {
        super.setCenterX(x);

        updateGunRotationPointX();
    }

    /**
     * Sets the y coordinate of the object's center point
     *
     * @param y new center y
     */
    @Override
    public void setCenterY(float y) {
        super.setCenterY(y);

        updateGunRotationPointY();
    }

    /**
     * Updates the x coordinates of the rotation point of all rotating guns
     */
    protected void updateGunRotationPointX() {
        for (RotatingGun gun: rotatingGuns.values()) {
            gun.setRotationPointX(getCenterX() + gun.getRelativeX().get(0));
        }
    }

    /**
     * Updates the y coordinates of the rotation point of all rotating guns
     */
    protected void updateGunRotationPointY() {
        for (RotatingGun gun: rotatingGuns.values()) {
            gun.setRotationPointY(getCenterY() + gun.getRelativeY().get(0));
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

        if (firingLogic != null) {
            firingLogic.update(false, false, target != null ? CombatUtils.getFacingDirection(getCenterX(), getCenterY(), target.x, target.y) : Unit.NONE,
                    delta, getCenterX(), getCenterY());
        }

        if (!destroyed) {
            updateTarget();
        }

        for (RotatingGun gun: rotatingGuns.values()) {
            gun.update(false, delta);
        }
    }

    /**
     * Updates the building's target data
     */
    @SuppressWarnings("Duplicates")
    protected void updateTarget() {
        if (targetObject != null) {
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

                if (target == null && targetObject == null) {
                    if (firingLogic != null) {
                        firingLogic.removeEnqueuedShots();
                    }

                    for (RotatingGun gun : rotatingGuns.values()) {
                        gun.removeEnqueuedShots();
                    }
                }
            }
        }

        if (firingLogic != null && target != null) {
            if (MathUtils.distance(getCenterX() / Block.BLOCK_WIDTH, target.x / Block.BLOCK_WIDTH, getCenterY() / Block.BLOCK_HEIGHT, target.y / Block.BLOCK_HEIGHT) <= offensiveSpecs.getAttackRange()) {
                firingLogic.target.x = target.x;
                firingLogic.target.y = target.y;
                firingLogic.enqueueShots(false);
            } else {
                firingLogic.removeEnqueuedShots();
                removeTarget();
                notifyTargetRemovalListeners();
            }
        }
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

        for (RotatingGun gun : rotatingGuns.values()) {
            gun.render(batch, resources);
        }

        if (firingLogic != null) {
            firingLogic.render(batch, resources);
        }

        if (!destroyed) {
            renderHp(batch, resources);
        }
    }
}
