package com.gasis.rts.logic.object.research;

import com.gasis.rts.logic.player.Player;

/**
 * Listens for tech application events
 */
public interface TechApplicationListener {

    /**
     * Called when a tech gets applied to a player
     *
     * @param player player the tech was applied to
     */
    void applied(Player player);
}
