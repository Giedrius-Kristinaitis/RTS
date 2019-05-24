package com.gasis.rts.logic.object;

/**
 * Indicates that whatever implements this interface can take damage
 */
public interface Damageable {

    /**
     * Does damage to the object
     *
     * @param attack attack stat of the attacker,
     *               damage will be calculated based on the object's defence
     */
    void doDamage(float attack);
}
