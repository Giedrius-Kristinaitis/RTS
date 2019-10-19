package com.gasis.rts.logic.player;

import com.gasis.rts.logic.player.exploration.ExplorationDataInterface;

/**
 * Holds player's state: money, electricity, other statistics...
 */
public class PlayerState {

    public int maxUnits = 100;
    public int units;
    public int buildings;
    public int money;
    public int availableElectricity;
    public int requiredElectricity;
    public int usedElectricity;
    public int unitsKilled;
    public int unitsLost;
    public int buildingsRaised;
    public int buildingsLost;

    public ExplorationDataInterface explorationData;
}
