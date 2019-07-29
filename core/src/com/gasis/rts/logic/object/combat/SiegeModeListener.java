package com.gasis.rts.logic.object.combat;

import com.gasis.rts.logic.object.unit.Unit;

/**
 * Listens for siege mode toggling events
 */
public interface SiegeModeListener {

    /**
     * Called when a unit toggles siege mode
     *
     * @param unit the unit that just toggled siege mode
     */
    void siegeModeToggled(Unit unit);
}
