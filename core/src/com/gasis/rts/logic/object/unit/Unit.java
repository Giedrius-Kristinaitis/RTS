package com.gasis.rts.logic.object.unit;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

import java.util.List;

/**
 * Represents a single unit on a map
 */
public abstract class Unit extends GameObject {

    // unit facing directions
    public static final byte NORTH = 0;
    public static final byte NORTH_EAST = 1;
    public static final byte EAST = 2;
    public static final byte SOUTH_EAST = 3;
    public static final byte SOUTH = 4;
    public static final byte SOUTH_WEST = 5;
    public static final byte WEST = 6;
    public static final byte NORTH_WEST = 7;

    // textures used by the unit
    // indexes of the textures must match the values of
    // the facing directions defined above
    protected List<String> textures;

    // the index of the current texture in the texture list
    protected byte currentTexture;

    // the direction the unit is currently facing
    protected byte facingDirection;

    /**
     * Sets the direction the unit is facing
     *
     * @param facingDirection new direction the unit is facing
     */
    public void setFacingDirection(byte facingDirection) {
        this.facingDirection = facingDirection;

        currentTexture = facingDirection;
    }

    /**
     * Gets the direction the unit is facing
     * @return
     */
    public byte getFacingDirection() {
        return facingDirection;
    }

    /**
     * Sets the textures of the unit
     *
     * @param textures new texture list
     */
    public void setTextures(List<String> textures) {
        this.textures = textures;
    }

    /**
     * Gets the textures of the unit
     * @return
     */
    public Iterable<String> getTextures() {
        return textures;
    }

    /**
     * Sets the current texture index
     *
     * @param currentTexture new texture index
     */
    public void setCurrentTexture(byte currentTexture) {
        this.currentTexture = currentTexture;
    }

    /**
     * Gets the index of the current unit's texture
     *
     * @return
     */
    public byte getCurrentTexture() {
        return currentTexture;
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
                resources.atlas(Constants.FOLDER_ATLASES + atlas).findRegion(textures.get(currentTexture)),
                x,
                y,
                width,
                height
        );
    }
}
