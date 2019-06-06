package com.gasis.rts.logic.object.unit;

import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.GameObjectLoader;
import com.gasis.rts.logic.object.LoaderUtils;
import com.gasis.rts.logic.object.combat.DefensiveSpecs;
import com.gasis.rts.logic.object.combat.FireSource;
import com.gasis.rts.logic.object.combat.OffensiveSpecs;
import com.gasis.rts.logic.object.combat.RotatingGun;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Loads a unit from a unit description file
 */
public class UnitLoader extends GameObjectLoader {

    // combat data
    protected DefensiveSpecs defensiveSpecs;
    protected OffensiveSpecs offensiveSpecs;

    // textures used when the unit is standing still or doesn't have movement animations
    // texture indexes must match facing direction values defined in Unit class
    protected List<String> stillTextures = new ArrayList<String>();

    // id values of the movement animations
    // animation id indexes must match facing direction values defined in Unit class
    protected List<Short> movementAnimationIds;

    // id values of the siege mode transition animations
    // animation id indexes must match facing direction values defined in Unit class
    protected List<Short> siegeModeTransitionAnimationIds;

    // textures used when firing
    // texture indexes must match facing direction values defined in Unit class
    protected List<String> firingTextures;

    // is siege mode available for the unit
    protected boolean siegeModeAvailable;

    // some firing data
    protected byte shotCount;
    protected byte siegeModeShotCount;
    protected float reloadSpeed;
    protected float siegeModeReloadSpeed;
    protected float shotInterval;
    protected float siegeModeShotInterval;

    // rotating guns of the unit (if it has any)
    protected Map<RotatingGun, List<FireSource>> rotatingGuns;

    // fire sources of the unit (if it has any)
    protected List<FireSource> fireSources;

    /**
     * Reads the combat related data of a unit
     *
     * @param reader reader to read data from
     */
    protected void readCombatData(FileLineReader reader) {
        siegeModeAvailable = Boolean.parseBoolean(reader.readLine("siege mode available"));

        defensiveSpecs.setMaxHp(Float.parseFloat(reader.readLine("hp")));
        defensiveSpecs.setDefence(Float.parseFloat(reader.readLine("defence")));
        defensiveSpecs.setSightRange(Float.parseFloat(reader.readLine("sight range")));

        offensiveSpecs.setAttack(Float.parseFloat(reader.readLine("attack")));
        offensiveSpecs.setSpeed(Float.parseFloat(reader.readLine("speed")));
        offensiveSpecs.setAttackRange(Float.parseFloat(reader.readLine("attack range")));

        shotCount = Byte.parseByte(reader.readLine("shot count"));
        shotInterval = Float.parseFloat(reader.readLine("shot interval"));
        reloadSpeed = Float.parseFloat(reader.readLine("reload speed"));

        if (siegeModeAvailable) {
            defensiveSpecs.setSiegeModeSightRange(Float.parseFloat(reader.readLine("siege mode sight range")));

            offensiveSpecs.setSiegeModeAttack(Float.parseFloat(reader.readLine("siege mode attack")));
            offensiveSpecs.setSiegeModeAttackRange(Float.parseFloat(reader.readLine("siege mode attack range")));

            siegeModeShotCount = Byte.parseByte(reader.readLine("siege mode shot count"));
            siegeModeShotInterval = Float.parseFloat(reader.readLine("siege mode shot interval"));
            siegeModeReloadSpeed = Float.parseFloat(reader.readLine("siege mode reload speed"));
        }

        // read unit's fire sources (excluding rotating gun sources)
        fireSources = LoaderUtils.readFireSources(reader);

        // read rotating gun data (and fire sources)
        rotatingGuns = LoaderUtils.readRotatingGuns(reader);
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
        if (Boolean.parseBoolean(reader.readLine("movement animations available"))) {
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

        // check if firing textures are present
        if (Boolean.parseBoolean(reader.readLine("firing textures available"))) {
            firingTextures = new ArrayList<String>();
            firingTextures.add(reader.readLine("firing texture north"));
            firingTextures.add(reader.readLine("firing texture north east"));
            firingTextures.add(reader.readLine("firing texture east"));
            firingTextures.add(reader.readLine("firing texture south east"));
            firingTextures.add(reader.readLine("firing texture south"));
            firingTextures.add(reader.readLine("firing texture south west"));
            firingTextures.add(reader.readLine("firing texture west"));
            firingTextures.add(reader.readLine("firing texture north west"));
        }

        // check if siege mode is available
        if (siegeModeAvailable) {
            siegeModeTransitionAnimationIds = new ArrayList<Short>();
            siegeModeTransitionAnimationIds.add(Short.parseShort(reader.readLine("siege mode transition animation id north")));
            siegeModeTransitionAnimationIds.add(Short.parseShort(reader.readLine("siege mode transition animation id north east")));
            siegeModeTransitionAnimationIds.add(Short.parseShort(reader.readLine("siege mode transition animation id east")));
            siegeModeTransitionAnimationIds.add(Short.parseShort(reader.readLine("siege mode transition animation id south east")));
            siegeModeTransitionAnimationIds.add(Short.parseShort(reader.readLine("siege mode transition animation id south")));
            siegeModeTransitionAnimationIds.add(Short.parseShort(reader.readLine("siege mode transition animation id south west")));
            siegeModeTransitionAnimationIds.add(Short.parseShort(reader.readLine("siege mode transition animation id west")));
            siegeModeTransitionAnimationIds.add(Short.parseShort(reader.readLine("siege mode transition animation id north west")));
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
