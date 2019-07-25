package com.gasis.rts.logic.animation.complexanimation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.animation.Animation;
import com.gasis.rts.logic.animation.AnimationFinishListener;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.resources.Resources;

import java.util.ArrayList;
import java.util.List;

/**
 * An animation of a flying missile
 */
public class MissileAnimation extends ProjectileAnimation {

    // smoke trails left by the flying missile
    protected List<FrameAnimation> smokeTrails = new ArrayList<FrameAnimation>();

    // the scale of the missile smoke trails
    protected float trailScale;

    // how often missile trails are spawned in seconds
    protected final float trailSpawnInterval = 0.016f;

    // how much time in seconds has passed since the last spawn of a trail
    protected float timeSinceTrailSpawn;

    // pretty self-explanatory
    protected boolean spawnTrails = true;

    /**
     * Class constructor
     *
     * @param explosionScale scale of the explosion animation
     * @param missileScale scale of the fire animation and missile
     */
    public MissileAnimation(float missileScale, float explosionScale) {
        super(
                FrameAnimationFactory.getInstance().create("missile"),
                FrameAnimationFactory.getInstance().create("missile_launch"),
                FrameAnimationFactory.getInstance().create("projectile_explosion")
             );

        trailScale = missileScale;

        initialize(missileScale, explosionScale);
    }

    /**
     * Initializes the animation
     *
     * @param missileScale scale of the missile
     * @param explosionScale scale of the explosion
     */
    private void initialize(float missileScale, float explosionScale) {
        fireAnimation.setScale(missileScale);
        fireAnimation.setInitialScale(missileScale);
        fireAnimation.setFinalScale(missileScale);

        projectile.setFinalScale(missileScale);
        projectile.setInitialScale(missileScale);
        projectile.setScale(missileScale);

        projectile.addFinishListener(new AnimationFinishListener() {
            @Override
            public void finished(Animation animation) {
                spawnTrails = false;
            }
        });
    }

    /**
     * Resets the state of the animation
     */
    @Override
    public void reset() {
        super.reset();
        timeSinceTrailSpawn = 0;
        spawnTrails = true;
        smokeTrails.clear();
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
            // spawn a new smoke trail
            FrameAnimation trail = FrameAnimationFactory.getInstance().create("missile_trail");

            trail.setInitialScale(trailScale);
            trail.setFinalScale(trailScale);
            trail.setScale(trailScale);
            trail.setUpdateInterval(trailSpawnInterval / trail.getFrameCount());
            trail.setCenterX(projectile.getCenterX());
            trail.setCenterY(projectile.getCenterY());
            trail.setFinalCenterX(projectile.getCenterX());
            trail.setFinalCenterY(projectile.getCenterY());
            trail.setInitialCenterX(projectile.getCenterX());
            trail.setInitialCenterY(projectile.getCenterY());

            smokeTrails.add(trail);

            timeSinceTrailSpawn = 0;
        }

        if (spawnTrails || timeSinceTrailSpawn < trailSpawnInterval) {
            for (int i = 0; i < smokeTrails.size(); i++) {
                smokeTrails.get(i).update(delta);

                if (smokeTrails.get(i).hasFinished()) {
                    smokeTrails.remove(i--);
                }
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
