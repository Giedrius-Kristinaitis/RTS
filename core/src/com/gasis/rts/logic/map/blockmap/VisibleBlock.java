package com.gasis.rts.logic.map.blockmap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.resources.Resources;

import java.util.Deque;
import java.util.LinkedList;

/**
 * A map block that has some image(s) displayed
 */
public class VisibleBlock extends Block {

    // the image(s) in block
    protected Deque<BlockImage> images = new LinkedList<BlockImage>();

    /**
     * Adds a new image to be drawn in the block.
     *
     * @param image name of the image texture. Format - "atlas_name/texture_region_name"
     * @param bottom should the image be inserted in the bottom of the block (meaning it
     * is being drawn first, and other images are drawn after it)
     */
    public void addImage(BlockImage image, boolean bottom) {
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
    public void removeImage(BlockImage image) {
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
        // render the block itself
        for (BlockImage image: images) {
            batch.draw(
                    res.atlas(image.atlas).findRegion(image.texture),
                    x * Block.BLOCK_WIDTH + image.offsetX,
                    y * Block.BLOCK_HEIGHT + image.offsetY,
                    image.width,
                    image.height
            );
        }

        // render the object that occupies this block
        super.render(batch, res, delta);
    }
}
