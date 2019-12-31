package com.gasis.rts.logic.map;

import com.badlogic.gdx.files.FileHandle;

/**
 * Generates a map
 */
public interface MapGenerator {

    /**
     * Generates a map from a script file
     *
     * @param scriptFile file to generate the map from
     * @return generated map
     */
    Map generate(FileHandle scriptFile);
}
