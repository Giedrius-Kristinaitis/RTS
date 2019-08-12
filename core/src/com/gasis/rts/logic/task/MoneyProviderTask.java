package com.gasis.rts.logic.task;

/**
 * Provides money to a player
 */
public class MoneyProviderTask extends ResourceProviderTask {

    // amount of money provided
    private int amount;

    /**
     * Sets the amount of money to provide
     *
     * @param amount new amount of money
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Executes the task
     */
    @Override
    public void execute() {
        player.getState().money += amount;
    }

    /**
     * Reverts the task
     */
    @Override
    public void revert() {
        player.getState().money -= amount;
    }
}
