package com.gasis.rts.logic.task;

/**
 * Provides electricity to a player
 */
public class ElectricityProviderTask extends ResourceProviderTask {

    /**
     * Executes the task
     */
    @Override
    public void execute() {
        player.getState().totalElectricity += amount;
        player.electricityGained(amount);
    }

    /**
     * Reverts the task
     */
    @Override
    public void revert() {
        player.getState().totalElectricity -= amount;
        player.electricityLost(amount);
    }
}
