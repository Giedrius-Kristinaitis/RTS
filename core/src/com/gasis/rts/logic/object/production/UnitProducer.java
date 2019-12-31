package com.gasis.rts.logic.object.production;

import com.gasis.rts.logic.object.unit.UnitLoader;

/**
 * Produces units
 */
public interface UnitProducer {

    /**
     * Queues up a unit to be produced
     *
     * @param unit loader of the unit
     */
    void queueUp(UnitLoader unit);
}
