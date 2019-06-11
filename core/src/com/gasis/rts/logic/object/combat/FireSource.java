package com.gasis.rts.logic.object.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.Updatable;
import com.gasis.rts.logic.animation.complexanimation.MissileAnimation;
import com.gasis.rts.logic.animation.complexanimation.ProjectileAnimation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.math.MathUtils;
import com.gasis.rts.math.Point;
import com.gasis.rts.resources.Resources;

import java.util.ArrayList;
import java.util.List;

import static com.gasis.rts.logic.object.unit.Unit.*;

/**
 * A point from which shots are fired
 */
public class FireSource implements Updatable, Renderable, TargetReachListener {

    // projectile speed (game world distance units per second, for reference,
    // a heavy tank is roughly 1.3 units long)
    protected float projectileSpeed;

    // all types of fire/projectiles
    public static final byte FIRE_TYPE_MISSILE = 0;
    public static final byte FIRE_TYPE_BULLET = 1;
    public static final byte FIRE_TYPE_SHELL = 2;

    // all scales of the projectile
    public static final byte SMALL = 0;
    public static final byte MEDIUM = 1;
    public static final byte HEAVY = 2;

    // the fire/projectile type of this fire source
    protected byte fireType;

    // the scale of this source's projectile
    protected byte projectileScale;

    // coordinates of the source
    protected float x;
    protected float y;

    // animations used by the fire source
    protected List<ProjectileAnimation> animations = new ArrayList<ProjectileAnimation>();

    // listeners for the reach of the target
    protected List<TargetReachListener> targetReachListeners = new ArrayList<TargetReachListener>();

    // how many guns are firing (only has effect on things that fire shells)
    protected byte gunCount = 1;

    // from where the shots are fired for each facing direction
    // point indexes must match facing directions defined in Unit class
    protected List<Point> firePoints;

    /**
     * Gets the fire points of the fire source
     * @return
     */
    public List<Point> getFirePoints() {
        return firePoints;
    }

    /**
     * Sets the fire points
     *
     * @param firePoints new fire points
     */
    public void setFirePoints(List<Point> firePoints) {
        this.firePoints = firePoints;
    }

    /**
     * Sets the gun count of the fire source
     *
     * @param gunCount can be 1 or 2
     */
    public void setGunCount(byte gunCount) {
        if (gunCount != 1 && gunCount != 2) {
            this.gunCount = 1;
        } else {
            this.gunCount = gunCount;
        }
    }

    /**
     * Gets the gun count of the fire source
     * @return
     */
    public byte getGunCount() {
        return gunCount;
    }

    /**
     * Gets the speed of the projectile
     * @return
     */
    public float getProjectileSpeed() {
        return projectileSpeed;
    }

    /**
     * Sets the speed of teh projectile (units per second)
     *
     * @param projectileSpeed new speed
     */
    public void setProjectileSpeed(float projectileSpeed) {
        this.projectileSpeed = projectileSpeed;
    }

    /**
     * Adds a new listener that listens for the reach of the target
     *
     * @param listener listener to add
     */
    public void addTargetReachListener(TargetReachListener listener) {
        targetReachListeners.add(listener);
    }

    /**
     * Sets the x coordinate of the fire source
     *
     * @param x new x coordinate
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Sets the y coordinate of the fire source
     *
     * @param y new y coordinate
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Gets the x coordinate of the fire source
     * @return
     */
    public float getX() {
        return x;
    }

    /**
     * Gets the y coordinate of the fire source
     * @return
     */
    public float getY() {
        return y;
    }

    /**
     * Gets the fire/projectile type of the fire source
     * @return
     */
    public byte getFireType() {
        return fireType;
    }

    /**
     * Sets the fire/projectile type of the fire source
     *
     * @param fireType new fire type
     */
    public void setFireType(byte fireType) {
        this.fireType = fireType;
    }

    /**
     * Gets the scale of the projectile
     * @return
     */
    public byte getProjectileScale() {
        return projectileScale;
    }

    /**
     * Sets the projectile scale
     *
     * @param projectileScale new projectile scale
     */
    public void setProjectileScale(byte projectileScale) {
        this.projectileScale = projectileScale;
    }

    /**
     * Gets called when a projectile reaches it's target
     *
     * @param targetX x coordinate of the target
     * @param targetY y coordinate of the target
     */
    @Override
    public void targetReached(float targetX, float targetY) {
        for (TargetReachListener listener: targetReachListeners) {
            listener.targetReached(targetX, targetY);
        }
    }

    /**
     * Fires a shot at the specified target coordinates
     *
     * @param facingDirection the direction the firing thing is facing
     * @param targetX x coordinate of the target
     * @param targetY y coordinate of the target
     */
    public void fire(byte facingDirection, float targetX, float targetY) {
        ProjectileAnimation animation = createProjectileAnimation(facingDirection, targetX, targetY);
        animation.addTargetReachedListener(this);
        animation.setFlightTime(MathUtils.distance(x, targetX, y, targetY) / projectileSpeed);

        animations.add(animation);
    }

    /**
     * Creates a new projectile animation
     *
     * @param facingDirection the direction the firing thing is facing
     * @param targetX x coordinate of the target
     * @param targetY y coordinate of the target
     * @return
     */
    protected ProjectileAnimation createProjectileAnimation(byte facingDirection, float targetX, float targetY) {
        ProjectileAnimation animation = null;
        float scale = 0;

        switch (projectileScale) {
            case SMALL:
                scale = 0.5f;
                break;
            case MEDIUM:
                scale = 0.75f;
                break;
            case HEAVY:
                scale = 1f;
                break;
            default:
                scale = 1f;
                break;
        }

        switch (fireType) {
            case FIRE_TYPE_MISSILE:
                animation = new MissileAnimation(scale, scale);
                break;
            case FIRE_TYPE_BULLET:
                animation = createBulletAnimation(facingDirection, targetX, targetY);
                break;
            case FIRE_TYPE_SHELL:
                animation = createShellAnimation(facingDirection, targetX, targetY);
                break;
            default:
                throw new RuntimeException("Invalid fire/projectile type");
        }

        animation.setTrajectory(x, y, targetX, targetY);

        return animation;
    }

    /**
     * Creates a bullet animation
     *
     * @param facingDirection the direction the firing thing is facing
     * @param targetX x coordinate of the target
     * @param targetY y coordinate of the target
     * @return
     */
    protected ProjectileAnimation createBulletAnimation(byte facingDirection, float targetX, float targetY) {
        String fireAnimationName = "";

        switch (facingDirection) {
            case NORTH:
                fireAnimationName = "bullet_fire_north";
                break;
            case NORTH_EAST:
                fireAnimationName = "bullet_fire_north_east";
                break;
            case EAST:
                fireAnimationName = "bullet_fire_east";
                break;
            case SOUTH_EAST:
                fireAnimationName = "bullet_fire_south_east";
                break;
            case SOUTH:
                fireAnimationName = "bullet_fire_south";
                break;
            case SOUTH_WEST:
                fireAnimationName = "bullet_fire_south_west";
                break;
            case WEST:
                fireAnimationName = "bullet_fire_west";
                break;
            case NORTH_WEST:
                fireAnimationName = "bullet_fire_north_west";
                break;
            default:
                throw new RuntimeException("Bad facing direction");
        }

        return new ProjectileAnimation(
                FrameAnimationFactory.getInstance().create("bullet"),
                FrameAnimationFactory.getInstance().create(fireAnimationName),
                FrameAnimationFactory.getInstance().create("bullet_ricochet")
        );
    }

    /**
     * Creates a shell animation
     *
     * @param facingDirection the direction the firing thing is facing
     * @param targetX x coordinate of the target
     * @param targetY y coordinate of the target
     * @return
     */
    protected ProjectileAnimation createShellAnimation(byte facingDirection, float targetX, float targetY) {
        String fireAnimationName = "";

        switch (facingDirection) {
            case NORTH:
                if (gunCount == 1) {
                    fireAnimationName = "one_tank_gun_shell_fire_north";
                } else {
                    fireAnimationName = "two_tank_gun_shell_fire_north";
                }

                break;
            case NORTH_EAST:
                if (gunCount == 1) {
                    fireAnimationName = "one_tank_gun_shell_fire_north_east";
                } else {
                    fireAnimationName = "two_tank_gun_shell_fire_north_east";
                }

                break;
            case EAST:
                if (gunCount == 1) {
                    fireAnimationName = "one_tank_gun_shell_fire_east";
                } else {
                    fireAnimationName = "two_tank_gun_shell_fire_east";
                }

                break;
            case SOUTH_EAST:
                if (gunCount == 1) {
                    fireAnimationName = "one_tank_gun_shell_fire_south_east";
                } else {
                    fireAnimationName = "two_tank_gun_shell_fire_south_east";
                }

                break;
            case SOUTH:
                if (gunCount == 1) {
                    fireAnimationName = "one_tank_gun_shell_fire_south";
                } else {
                    fireAnimationName = "two_tank_gun_shell_fire_south";
                }

                break;
            case SOUTH_WEST:
                if (gunCount == 1) {
                    fireAnimationName = "one_tank_gun_shell_fire_south_west";
                } else {
                    fireAnimationName = "two_tank_gun_shell_fire_south_west";
                }

                break;
            case WEST:
                if (gunCount == 1) {
                    fireAnimationName = "one_tank_gun_shell_fire_west";
                } else {
                    fireAnimationName = "two_tank_gun_shell_fire_west";
                }

                break;
            case NORTH_WEST:
                if (gunCount == 1) {
                    fireAnimationName = "one_tank_gun_shell_fire_north_west";
                } else {
                    fireAnimationName = "two_tank_gun_shell_fire_north_west";
                }

                break;
            default:
                throw new RuntimeException("Bad facing direction");
        }

        return new ProjectileAnimation(
                FrameAnimationFactory.getInstance().create("shell"),
                FrameAnimationFactory.getInstance().create(fireAnimationName),
                FrameAnimationFactory.getInstance().create("projectile_explosion")
        );
    }

    /**
     * Updates the state of the object
     *
     * @param delta time elapsed since the last update
     */
    @Override
    public void update(float delta) {
        for (int i = 0; i < animations.size(); i++) {
            animations.get(i).update(delta);

            if (animations.get(i).hasEndAnimationFinished()) {
                animations.remove(i--);
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
        for (ProjectileAnimation animation: animations) {
            animation.render(batch, resources);
        }
    }
}
