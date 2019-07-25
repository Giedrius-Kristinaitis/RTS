package com.gasis.rts.logic.object.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.Updatable;
import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

import java.util.Random;

/**
 * Handles game object destruction
 */
public class DestructionHandler implements TargetReachListener, Renderable, Updatable {

    // the game's map
    protected BlockMap map;

    // used to generate random data
    protected final Random random = new Random();

    /**
     * Default class constructor
     * @param map
     */
    public DestructionHandler(BlockMap map) {
        this.map = map;
    }

    /**
     * Gets called when a projectile reaches it's target
     *
     * @param targetX   x coordinate of the target
     * @param targetY   y coordinate of the target
     * @param damage    the damage caused by the projectile
     * @param explosive is the projectile that reached the target explosive or not
     * @param scale     the scale of the projectile
     */
    @Override
    public void targetReached(float targetX, float targetY, float damage, boolean explosive, byte scale) {
        if (explosive) {
            createCrater(targetX, targetY, scale);
        }
    }

    /**
     * Creates a new crater and adds it to the map
     *
     * @param targetX target x
     * @param targetY target y
     * @param scale projectile's scale
     */
    protected void createCrater(float targetX, float targetY, byte scale) {
        short blockX = (short) (targetX / Block.BLOCK_WIDTH);
        short blockY = (short) (targetY / Block.BLOCK_HEIGHT);

        float offsetX = targetX - blockX * Block.BLOCK_WIDTH;
        float offsetY = targetY - blockY * Block.BLOCK_HEIGHT;

        float textureScale = 1.2f;

        String craterTexture = "";

        if (scale == FireSource.HEAVY || scale == FireSource.MEDIUM) {
            craterTexture = Constants.LARGE_CRATER_PREFIX + random.nextInt(Constants.LARGE_CRATER_COUNT);

            if (scale == FireSource.MEDIUM) {
                textureScale = Math.min(1.25f , 0.75f + random.nextFloat());
            }
        } else if (scale == FireSource.SMALL) {
            craterTexture = Constants.SMALL_CRATER_PREFIX + random.nextInt(Constants.SMALL_CRATER_COUNT);
        }

        map.addCrater(craterTexture, blockX, blockY, offsetX, offsetY, textureScale);
    }

    /**
     * Updates the state of the object
     *
     * @param delta time elapsed since the last update
     */
    @Override
    public void update(float delta) {

    }

    /**
     * Renders the object to the screen
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {

    }
}
