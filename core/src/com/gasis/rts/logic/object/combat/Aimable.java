package com.gasis.rts.logic.object.combat;

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
}
