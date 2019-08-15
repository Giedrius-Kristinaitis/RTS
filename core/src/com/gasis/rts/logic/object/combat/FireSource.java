package com.gasis.rts.logic.object.combat;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.Updatable;
import com.gasis.rts.logic.animation.Animation;
import com.gasis.rts.logic.animation.AnimationFinishListener;
import com.gasis.rts.logic.animation.complexanimation.MissileAnimation;
import com.gasis.rts.logic.animation.complexanimation.ProjectileAnimation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.math.MathUtils;
import com.gasis.rts.math.Point;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.sound.SoundPlayerProvider;
import com.gasis.rts.utils.Constants;

import java.util.*;

import static com.gasis.rts.logic.object.unit.Unit.*;

/**
 * A point from which shots are fired
 */
public class FireSource implements Updatable, Renderable, AnimationFinishListener {

    // used to deviate the projectile
    private static final Random random = new Random();

    // projectile speed (game world distance units per second, for reference,
    // a heavy tank is roughly 1.3 units long)
    protected float projectileSpeed;

    // all types of fire/projectiles
    public static final byte FIRE_TYPE_MISSILE = 0;
    public static final byte FIRE_TYPE_BULLET = 1;
    public static final byte FIRE_TYPE_SHELL = 2;
    public static final byte FIRE_TYPE_FLAME = 3;

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
    protected Set<TargetReachListener> targetReachListeners = new HashSet<TargetReachListener>();

    // how many guns are firing (only has effect on things that fire shells)
    protected byte gunCount = 1;

    // from where the shots are fired for each facing direction
    // point indexes must match facing directions defined in Unit class
    protected List<Point> firePoints;

    // is the source present in these conditions
    protected boolean presentInSiegeMode;
    protected boolean presentOutOfSiegeMode;

    // how much can the projectile deviate from it's target
    protected float projectileDeviation;

    // provides firing thing's damage
    protected DamageValueProvider specProvider;

    // how much of the holder's damage does this fire source do (between 0 and 1)
    protected float damageCoefficient = 1f;

    // the sound effect that is played when the source fires
    protected String soundEffect;

    // provides the source's owner
    protected OwnerProvider ownerProvider;

    /**
     * Sets the fire source owner provider
     *
     * @param ownerProvider new owner provider
     */
    public void setOwnerProvider(OwnerProvider ownerProvider) {
        this.ownerProvider = ownerProvider;
    }

    /**
     * Gets the sound effect
     * @return
     */
    public String getSoundEffect() {
        return soundEffect;
    }

    /**
     * Sets the sound effect
     *
     * @param soundEffect new sound effect
     */
    public void setSoundEffect(String soundEffect) {
        this.soundEffect = soundEffect;
    }

    /**
     * Checks if all animations have finished
     * @return
     */
    public boolean allAnimationsFinished() {
        for (ProjectileAnimation animation: animations) {
            if (!animation.hasFinished()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Sets the damage coefficient for the fire source
     *
     * @param damageCoefficient new damage coefficient
     */
    public void setDamageCoefficient(float damageCoefficient) {
        this.damageCoefficient = damageCoefficient;
    }

    /**
     * Gets the fire source's damage coefficient
     * @return
     */
    public float getDamageCoefficient() {
        return damageCoefficient;
    }

    /**
     * Sets the damage provider
     *
     * @param provider provider to use
     */
    public void setDamageProvider(DamageValueProvider provider) {
        this.specProvider = provider;
    }

    /**
     * Sets the maximum possible deviation from the target
     *
     * @param projectileDeviation new deviation value
     */
    public void setProjectileDeviation(float projectileDeviation) {
        this.projectileDeviation = projectileDeviation;
    }

    /**
     * Gets the maximum possible deviation from the target
     * @return
     */
    public float getProjectileDeviation() {
        return projectileDeviation;
    }

    /**
     * Checks if the source is present when the holder in siege mode
     * @return
     */
    public boolean isPresentInSiegeMode() {
        return presentInSiegeMode;
    }

    /**
     * Sets the source's presence in is siege mode
     *
     * @param presentInSiegeMode new presence value
     */
    public void setPresentInSiegeMode(boolean presentInSiegeMode) {
        this.presentInSiegeMode = presentInSiegeMode;
    }

    /**
     * Checks if the source is present when the holder is not in siege mode
     * @return
     */
    public boolean isPresentOutOfSiegeMode() {
        return presentOutOfSiegeMode;
    }

    /**
     * Sets the source's presence when not in siege mode
     *
     * @param presentOutOfSiegeMode new presence value
     */
    public void setPresentOutOfSiegeMode(boolean presentOutOfSiegeMode) {
        this.presentOutOfSiegeMode = presentOutOfSiegeMode;
    }

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
     * Removes a target reach listener
     *
     * @param listener listener to remove
     */
    public void removeTargetReachListener(TargetReachListener listener) {
        targetReachListeners.remove(listener);
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
     * Called when a projectile animation finishes
     *
     * @param animation the animation that just finished
     */
    @Override
    public void finished(Animation animation) {
        for (TargetReachListener listener: targetReachListeners) {
            boolean projectileExplosive = false;

            if (fireType == FIRE_TYPE_SHELL || fireType == FIRE_TYPE_MISSILE) {
                projectileExplosive = true;
            }

            listener.targetReached(((FrameAnimation) animation).getCenterX(), ((FrameAnimation) animation).getCenterY(), specProvider.getDamage() * damageCoefficient, projectileExplosive, projectileScale, ownerProvider.getOwner());
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
        float deviatedTargetX = targetX + projectileDeviation * random.nextFloat() * (random.nextBoolean() ? -1 : 1);
        float deviatedTargetY = targetY + projectileDeviation * random.nextFloat() * (random.nextBoolean() ? -1 : 1);

        boolean explosive = fireType == FIRE_TYPE_MISSILE || fireType == FIRE_TYPE_SHELL;

        ProjectileAnimation animation = createProjectileAnimation(facingDirection, deviatedTargetX, deviatedTargetY, explosive);
        animation.addProjectileAnimationFinishListener(this);
        animation.setFlightTime(MathUtils.distance(x, deviatedTargetX, y, deviatedTargetY) / projectileSpeed);

        animations.add(animation);

        if (soundEffect != null) {
            SoundPlayerProvider.getSoundPlayer().playSoundEffect(Constants.FOLDER_SOUNDS + soundEffect,
                    x, y);
        }
    }

    /**
     * Creates a new projectile animation
     *
     * @param facingDirection the direction the firing thing is facing
     * @param targetX x coordinate of the target
     * @param targetY y coordinate of the target
     * @param explosiveEnd is the end animation an explosion
     * @return
     */
    protected ProjectileAnimation createProjectileAnimation(byte facingDirection, float targetX, float targetY, boolean explosiveEnd) {
        ProjectileAnimation animation = null;
        float scale = 0;

        switch (projectileScale) {
            case SMALL:
                scale = 0.75f;
                break;
            case MEDIUM:
                scale = 0.875f;
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
            case FIRE_TYPE_FLAME:
                animation = createFlameAnimation(targetX, targetY);
                break;
            default:
                throw new RuntimeException("Invalid fire/projectile type");
        }

        animation.setTrajectory(x, y, targetX, targetY, explosiveEnd);

        return animation;
    }

    /**
     * Creates a flame ball animation
     *
     * @param targetX target x
     * @param targetY target y
     * @return
     */
    protected ProjectileAnimation createFlameAnimation(float targetX, float targetY) {
        return new ProjectileAnimation(
                FrameAnimationFactory.getInstance().create("flame_ball"),
                null,
                null
        );
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
