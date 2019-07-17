package com.gasis.rts.logic.tech;

import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.logic.player.Player;

/**
 * A unit's tactic
 */
public class TacticalTech extends Tech {

    // the action executed by the tech
    protected String action;

    /**
     * Applies the tech to the specified player
     *
     * @param player player to apply the tech to
     */
    @Override
    public void apply(Player player) {
        if (action != null) {
            if (action.equalsIgnoreCase("siege mode")) {
                toggleUnitSiegeMode(player);
            }
        }
    }

    /**
     * Toggles siege mode of the player's selected units
     *
     * @param player player owning the units
     */
    protected void toggleUnitSiegeMode(Player player) {
        if (player.getSelectedUnits() != null) {
            for (Unit unit : player.getSelectedUnits()) {
                if (unit.isSiegeModeAvailable()) {
                    unit.setInSiegeMode(!unit.isInSiegeMode());
                }
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
    protected boolean loadData(FileLineReader reader) {
        try {
            action = reader.readLine("action");
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }
}
