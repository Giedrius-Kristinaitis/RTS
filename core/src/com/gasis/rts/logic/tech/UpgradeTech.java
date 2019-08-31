package com.gasis.rts.logic.tech;

import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.object.research.TechApplicationListener;
import com.gasis.rts.logic.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Any unit or building upgrade
 */
public class UpgradeTech extends Tech implements TechApplicationListener {

    // to what object type this tech applies
    protected List<String> objectCodes = new ArrayList<String>();



    /**
     * Applies the tech to the specified player
     *
     * @param player player to apply the tech to
     */
    @Override
    public void apply(Player player) {
        if ((requiredTechId == null || player.isTechResearched(requiredTechId)) && player.getSelectedBuilding() != null && !player.isTechResearched(id) && !player.isTechQueuedUp(id)) {
            player.getSelectedBuilding().queueUpTech(this);
        }
    }

    /**
     * Called when the tech is finished being applied
     *
     * @param player player the tech was applied to
     */
    @Override
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
    protected void loadData(FileLineReader reader) {
        try {
            List<String> objectCodes = reader.readLines("applies to object");
            this.objectCodes.addAll(objectCodes);
        } catch (Exception ex) {}
    }
}
