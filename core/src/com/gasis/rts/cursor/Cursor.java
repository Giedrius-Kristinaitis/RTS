package com.gasis.rts.cursor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gasis.rts.logic.animation.Animation;
import com.gasis.rts.logic.animation.AnimationFinishListener;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

/**
 * Manages cursors
 */
public class Cursor {

    // cursor types
    public static final byte CURSOR_NONE = -1;
    public static final byte CURSOR_NORMAL = 0;
    public static final byte CURSOR_ATTACK = 1;

    // animation types
    public static final byte ANIMATION_MOVE = 0;
    public static final byte ANIMATION_ATTACK = 1;

    // cursors
    private static com.badlogic.gdx.graphics.Cursor cursorNormal;
    private static com.badlogic.gdx.graphics.Cursor cursorAttack;
    private static com.badlogic.gdx.graphics.Cursor cursorNone;

    // order animations
    private static FrameAnimation attackOrderAnimation;
    private static FrameAnimation moveOrderAnimation;
    private static FrameAnimation currentAnimation;

    // was the cursor data initialized
    private static boolean initialized = false;

    // the current cursor
    private static byte cursor = CURSOR_NONE;

    /**
     * Initializes cursor textures and animations
     *
     * @param resources game's assets
     */
    public static void initialize(Resources resources) {
        // create an empty pixmap
        Pixmap emptyPixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
        emptyPixmap.setColor(0, 0, 0, 0);
        emptyPixmap.drawPixel(0, 0);

        // initialize normal cursor pixmap
        TextureRegion region = resources.atlas(Constants.CURSOR_ATLAS).findRegion(Constants.CURSOR_TEXTURE_NORMAL);

        Pixmap normalCursorPixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
        fillPixmap(normalCursorPixmap, region);

        // initialize attack cursor pixmaps
        region = resources.atlas(Constants.CURSOR_ATLAS).findRegion(Constants.CURSOR_TEXTURE_ATTACK);

        Pixmap attackCursorPixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
        fillPixmap(attackCursorPixmap, region);

        // initialize animations
        attackOrderAnimation = FrameAnimationFactory.getInstance().create("cursor_animation_attack");
        moveOrderAnimation = FrameAnimationFactory.getInstance().create("cursor_animation_move");

        // initialize cursors
        cursorNormal = Gdx.graphics.newCursor(normalCursorPixmap, 0, 0);
        cursorAttack = Gdx.graphics.newCursor(attackCursorPixmap, 0, 0);
        cursorNone = Gdx.graphics.newCursor(emptyPixmap, 0, 0);

        // get rid of the pixmaps
        normalCursorPixmap.dispose();
        attackCursorPixmap.dispose();
        emptyPixmap.dispose();

        initialized = true;
    }

    /**
     * Fills a pixmap with texture data
     *
     * @param pixmap pixmap to fill
     * @param region texture region to take data from
     */
    private static void fillPixmap(Pixmap pixmap, TextureRegion region) {
        if (!region.getTexture().getTextureData().isPrepared()) {
            region.getTexture().getTextureData().prepare();
        }

        Pixmap full = region.getTexture().getTextureData().consumePixmap();

        for (int x = 0; x < region.getRegionWidth(); x++) {
            for (int y = 0; y < region.getRegionHeight(); y++) {
                int pixel = full.getPixel(region.getRegionX() + x, region.getRegionY() + y);

                pixmap.setColor(pixel);
                pixmap.drawPixel(x, y);
            }
        }
    }

    /**
     * Sets the currently displayed cursor
     *
     * @param cursorType cursor to display
     */
    public static void setCursor(byte cursorType) {
        if (!initialized) {
            throw new IllegalStateException("Cursors not initialized");
        }

        if (cursorType == cursor) {
            return;
        }

        switch (cursorType) {
            case CURSOR_NONE:
                Gdx.graphics.setCursor(cursorNone);
                break;
            case CURSOR_NORMAL:
                Gdx.graphics.setCursor(cursorNormal);
                break;
            case CURSOR_ATTACK:
                Gdx.graphics.setCursor(cursorAttack);
                break;
            default:
                throw new IllegalArgumentException("Unknown cursor type");
        }

        cursor = cursorType;
    }

    /**
     * Plays a cursor animation
     *
     * @param animation animation to play
     * @param x x coordinate in world system
     * @param y y coordinate in world system
     */
    public static void playCursorAnimation(byte animation, float x, float y) {
        switch (animation) {
            case ANIMATION_MOVE:
                playCursorAnimation(moveOrderAnimation, x, y);
                break;
            case ANIMATION_ATTACK:
                playCursorAnimation(attackOrderAnimation, x, y);
                break;
            default:
                throw new IllegalArgumentException("Unknown animation");
        }
    }

    /**
     * Plays a cursor animation
     *
     * @param animation animation to play
     * @param x animation x
     * @param y animation y
     */
    private static void playCursorAnimation(FrameAnimation animation, float x, float y) {
        animation.resetAnimation();
        animation.setCenterX(x);
        animation.setCenterY(y);

        currentAnimation = animation;

        animation.addFinishListener(new AnimationFinishListener() {
            @Override
            public void finished(Animation animation) {
                currentAnimation = null;
            }
        });
    }

    /**
     * Disposes of cursor resources
     */
    public static void dispose() {
        if (initialized) {
            cursorNormal.dispose();
            cursorAttack.dispose();
            cursorNone.dispose();

            attackOrderAnimation = null;
            moveOrderAnimation = null;
        }
    }

    /**
     * Renders cursor animation
     *
     * @param batch sprite batch to draw to
     * @param resources game's assets
     */
    public static void renderAnimation(SpriteBatch batch, Resources resources) {
        if (currentAnimation != null) {
            currentAnimation.render(batch, resources);
        }
    }

    /**
     * Updates cursor animation
     *
     * @param delta time since the last update
     */
    public static void updateAnimation(float delta) {
        if (currentAnimation != null) {
            currentAnimation.update(delta);
        }
    }
}
