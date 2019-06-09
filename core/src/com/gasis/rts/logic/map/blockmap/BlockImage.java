package com.gasis.rts.logic.map.blockmap;

/**
 * Represents an image in a map block
 */
public class BlockImage {

    public String atlas; // name of the texture atlas
    public String texture; // name of the texture

    public float offsetX; // bottom left corner offset on the x axis
    public float offsetY; // bottom left corner offset on the y axis

    public float width = Block.BLOCK_WIDTH; // width of the image
    public float height = Block.BLOCK_HEIGHT; // height of the image

    public float rotation; // rotation of the image in degrees
    public float scale = 1; // the scale of the image

    /**
     * Gets the hash code of this object
     */
    @Override
    public int hashCode() {
        return atlas.hashCode() ^ texture.hashCode();
    }

    /**
     * Checks if this image is equal to another image
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof BlockImage)) {
            return false;
        }

        BlockImage image = (BlockImage) other;

        return atlas.equals(image.atlas) && texture.equals(image.texture);
    }
}
