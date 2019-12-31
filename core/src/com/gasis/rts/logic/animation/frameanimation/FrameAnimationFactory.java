package com.gasis.rts.logic.animation.frameanimation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.gasis.rts.logic.animation.AnimationFactory;
import com.gasis.rts.resources.NotLoadedException;
import com.gasis.rts.utils.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates frame animations
 */
public class FrameAnimationFactory implements AnimationFactory {

    // instance of the factory
    private static FrameAnimationFactory instance;

    // are the animations in memory or not
    private static boolean animationsLoaded = false;

    // loaders used to create new animation instances
    private static Map<String, FrameAnimationLoader> animationLoaders = new HashMap<String, FrameAnimationLoader>();

    /**
     * Class constructor
     */
    private FrameAnimationFactory() {
    }

    /**
     * Loads all the animations from animation files
     */
    public static void loadAnimations() {
        if (animationsLoaded) {
            return;
        }

        FileHandle directory = Gdx.files.internal(Constants.FOLDER_ANIMATIONS);

        // get all files in the animations directory
        for (FileHandle file : directory.list()) {
            if (!file.isDirectory()) {
                FrameAnimationLoader loader = new FrameAnimationLoader();

                loader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + file.name()));

                animationLoaders.put(file.name(), loader);
            }
        }

        animationsLoaded = true;
    }

    /**
     * Gets the instance of the frame animation factory
     *
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
     * @param name name of the animation
     * @return
     */
    @Override
    public FrameAnimation create(String name) {
        if (!animationsLoaded) {
            throw new IllegalStateException("Animations not loaded");
        }

        if (!animationLoaders.containsKey(name)) {
            throw new NotLoadedException("Animation '" + name + "' is not loaded");
        }

        return animationLoaders.get(name).newInstance();
    }

    /**
     * Creates a new instance of the specified frame animation
     *
     * @param name   name of the animation
     * @param x      starting x position
     * @param y      starting y position
     * @param finalX ending x position
     * @param finalY ending y position
     * @param center are the given coordinates of the center point
     * @return new animation instance
     */
    public FrameAnimation create(String name, float x, float y, float finalX, float finalY, boolean center) {
        FrameAnimation animation = create(name);

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
