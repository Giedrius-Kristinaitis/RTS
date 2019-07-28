package com.gasis.rts.logic.object.production;

import com.gasis.rts.logic.object.unit.Unit;

/**
 * Listens for unit production events
 */
public interface UnitProductionListener {

    /**
     * Called when a unit gets produced
     *
     * @param unit the unit that was just produced
     */
    void unitProduced(Unit unit);
}
