package com.gasis.rts.logic.tech;

import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.faction.Faction;
import com.gasis.rts.logic.player.Player;
import com.gasis.rts.logic.player.controls.BuildingPlacer;

/**
 * A building placement tech
 */
public class PlacementTech extends Tech {

    // the placeable building
    protected String building;

    // used to place buildings
    protected BuildingPlacer placer;

    /**
     * Default class constructor
     *
     * @param placer
     */
    public PlacementTech(BuildingPlacer placer) {
        this.placer = placer;
    }

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
        try {
            String type = reader.readLine("type");

            if (type.equalsIgnoreCase("building")) {
                building = reader.readLine("building");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }
}
