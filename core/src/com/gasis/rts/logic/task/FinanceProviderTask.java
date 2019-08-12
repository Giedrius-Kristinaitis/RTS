package com.gasis.rts.logic.task;

/**
 * Provides money to a player
 */
public class FinanceProviderTask extends ResourceProviderTask {

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
