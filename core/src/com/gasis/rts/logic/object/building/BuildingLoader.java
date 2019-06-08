package com.gasis.rts.logic.object.building;

import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.logic.object.GameObjectLoader;
import com.gasis.rts.logic.object.LoaderUtils;
import com.gasis.rts.logic.object.combat.*;
import com.gasis.rts.math.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads buildings from their description files
 */
public class BuildingLoader extends GameObjectLoader {

    // combat data
    protected DefensiveSpecs defensiveSpecs = new DefensiveSpecs();
    protected OffensiveSpecs offensiveSpecs = new OffensiveSpecs();

    // firing data
    protected FiringData firingData = new FiringData();

    // fire sources of the building (if it has any)
    protected List<FireSource> fireSources;

    // rotating guns of the building (if it has any)
    protected Map<RotatingGun, List<FireSource>> rotatingGuns;

    // texture of the building
    protected String texture;

    // the ids of the frame animations and their center coordinates relative to
    // the building's center
    protected Map<Point, Short> frameAnimations;

    // names of the complex animations ant their center coordinates relative
    // to the building's center
    protected Map<Point, String> complexAnimations;

    // is the building offensive or not
    protected boolean offensive;

    /**
     * Reads combat data of the building
     *
     * @param reader file reader to read data from
     */
    @SuppressWarnings("Duplicates")
    protected void readCombatData(FileLineReader reader) {
        defensiveSpecs.setMaxHp(Float.parseFloat(reader.readLine("hp")));
        defensiveSpecs.setDefence(Float.parseFloat(reader.readLine("defence")));
        defensiveSpecs.setSightRange(Float.parseFloat(reader.readLine("sight range")));

        offensive = Boolean.parseBoolean(reader.readLine("offensive"));

        if (offensive) {
            offensiveSpecs.setAttack(Float.parseFloat(reader.readLine("attack")));
            offensiveSpecs.setSpeed(Float.parseFloat(reader.readLine("speed")));
            offensiveSpecs.setAttackRange(Float.parseFloat(reader.readLine("attack range")));

            firingData.setShotCount(Byte.parseByte(reader.readLine("shot count")));
            firingData.setShotInterval(Float.parseFloat(reader.readLine("shot interval")));
            firingData.setReloadSpeed(Float.parseFloat(reader.readLine("reload speed")));

            // read building's fire sources (excluding rotating gun sources)
            fireSources = LoaderUtils.readFireSources(reader);

            // read rotating gun data (and fire sources)
            rotatingGuns = LoaderUtils.readRotatingGuns(reader);
        }
    }

    /**
     * Reads textures and animations of the building
     *
     * @param reader file reader to read data from
     */
    @SuppressWarnings("Duplicates")
    protected void readTexturesAndAnimations(FileLineReader reader) {
        texture = reader.readLine("texture");

        // read frame animations
        List<String> animations = reader.readLines("animation");

        if (animations != null) {
            frameAnimations = new HashMap<Point, Short>();

            for (String animation : animations) {
                short id = Short.parseShort(reader.readLine(animation + " animation id"));
                float x = Float.parseFloat(reader.readLine(animation + " relative x"));
                float y = Float.parseFloat(reader.readLine(animation + " relative y"));

                frameAnimations.put(new Point(x, y), id);
            }
        }

        // read complex animations
        animations = reader.readLines("complex animation");

        if (animations != null) {
            complexAnimations = new HashMap<Point, String>();

            for (String animation : animations) {
                String name = reader.readLine(animation + " animation name");
                float x = Float.parseFloat(reader.readLine(animation + " relative x"));
                float y = Float.parseFloat(reader.readLine(animation + " relative y"));

                complexAnimations.put(new Point(x, y), name);
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
     * Creates a new instance of the loaded object
     *
     * @return new instance of the loaded object
     */
    @Override
    public Building newInstance() {
        if (!loaded) {
            throw new IllegalStateException("Building not loaded");
        }

        Building building = !offensive ? new Building() : new OffensiveBuilding();

        building.setAtlas(atlas);
        building.setWidth(width);
        building.setHeight(height);
        building.setTexture(texture);
        building.setCode(code);
        building.setDefensiveSpecs(defensiveSpecs);
        building.setHp(defensiveSpecs.getMaxHp());

        // add firing things to the building if it has any
        if (offensive) {
            if (fireSources.size() > 0) {
                ((OffensiveBuilding) building).setFiringLogic(CombatUtils.createFiringLogic(fireSources, firingData));
            }

            if (rotatingGuns.size() > 0) {
                int name = 1;

                for (Map.Entry<RotatingGun, List<FireSource>> entry : rotatingGuns.entrySet()) {
                    ((OffensiveBuilding) building).addGun(String.valueOf(name++), CombatUtils.createRotatingGun(entry, firingData, offensiveSpecs));
                }
            }

            ((OffensiveBuilding) building).setOffensiveSpecs(offensiveSpecs);
        }

        // add animations to the building
        if (frameAnimations != null) {
            building.setFrameAnimations(frameAnimations);
        }

        if (complexAnimations != null) {
            building.setComplexAnimations(complexAnimations);
        }

        return building;
    }
}
