package com.gasis.rts.ui.screen.component.minimap;

/**
 * Provides dimension data for the minimap component
 */
public interface MinimapDimensionsProvider {

    /**
     * Gets the minimap's x
     *
     * @return
     */
    float getMinimapX();

    /**
     * Gets the minimap's y
     *
     * @return
     */
    float getMinimapY();

    /**
     * Gets the minimap's width
     *
     * @return
     */
    float getMinimapWidth();

    /**
     * Gets the minimap's height
     *
     * @return
     */
    float getMinimapHeight();

    /**
     * Gets the width of a block
     *
     * @return
     */
    float getBlockWidth();

    /**
     * Gets the height of a block
     *
     * @return
     */
    float getBlockHeight();
}
