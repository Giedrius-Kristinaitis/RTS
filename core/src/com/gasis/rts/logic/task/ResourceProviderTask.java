package com.gasis.rts.logic.task;

import com.gasis.rts.logic.player.Player;

/**
 * A building's task that can be executed and reverted
 */
public abstract class ResourceProviderTask implements Task {

    // the player on which the task will be applied
    private Player player;

    /**
     * Sets the played that will be affected by the task
     *
     * @param player player that will be affected
     */
    public void setPlayer(Player player) {
        this.player = player;
    }
}
