package com.gasis.rts.logic.map.blockmap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

import java.util.Deque;
import java.util.LinkedList;

/**
 * A map block that has some image(s) displayed
 */
public class VisibleBlock extends Block implements Renderable {

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
     * Gets the image that is at the bottom
     * @return
     */
    public BlockImage getBottomImage() {
        if (images.size() > 0) {
            return images.getFirst();
        }

        return null;
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
     */
    @Override
    public void render(SpriteBatch batch, Resources res) {
        for (BlockImage image: images) {
            batch.draw(
                    res.atlas(Constants.FOLDER_ATLASES + image.atlas).findRegion(image.texture),
                    x * Block.BLOCK_WIDTH + image.offsetX,
                    y * Block.BLOCK_HEIGHT + image.offsetY,
                    image.width / 2,
                    image.height / 2,
                    image.width,
                    image.height,
                    image.scale,
                    image.scale,
                    image.rotation
            );
        }
    }
}
