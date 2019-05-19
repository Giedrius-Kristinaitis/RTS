package com.gasis.rts.logic.animation.complexanimation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.animation.Animation;
import com.gasis.rts.logic.animation.AnimationFinishListener;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimation;
import com.gasis.rts.resources.Resources;

/**
 * An animation of a flying projectile. Do not dispose of this animation until the
 * end animation finishes. If you do, nothing will crash, just the end animation will
 * disappear
 */
public abstract class ProjectileAnimation implements Animation, AnimationFinishListener {

    // the flying projectile
    protected FrameAnimation projectile;

    // the animation at the end of the firing thing
    protected FrameAnimation fireAnimation;

    // the animation that gets played when the projectile reaches it's target
    protected FrameAnimation endAnimation;

    // has the fire animation finished or not
    protected boolean fireAnimationFinished = false;

    // has the projectile reached it's target
    protected boolean targetReached = false;

    /**
     * Class constructor
     */
    public ProjectileAnimation(FrameAnimation projectile, FrameAnimation fireAnimation, FrameAnimation endAnimation) {
        this.fireAnimation = fireAnimation;
        this.projectile = projectile;
        this.endAnimation = endAnimation;

        fireAnimation.addFinishListener(this);
        projectile.addFinishListener(this);
    }

    /**
     * Notifies the observer that the animation has finished
     *
     * @param animation the animation that just finished
     */
    @Override
    public void finished(Animation animation) {
        if (animation == fireAnimation) {
            fireAnimationFinished = true;
        } else if (animation == projectile) {
            targetReached = true;
        }
    }

    /**
     * Adds a listener that listens for the event when the projectile reaches it's target
     *
     * @param listener target listener
     */
    public void addTargetReachedListener(AnimationFinishListener listener) {
        projectile.addFinishListener(listener);
    }

    /**
     * Gets the x coordinate of firing location
     * @return
     */
    public float getStartingX() {
        return projectile.getInitialCenterX();
    }

    /**
     * Gets the y coordinate of firing location
     * @return
     */
    public float getStartingY() {
        return projectile.getInitialCenterY();
    }

    /**
     * Gets the x coordinate of the target
     * @return
     */
    public float getTargetX() {
        return projectile.getFinalCenterX();
    }

    /**
     * Gets the y coordinate of the target
     * @return
     */
    public float getTargetY() {
        return projectile.getFinalCenterY();
    }

    /**
     * Sets the firing x coordinate
     *
     * @param startingX new x coordinate
     */
    public void setStartingX(float startingX) {
        projectile.setCenterX(startingX);
        projectile.setInitialCenterX(startingX);
    }

    /**
     * Sets the firing y coordinate
     *
     * @param startingY new y coordinate
     */
    public void setStartingY(float startingY) {
        projectile.setCenterY(startingY);
        projectile.setInitialCenterY(startingY);
    }

    /**
     * Sets the x coordinate of the target
     *
     * @param targetX new target x
     */
    public void setTargetX(float targetX) {
        projectile.setFinalCenterX(targetX);
    }

    /**
     * Sets the y coordinate of the target
     *
     * @param targetY new target y
     */
    public void setTargetY(float targetY) {
        projectile.setFinalCenterY(targetY);
    }

    /**
     * Updates the animation
     *
     * @param delta time elapsed since the last render
     */
    @Override
    public void update(float delta) {
        if (!fireAnimationFinished) {
            fireAnimation.update(delta);
        }

        if (targetReached) {
            if (endAnimation != null) {
                endAnimation.update(delta);
            }
        } else {
            projectile.update(delta);
        }
    }

    /**
     * Renders the animation
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {
        if (!fireAnimationFinished) {
            fireAnimation.render(batch, resources);
        }

        if (targetReached) {
            if (endAnimation != null) {
                endAnimation.render(batch, resources);
            }
        } else {
            projectile.render(batch, resources);
        }
    }
}
