package com.gasis.rts.logic.faction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.building.BuildingLoader;
import com.gasis.rts.logic.object.unit.UnitLoader;
import com.gasis.rts.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads factions from it's files
 */
public class FactionLoader {

    // has any faction been loaded or not
    protected boolean loaded = false;

    // name of the faction
    protected String name;

    // all unit loaders for the units of the faction
    protected Map<String, UnitLoader> unitLoaders = new HashMap<String, UnitLoader>();

    // all building loaders for the buildings of the faction
    protected Map<String, BuildingLoader> buildingLoaders = new HashMap<String, BuildingLoader>();

    // the name of the default control context for the faction
    protected String defaultControlContextName;

    // faction's music
    protected List<String> soundtrack = new ArrayList<String>();

    /**
     * Loads a faction
     *
     * @param file the faction file
     * @param map the game's map
     *
     * @return
     */
    public boolean load(FileHandle file, BlockMap map) {
        try {
            FileLineReader reader = new FileLineReader(file.read(), ":");

            readMetaData(reader);
            readSoundtrack(reader);

            initializeUnitLoaders(reader, map);
            initializeBuildingLoaders(reader, map);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return (loaded = true);
    }

    /**
     * Reads faction's soundtrack
     *
     * @param reader reader to read data from
     */
    protected void readSoundtrack(FileLineReader reader) {
        List<String> music = reader.readLines("soundtrack");

        if (music != null) {
            soundtrack.addAll(music);
        }
    }

    /**
     * Reads meta-data of the faction
     *
     * @param reader file line reader to read data from
     */
    protected void readMetaData(FileLineReader reader) {
        name = reader.readLine("name");
        defaultControlContextName = reader.readLine("default control context");
    }

    /**
     * Initializes the faction's unit loaders
     *
     * @param reader file line reader to read data from
     * @param map the game's map
     */
    protected void initializeUnitLoaders(FileLineReader reader, BlockMap map) {
        List<String> units = reader.readLines("unit");

        if (units != null) {
            for (String unit : units) {
                // create an instance of a unit loader
                UnitLoader loader = new UnitLoader(map);
                loader.load(Gdx.files.internal(Constants.FOLDER_UNITS + unit));
                unitLoaders.put(unit, loader);
            }
        }
    }

    /**
     * Initializes the faction's building loaders
     *
     * @param reader file line reader to read data from
     * @param map the game's map
     */
    protected void initializeBuildingLoaders(FileLineReader reader, BlockMap map) {
        List<String> buildings = reader.readLines("building");

        if (buildings != null) {
            for (String building : buildings) {
                // create an instance of a building loader
                BuildingLoader loader = new BuildingLoader(map);
                loader.load(Gdx.files.internal(Constants.FOLDER_BUILDINGS + building));
                buildingLoaders.put(building, loader);
            }
        }
    }

    /**
     * Creates an instance of the loaded faction
     *
     * @return
     */
    public Faction createInstance() {
        if (!loaded) {
            throw new IllegalStateException("Faction not loaded");
        }

        Faction faction = new Faction();

        faction.setName(name);
        faction.setDefaultControlContextName(defaultControlContextName);
        faction.setUnitLoaders(unitLoaders);
        faction.setBuildingLoaders(buildingLoaders);
        faction.setSoundtrack(soundtrack);

        return faction;
    }
}
