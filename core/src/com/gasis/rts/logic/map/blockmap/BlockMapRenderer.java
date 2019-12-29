package com.gasis.rts.logic.map.blockmap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.map.Map;
import com.gasis.rts.logic.map.MapRenderer;
import com.gasis.rts.logic.player.exploration.ExplorationDataInterface;
import com.gasis.rts.logic.render.RenderQueueInterface;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

/**
 * Renders a block map to the screen
 */
@SuppressWarnings("Duplicates")
public class BlockMapRenderer implements MapRenderer {

    // position and dimensions of the rendered area
    protected float renderX;
    protected float renderY;
    protected float renderWidth;
    protected float renderHeight;

    // the rendered map
    protected BlockMap map;

    // exploration data
    protected ExplorationDataInterface explorationData;

    /**
     * Renders the map
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources, RenderQueueInterface renderQueue) {
        // position and dimensions of the rendered map portion (window)
        short windowX = (short) Math.max(0, (short) renderX - 3);
        short windowY = (short) Math.max(0, (short) renderY - 3);

        short windowWidth = (short) Math.min(map.getWidth() - windowX, (short) renderWidth + 6);
        short windowHeight = (short) Math.min(map.getHeight() - windowY, (short) renderHeight + 6);

        for (BlockMapLayer layer: map.getLayers()) {
            for (short x = windowX; x < windowX + windowWidth; x++) {
                for (short y = windowY; y < windowY + windowHeight; y++) {
                    Block block = layer.getBlock(x, y);

                    if (block == null) {
                        continue;
                    }

                    if (block instanceof VisibleBlock) {
                        ((VisibleBlock) block).render(batch, resources, renderQueue);
                    }
                }
            }
        }
    }

    /**
     * Renders for of war
     *
     * @param batch sprite batch to draw to
     * @param resources game's assets
     */
    @Override
    public void renderFogOfWar(SpriteBatch batch, Resources resources) {
        // position and dimensions of the rendered map portion (window)
        short windowX = (short) Math.max(0, (short) renderX - 3);
        short windowY = (short) Math.max(0, (short) renderY - 3);

        short windowWidth = (short) Math.min(map.getWidth() - windowX, (short) renderWidth + 6);
        short windowHeight = (short) Math.min(map.getHeight() - windowY, (short) renderHeight + 6);

        for (short x = windowX; x < windowX + windowWidth; x++) {
            for (short y = windowY; y < windowY + windowHeight; y++) {
                if (!explorationData.isExplored(x, y)) {
                    batch.draw(
                            resources.atlas(Constants.GENERAL_TEXTURE_ATLAS).findRegion(Constants.UNEXPLORED_AREA),
                            x * Block.BLOCK_WIDTH - Block.BLOCK_WIDTH / 5f,
                            y * Block.BLOCK_HEIGHT - Block.BLOCK_HEIGHT / 5f,
                            Block.BLOCK_WIDTH * 1.4f,
                            Block.BLOCK_HEIGHT * 1.4f
                    );
                } else if (!explorationData.isVisible(x, y)) {
                    batch.draw(
                            resources.atlas(Constants.GENERAL_TEXTURE_ATLAS).findRegion(Constants.FOG_OF_WAR),
                            x * Block.BLOCK_WIDTH - Block.BLOCK_WIDTH / 5.9f,
                            y * Block.BLOCK_HEIGHT - Block.BLOCK_HEIGHT / 5.9f,
                            Block.BLOCK_WIDTH * 1.33898f,
                            Block.BLOCK_HEIGHT * 1.33898f
                    );
                }
            }
        }
    }

    /**
     * Sets map exploration data to use
     *
     * @param explorationData exploration data
     */
    @Override
    public void setExplorationData(ExplorationDataInterface explorationData) {
        this.explorationData = explorationData;
    }

    /**
     * Updates the state of the map renderer
     *
     * @param delta time elapsed since the last update
     */
    @Override
    public void update(float delta) {

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
            renderWidth = map.getWidth();
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

    /**
     * Gets render x
     *
     * @return
     */
    @Override
    public float getActualRenderXInWorldCoordinates() {
        return (renderX - 3f) * Block.BLOCK_WIDTH;
    }

    /**
     * Gets render y
     *
     * @return
     */
    @Override
    public float getActualRenderYInWorldCoordinates() {
        return (renderY - 3f) * Block.BLOCK_HEIGHT;
    }

    /**
     * Gets render width
     *
     * @return
     */
    @Override
    public float getActualRenderWidthInWorldCoordinates() {
        return (renderWidth + 6f) * Block.BLOCK_WIDTH;
    }

    /**
     * Gets render height
     *
     * @return
     */
    @Override
    public float getActualRenderHeightInWorldCoordinates() {
        return (renderHeight + 6f) * Block.BLOCK_HEIGHT;
    }
}
