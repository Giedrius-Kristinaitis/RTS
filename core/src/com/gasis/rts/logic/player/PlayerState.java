package com.gasis.rts.logic.player;

/**
 * Holds player's state: money, electricity, other statistics...
 */
public class PlayerState {

    public int maxUnits = 100;
    public int units;
    public int buildings;
    public int money;
    public int totalElectricity;
    public int usedElectricity;
    public int unitsKilled;
    public int unitsLost;
    public int buildingsRaised;
    public int buildingsLost;
}
