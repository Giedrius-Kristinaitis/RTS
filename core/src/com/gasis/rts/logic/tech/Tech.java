package com.gasis.rts.logic.tech;

import com.badlogic.gdx.files.FileHandle;
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

    /**
     * Loads the tech from it's description file
     *
     * @param file file to load the tech from
     * @return
     */
    boolean load(FileHandle file);
}
