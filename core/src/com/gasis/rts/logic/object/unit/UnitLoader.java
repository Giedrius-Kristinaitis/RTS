package com.gasis.rts.logic.object.unit;

import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.object.GameObjectLoader;
import com.gasis.rts.logic.object.LoaderUtils;
import com.gasis.rts.logic.object.combat.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Loads a unit from a unit description file
 */
public class UnitLoader extends GameObjectLoader {

    // combat data
    protected DefensiveSpecs defensiveSpecs = new DefensiveSpecs();
    protected OffensiveSpecs offensiveSpecs = new OffensiveSpecs();

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

    // textures used when in siege mode
    // texture indexes must match facing direction values defined in Unit class
    protected List<String> siegeModeTextures;

    // is siege mode available for the unit
    protected boolean siegeModeAvailable;

    // firing data
    protected FiringData firingData = new FiringData();

    // rotating guns of the unit (if it has any)
    protected Map<RotatingGun, List<FireSource>> rotatingGuns;

    // fire sources of the unit (if it has any)
    protected List<FireSource> fireSources;

    // the direction the unit faces when in siege mode (only applied when there is
    // 1 direction)
    protected byte siegeModeFacingDirection = Unit.EAST;

    /**
     * Reads the combat related data of a unit
     *
     * @param reader reader to read data from
     */
    @SuppressWarnings("Duplicates")
    protected void readCombatData(FileLineReader reader) {
        siegeModeAvailable = Boolean.parseBoolean(reader.readLine("siege mode available"));

        defensiveSpecs.setMaxHp(Float.parseFloat(reader.readLine("hp")));
        defensiveSpecs.setDefence(Float.parseFloat(reader.readLine("defence")));
        defensiveSpecs.setSightRange(Float.parseFloat(reader.readLine("sight range")));

        offensiveSpecs.setAttack(Float.parseFloat(reader.readLine("attack")));
        offensiveSpecs.setSpeed(Float.parseFloat(reader.readLine("speed")));
        offensiveSpecs.setAttackRange(Float.parseFloat(reader.readLine("attack range")));

        firingData.setShotCount(Byte.parseByte(reader.readLine("shot count")));
        firingData.setShotInterval(Float.parseFloat(reader.readLine("shot interval")));
        firingData.setReloadSpeed(Float.parseFloat(reader.readLine("reload speed")));

        if (siegeModeAvailable) {
            try {
                siegeModeFacingDirection = Unit.class.getField(reader.readLine("siege mode facing direction")).getByte(null);
            } catch (NoSuchFieldException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }

            defensiveSpecs.setSiegeModeSightRange(Float.parseFloat(reader.readLine("siege mode sight range")));

            offensiveSpecs.setSiegeModeAttack(Float.parseFloat(reader.readLine("siege mode attack")));
            offensiveSpecs.setSiegeModeAttackRange(Float.parseFloat(reader.readLine("siege mode attack range")));

            firingData.setSiegeModeShotCount(Byte.parseByte(reader.readLine("siege mode shot count")));
            firingData.setSiegeModeShotInterval(Float.parseFloat(reader.readLine("siege mode shot interval")));
            firingData.setSiegeModeReloadSpeed(Float.parseFloat(reader.readLine("siege mode reload speed")));
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
    @SuppressWarnings("Duplicates")
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
            siegeModeTextures = new ArrayList<String>();

            if (Byte.parseByte(reader.readLine("siege mode facing directions")) == 1) {
                siegeModeTransitionAnimationIds.add(Short.parseShort(reader.readLine("siege mode transition animation id")));
                siegeModeTextures.add(reader.readLine("siege mode texture"));
            } else {
                siegeModeTransitionAnimationIds.add(Short.parseShort(reader.readLine("siege mode transition animation id north")));
                siegeModeTransitionAnimationIds.add(Short.parseShort(reader.readLine("siege mode transition animation id north east")));
                siegeModeTransitionAnimationIds.add(Short.parseShort(reader.readLine("siege mode transition animation id east")));
                siegeModeTransitionAnimationIds.add(Short.parseShort(reader.readLine("siege mode transition animation id south east")));
                siegeModeTransitionAnimationIds.add(Short.parseShort(reader.readLine("siege mode transition animation id south")));
                siegeModeTransitionAnimationIds.add(Short.parseShort(reader.readLine("siege mode transition animation id south west")));
                siegeModeTransitionAnimationIds.add(Short.parseShort(reader.readLine("siege mode transition animation id west")));
                siegeModeTransitionAnimationIds.add(Short.parseShort(reader.readLine("siege mode transition animation id north west")));

                siegeModeTextures.add(reader.readLine("siege mode texture north"));
                siegeModeTextures.add(reader.readLine("siege mode texture north east"));
                siegeModeTextures.add(reader.readLine("siege mode texture east"));
                siegeModeTextures.add(reader.readLine("siege mode texture south east"));
                siegeModeTextures.add(reader.readLine("siege mode texture south"));
                siegeModeTextures.add(reader.readLine("siege mode texture south west"));
                siegeModeTextures.add(reader.readLine("siege mode texture west"));
                siegeModeTextures.add(reader.readLine("siege mode texture north west"));
            }
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
    public Unit newInstance() {
        if (!loaded) {
            throw new IllegalStateException("Unit not loaded");
        }

        Unit unit = rotatingGuns.size() > 0 ? new RotatingGunUnit() : new Unit();

        unit.setAtlas(atlas);
        unit.setCode(code);
        unit.setWidth(width);
        unit.setHeight(height);
        unit.setHp(defensiveSpecs.getMaxHp());
        unit.setDefensiveSpecs(defensiveSpecs);
        unit.setOffensiveSpecs(offensiveSpecs);
        unit.setStillTextures(stillTextures);
        unit.setSiegeModeTextures(siegeModeTextures);
        unit.setMovementAnimationIds(movementAnimationIds);
        unit.setSiegeModeAvailable(siegeModeAvailable);
        unit.setSiegeModeTransitionAnimationIds(siegeModeTransitionAnimationIds);
        unit.setFiringTextures(firingTextures);
        unit.setSiegeModeFacingDirection(siegeModeFacingDirection);

        // create firing logic of the unit
        if (fireSources.size() > 0) {
            unit.setFiringLogic(CombatUtils.createFiringLogic(fireSources, firingData));
        }

        // create rotating guns
        if (rotatingGuns.size() > 0) {
            int name = 1;

            for (Map.Entry<RotatingGun, List<FireSource>> entry : rotatingGuns.entrySet()) {
                ((RotatingGunUnit) unit).addGun(String.valueOf(name++), CombatUtils.createRotatingGun(entry, firingData, offensiveSpecs));
            }
        }

        return unit;
    }
}
