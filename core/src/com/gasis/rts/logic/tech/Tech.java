package com.gasis.rts.logic.tech;

import com.badlogic.gdx.files.FileHandle;
import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.player.Player;

/**
 * An improvement a.k.a. tech
 */
public abstract class Tech {

    // unique tech id
    protected String id;

    // time to research the tech (in seconds)
    protected float researchTime;

    // the tech that is needed in order to research this tech
    protected String requiredTechId;

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
    public final void load(FileHandle file) {
        FileLineReader reader = new FileLineReader(file.read(), ":");

        try {
            id = reader.readLine("id");
        } catch (Exception ex) {
        }

        try {
            researchTime = Float.parseFloat(reader.readLine("research time"));
        } catch (Exception ex) {
        }

        try {
            requiredTechId = reader.readLine("required tech id");
        } catch (Exception ex) {
        }

        loadData(reader);
    }

    /**
     * Loads tech data
     *
     * @param reader file reader to read data from
     * @return
     */
    protected abstract void loadData(FileLineReader reader);

    /**
     * Gets the tech's research time
     *
     * @return
     */
    public float getResearchTime() {
        return researchTime;
    }

    /**
     * Gets the tech's id
     *
     * @return
     */
    public String getId() {
        return id;
    }
}
