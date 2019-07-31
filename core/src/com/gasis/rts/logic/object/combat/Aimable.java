package com.gasis.rts.logic.object.combat;

import com.gasis.rts.logic.object.GameObject;

/**
 * Aims at a target
 */
public interface Aimable {

    /**
     * Aims at the specified target coordinates
     *
     * @param targetX x of the target
     * @param targetY y of the target
     */
    void aimAt(float targetX, float targetY);

    /**
     * Aims at the specified object
     *
     * @param target object to aim at
     */
    void aimAt(GameObject target);

    /**
     * Checks if this object has a target
     * @return
     */
    boolean hasTarget();

    /**
     * Removes the current target
     */
    void removeTarget();

    /**
     * Removes all enqueued shots
     */
    void removeEnqueuedShots();

    /**
     * Checks if the object is aimed at the ground or not
     * @return
     */
    boolean aimedAtGround();
}
