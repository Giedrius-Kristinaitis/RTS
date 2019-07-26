package com.gasis.rts.logic.object.combat;

import com.gasis.rts.logic.object.GameObject;

/**
 * Listens for game objects' destruction event
 */
public interface DestructionListener {

    /**
     * Called when a game object gets destroyed
     *
     * @param object the destroyed object
     */
    void objectDestroyed(GameObject object);
}
