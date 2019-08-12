package com.gasis.rts.logic.object.combat;

import com.gasis.rts.logic.object.GameObject;

/**
 * Provides owner for an object
 */
public interface OwnerProvider {

    /**
     * Gets the owner of an object
     * @return
     */
    GameObject getOwner();
}
