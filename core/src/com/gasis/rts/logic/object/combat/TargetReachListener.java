package com.gasis.rts.logic.object.combat;

/**
 * Listens for the event in which a projectile reaches it's target
 */
public interface TargetReachListener {

    /**
     * Gets called when a projectile reaches it's target
     *
     * @param targetX x coordinate of the target
     * @param targetY y coordinate of the target
     */
    void targetReached(float targetX, float targetY);
}
