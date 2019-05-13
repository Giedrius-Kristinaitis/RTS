package com.gasis.rts.logic.map.blockmap;

/**
 * Represents an image in a map block
 */
public class BlockImage {

    public String atlas; // name of the texture atlas
    public String texture; // name of the texture

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
