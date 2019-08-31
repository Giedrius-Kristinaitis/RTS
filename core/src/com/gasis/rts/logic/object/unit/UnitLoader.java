package com.gasis.rts.logic.object.unit;

import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.GameObjectLoader;
import com.gasis.rts.logic.object.LoaderUtils;
import com.gasis.rts.logic.object.combat.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Loads a unit from a unit description file
 */
@SuppressWarnings("Duplicates")
public class UnitLoader extends GameObjectLoader {

    // combat data
    protected DefensiveSpecs defensiveSpecs = new DefensiveSpecs();
    protected OffensiveSpecs offensiveSpecs = new OffensiveSpecs();

    // textures used when the unit is standing still or doesn't have movement animations
    // texture indexes must match facing direction values defined in Unit class
    protected List<String> stillTextures = new ArrayList<String>();

    // name values of the movement animations
    // animation name indexes must match facing direction values defined in Unit class
    protected List<String> movementAnimationNames;

    // name values of the siege mode transition animations
    // animation name indexes must match facing direction values defined in Unit class
    protected List<String> siegeModeTransitionAnimationNames;

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

    // how much time does it take to produce the unit (in seconds)
    protected float productionTime;

    // the tech required in order for siege mode to work
    protected String siegeModeRequiredTechId;

    /**
     * Default class constructor
     * @param map
     */
    public UnitLoader(BlockMap map) {
        super(map);
    }

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

            try {
                siegeModeRequiredTechId = reader.readLine("siege mode required tech id");
            } catch (Exception ex) {}

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
            movementAnimationNames = new ArrayList<String>();
            movementAnimationNames.add(reader.readLine("movement animation name north"));
            movementAnimationNames.add(reader.readLine("movement animation name north east"));
            movementAnimationNames.add(reader.readLine("movement animation name east"));
            movementAnimationNames.add(reader.readLine("movement animation name south east"));
            movementAnimationNames.add(reader.readLine("movement animation name south"));
            movementAnimationNames.add(reader.readLine("movement animation name south west"));
            movementAnimationNames.add(reader.readLine("movement animation name west"));
            movementAnimationNames.add(reader.readLine("movement animation name north west"));
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
            siegeModeTransitionAnimationNames = new ArrayList<String>();
            siegeModeTextures = new ArrayList<String>();

            if (Byte.parseByte(reader.readLine("siege mode facing directions")) == 1) {
                siegeModeTransitionAnimationNames.add(reader.readLine("siege mode transition animation name"));
                siegeModeTextures.add(reader.readLine("siege mode texture"));
            } else {
                siegeModeTransitionAnimationNames.add(reader.readLine("siege mode transition animation name north"));
                siegeModeTransitionAnimationNames.add(reader.readLine("siege mode transition animation name north east"));
                siegeModeTransitionAnimationNames.add(reader.readLine("siege mode transition animation name east"));
                siegeModeTransitionAnimationNames.add(reader.readLine("siege mode transition animation name south east"));
                siegeModeTransitionAnimationNames.add(reader.readLine("siege mode transition animation name south"));
                siegeModeTransitionAnimationNames.add(reader.readLine("siege mode transition animation name south west"));
                siegeModeTransitionAnimationNames.add(reader.readLine("siege mode transition animation name west"));
                siegeModeTransitionAnimationNames.add(reader.readLine("siege mode transition animation name north west"));

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
     * Reads the unit's production data
     * @param reader file reader to read data from
     */
    protected void readProductionData(FileLineReader reader) {
        productionTime = Float.parseFloat(reader.readLine("production time"));
    }

    /**
     * Reads other data of the object that is not meta data
     *
     * @param reader reader to read data from
     */
    @Override
    protected void readOtherData(FileLineReader reader) {
        readProductionData(reader);
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

        Unit unit = rotatingGuns.size() > 0 ? new RotatingGunUnit(map) : new Unit(map);

        unit.setAtlas(atlas);
        unit.setCode(code);
        unit.setWidth(width);
        unit.setHeight(height);
        unit.setHp(defensiveSpecs.getMaxHp());
        unit.setDefensiveSpecs(defensiveSpecs);
        unit.setOffensiveSpecs(offensiveSpecs);
        unit.setStillTextures(stillTextures);
        unit.setSiegeModeTextures(siegeModeTextures);
        unit.setMovementAnimationNames(movementAnimationNames);
        unit.setSiegeModeAvailable(siegeModeAvailable);
        unit.setSiegeModeTransitionAnimationNames(siegeModeTransitionAnimationNames);
        unit.setFiringTextures(firingTextures);
        unit.setSiegeModeFacingDirection(siegeModeFacingDirection);
        unit.setHpBarWidth(hpBarWidth);
        unit.setControlContextName(controlContextName);
        unit.setHpBarYOffset(hpBarYOffset);
        unit.setHpBarXOffset(hpBarXOffset);
        unit.setDestructionAnimationName(destructionAnimationName);
        unit.setDestructionAnimationScale(destructionAnimationScale);
        unit.setJunkScale(junkScale);
        unit.setJunkTexture(junkTexture);
        unit.setJunkAtlas(junkAtlas);
        unit.setHealingSpeed(healingSpeed);
        unit.setPassable(passable);
        unit.setSiegeModeRequiredTechId(siegeModeRequiredTechId);

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

    /**
     * Gets the unit's production time in seconds
     * @return
     */
    public float getProductionTime() {
        return productionTime;
    }

    /**
     * Gets the unit's offensive specs
     * @return
     */
    public OffensiveSpecs getOffensiveSpecs() {
        return offensiveSpecs;
    }

    /**
     * Gets the unit's defensive specs
     * @return
     */
    public DefensiveSpecs getDefensiveSpecs() {
        return defensiveSpecs;
    }
}
