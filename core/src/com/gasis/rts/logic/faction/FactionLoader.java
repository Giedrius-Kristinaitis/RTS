package com.gasis.rts.logic.faction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.object.building.BuildingLoader;
import com.gasis.rts.logic.object.unit.UnitLoader;
import com.gasis.rts.utils.Constants;

import java.util.List;

/**
 * Loads factions from it's files
 */
public class FactionLoader {

    // has any faction been loaded or not
    protected boolean loaded = false;

    // name of the faction
    protected String name;

    // all unit loaders for the units of the faction
    protected List<UnitLoader> unitLoaders;

    // all building loaders for the buildings of the faction
    protected List<BuildingLoader> buildingLoaders;

    /**
     * Loads a faction
     *
     * @param file the faction file
     *
     * @return
     */
    public boolean load(FileHandle file) {
        try {
            FileLineReader reader = new FileLineReader(file.read(), ":");

            readMetaData(reader);

            initializeUnitLoaders(reader);
            initializeBuildingLoaders(reader);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return (loaded = true);
    }

    /**
     * Reads meta-data of the faction
     *
     * @param reader file line reader to read data from
     */
    protected void readMetaData(FileLineReader reader) {
        name = reader.readLine("name");
    }

    /**
     * Initializes the faction's unit loaders
     *
     * @param reader file line reader to read data from
     */
    protected void initializeUnitLoaders(FileLineReader reader) {
        List<String> units = reader.readLines("unit");

        for (String unit: units) {
            // create an instance of a unit loader
            UnitLoader loader = new UnitLoader();
            loader.load(Gdx.files.internal(Constants.FOLDER_UNITS + unit));
            unitLoaders.add(loader);
        }
    }

    /**
     * Initializes the faction's building loaders
     *
     * @param reader file line reader to read data from
     */
    protected void initializeBuildingLoaders(FileLineReader reader) {
        List<String> buildings = reader.readLines("building");

        for (String building: buildings) {
            // create an instance of a building loader
            BuildingLoader loader = new BuildingLoader();
            loader.load(Gdx.files.internal(Constants.FOLDER_BUILDINGS + building));
            buildingLoaders.add(loader);
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
        faction.setUnitLoaders(unitLoaders);
        faction.setBuildingLoaders(buildingLoaders);

        return faction;
    }
}
