package com.gasis.rts.logic.tech;

import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.object.unit.UnitLoader;
import com.gasis.rts.logic.player.Player;

/**
 * A tech that produces something
 */
public class ProductionTech extends Tech {

    // the produced unit
    protected String unit;

    /**
     * Applies the tech to the specified player
     *
     * @param player player to apply the tech to
     */
    @Override
    public void apply(Player player) {
        if (unit != null && player.getSelectedBuilding() != null && (requiredTechId == null || player.isTechResearched(requiredTechId))) {
            UnitLoader loader = player.getFaction().getUnitLoaders().get(unit);

            if (loader != null) {
                player.getSelectedBuilding().queueUp(loader);
            }
        }
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
            unit = reader.readLine("unit");
        } catch (Exception ex) {}
    }
}
