package com.gasis.rts.logic.tech;

import com.badlogic.gdx.files.FileHandle;
import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.faction.Faction;
import com.gasis.rts.logic.player.Player;

/**
 * An improvement a.k.a. tech
 */
public abstract class Tech {

    /**
     * Applies the tech to the specified player
     *
     * @param player player to apply the tech to
     */
    public abstract void apply(Player player);

    /**
     * Loads the tech from it's description file
     *
     * @param file file to load the tech from
     * @return
     */
    public final boolean load(FileHandle file) {
        FileLineReader reader = new FileLineReader(file.read(), ":");

        return loadData(reader);
    }

    /**
     * Loads tech data
     *
     * @param reader file reader to read data from
     * @return
     */
    protected abstract boolean loadData(FileLineReader reader);
}
