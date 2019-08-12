package com.gasis.rts.logic.task;

import com.gasis.rts.logic.player.Player;

/**
 * A building's task that can be executed and reverted
 */
public abstract class ResourceProviderTask implements Task {

    // the player on which the task will be applied
    protected Player player;

    // amount of money provided
    protected int amount;

    /**
     * Sets the amount of money to provide
     *
     * @param amount new amount of money
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }


    /**
     * Sets the played that will be affected by the task
     *
     * @param player player that will be affected
     */
    public void setPlayer(Player player) {
        this.player = player;
    }
}
