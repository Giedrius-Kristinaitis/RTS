package com.gasis.rts.logic.map.blockmap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.map.Map;
import com.gasis.rts.logic.map.MapLayer;
import com.gasis.rts.logic.map.MapRenderer;
import com.gasis.rts.resources.Resources;

import java.util.Iterator;

/**
 * Renders a block map to the screen
 */
public class BlockMapRenderer implements MapRenderer {

    // position and dimensions of the rendered area
    protected float renderX;
    protected float renderY;
    protected float renderWidth;
    protected float renderHeight;

    // the rendered map
    protected BlockMap map;

    /**
     * Renders the map
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     * @param delta     time elapsed since the last render
     */
    @Override
    public void render(SpriteBatch batch, Resources resources, float delta) {
        // position and dimensions of the rendered map portion (window)
        short windowX = (short) Math.max(0, (short) renderX - 3);
        short windowY = (short) Math.max(0, (short) renderY - 3);

        short windowWidth = (short) Math.min(map.getWidth() - windowX, (short) renderWidth + 6);
        short windowHeight = (short) Math.min(map.getHeight() - windowY, (short) renderHeight + 6);

        for (short x = windowX; x < windowX + windowWidth; x++) {
            for (short y = windowY; y < windowY + windowHeight; y++) {
                Iterator<MapLayer> iterator = map.getLayers();

                // loop through map layers and render the (x, y) block of each layer
                while (iterator.hasNext()) {
                    Block block = ((BlockMapLayer) iterator.next()).getBlock(x, y);

                    if (block == null) {
                        continue;
                    }

                    if (block instanceof VisibleBlock) {
                        ((VisibleBlock) block).render(batch, resources, delta);
                    }
                }
            }
        }
    }

    /**
     * Sets the map to be rendered by this renderer
     *
     * @param map
     */
    @Override
    public void setRenderedMap(Map map) {
        if (!(map instanceof BlockMap)) {
            throw new IllegalArgumentException("The map must be a block map");
        }

        this.map = (BlockMap) map;
    }

    /**
     * Sets the x position of the rendered map portion's bottom-left corner
     *
     * @param x
     */
    @Override
    public void setRenderX(float x) {
        renderX = x;

        if (renderX < 0) {
            renderX = 0;
        }
    }

    /**
     * Sets the y position of the rendered map portion's bottom-left corner
     *
     * @param y
     */
    @Override
    public void setRenderY(float y) {
        renderY = y;

        if (renderY < 0) {
            renderY = 0;
        }
    }

    /**
     * Sets the width of the rendered map portion
     *
     * @param width
     */
    @Override
    public void setRenderWidth(float width) {
        renderWidth = width;

        if (renderWidth >= map.getWidth()) {
            renderWidth = (float) map.getWidth();
            renderX = 0;
        }
    }

    /**
     * Sets the height of the rendered map portion
     *
     * @param height
     */
    @Override
    public void setRenderHeight(float height) {
        renderHeight = height;

        if (renderHeight >= map.getHeight()) {
            renderHeight = map.getHeight();
            renderY = 0;
        }
    }

    /**
     * Offsets the x rendering position by the given value
     *
     * @param offsetX
     */
    @Override
    public void offsetRenderX(float offsetX) {
        renderX += offsetX;

        if (renderX > (float) map.getWidth() - renderWidth) {
            renderX = (float) map.getWidth() - renderWidth;
        } else if (renderX < 0) {
            renderX = 0;
        }
    }

    /**
     * Offsets the y rendering position by the given value
     *
     * @param offsetY
     */
    @Override
    public void offsetRenderY(float offsetY) {
        renderY += offsetY;

        if (renderY > (float) map.getHeight() - renderHeight) {
            renderY = (float) map.getHeight() - renderHeight;
        } else if (renderY < 0) {
            renderY = 0;
        }
    }
}
