package com.gasis.rts.logic.object.research;

import com.gasis.rts.logic.player.Player;

/**
 * Listens for tech research events
 */
public interface TechListener {

    /**
     * Called when a tech gets researched
     *
     * @param player the player the tech was applied to
     * @param tech the researched tech
     */
    void techResearched(Player player, String tech);
}
