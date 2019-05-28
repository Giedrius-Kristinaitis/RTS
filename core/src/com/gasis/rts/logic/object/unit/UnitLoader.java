package com.gasis.rts.logic.object.unit;

import com.badlogic.gdx.files.FileHandle;
import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.GameObjectLoader;

/**
 * Loads a unit from a unit description file
 */
public class UnitLoader extends GameObjectLoader {

    // code of the unit type
    protected String code;

    // name of the texture atlas that holds the unit's textures
    protected String atlas;

    // combat data
    protected float hp;
    protected float attack;
    protected float defence;
    protected float speed;

    /**
     * Loads a unit from the given file
     *
     * @param file file to load from
     * @return true if the unit was loaded successfully
     */
    @Override
    public boolean load(FileHandle file) {
        try {
            FileLineReader reader = new FileLineReader(file.read(), ":");


        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return (loaded = true);
    }

    /**
     * Creates a new instance of the loaded unit
     *
     * @return new instance of the loaded unit
     */
    @Override
    public GameObject newInstance() {
        if (!loaded) {
            throw new IllegalStateException("Unit not loaded");
        }

        return null;
    }
}
