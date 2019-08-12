package com.gasis.rts.logic.task;

/**
 * Provides electricity to a player
 */
public class ElectricityProviderTask extends ResourceProviderTask {

    // the amount of provided electricity
    private int amount;

    /**
     * Sets the amount of provided electricity
     *
     * @param amount amount of electricity
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Executes the task
     */
    @Override
    public void execute() {
        player.getState().totalElectricity += amount;
    }

    /**
     * Reverts the task
     */
    @Override
    public void revert() {
        player.getState().totalElectricity -= amount;
    }
}
