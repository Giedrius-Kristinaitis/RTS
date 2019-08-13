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
        player.getState().availableElectricity += amount;
        player.electricityGained(amount);
    }

    /**
     * Reverts the task
     */
    @Override
    public void revert() {
        player.getState().availableElectricity -= amount;
        player.electricityLost(amount);
    }
}
