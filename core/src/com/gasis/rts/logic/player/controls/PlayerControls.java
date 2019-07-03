package com.gasis.rts.logic.player.controls;

import com.gasis.rts.logic.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Handles controlling of a player
 */
public class PlayerControls {

    // all the players that are being controlled
    protected List<Player> controlledPlayers = new ArrayList<Player>();

    /**
     * Default class constructor
     */
    public PlayerControls(Player... players) {
        controlledPlayers.addAll(Arrays.asList(players));
    }


}
