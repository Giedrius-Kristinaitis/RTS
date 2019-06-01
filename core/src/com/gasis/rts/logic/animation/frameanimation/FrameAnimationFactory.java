package com.gasis.rts.logic.animation.frameanimation;

import com.gasis.rts.logic.animation.AnimationFactory;

/**
 * Creates frame animations
 */
public class FrameAnimationFactory implements AnimationFactory {

    // ***** ANIMATION ID VALUES ***** //
    public static final short ID_WHITE_SMOKE_BALL = 0;
    public static final short ID_ONE_TANK_GUN_SHELL_FIRE_EAST = 1;
    public static final short ID_ONE_TANK_GUN_SHELL_FIRE_NORTH_EAST = 2;
    public static final short ID_ONE_TANK_GUN_SHELL_FIRE_NORTH = 3;
    public static final short ID_ONE_TANK_GUN_SHELL_FIRE_NORTH_WEST = 4;
    public static final short ID_ONE_TANK_GUN_SHELL_FIRE_WEST = 5;
    public static final short ID_ONE_TANK_GUN_SHELL_FIRE_SOUTH_WEST = 6;
    public static final short ID_ONE_TANK_GUN_SHELL_FIRE_SOUTH = 7;
    public static final short ID_ONE_TANK_GUN_SHELL_FIRE_SOUTH_EAST = 8;
    public static final short ID_PROJECTILE_EXPLOSION = 9;
    public static final short ID_SHELL = 10;
    public static final short ID_MISSILE = 11;
    public static final short ID_MISSILE_TRAIL = 12;
    public static final short ID_MISSILE_LAUNCH = 13;
    public static final short ID_BULLET = 14;
    public static final short ID_BULLET_FIRE_NORTH = 15;
    public static final short ID_BULLET_FIRE_NORTH_EAST = 16;
    public static final short ID_BULLET_FIRE_EAST = 17;
    public static final short ID_BULLET_FIRE_SOUTH_EAST = 18;
    public static final short ID_BULLET_FIRE_SOUTH = 19;
    public static final short ID_BULLET_FIRE_SOUTH_WEST = 20;
    public static final short ID_BULLET_FIRE_WEST = 21;
    public static final short ID_BULLET_FIRE_NORTH_WEST = 22;
    public static final short ID_BULLET_RICOCHET = 23;
    public static final short ID_TWO_TANK_GUN_SHELL_FIRE_EAST = 24;
    public static final short ID_TWO_TANK_GUN_SHELL_FIRE_NORTH_EAST = 25;
    public static final short ID_TWO_TANK_GUN_SHELL_FIRE_NORTH = 26;
    public static final short ID_TWO_TANK_GUN_SHELL_FIRE_NORTH_WEST = 27;
    public static final short ID_TWO_TANK_GUN_SHELL_FIRE_WEST = 28;
    public static final short ID_TWO_TANK_GUN_SHELL_FIRE_SOUTH_WEST = 29;
    public static final short ID_TWO_TANK_GUN_SHELL_FIRE_SOUTH = 30;
    public static final short ID_TWO_TANK_GUN_SHELL_FIRE_SOUTH_EAST = 31;
    // ***** END OF ANIMATION ID ***** //

    // ***** ANIMATION LOADERS ***** //
    private static final FrameAnimationLoader whiteSmokeBallLoader = new FrameAnimationLoader();
    private static final FrameAnimationLoader oneTankGunShellFireEast_loader = new FrameAnimationLoader();
    private static final FrameAnimationLoader oneTankGunShellFireNorthEast_loader = new FrameAnimationLoader();
    private static final FrameAnimationLoader oneTankGunShellFireNorth_loader = new FrameAnimationLoader();
    private static final FrameAnimationLoader oneTankGunShellFireNorthWest_loader = new FrameAnimationLoader();
    private static final FrameAnimationLoader oneTankGunShellFireWest_loader = new FrameAnimationLoader();
    private static final FrameAnimationLoader oneTankGunShellFireSouthWest_loader = new FrameAnimationLoader();
    private static final FrameAnimationLoader oneTankGunShellFireSouth_loader = new FrameAnimationLoader();
    private static final FrameAnimationLoader oneTankGunShellFireSouthEast_loader = new FrameAnimationLoader();
    private static final FrameAnimationLoader projectileExplosionLoader = new FrameAnimationLoader();
    private static final FrameAnimationLoader shellLoader = new FrameAnimationLoader();
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
        //oneTankGunShellFireEast_loader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "one_tank_gun_shell_fire_east"));
        //oneTankGunShellFireNorthEast_loader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "one_tank_gun_shell_fire_north_east"));
        //oneTankGunShellFireNorth_loader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "one_tank_gun_shell_fire_north"));
        //oneTankGunShellFireNorthWest_loader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "one_tank_gun_shell_fire_north_west"));
        //oneTankGunShellFireWest_loader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "one_tank_gun_shell_fire_west"));
        //oneTankGunShellFireSouthWest_loader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "one_tank_gun_shell_fire_south_west"));
        //oneTankGunShellFireSouth_loader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "one_tank_gun_shell_fire_south"));
        //oneTankGunShellFireSouthEast_loader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "one_tank_gun_shell_fire_south_east"));
        //projectileExplosionLoader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "projectile_explosion"));
        //shellLoader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "shell"));
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
            case ID_ONE_TANK_GUN_SHELL_FIRE_EAST:
                animation = oneTankGunShellFireEast_loader.newInstance();
                break;
            case ID_ONE_TANK_GUN_SHELL_FIRE_NORTH_EAST:
                animation = oneTankGunShellFireNorthEast_loader.newInstance();
                break;
            case ID_ONE_TANK_GUN_SHELL_FIRE_NORTH:
                animation = oneTankGunShellFireNorth_loader.newInstance();
                break;
            case ID_ONE_TANK_GUN_SHELL_FIRE_NORTH_WEST:
                animation = oneTankGunShellFireNorthWest_loader.newInstance();
                break;
            case ID_ONE_TANK_GUN_SHELL_FIRE_WEST:
                animation = oneTankGunShellFireWest_loader.newInstance();
                break;
            case ID_ONE_TANK_GUN_SHELL_FIRE_SOUTH_WEST:
                animation = oneTankGunShellFireSouthWest_loader.newInstance();
                break;
            case ID_ONE_TANK_GUN_SHELL_FIRE_SOUTH:
                animation = oneTankGunShellFireSouth_loader.newInstance();
                break;
            case ID_ONE_TANK_GUN_SHELL_FIRE_SOUTH_EAST:
                animation = oneTankGunShellFireSouthEast_loader.newInstance();
                break;
            case ID_PROJECTILE_EXPLOSION:
                animation = projectileExplosionLoader.newInstance();
                break;
            case ID_SHELL:
                animation = shellLoader.newInstance();
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

            animation.setFinalCenterX(finalX);
            animation.setFinalCenterY(finalY);

            animation.setInitialCenterY(y);
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
