package com.gasis.rts.logic.object.building;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.animation.frameanimation.FrameAnimation;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * A building on the map
 */
public class Building extends GameObject {

    // the name of the building's texture
    protected String texture;

    // animations of the building
    protected List<FrameAnimation> animations = new ArrayList<FrameAnimation>();

    /**
     * Gets the name of the building's texture
     * @return
     */
    public String getTexture() {
        return texture;
    }

    /**
     * Gets the animations of the building
     * @return
     */
    public Iterable<FrameAnimation> getAnimations() {
        return animations;
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
     * Adds an animation to this building
     *
     * @param animation new animation to add
     */
    public void addAnimation(FrameAnimation animation) {
        animations.add(animation);
    }

    /**
     * Updates the game object
     *
     * @param delta time elapsed since the last render
     */
    @Override
    public void update(float delta) {
        for (FrameAnimation animation: animations) {
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

        for (FrameAnimation animation: animations) {
            animation.render(batch, resources);
        }
    }
}
