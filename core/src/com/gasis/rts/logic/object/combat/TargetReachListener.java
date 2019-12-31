package com.gasis.rts.logic.object.combat;

import com.gasis.rts.logic.object.GameObject;

/**
 * Listens for the event in which a projectile reaches it's target
 */
public interface TargetReachListener {

    /**
     * Gets called when a projectile reaches it's target
     *
     * @param targetX   x coordinate of the target
     * @param targetY   y coordinate of the target
     * @param damage    the damage caused by the projectile
     * @param explosive is the projectile that reached the target explosive or not
     * @param scale     the scale of the projectile
     * @param shooter   the object that launched the shot
     */
    void targetReached(float targetX, float targetY, float damage, boolean explosive, byte scale, GameObject shooter);
}
