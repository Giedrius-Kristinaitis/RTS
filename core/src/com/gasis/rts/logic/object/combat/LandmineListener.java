package com.gasis.rts.logic.object.combat;

import com.gasis.rts.logic.object.building.Landmine;

/**
 * Listens for landmine events
 */
public interface LandmineListener {

    /**
     * Called when a landmine gets detonated
     *
     * @param landmine landmine that just detonated
     */
    void landmineDetonated(Landmine landmine);
}
