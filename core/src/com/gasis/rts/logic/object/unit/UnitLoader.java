package com.gasis.rts.logic.object.unit;

import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.GameObjectLoader;

import java.util.List;

/**
 * Loads a unit from a unit description file
 */
public class UnitLoader extends GameObjectLoader {

    // combat data
    protected float hp;
    protected float attack;
    protected float defence;
    protected float speed;

    // textures used when the unit is standing still or doesn't have movement animations
    // texture indexes must match facing direction values defined in Unit class
    protected List<String> stillTextures;

    // id values of the movement animations
    // animation id indexes must match facing direction values defined in Unit class
    protected List<Short> movementAnimationIds;

    /**
     * Reads the combat related data of a unit
     *
     * @param reader reader to read data from
     */
    protected void readCombatData(FileLineReader reader) {

    }

    /**
     * Reads the texture and animation data of a unit
     *
     * @param reader reader to read data from
     */
    protected void readTexturesAndAnimations(FileLineReader reader) {

    }

    /**
     * Reads other data of the object that is not meta data
     *
     * @param reader reader to read data from
     */
    @Override
    protected void readOtherData(FileLineReader reader) {
        readCombatData(reader);
        readTexturesAndAnimations(reader);
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
