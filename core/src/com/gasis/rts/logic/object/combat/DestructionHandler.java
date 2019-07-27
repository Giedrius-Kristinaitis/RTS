package com.gasis.rts.logic.object.combat;

import com.gasis.rts.logic.animation.AnimationPlayerInterface;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Handles game object destruction
 */
public class DestructionHandler implements TargetReachListener {

    // the game's map
    protected BlockMap map;

    // used to generate random data
    protected final Random random = new Random();

    // used to store a game object's neighbour objects if there are any (done to avoid
    // creating new instance every time)
    protected List<GameObject> neighbourObjects = new ArrayList<GameObject>();

    // plays animations
    protected AnimationPlayerInterface animationPlayer;

    /**
     * Default class constructor
     */
    public DestructionHandler(BlockMap map, AnimationPlayerInterface animationPlayer) {
        this.map = map;
        this.animationPlayer = animationPlayer;
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

        dealDamage((short) (targetX / Block.BLOCK_WIDTH), (short) (targetY / Block.BLOCK_HEIGHT), damage, explosive);
    }

    /**
     * Deals damage to the specified block and if the projectile is explosive, deals
     * damage to nearby blocks
     *
     * @param x target's block x
     * @param y target's block y
     * @param damage damage dealt by the projectile
     * @param explosive is the projectile explosive
     */
    protected void dealDamage(short x, short y, float damage, boolean explosive) {
        GameObject occupyingObject = map.getOccupyingObject(x, y);

        if (occupyingObject != null) {
            occupyingObject.doDamage(damage);

            if (occupyingObject.isDestroyed()) {
                playDestructionAnimation(occupyingObject);
            }
        }

        // also do damage to nearby objects
        if (explosive) {
            neighbourObjects.clear();

            neighbourObjects.add(map.getOccupyingObject(x, (short) (y + 1)));
            neighbourObjects.add(map.getOccupyingObject((short) (x + 1), (short) (y + 1)));
            neighbourObjects.add(map.getOccupyingObject((short) (x + 1), y));
            neighbourObjects.add(map.getOccupyingObject((short) (x + 1), (short) (y - 1)));
            neighbourObjects.add(map.getOccupyingObject(x, (short) (y - 1)));
            neighbourObjects.add(map.getOccupyingObject((short) (x - 1), (short) (y - 1)));
            neighbourObjects.add(map.getOccupyingObject((short) (x - 1), y));
            neighbourObjects.add(map.getOccupyingObject((short) (x - 1), (short) (y + 1)));

            for (GameObject object: neighbourObjects) {
                if (object != null && object != occupyingObject) {
                    object.doDamage(damage * 0.25f);

                    if (object.isDestroyed()) {
                        playDestructionAnimation(object);
                    }
                }
            }
        }
    }

    /**
     * Plays a destruction animation
     *
     * @param object the object for which the animation will be played
     */
    protected void playDestructionAnimation(GameObject object) {
        if (object.getDestructionAnimationName() == null) {
            return;
        }

        FrameAnimation animation = FrameAnimationFactory.getInstance().create(object.getDestructionAnimationName());

        animation.setCenterX(object.getCenterX());
        animation.setCenterY(object.getCenterY());

        animationPlayer.play(animation);
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
}
