package com.gasis.rts.logic.tech;

import com.badlogic.gdx.files.FileHandle;
import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.faction.Faction;
import com.gasis.rts.logic.player.Player;

/**
 * A building placement tech
 */
public class PlacementTech extends Tech {

    /**
     * Applies the tech to the specified player
     *
     * @param player player to apply the tech to
     * @param faction the faction the tech belongs to
     */
    @Override
    public void apply(Player player, Faction faction) {

    }

    /**
     * Loads tech data
     *
     * @param reader file reader to read data from
     * @return
     */
    @Override
    protected boolean loadData(FileLineReader reader) {
        return false;
    }
}
