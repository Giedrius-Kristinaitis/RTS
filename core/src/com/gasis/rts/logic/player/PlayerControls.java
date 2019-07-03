package com.gasis.rts.logic.player;

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

    /**
     * Called when a key on the keyboard is pressed down
     *
     * @param keycode code of the pressed key
     */
    public void keyDown(int keycode) {

    }
}
