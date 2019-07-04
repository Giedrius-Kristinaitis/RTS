package com.gasis.rts.logic.object.building;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.animation.Animation;
import com.gasis.rts.logic.animation.complexanimation.RisingSmokeAnimation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimation;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimationFactory;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.math.Point;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A building on the map
 */
public class Building extends GameObject {

    // the name of the building's texture
    protected String texture;

    // building frame animations' names and coordinates
    protected Map<Point, String> frameAnimations;

    // buildings complex animation's names and coordinates
    protected Map<Point, String> complexAnimations;

    // animations of the building
    protected List<Animation> animations = new ArrayList<Animation>();

    // building's dimensions in blocks
    protected byte widthInBlocks;
    protected byte heightInBlocks;

    // building's coordinates in blocks
    protected byte xInBlocks;
    protected byte yInBlocks;

    /**
     * Initializes the building's animations. Must be called after the building's position
     * is set
     */
    public void initializeAnimations() {
        initializeFrameAnimations();
        initializeComplexAnimations();
    }

    /**
     * Initializes frame animations
     */
    private void initializeFrameAnimations() {
        if (frameAnimations != null && frameAnimations.size() != 0) {
            for (Map.Entry<Point, String> animation: frameAnimations.entrySet()) {
                FrameAnimation frameAnimation = FrameAnimationFactory.getInstance().create(
                        animation.getValue(),
                        getCenterX() + animation.getKey().x,
                        getCenterY() + animation.getKey().y,
                        getCenterX() + animation.getKey().x,
                        getCenterY() + animation.getKey().y,
                        true);


                frameAnimation.setDelayedOnLoop(true);

                animations.add(frameAnimation);
            }
        }
    }

    /**
     * Initializes complex animations
     */
    private void initializeComplexAnimations() {
        if (complexAnimations != null && complexAnimations.size() != 0) {
            for (Map.Entry<Point, String> animation: complexAnimations.entrySet()) {
                Animation complexAnimation = null;

                if (animation.getValue().equalsIgnoreCase("rising smoke")) {
                    complexAnimation = new RisingSmokeAnimation(
                            getCenterX() + animation.getKey().x,
                            getCenterY() + animation.getKey().y
                    );
                } else {
                    throw new RuntimeException("Bad animation name");
                }

                animations.add(complexAnimation);
            }
        }
    }

    /**
     * Sets the frame animations' names and coordinates
     *
     * @param frameAnimations map with names and coordinates
     */
    public void setFrameAnimations(Map<Point, String> frameAnimations) {
        this.frameAnimations = frameAnimations;
    }

    /**
     * Sets the complex animations' names and coordinates
     *
     * @param complexAnimations map with animation names and coordinates
     */
    public void setComplexAnimations(Map<Point, String> complexAnimations) {
        this.complexAnimations = complexAnimations;
    }

    /**
     * Gets the name of the building's texture
     * @return
     */
    public String getTexture() {
        return texture;
    }

    /**
     * Sets the texture of the building
     *
     * @param texture name of the building's texture
     */
    public void setTexture(String texture) {
        this.texture = texture;
    }

    /**
     * Updates the game object
     *
     * @param delta time elapsed since the last render
     */
    @Override
    public void update(float delta) {
        for (Animation animation: animations) {
            animation.update(delta);
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
        batch.draw(
                resources.atlas(Constants.FOLDER_ATLASES + atlas).findRegion(texture),
                x,
                y,
                width,
                height
        );

        for (Animation animation: animations) {
            animation.render(batch, resources);
        }
    }

    /**
     * Gets the building's width in blocks
     * @return
     */
    public byte getWidthInBlocks() {
        return widthInBlocks;
    }

    /**
     * Sets the building's width in blocks
     *
     * @param widthInBlocks new width in blocks
     */
    public void setWidthInBlocks(byte widthInBlocks) {
        this.widthInBlocks = widthInBlocks;
    }

    /**
     * Gets the building's height in blocks
     * @return
     */
    public byte getHeightInBlocks() {
        return heightInBlocks;
    }

    /**
     * Sets the building's height in blocks
     *
     * @param heightInBlocks new height in blocks
     */
    public void setHeightInBlocks(byte heightInBlocks) {
        this.heightInBlocks = heightInBlocks;
    }

    /**
     * Gets the building's x in blocks
     * @return
     */
    public byte getXInBlocks() {
        return xInBlocks;
    }

    /**
     * Sets the building's x in blocks
     *
     * @param xInBlocks new x in blocks
     */
    public void setXInBlocks(byte xInBlocks) {
        this.xInBlocks = xInBlocks;
    }

    /**
     * Gets the building's y in blocks
     * @return
     */
    public byte getYInBlocks() {
        return yInBlocks;
    }

    /**
     * Sets the building's y in blocks
     *
     * @param yInBlocks new y in blocks
     */
    public void setYInBlocks(byte yInBlocks) {
        this.yInBlocks = yInBlocks;
    }
}
