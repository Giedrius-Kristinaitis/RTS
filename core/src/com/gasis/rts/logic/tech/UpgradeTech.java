package com.gasis.rts.logic.tech;

import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.faction.Faction;
import com.gasis.rts.logic.player.Player;

/**
 * Any unit or building upgrade
 */
public class UpgradeTech extends Tech {

    // to what object type this tech applies
    protected String objectCode;

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
