package com.gasis.rts.logic.player.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.tech.*;
import com.gasis.rts.utils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A context with different techs that a player can control. Examples of control contexts:
 * machine factory, command center, research center
 */
public class ControlContext {

    // all techs in this context
    protected Map<String, Tech> techs = new HashMap<String, Tech>();

    /**
     * Loads the control context
     *
     * @param file   control context description file
     * @param placer building placer used when initializing placement techs
     */
    public boolean load(FileHandle file, BuildingPlacer placer) {
        try {
            FileLineReader reader = new FileLineReader(file.read(), ":");
            loadTechs(reader, placer);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Loads available techs
     *
     * @param reader file reader to read techs from
     * @param placer building placer used when initializing placement techs
     */
    protected void loadTechs(FileLineReader reader, BuildingPlacer placer) {
        List<String> techList = reader.readLines("tech");

        if (techList == null) {
            return;
        }

        for (String tech : techList) {
            loadTech(reader, tech, placer);
        }
    }

    /**
     * Loads a single tech
     *
     * @param reader file reader to read the tech from
     * @param prefix tech prefix
     * @param placer building placer used when initializing placement techs
     */
    protected void loadTech(FileLineReader reader, String prefix, BuildingPlacer placer) {
        String techType = reader.readLine(prefix + " type");
        String techFile = reader.readLine(prefix + " file");
        String keyBinding = reader.readLine(prefix + " key binding");

        Tech tech;

        if (techType.equalsIgnoreCase("upgrade")) {
            tech = new UpgradeTech();
        } else if (techType.equalsIgnoreCase("placement")) {
            tech = new PlacementTech(placer);
        } else if (techType.equalsIgnoreCase("production")) {
            tech = new ProductionTech();
        } else if (techType.equalsIgnoreCase("tactical")) {
            tech = new TacticalTech();
        } else {
            return;
        }

        tech.load(Gdx.files.internal(Constants.FOLDER_TECHS + techFile));

        techs.put(keyBinding, tech);
    }

    /**
     * Gets a tech in this control context
     *
     * @param keyBinding hotkey for the tech
     * @return
     */
    public Tech getTech(String keyBinding) {
        return techs.get(keyBinding);
    }
}
