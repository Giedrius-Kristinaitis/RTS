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

    // ***** END OF ANIMATION ID ***** //

    // ***** ANIMATION LOADERS ***** //
    private static final FrameAnimationLoader whiteSmokeBallLoader = new FrameAnimationLoader();
    // ***** END OF ANIMATION LOADERS ***** //

    // instance of the factory
    private static FrameAnimationFactory instance;

    /**
     * Class constructor. Loads animations from animation files
     */
    private FrameAnimationFactory() {
        whiteSmokeBallLoader.load(Gdx.files.internal(Constants.FOLDER_ANIMATIONS + "white_smoke_ball"));
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
        FrameAnimation animation = null;

        switch (animationId) {
            case ID_WHITE_SMOKE_BALL:
                animation = whiteSmokeBallLoader.newInstance();
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
