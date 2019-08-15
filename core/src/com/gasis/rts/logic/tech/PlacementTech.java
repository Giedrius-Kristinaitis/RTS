package com.gasis.rts.logic.tech;

import com.gasis.rts.cursor.Cursor;
import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.object.building.BuildingLoader;
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
     */
    @Override
    public void apply(Player player) {
        if (building != null) {
            BuildingLoader loader = player.getFaction().getBuildingLoaders().get(building);

            // if the loader is null, that means the faction doesn't have this building
            if (loader != null) {
                placer.initiateBuildingPlacement(loader);
                Cursor.setCursor(Cursor.CURSOR_NONE);
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
            building = reader.readLine("building");
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }
}
