package com.gasis.rts.logic.tech;

import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Any unit or building upgrade
 */
public class UpgradeTech extends Tech {

    // to what object type this tech applies
    protected List<String> objectCodes = new ArrayList<String>();

    // the action done by the tech (e.g. availability)
    protected String action;

    // availability action's data


    // alter value action's data


    /**
     * Applies the tech to the specified player
     *
     * @param player player to apply the tech to
     */
    @Override
    public void apply(Player player) {
        if (player.getSelectedBuilding() != null) {
            player.getSelectedBuilding().queueUpTech(this);
        }
    }

    /**
     * Called when the tech is finished being applied
     *
     * @param player player the tech was applied to
     */
    public void applied(Player player) {
        player.addResearchedTech(id);
    }

    /**
     * Loads tech data
     *
     * @param reader file reader to read data from
     * @return
     */
    @Override
    protected boolean loadData(FileLineReader reader) {
        try {

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }
}
