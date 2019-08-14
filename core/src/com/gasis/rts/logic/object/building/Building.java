package com.gasis.rts.logic.object.building;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.animation.Animation;
import com.gasis.rts.logic.animation.complexanimation.RisingSmokeAnimation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.object.production.UnitProducer;
import com.gasis.rts.logic.object.production.UnitProductionListener;
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.logic.object.unit.UnitLoader;
import com.gasis.rts.logic.player.Player;
import com.gasis.rts.logic.task.ResourceProviderTask;
import com.gasis.rts.logic.task.Task;
import com.gasis.rts.math.Point;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

import java.util.*;

/**
 * A building on the map
 */
public class Building extends GameObject implements UnitProducer {

    // the name of the building's texture
    protected String texture;

    // building frame animations' names and coordinates
    protected Map<Point, String> frameAnimations;

    // buildings complex animation's names and coordinates
    protected Map<Point, String> complexAnimations;

    // animations of the building
    protected List<Animation> animations = new ArrayList<Animation>();

    // building's dimensions in blocks
    protected byte widthInBlocks;
    protected byte heightInBlocks;

    // building's coordinates in blocks
    protected short xInBlocks;
    protected short yInBlocks;

    // animation availability
    protected boolean animationsWhenIdle = false;
    protected boolean animationsWhenActive = false;

    // the point at which units spawn
    protected Point spawnPoint;

    // the loader of the unit that is currently being produced
    protected UnitLoader producedUnitLoader;

    // is the building producing units or researching something right now
    protected boolean producing = false;

    // production/research progress
    protected float progress = 0f;

    // is the building currently being constructed
    protected boolean beingConstructed = false;

    // how long does it take to construct the building (in seconds)
    protected float constructionTime;

    // the blocks the building has occupied
    protected List<Point> occupiedBlocks;

    // construction listeners
    protected Set<BuildingConstructionListener> constructionListeners = new HashSet<BuildingConstructionListener>();

    // unit production listeners
    protected Set<UnitProductionListener> unitProductionListeners = new HashSet<UnitProductionListener>();

    // the point to which spawned units go
    protected Point gatherPoint;

    // task executed by the building
    protected Task task;

    // should the task be executed periodically or just once
    protected boolean taskExecutedPeriodically;

    // time period in which the task will be executed (in seconds)
    protected float taskPeriod;

    // the time that has elapsed since the last execution of the task
    protected float timeSinceLastTaskExecution;

    // should the task be reverted on building's destruction
    protected boolean revertTaskOnDestruction;

    // how much electricity does the building require in order to work
    protected int electricityRequirement;

    // does the building have electricity to function
    protected boolean electricityAvailable;

    // time period in which the no electricity indicator flashes
    protected final float noElectricityIndicatorPeriod = 0.5f;

    // how much time has passed since the last time electricity indicator flashed
    protected float timeSinceElectricityIndicatorFlash;

    // should the building render it's gather point
    protected boolean renderGatherPoint;

    // the animation that is played at the building's gather point
    protected FrameAnimation gatherPointAnimation;

    /**
     * Default class constructor
     * @param map
     */
    public Building(BlockMap map) {
        super(map);
    }

    /**
     * Sets the render gather point flag
     *
     * @param renderGatherPoint should the building render it's gather point
     */
    public void setRenderGatherPoint(boolean renderGatherPoint) {
        this.renderGatherPoint = renderGatherPoint;

        if (gatherPointAnimation == null) {
            gatherPointAnimation = FrameAnimationFactory.getInstance().create("gather_point");
        }
    }

    /**
     * Enables or disabled building's electricity
     *
     * @param electricityAvailable is electricity available
     */
    public void setElectricityAvailable(boolean electricityAvailable) {
        this.electricityAvailable = electricityAvailable;
    }

    /**
     * Checks if the building has electricity
     * @return
     */
    public boolean isElectricityAvailable() {
        return electricityAvailable;
    }

    /**
     * Sets the building's electricity requirement
     *
     * @param electricityRequirement building's electricity requirement
     */
    public void setElectricityRequirement(int electricityRequirement) {
        this.electricityRequirement = electricityRequirement;
    }

    /**
     * Gets the building's electricity requirement
     * @return
     */
    public int getElectricityRequirement() {
        return electricityRequirement;
    }

    /**
     * Sets the task that will be executed by the building
     *
     * @param task task to execute
     */
    public void setTask(Task task) {
        this.task = task;
    }

    /**
     * Makes the task be executed periodically or once
     *
     * @param taskExecutedPeriodically should the task be executed periodically
     */
    public void setTaskExecutedPeriodically(boolean taskExecutedPeriodically) {
        this.taskExecutedPeriodically = taskExecutedPeriodically;
    }

    /**
     * Sets the task's execution period
     *
     * @param taskPeriod task execution period
     */
    public void setTaskPeriod(float taskPeriod) {
        this.taskPeriod = taskPeriod;
        timeSinceLastTaskExecution = taskPeriod;
    }

    /**
     * Sets the task's revert on destruction flag
     *
     * @param revertTaskOnDestruction should the task be reverted on building's destruction
     */
    public void setRevertTaskOnDestruction(boolean revertTaskOnDestruction) {
        this.revertTaskOnDestruction = revertTaskOnDestruction;
    }

    /**
     * Sets the owner of the object
     *
     * @param owner new owner
     */
    @Override
    public void setOwner(Player owner) {
        super.setOwner(owner);

        if (task != null && task instanceof ResourceProviderTask) {
            ((ResourceProviderTask) task).setPlayer(owner);
        }
    }

    /**
     * Does damage to the object
     *
     * @param attack attack stat of the attacker,
     *               damage will be calculated based on the object's defence
     */
    @Override
    public void doDamage(float attack) {
        if (!destroyed) {
            super.doDamage(attack);

            if (destroyed && revertTaskOnDestruction && task != null) {
                task.revert();
            }
        }
    }

    /**
     * Sets the new gather point of the building
     *
     * @param gatherPoint new gather point
     */
    public void setGatherPoint(Point gatherPoint) {
        this.gatherPoint = gatherPoint;
    }

    /**
     * Adds a unit production listener
     *
     * @param listener listener to add
     */
    public void addUnitProductionListener(UnitProductionListener listener) {
        unitProductionListeners.add(listener);
    }

    /**
     * Removes a unit production listener
     *
     * @param listener listener to remove
     */
    public void removeUnitProductionListener(UnitProductionListener listener) {
        unitProductionListeners.remove(listener);
    }

    /**
     * Notifies unit production listeners that a unit has been produced
     *
     * @param unit the unit that was just produced
     */
    protected void notifyUnitProductionListeners(Unit unit) {
        for (UnitProductionListener listener: unitProductionListeners) {
            listener.unitProduced(unit);
        }
    }

    /**
     * Adds a construction listener
     *
     * @param listener listener to add
     */
    public void addConstructionListener(BuildingConstructionListener listener) {
        constructionListeners.add(listener);
    }

    /**
     * Removes a construction listener
     *
     * @param listener listener to remove
     */
    public void removeConstructionListener(BuildingConstructionListener listener) {
        constructionListeners.remove(listener);
    }

    /**
     * Checks if the object can be safely removed from object list
     *
     * @return
     */
    @Override
    public boolean canBeRemoved() {
        return true;
    }

    /**
     * Sets the building's construction time
     *
     * @param constructionTime new construction time in seconds
     */
    public void setConstructionTime(float constructionTime) {
        this.constructionTime = constructionTime;
    }

    /**
     * Sets the building's construction flag
     *
     * @param beingConstructed is the building being constructed right now
     */
    public void setBeingConstructed(boolean beingConstructed) {
        this.beingConstructed = beingConstructed;

        if (beingConstructed) {
            hp = defensiveSpecs.getMaxHp() * 0.1f;
            renderHp = true;
        }
    }

    /**
     * Checks if the building is being constructed
     * @return
     */
    public boolean isBeingConstructed() {
        return beingConstructed;
    }

    /**
     * Sets animation availability when the building is idle
     *
     * @param animationsWhenIdle are the animations available when the building is idle
     */
    public void setAnimationsWhenIdle(boolean animationsWhenIdle) {
        this.animationsWhenIdle = animationsWhenIdle;
    }

    /**
     * Sets animation availability when the building is active
     *
     * @param animationsWhenActive are the animations available when the building is active
     */
    public void setAnimationsWhenActive(boolean animationsWhenActive) {
        this.animationsWhenActive = animationsWhenActive;
    }

    /**
     * Queues up a unit to be produced
     *
     * @param unit loader of the unit
     */
    @Override
    public void queueUp(UnitLoader unit) {
        if (!producing && !beingConstructed && (electricityAvailable || electricityRequirement == 0)) {
            producedUnitLoader = unit;
            progress = 0;
            producing = true;
        }
    }

    /**
     * Gets the closest point to the spawn point that is empty
     * @return
     */
    protected Point getClosestPointToSpawnPoint() {
        Point spawnPoint = getSpawnPoint();
        Point closest = new Point(spawnPoint.x, spawnPoint.y);

        if (!map.isBlockPassable((short) closest.x, (short) closest.y) || map.isBlockOccupied((short) closest.x, (short) closest.y)) {
            short size = 3;

            /*
             * I know this piece of code is ugly, but I'm really lazy at the time of writing this
             * so I'm gonna leave it as it is...
             *
             * At least it does it's job
             */
            while (true) {
                // loop through the left side of the square
                for (short y = (short) (closest.y + (short) (size / 2)); y > closest.y - (short) (size / 2); y--) {
                    if (map.isBlockPassable((short) (closest.x - (short) (size / 2)), y) && !map.isBlockOccupied((short) (closest.x - (short) (size / 2)), y)) {
                        closest.y = y;
                        closest.x = (short) (closest.x - (short) (size / 2));
                        return closest;
                    }
                }

                // loop through the right side of the square
                for (short y = (short) (closest.y - (short) (size / 2)); y < closest.y + (short) (size / 2); y++) {
                    if (map.isBlockPassable((short) (closest.x + (short) (size / 2)), y) && !map.isBlockOccupied((short) (closest.x + (short) (size / 2)), y)) {
                        closest.y = y;
                        closest.x = (short) (closest.x + (short) (size / 2));
                        return closest;
                    }
                }

                // loop through the bottom side of the square
                for (short x = (short) (closest.x - (short) (size / 2)); x < closest.x + (short) (size / 2); x++) {
                    if (map.isBlockPassable(x, (short) (closest.y - (short) (size / 2))) && !map.isBlockOccupied(x, (short) (closest.y - (short) (size / 2)))) {
                        closest.x = x;
                        closest.y = (short) (closest.y - (short) (size / 2));
                        return closest;
                    }
                }

                // loop through the top side of the square
                for (short x = (short) (closest.x - (short) (size / 2)); x < closest.x + (short) (size / 2); x++) {
                    if (map.isBlockPassable(x, (short) (closest.y + (short) (size / 2))) && !map.isBlockOccupied(x, (short) (closest.y + (short) (size / 2)))) {
                        closest.x = x;
                        closest.y = (short) (closest.y + (short) (size / 2));
                        return closest;
                    }
                }

                size += 2;

                if (size > Math.max(map.getWidth(), map.getHeight())) {
                    break;
                }
            }
        }

        return closest;
    }

    /**
     * Spawns the unit that was just produced
     */
    protected void spawnUnit() {
        Point spawn = getClosestPointToSpawnPoint();

        Unit unit = producedUnitLoader.newInstance();

        unit.setCenterX(spawn.x * Block.BLOCK_WIDTH + Block.BLOCK_WIDTH / 2f);
        unit.setCenterY(spawn.y * Block.BLOCK_HEIGHT + Block.BLOCK_HEIGHT / 2f);

        owner.addUnit(unit);
        unit.setOwner(owner);

        map.occupyBlock((short) spawn.x, (short) spawn.y, unit);

        notifyUnitProductionListeners(unit);

        if (gatherPoint != null) {
            unit.requestToMove((short) gatherPoint.x, (short) gatherPoint.y);
        }

        producing = false;
    }

    /**
     * Initializes the building's animations. Must be called after the building's position
     * is set
     */
    public void initializeAnimations() {
        initializeFrameAnimations();
        initializeComplexAnimations();
    }

    /**
     * Initializes frame animations
     */
    private void initializeFrameAnimations() {
        if (frameAnimations != null && frameAnimations.size() != 0) {
            for (Map.Entry<Point, String> animation: frameAnimations.entrySet()) {
                FrameAnimation frameAnimation = FrameAnimationFactory.getInstance().create(
                        animation.getValue(),
                        getCenterX() + animation.getKey().x,
                        getCenterY() + animation.getKey().y,
                        getCenterX() + animation.getKey().x,
                        getCenterY() + animation.getKey().y,
                        true);


                frameAnimation.setDelayedOnLoop(true);

                animations.add(frameAnimation);
            }
        }
    }

    /**
     * Initializes complex animations
     */
    private void initializeComplexAnimations() {
        if (complexAnimations != null && complexAnimations.size() != 0) {
            for (Map.Entry<Point, String> animation: complexAnimations.entrySet()) {
                Animation complexAnimation = null;

                if (animation.getValue().equalsIgnoreCase("rising smoke")) {
                    complexAnimation = new RisingSmokeAnimation(
                            getCenterX() + animation.getKey().x,
                            getCenterY() + animation.getKey().y
                    );
                } else {
                    throw new RuntimeException("Bad animation name");
                }

                animations.add(complexAnimation);
            }
        }
    }

    /**
     * Sets the frame animations' names and coordinates
     *
     * @param frameAnimations map with names and coordinates
     */
    public void setFrameAnimations(Map<Point, String> frameAnimations) {
        this.frameAnimations = frameAnimations;
    }

    /**
     * Sets the complex animations' names and coordinates
     *
     * @param complexAnimations map with animation names and coordinates
     */
    public void setComplexAnimations(Map<Point, String> complexAnimations) {
        this.complexAnimations = complexAnimations;
    }

    /**
     * Gets the name of the building's texture
     * @return
     */
    public String getTexture() {
        return texture;
    }

    /**
     * Sets the texture of the building
     *
     * @param texture name of the building's texture
     */
    public void setTexture(String texture) {
        this.texture = texture;
    }

    /**
     * De-occupies the building's occupied blocks
     */
    @Override
    public void deoccupyBlocks() {
        for (Point block: occupiedBlocks) {
            map.occupyBlock((short) block.x, (short) block.y, null);
        }
    }

    /**
     * Occupies blocks on the map
     */
    public void occupyBlocks(List<Point> blocks) {
        occupiedBlocks = blocks;

        for (Point block: occupiedBlocks) {
            map.occupyBlock((short) block.x, (short) block.y, this);
        }
    }

    /**
     * Gets the x coordinate of it's occupied block (used for targeting the object)
     *
     * @return
     */
    @Override
    public float getOccupiedBlockX() {
        return (int) (getCenterX() / Block.BLOCK_WIDTH) * Block.BLOCK_WIDTH;
    }

    /**
     * Gets the y coordinate of it's occupied block (used for targeting the object)
     *
     * @return
     */
    @Override
    public float getOccupiedBlockY() {
        return (int) (getCenterY() / Block.BLOCK_HEIGHT) * Block.BLOCK_HEIGHT;
    }

    /**
     * Updates the game object
     *
     * @param delta time elapsed since the last render
     */
    @Override
    public void update(float delta) {
        if (!destroyed) {
            if (!electricityAvailable && electricityRequirement > 0) {
                timeSinceElectricityIndicatorFlash += delta;

                if (timeSinceElectricityIndicatorFlash >= noElectricityIndicatorPeriod * 2f) {
                    timeSinceElectricityIndicatorFlash = 0;
                }

                return;
            }

            if (!beingConstructed && ((animationsWhenActive && producing) || (animationsWhenIdle && !producing))) {
                for (Animation animation : animations) {
                    animation.update(delta);
                }
            }

            if (!beingConstructed) {
                updateHealing(delta);

                if (gatherPointAnimation != null) {
                    gatherPointAnimation.update(delta);
                }
            }

            if (beingConstructed) {
                updateConstruction(delta);
            } else {
                updateProduction(delta);

                if (task != null) {
                    updateTaskExecution(delta);
                }
            }
        }
    }

    /**
     * Updates the building task's execution
     *
     * @param delta time since the last update
     */
    protected void updateTaskExecution(float delta) {
        if (timeSinceLastTaskExecution >= taskPeriod) {
            task.execute();

            if (taskExecutedPeriodically) {
                timeSinceLastTaskExecution = 0;
            } else {
                timeSinceLastTaskExecution = -1;
            }
        } else if (taskExecutedPeriodically) {
            timeSinceLastTaskExecution += delta;
        }
    }

    /**
     * Updates the building's construction progress
     *
     * @param delta time elapsed since the last update
     */
    protected void updateConstruction(float delta) {
        hp += defensiveSpecs.getMaxHp() * delta / constructionTime;

        if (hp >= defensiveSpecs.getMaxHp()) {
            hp = defensiveSpecs.getMaxHp();
            beingConstructed = false;
            renderHp = false;

            notifyConstructionListeners();
        }
    }

    /**
     * Notifies construction listeners that the building has been constructed
     */
    protected void notifyConstructionListeners() {
        for (BuildingConstructionListener listener: constructionListeners) {
            listener.buildingConstructed(this);
        }
    }

    /**
     * Updates unit production
     *
     * @param delta time elapsed since the last update
     */
    protected void updateProduction(float delta) {
        if (producing) {
            if (progress < 1) {
                progress += delta / producedUnitLoader.getProductionTime();
            } else {
                progress = 1;
            }

            if (progress >= 1 && owner.getState().units < owner.getState().maxUnits) {
                spawnUnit();
            }
        }
    }

    /**
     * Renders the object to the screen
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {
        if (!destroyed) {
            batch.draw(
                    resources.atlas(Constants.FOLDER_ATLASES + atlas).findRegion(texture),
                    x,
                    y,
                    width,
                    height
            );

            if (!beingConstructed && ((animationsWhenActive && producing) || (animationsWhenIdle && !producing)) && (electricityAvailable || electricityRequirement == 0)) {
                for (Animation animation : animations) {
                    animation.render(batch, resources);
                }
            }

            renderHp(batch, resources);

            if (renderGatherPoint && gatherPoint != null) {
                renderGatherPoint(batch, resources);
            }

            if (producing) {
                renderProgress(batch, resources);
            }

            if (!electricityAvailable && electricityRequirement > 0 && timeSinceElectricityIndicatorFlash >= noElectricityIndicatorPeriod) {
                batch.draw(
                        resources.atlas(Constants.GENERAL_TEXTURE_ATLAS).findRegion(Constants.NO_ELECTRICITY_INDICATOR_TEXTURE),
                        getCenterX() - 0.25f,
                        getCenterY() - 0.25f,
                        0.5f,
                        0.5f
                );
            }
        }
    }

    /**
     * Renders the building's gather point
     *
     * @param batch sprite batch to draw to
     * @param resources game's assets
     */
    protected void renderGatherPoint(SpriteBatch batch, Resources resources) {
        gatherPointAnimation.setCenterX((int) (gatherPoint.x) * Block.BLOCK_WIDTH + Block.BLOCK_WIDTH / 2f);
        gatherPointAnimation.setCenterY((int) (gatherPoint.y) * Block.BLOCK_HEIGHT + Block.BLOCK_HEIGHT / 2f);
        gatherPointAnimation.render(batch, resources);
    }

    /**
     * Renders the current production/research progress
     *
     * @param batch sprite batch to draw to
     * @param resources game's assets
     */
    protected void renderProgress(SpriteBatch batch, Resources resources) {
        batch.draw(resources.atlas(Constants.GENERAL_TEXTURE_ATLAS).findRegion(Constants.HP_BAR_BACKGROUND_TEXTURE),
                getCenterX() - hpBarWidth / 2f, y + height + hpBarYOffset - 0.15f, hpBarWidth, 0.1f);

        batch.draw(resources.atlas(Constants.GENERAL_TEXTURE_ATLAS).findRegion(Constants.PRODUCTION_PROGRESS_TEXTURE),
                getCenterX() - hpBarWidth / 2f + 0.025f, y + height + 0.025f + hpBarYOffset - 0.15f, hpBarWidth * progress - 0.05f, 0.05f);
    }

    /**
     * Renders the object's hp bar
     *
     * @param batch     sprite batch to draw to
     * @param resources game's assets
     */
    @Override
    protected void renderHp(SpriteBatch batch, Resources resources) {
        if (!beingConstructed) {
            super.renderHp(batch, resources);
        } else {
            batch.draw(resources.atlas(Constants.GENERAL_TEXTURE_ATLAS).findRegion(Constants.HP_BAR_BACKGROUND_TEXTURE),
                    hpBarXOffset + getCenterX() - hpBarWidth / 2f, y + height + hpBarYOffset, hpBarWidth, 0.1f);

            batch.draw(resources.atlas(Constants.GENERAL_TEXTURE_ATLAS).findRegion(Constants.CONSTRUCTION_HP_BAR_TEXTURE),
                    hpBarXOffset + getCenterX() - hpBarWidth / 2f + 0.025f, y + height + 0.025f + hpBarYOffset, hpBarWidth * hp / defensiveSpecs.getMaxHp() - 0.05f, 0.05f);
        }
    }

    /**
     * Gets the building's width in blocks
     * @return
     */
    public byte getWidthInBlocks() {
        return widthInBlocks;
    }

    /**
     * Sets the building's width in blocks
     *
     * @param widthInBlocks new width in blocks
     */
    public void setWidthInBlocks(byte widthInBlocks) {
        this.widthInBlocks = widthInBlocks;
    }

    /**
     * Gets the building's height in blocks
     * @return
     */
    public byte getHeightInBlocks() {
        return heightInBlocks;
    }

    /**
     * Sets the building's height in blocks
     *
     * @param heightInBlocks new height in blocks
     */
    public void setHeightInBlocks(byte heightInBlocks) {
        this.heightInBlocks = heightInBlocks;
    }

    /**
     * Gets the building's x in blocks
     * @return
     */
    public short getXInBlocks() {
        return xInBlocks;
    }

    /**
     * Sets the building's x in blocks
     *
     * @param xInBlocks new x in blocks
     */
    public void setXInBlocks(short xInBlocks) {
        this.xInBlocks = xInBlocks;
    }

    /**
     * Gets the building's y in blocks
     * @return
     */
    public short getYInBlocks() {
        return yInBlocks;
    }

    /**
     * Sets the building's y in blocks
     *
     * @param yInBlocks new y in blocks
     */
    public void setYInBlocks(short yInBlocks) {
        this.yInBlocks = yInBlocks;
    }

    /**
     * Gets the spawn point
     */
    public Point getSpawnPoint() {
        if (spawnPoint == null) {
            spawnPoint = new Point(xInBlocks + widthInBlocks - 1,
                    yInBlocks - 1);
        }

        return spawnPoint;
    }
}
