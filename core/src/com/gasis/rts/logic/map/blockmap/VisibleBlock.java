package com.gasis.rts.logic.map.blockmap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.resources.Resources;

import java.util.Deque;
import java.util.LinkedList;

/**
 * A map block that has some image(s) displayed
 */
public class VisibleBlock extends Block {

    // the image in the foreground of the block
    protected Deque<String> images = new LinkedList<String>();

    /**
     * Adds a new image to be drawn in the block
     *
     * @param image name of the image texture
     * @param bottom should the image be inserted in the bottom of the block (meaning it
     * is being drawn first, and other images are drawn after it)
     */
    public void addImage(String image, boolean bottom) {
        if (bottom) {
            images.addFirst(image);
        } else {
            images.addLast(image);
        }
    }

    /**
     * Removes an image from the block
     *
     * @param image name of the image to remove
     */
    public void removeImage(String image) {
        images.remove(image);
    }

    /**
     * Renders the block to the screen
     *
     * @param batch sprite batch to draw the map to
     * @param res object used to access assets
     * @param delta time elapsed since the last render
     */
    @Override
    public void render(SpriteBatch batch, Resources res, float delta) {
        for (String image: images) {
            batch.draw(res.texture(image), x * Block.BLOCK_SIZE, y * Block.BLOCK_SIZE, Block.BLOCK_SIZE, Block.BLOCK_SIZE);
        }
    }
}
