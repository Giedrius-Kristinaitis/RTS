package com.gasis.rts.logic.object.unit;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.resources.Resources;

import java.util.List;

/**
 * An unit that has an animation when moving
 */
public class AnimatedUnit extends Unit {

    // is the unit moving or not
    protected boolean moving;

    // the animation that is played when the unit moves
    protected FrameAnimation animation;

    // the ids of the movement animations
    // indexes of animation ids must match the values of facing directions
    // defined in the Unit class
    protected List<Short> animationIds;

    /**
     * Checks if the unit is moving
     * @return
     */
    public boolean isMoving() {
        return moving;
    }

    /**
     * Sets the moving value of the unit
     *
     * @param moving is the unit moving or not
     */
    public void setMoving(boolean moving) {
        this.moving = moving;

        if (moving) {
            // create a new movement animation
            animation = FrameAnimationFactory.getInstance().create(
                animationIds.get(facingDirection),
                getX(),
                getY(),
                getX(),
                getY(),
                false
            );

            animation.setWidth(getWidth());
            animation.setHeight(getHeight());
        } else {
            animation = null;
        }
    }

    /**
     * Sets the ids of the movement animations
     *
     * @param animationIds list of animation ids
     */
    public void setAnimationIds(List<Short> animationIds) {
        this.animationIds = animationIds;
    }

    /**
     * Gets the ids of the movement animations
     * @return
     */
    public Iterable<Short> getAnimationIds() {
        return animationIds;
    }

    /**
     * Updates the game object
     *
     * @param delta time elapsed since the last render
     */
    @Override
    public void update(float delta) {
        if (moving && animation != null) {
            // update the moving animation
            animation.update(delta);

            animation.setCenterX(getCenterX());
            animation.setCenterY(getCenterY());
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
        if (!moving) {
            super.render(batch, resources);
        } else if (animation != null) {
            // render the moving animation
            animation.render(batch, resources);
        }
    }
}
