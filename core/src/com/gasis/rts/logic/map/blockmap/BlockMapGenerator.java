package com.gasis.rts.logic.map.blockmap;

import com.badlogic.gdx.files.FileHandle;
import com.gasis.rts.logic.map.Map;
import com.gasis.rts.logic.map.MapGenerator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Generates a block map
 */
public class BlockMapGenerator implements MapGenerator {

    // used to generate random structures and their properties
    protected Random random = new Random();

    /**
     * Generates a map from a script file
     *
     * @param scriptFile file to generate the map from
     *
     * @return generated map, null if there was an error parsing the map script
     */
    @Override
    public Map generate(FileHandle scriptFile) {
        // create a buffered reader for easier reading
        BufferedReader reader = new BufferedReader(new InputStreamReader(scriptFile.read()));

        try {
            // read the map's meta-data
            short width = Short.parseShort(reader.readLine());
            short height = Short.parseShort(reader.readLine());

            long seed = Long.parseLong(reader.readLine());

            // initialize the map and other objects
            BlockMap map = new BlockMap(width, height);

            random.setSeed(seed);

            // read map commands and generate a map based on them
            String command = null;

            while ((command = reader.readLine()) != null) {
                processCommand(command, map);
            }

            // return the final result
            return map;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Processes a map script command
     *
     * @param command command to process
     * @param map map to apply the command to
     */
    protected void processCommand(String command, BlockMap map) {

    }
}
