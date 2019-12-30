package com.gasis.rts.logic.player;

import com.gasis.rts.logic.map.blockmap.BlockMap;

/**
 * Initializes players with initial state and objects (buildings, units)
 */
public interface PlayerInitializerInterface {

    /**
     * Initializes players
     *
     * @param players players to initialize
     * @param map     game map
     */
    void initializePlayers(Iterable<Player> players, BlockMap map);
}
