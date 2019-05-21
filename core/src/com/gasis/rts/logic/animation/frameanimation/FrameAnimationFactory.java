package com.gasis.rts.logic.animation.frameanimation;

import com.badlogic.gdx.Gdx;
import com.gasis.rts.logic.animation.AnimationFactory;
import com.gasis.rts.utils.Constants;

/**
 * Creates frame animations
 */
public class FrameAnimationFactory implements AnimationFactory {

    // ***** ANIMATION ID VALUES ***** //
    public static final short ID_WHITE_SMOKE_BALL = 0;
    public static final short ID_ONE_TANK_GUN_SHELL_FIRE_R = 1;
    public static final short ID_ONE_TANK_GUN_SHELL_FIRE_TR = 2;
    public static final short ID_ONE_TANK_GUN_SHELL_FIRE_T = 3;
    public static final short ID_ONE_TANK_GUN_SHELL_FIRE_TL = 4;
    public static final short ID_ONE_TANK_GUN_SHELL_FIRE_L = 5;
    public static final short ID_ONE_TANK_GUN_SHELL_FIRE_BL = 6;
    public static final short ID_ONE_TANK_GUN_SHELL_FIRE_B = 7;
    public static final short ID_ONE_TANK_GUN_SHELL_FIRE_BR = 8;
    public static final short ID_PROJECTILE_EXPLOSION = 9;
    public static final short ID_TANK_PROJECTILE = 10;
    public static final short ID_MISSILE = 11;
    public static final short ID_MISSILE_TRAIL = 12;
    public static final short ID_MISSILE_LAUNCH = 13;
    // ***** END OF ANIMATION ID ***** //

    // ***** ANIMATION LOADERS ***** //
    private static final FrameAnimationLoader whiteSmokeBallLoader = new FrameAnimationLoader();
    private static final FrameAnimationLoader oneTankGunShellFireR_loader = new FrameAnimationLoader();
    private static final FrameAnimationLoader oneTankGunShellFireTR_loader = new FrameAnimationLoader();
    private static final FrameAnimationLoader oneTankGunShellFireT_loader = new FrameAnimationLoader();
    private static final FrameAnimationLoader oneTankGunShellFireTL_loader = new FrameAnimationLoader();
    private static final FrameAnimationLoader oneTankGunShellFireL_loader = new FrameAnimationLoader();
    private static final FrameAnimationLoader oneTankGunShellFireBL_loader = new FrameAnimationLoader();
    private static final FrameAnimationLoader oneTankGunShellFireB_loader = new FrameAnimationLoader();
    private static final FrameAnimationLoader oneTankGunShellFireBR_loader = new FrameAnimationLoader();
    private static final FrameAnimationLoader projectileExplosionLoader = new FrameAnimationLoader();
    private static final FrameAnimationLoader tankProjectileLoader = new FrameAnimationLoader();
    private static final FrameAnimationLoader missileLoader = new FrameAnimationLoader();
    private static final FrameAnimationLoader missileTrailLoader = new FrameAnimationLoader();
    private static final FrameAnimationLoader missileLaunchLoader = new FrameAnimationLoader();
    // ***** END OF ANIMATION LOADERS ***** //

    // instance of the factory
    private static FrameAnimationFactory instance;

    // are the animations in memory or not
    private static boolean animationsLoaded = false;

    /**
     * Class constructor
     */
    private FrameAnimationFactory() { }

    /**
     * Loads all the animations from animation files
     */
    public static void loadAnimations() {
        if (animationsLoaded) {
            return;
        }

        //whiteSmokeBallLoader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "white_smoke_ball"));
        //oneTankGunShellFireR_loader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "one_tank_gun_shell_fire_r"));
        //oneTankGunShellFireTR_loader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "one_tank_gun_shell_fire_tr"));
        //oneTankGunShellFireT_loader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "one_tank_gun_shell_fire_t"));
        //oneTankGunShellFireTL_loader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "one_tank_gun_shell_fire_tl"));
        //oneTankGunShellFireL_loader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "one_tank_gun_shell_fire_l"));
        //oneTankGunShellFireBL_loader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "one_tank_gun_shell_fire_bl"));
        //oneTankGunShellFireB_loader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "one_tank_gun_shell_fire_b"));
        //oneTankGunShellFireBR_loader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "one_tank_gun_shell_fire_br"));
        //projectileExplosionLoader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "projectile_explosion"));
        //tankProjectileLoader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "tank_projectile"));
        //missileLoader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "missile"));
        //missileTrailLoader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "missile_trail"));
        //missileLaunchLoader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "missile_launch"));

        animationsLoaded = true;
    }

    /**
     * Gets the instance of the frame animation factory
     * @return
     */
    public static FrameAnimationFactory getInstance() {
        if (instance == null) {
            instance = new FrameAnimationFactory();
        }

        return instance;
    }

    /**
     * Creates a new instance of the specified frame animation
     *
     * @param animationId id of the animation
     * @return
     */
    @Override
    public FrameAnimation create(short animationId) {
        if (!animationsLoaded) {
            throw new IllegalStateException("Animations not loaded");
        }

        FrameAnimation animation = null;

        switch (animationId) {
            case ID_WHITE_SMOKE_BALL:
                animation = whiteSmokeBallLoader.newInstance();
                break;
            case ID_ONE_TANK_GUN_SHELL_FIRE_R:
                animation = oneTankGunShellFireR_loader.newInstance();
                break;
            case ID_ONE_TANK_GUN_SHELL_FIRE_TR:
                animation = oneTankGunShellFireTR_loader.newInstance();
                break;
            case ID_ONE_TANK_GUN_SHELL_FIRE_T:
                animation = oneTankGunShellFireT_loader.newInstance();
                break;
            case ID_ONE_TANK_GUN_SHELL_FIRE_TL:
                animation = oneTankGunShellFireTL_loader.newInstance();
                break;
            case ID_ONE_TANK_GUN_SHELL_FIRE_L:
                animation = oneTankGunShellFireL_loader.newInstance();
                break;
            case ID_ONE_TANK_GUN_SHELL_FIRE_BL:
                animation = oneTankGunShellFireBL_loader.newInstance();
                break;
            case ID_ONE_TANK_GUN_SHELL_FIRE_B:
                animation = oneTankGunShellFireB_loader.newInstance();
                break;
            case ID_ONE_TANK_GUN_SHELL_FIRE_BR:
                animation = oneTankGunShellFireBR_loader.newInstance();
                break;
            case ID_PROJECTILE_EXPLOSION:
                animation = projectileExplosionLoader.newInstance();
                break;
            case ID_TANK_PROJECTILE:
                animation = tankProjectileLoader.newInstance();
                break;
            case ID_MISSILE:
                animation = missileLoader.newInstance();
                break;
            case ID_MISSILE_TRAIL:
                animation = missileTrailLoader.newInstance();
                break;
            case ID_MISSILE_LAUNCH:
                animation = missileLaunchLoader.newInstance();
                break;
            default:
                throw new IllegalArgumentException("Bad animation id");
        }

        return animation;
    }

    /**
     * Creates a new instance of the specified frame animation
     *
     * @param animationId id of the animation
     * @param x starting x position
     * @param y starting y position
     * @param finalX ending x position
     * @param finalY ending y position
     * @param center are the given coordinates of the center point
     *
     * @return new animation instance
     */
    public FrameAnimation create(short animationId, float x, float y, float finalX, float finalY, boolean center) {
        FrameAnimation animation = create(animationId);

        if (center) {
            animation.setCenterX(x);
            animation.setCenterY(y);

            animation.setInitialCenterX(x);
            animation.setInitialCenterY(y);

            animation.setFinalCenterX(finalX);
            animation.setFinalCenterY(finalY);
        } else {
            animation.setX(x);
            animation.setY(y);

            animation.setInitialX(x);
            animation.setInitialY(y);

            animation.setFinalX(finalX);
            animation.setFinalY(finalY);
        }

        return animation;
    }
}
