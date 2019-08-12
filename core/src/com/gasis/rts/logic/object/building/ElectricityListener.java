package com.gasis.rts.logic.object.building;

/**
 * Listens for electricity events
 */
public interface ElectricityListener {

    /**
     * Called when electricity is gained
     *
     * @param amount amount of gained electricity
     */
    void electricityGained(int amount);

    /**
     * Called when electricity is lost
     *
     * @param amount amount of lost electricity
     */
    void electricityLost(int amount);
}
