package com.gasis.rts.logic.object.unit;

import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.GameObjectLoader;

import java.util.ArrayList;
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
    protected List<String> stillTextures = new ArrayList<String>();

    // id values of the movement animations
    // animation id indexes must match facing direction values defined in Unit class
    protected List<Short> movementAnimationIds;

    /**
     * Reads the combat related data of a unit
     *
     * @param reader reader to read data from
     */
    protected void readCombatData(FileLineReader reader) {
        hp = Float.parseFloat(reader.readLine("hp"));
        attack = Float.parseFloat(reader.readLine("attack"));
        defence = Float.parseFloat(reader.readLine("defence"));
        speed = Float.parseFloat(reader.readLine("speed"));
    }

    /**
     * Reads the texture and animation data of a unit
     *
     * @param reader reader to read data from
     */
    protected void readTexturesAndAnimations(FileLineReader reader) {
        stillTextures.add(reader.readLine("still texture north"));
        stillTextures.add(reader.readLine("still texture north east"));
        stillTextures.add(reader.readLine("still texture east"));
        stillTextures.add(reader.readLine("still texture south east"));
        stillTextures.add(reader.readLine("still texture south"));
        stillTextures.add(reader.readLine("still texture south west"));
        stillTextures.add(reader.readLine("still texture west"));
        stillTextures.add(reader.readLine("still texture north west"));

        // check if movement animations are present
        if (Boolean.parseBoolean(reader.readLine("movement animations"))) {
            movementAnimationIds = new ArrayList<Short>();
            movementAnimationIds.add(Short.parseShort(reader.readLine("movement animation id north")));
            movementAnimationIds.add(Short.parseShort(reader.readLine("movement animation id north east")));
            movementAnimationIds.add(Short.parseShort(reader.readLine("movement animation id east")));
            movementAnimationIds.add(Short.parseShort(reader.readLine("movement animation id south east")));
            movementAnimationIds.add(Short.parseShort(reader.readLine("movement animation id south")));
            movementAnimationIds.add(Short.parseShort(reader.readLine("movement animation id south west")));
            movementAnimationIds.add(Short.parseShort(reader.readLine("movement animation id west")));
            movementAnimationIds.add(Short.parseShort(reader.readLine("movement animation id north west")));
        }
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
