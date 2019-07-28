package com.gasis.rts.logic.object.combat;

import com.gasis.rts.logic.object.GameObject;

/**
 * Listens for target removal events
 */
public interface TargetRemovalListener {

    /**
     * Called when a game object gets it's target removed
     *
     * @param object object that got the target removed
     */
    void targetRemoved(GameObject object);
}
