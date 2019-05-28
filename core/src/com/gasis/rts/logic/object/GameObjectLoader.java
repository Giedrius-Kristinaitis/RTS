package com.gasis.rts.logic.object;

import com.badlogic.gdx.files.FileHandle;

/**
 * Loads a game object from a file
 */
public abstract class GameObjectLoader {

    // has the object been loaded yet
    protected boolean loaded = false;

    /**
     * Loads a game object from the given file
     *
     * @param file file to load from
     *
     * @return true if the object was loaded successfully
     */
    public abstract boolean load(FileHandle file);

    /**
     * Creates a new instance of the loaded object
     *
     * @return new instance of the loaded object
     */
    public abstract GameObject newInstance();
}
