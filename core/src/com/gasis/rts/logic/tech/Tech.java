package com.gasis.rts.logic.tech;

import com.gasis.rts.logic.player.Player;

/**
 * An improvement a.k.a. tech
 */
public interface Tech {

    /**
     * Applies the tech to the specified player
     *
     * @param player player to apply the tech to
     */
    void apply(Player player);
}
