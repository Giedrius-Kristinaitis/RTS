package com.gasis.rts.logic.animation.complexanimation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.animation.Animation;
import com.gasis.rts.logic.animation.AnimationFinishListener;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimation;
import com.gasis.rts.logic.object.combat.TargetReachListener;
import com.gasis.rts.resources.Resources;

import java.util.ArrayList;
import java.util.List;

/**
 * An animation of a flying projectile. Do not dispose of this animation until the
 * end animation finishes. If you do, nothing will crash, just the end animation will
 * disappear
 */
public class ProjectileAnimation implements Animation, AnimationFinishListener {

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

    // has the end animation finished or not
    protected boolean endAnimationFinished = false;

    // target reach listeners
    protected List<TargetReachListener> targetReachListeners = new ArrayList<TargetReachListener>();

    /**
     * Class constructor
     */
    public ProjectileAnimation(FrameAnimation projectile, FrameAnimation fireAnimation, FrameAnimation endAnimation) {
        this.fireAnimation = fireAnimation;
        this.projectile = projectile;
        this.endAnimation = endAnimation;

        fireAnimation.addFinishListener(this);
        projectile.addFinishListener(this);
        endAnimation.addFinishListener(this);
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

            // notify listeners
            for (TargetReachListener listener: targetReachListeners) {
                listener.targetReached(projectile.getFinalCenterX(), projectile.getFinalCenterY());
            }
        } else if (animation == endAnimation) {
            endAnimationFinished = true;
        }
    }

    /**
     * Adds a listener that listens for the event when the projectile reaches it's target
     *
     * @param listener target listener
     */
    public void addTargetReachedListener(TargetReachListener listener) {
        targetReachListeners.add(listener);
    }

    /**
     * Adds a listener that listens for the end of the end animation
     *
     * @param listener end animation's finish listener
     */
    public void addEndAnimationFinishListener(AnimationFinishListener listener) {
        endAnimation.addFinishListener(listener);
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
     * Sets the trajectory of the projectile
     *
     * @param x starting x coordinate
     * @param y starting y coordinate
     * @param targetX target x coordinate
     * @param targetY target y coordinate
     */
    public void setTrajectory(float x, float y, float targetX, float targetY) {
        projectile.setInitialCenterX(x);
        projectile.setInitialCenterY(y);

        projectile.setCenterX(x);
        projectile.setCenterY(y);

        projectile.setFinalCenterX(targetX);
        projectile.setFinalCenterY(targetY);

        fireAnimation.setCenterX(x);
        fireAnimation.setCenterY(y);

        fireAnimation.setFinalCenterX(x);
        fireAnimation.setFinalCenterY(y);

        fireAnimation.setInitialCenterX(x);
        fireAnimation.setInitialCenterY(y);

        endAnimation.setCenterX(targetX);
        endAnimation.setCenterY(targetY);

        endAnimation.setInitialCenterX(targetX);
        endAnimation.setInitialCenterY(targetY);

        endAnimation.setFinalCenterX(targetX);
        endAnimation.setFinalCenterY(targetY);

        // rotate projectile to match the moving direction
        rotateProjectile(x, y, targetX, targetY);
    }

    /**
     * Rotates the projectile animation to match the moving direction
     *
     * @param x starting x coordinate
     * @param y starting y coordinate
     * @param targetX target x coordinate
     * @param targetY target y coordinate
     */
    protected void rotateProjectile(float x, float y, float targetX, float targetY) {
        float xDiff = x - targetX;
        float yDiff = y - targetY;

        // if the yDiff is 0, the rotation is either 0 or 180 degrees
        if (yDiff == 0) {
            if (xDiff > 0) {
                projectile.setRotation(180);
            } else {
                projectile.setRotation(0);
            }

            return;
        }

        // calculate the tangent of the angle
        float tan = Math.abs(yDiff / xDiff);

        // get the angle from the tangent value
        float angle = (float) Math.atan(tan);

        // convert the angle from radians to degrees
        angle = (float) (angle * (180 / Math.PI));

        // rotate the projectile
        if (yDiff > 0) {
            if (xDiff < 0) {
                projectile.setRotation(-angle);
            } else {
                projectile.setRotation(-180 + angle);
            }
        } else {
            if (xDiff < 0) {
                projectile.setRotation(angle);
            } else {
                projectile.setRotation(180 - angle);
            }
        }
    }

    /**
     * Sets the projectile speed based on the flight time
     *
     * @param flightTime how many seconds will the projectile fly
     */
    public void setFlightTime(float flightTime) {
        projectile.setUpdateInterval(flightTime / projectile.getFrameCount());
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
            if (endAnimation != null && !endAnimationFinished) {
                endAnimation.update(delta);
            }
        } else {
            projectile.update(delta);
        }
    }

    /**
     * Resets the state of the animation
     */
    public void reset() {
        endAnimationFinished = false;
        targetReached = false;
        fireAnimationFinished = false;

        endAnimation.resetAnimation();
        fireAnimation.resetAnimation();
        projectile.resetAnimation();

        rotateProjectile(projectile.getInitialCenterX(), projectile.getInitialCenterY(),
                projectile.getFinalCenterX(), projectile.getFinalCenterY());
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
            if (endAnimation != null && !endAnimationFinished) {
                endAnimation.render(batch, resources);
            }
        } else {
            projectile.render(batch, resources);
        }
    }
}
