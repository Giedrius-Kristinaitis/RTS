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
import com.gasis.rts.logic.object.unit.Unit;
import com.gasis.rts.logic.object.unit.UnitLoader;
import com.gasis.rts.logic.player.Player;
import com.gasis.rts.math.Point;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    // the point at which units spawn
    protected Point spawnPoint;

    // the loader of the unit that is currently being produced
    protected UnitLoader producedUnitLoader;

    // is the building producing units or researching something right now
    protected boolean producing = false;

    // production/research progress
    protected float progress = 0f;

    // the player that owns the building
    protected Player owner;

    /**
     * Default class constructor
     * @param map
     */
    public Building(BlockMap map) {
        super(map);
    }

    /**
     * Queues up a unit to be produced
     *
     * @param unit loader of the unit
     */
    @Override
    public void queueUp(UnitLoader unit) {
        if (!producing) {
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
                for (short y = (short) (closest.y + (short) (size / 2)); y > closest.y - (short) (size / 2); y-) {
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

        unit.setX(spawn.x * Block.BLOCK_WIDTH);
        unit.setY(spawn.y * Block.BLOCK_HEIGHT);

        owner.addUnit(unit);

        map.occupyBlock((short) spawn.x, (short) spawn.y, unit);

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
     * Updates the game object
     *
     * @param delta time elapsed since the last render
     */
    @Override
    public void update(float delta) {
        for (Animation animation: animations) {
            animation.update(delta);
        }

        updateProduction(delta);
    }

    /**
     * Updates unit production
     *
     * @param delta time elapsed since the last update
     */
    protected void updateProduction(float delta) {
        if (producing) {
            progress += delta / producedUnitLoader.getProductionTime();

            if (progress >= 1) {
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
        batch.draw(
                resources.atlas(Constants.FOLDER_ATLASES + atlas).findRegion(texture),
                x,
                y,
                width,
                height
        );

        for (Animation animation: animations) {
            animation.render(batch, resources);
        }

        renderHp(batch, resources);

        if (producing) {
            renderProgress(batch, resources);
        }
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

    /**
     * Sets the owner of the building
     *
     * @param owner new owner
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
