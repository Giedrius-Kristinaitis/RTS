package com.gasis.rts.logic.object.building;

import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.map.blockmap.BlockMap;
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

    // the texture used when the building is being placed
    protected String placementTexture;

    // the names of the frame animations and their center coordinates relative to
    // the building's center
    protected Map<Point, String> frameAnimations;

    // names of the complex animations ant their center coordinates relative
    // to the building's center
    protected Map<Point, String> complexAnimations;

    // is the building offensive or not
    protected boolean offensive;

    // the building's dimensions in block map's blocks
    protected byte widthInBlocks;
    protected byte heightInBlocks;

    // animation availability
    protected boolean animationsWhenIdle = false;
    protected boolean animationsWhenActive = false;

    // how long does it take to construct the building (in seconds)
    protected float constructionTime;

    /**
     * Default class constructor
     * @param map
     */
    public BuildingLoader(BlockMap map) {
        super(map);
    }

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
        placementTexture = reader.readLine("placement texture");

        // read frame animations
        List<String> animations = reader.readLines("animation");

        if (animations != null) {
            frameAnimations = new HashMap<Point, String>();

            for (String animation : animations) {
                String name = reader.readLine(animation + " animation name");
                float x = Float.parseFloat(reader.readLine(animation + " relative x"));
                float y = Float.parseFloat(reader.readLine(animation + " relative y"));

                frameAnimations.put(new Point(x, y), name);
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

        // read animation availability
        String animationAvailability = reader.readLine("animation availability");

        if (animationAvailability != null) {
            if (animationAvailability.equalsIgnoreCase("always")) {
                animationsWhenActive = true;
                animationsWhenIdle = true;
            } else if (animationAvailability.equalsIgnoreCase("when active")) {
                animationsWhenActive = true;
                animationsWhenIdle = false;
            } else {
                animationsWhenActive = false;
                animationsWhenIdle = false;
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

        widthInBlocks = Byte.parseByte(reader.readLine("width in blocks"));
        heightInBlocks = Byte.parseByte(reader.readLine("height in blocks"));

        constructionTime = Float.parseFloat(reader.readLine("construction time"));
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

        Building building = !offensive ? new Building(map) : new OffensiveBuilding(map);

        building.setAtlas(atlas);
        building.setWidth(width);
        building.setHeight(height);
        building.setWidthInBlocks(widthInBlocks);
        building.setHeightInBlocks(heightInBlocks);
        building.setTexture(texture);
        building.setCode(code);
        building.setDefensiveSpecs(defensiveSpecs);
        building.setHp(defensiveSpecs.getMaxHp());
        building.setHpBarWidth(hpBarWidth);
        building.setControlContextName(controlContextName);
        building.setHpBarYOffset(hpBarYOffset);
        building.setAnimationsWhenActive(animationsWhenActive);
        building.setAnimationsWhenIdle(animationsWhenIdle);
        building.setConstructionTime(constructionTime);

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

    /**
     * Gets the building's placement texture
     * @return
     */
    public String getPlacementTexture() {
        return placementTexture;
    }

    /**
     * Gets the name of the loaded object's texture atlas
     * @return
     */
    public String getAtlas() {
        return atlas;
    }

    /**
     * Gets the building's width in blocks
     * @return
     */
    public byte getWidthInBlocks() {
        return widthInBlocks;
    }

    /**
     * Gets the building's height in blocks
     * @return
     */
    public byte getHeightInBlocks() {
        return heightInBlocks;
    }
}
