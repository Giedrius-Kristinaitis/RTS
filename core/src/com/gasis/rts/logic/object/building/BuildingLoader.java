package com.gasis.rts.logic.object.building;

import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.GameObjectLoader;
import com.gasis.rts.logic.object.LoaderUtils;
import com.gasis.rts.logic.object.combat.*;
import com.gasis.rts.logic.task.ElectricityProviderTask;
import com.gasis.rts.logic.task.FinanceProviderTask;
import com.gasis.rts.logic.task.ResourceProviderTask;
import com.gasis.rts.logic.task.Task;
import com.gasis.rts.math.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads buildings from their description files
 */
@SuppressWarnings("Duplicates")
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

    // provider task executed by the building
    protected String providerTask;

    // should the task be executed periodically or just once
    protected boolean taskExecutedPeriodically;

    // time period in which the task will be executed (in seconds)
    protected float taskPeriod;

    // should the task be reverted on building's destruction
    protected boolean revertTaskOnDestruction;

    // the amount of resource provided by provider task
    protected int providerTaskAmount;

    // how much electricity does the building require in order to work
    protected int electricityRequirement;

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
     * Reads building's task data
     *
     * @param reader file reader to read data from
     */
    protected void readTaskData(FileLineReader reader) {
        try {
            providerTask = reader.readLine("provider task");

            providerTaskAmount = Integer.parseInt(reader.readLine(providerTask + " task provided amount"));

            revertTaskOnDestruction = Boolean.parseBoolean(reader.readLine(providerTask + " task reverted on destruction"));

            taskExecutedPeriodically = Boolean.parseBoolean(reader.readLine(providerTask + " task executed periodically"));
            taskPeriod = Float.parseFloat(reader.readLine(providerTask + " task execution period"));
        } catch (Exception ex) {}
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
        readTaskData(reader);

        widthInBlocks = Byte.parseByte(reader.readLine("width in blocks"));
        heightInBlocks = Byte.parseByte(reader.readLine("height in blocks"));

        constructionTime = Float.parseFloat(reader.readLine("construction time"));

        try {
            electricityRequirement = Integer.parseInt(reader.readLine("electricity requirement"));
        } catch (Exception ex) {}
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
        building.setDestructionAnimationName(destructionAnimationName);
        building.setDestructionAnimationScale(destructionAnimationScale);
        building.setJunkScale(junkScale);
        building.setJunkTexture(junkTexture);
        building.setJunkAtlas(junkAtlas);
        building.setElectricityRequirement(electricityRequirement);

        initializeTasks(building);

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
     * Initializes tasks for the building
     *
     * @param building building to add the task(s) to
     */
    protected void initializeTasks(Building building) {
        if (providerTask != null) {
            ResourceProviderTask task = null;

            if (providerTask.equalsIgnoreCase("electricity")) {
                task = new ElectricityProviderTask();
                task.setAmount(providerTaskAmount);
            } else if (providerTask.equalsIgnoreCase("finance")) {
                task = new FinanceProviderTask();
                task.setAmount(providerTaskAmount);
            }

            building.setTask(task);
            building.setTaskExecutedPeriodically(taskExecutedPeriodically);
            building.setTaskPeriod(taskPeriod);
            building.setRevertTaskOnDestruction(revertTaskOnDestruction);
        }
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
