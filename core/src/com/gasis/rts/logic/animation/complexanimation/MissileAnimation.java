package com.gasis.rts.logic.animation.complexanimation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.animation.Animation;
import com.gasis.rts.logic.animation.AnimationFinishListener;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.resources.Resources;

/**
 * An animation of a flying missile
 */
public class MissileAnimation extends ProjectileAnimation {

    // smoke trails left by the flying missile
    protected FrameAnimation[] smokeTrails = new FrameAnimation[5];

    // how often missile trails are spawned in seconds
    protected float trailSpawnInterval;

    // how much time in seconds has passed since the last spawn of a trail
    protected float timeSinceTrailSpawn;

    // which smoke trail needs to be put in front of other trails behind the missile
    protected byte trailToPutInFront = -1;

    // pretty self-explanatory
    protected boolean spawnTrails = true;

    /**
     * Class constructor
     *
     * @param fireAnimation
     * @param explosionScale
     */
    public MissileAnimation(FrameAnimation fireAnimation, float missileScale, float explosionScale) {
        super(
                FrameAnimationFactory.getInstance().create(FrameAnimationFactory.ID_MISSILE),
                fireAnimation,
                FrameAnimationFactory.getInstance().create(FrameAnimationFactory.ID_PROJECTILE_EXPLOSION)
             );

        initialize(missileScale, explosionScale);
    }

    /**
     * Initializes the animation
     *
     * @param missileScale scale of the missile
     * @param explosionScale scale of the explosion
     */
    private void initialize(float missileScale, float explosionScale) {
        endAnimation.setInitialScale(explosionScale);
        endAnimation.setFinalScale(explosionScale);
        endAnimation.setScale(explosionScale);

        fireAnimation.setScale(missileScale);
        fireAnimation.setInitialScale(missileScale);
        fireAnimation.setFinalScale(missileScale);

        projectile.setFinalScale(missileScale);
        projectile.setInitialScale(missileScale);
        projectile.setScale(missileScale);

        for (int i = 0; i < smokeTrails.length; i++) {
            smokeTrails[i] = FrameAnimationFactory.getInstance().create(FrameAnimationFactory.ID_MISSILE_TRAIL);
            smokeTrails[i].setScale(missileScale);
            smokeTrails[i].setInitialScale(missileScale);
            smokeTrails[i].setFinalScale(missileScale);
        }

        projectile.addFinishListener(new AnimationFinishListener() {
            @Override
            public void finished(Animation animation) {
                spawnTrails = false;
            }
        });
    }

    /**
     * Sets the missile speed based on the flight time
     *
     * @param flightTime how many seconds will the projectile fly
     */
    @Override
    public void setFlightTime(float flightTime) {
        super.setFlightTime(flightTime);

        trailSpawnInterval = flightTime / 30f;

        for (FrameAnimation trail: smokeTrails) {
            trail.setUpdateInterval(trailSpawnInterval / trail.getFrameCount());
        }
    }

    /**
     * Resets the state of the animation
     */
    @Override
    public void reset() {
        super.reset();

        trailToPutInFront = -1;
        timeSinceTrailSpawn = 0;
        spawnTrails = true;

        setInitialTrailPosition();
    }

    /**
     * Sets the trajectory of the projectile
     *
     * @param x       starting x coordinate
     * @param y       starting y coordinate
     * @param targetX target x coordinate
     * @param targetY target y coordinate
     */
    @Override
    public void setTrajectory(float x, float y, float targetX, float targetY) {
        super.setTrajectory(x, y, targetX, targetY);

        setInitialTrailPosition();
    }

    /**
     * Sets the initial position of the smoke trails
     */
    protected void setInitialTrailPosition() {
        for (FrameAnimation animation: smokeTrails) {
            animation.setCenterX(projectile.getCenterX());
            animation.setCenterY(projectile.getCenterY());

            animation.setInitialCenterX(projectile.getCenterX());
            animation.setInitialCenterY(projectile.getCenterY());

            animation.setFinalCenterX(projectile.getCenterX());
            animation.setFinalCenterY(projectile.getCenterY());
        }
    }

    /**
     * Updates the animation
     *
     * @param delta time elapsed since the last render
     */
    @Override
    public void update(float delta) {
        super.update(delta);

        if (timeSinceTrailSpawn >= trailSpawnInterval && spawnTrails) {
            // spawn a new trail (or, more precisely, move one trail to the front)
            trailToPutInFront = (byte) (trailToPutInFront == 4 ? 0 : trailToPutInFront + 1);

            FrameAnimation trail = smokeTrails[trailToPutInFront];

            trail.resetAnimation();

            trail.setCenterX(projectile.getCenterX());
            trail.setCenterY(projectile.getCenterY());
            trail.setFinalCenterX(projectile.getCenterX());
            trail.setFinalCenterY(projectile.getCenterY());
            trail.setInitialCenterX(projectile.getCenterX());
            trail.setInitialCenterY(projectile.getCenterY());

            timeSinceTrailSpawn = 0;
        }

        if (spawnTrails || timeSinceTrailSpawn < trailSpawnInterval) {
            for (FrameAnimation animation: smokeTrails) {
                animation.update(delta);
            }
        }

        timeSinceTrailSpawn += delta;
    }

    /**
     * Renders the animation
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {
        super.render(batch, resources);

        if (spawnTrails || timeSinceTrailSpawn < trailSpawnInterval) {
            for (FrameAnimation animation: smokeTrails) {
                animation.render(batch, resources);
            }
        }
    }
}
