package com.gasis.rts.ui.screen.component.minimap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.gasis.rts.logic.map.blockmap.Block;

/**
 * Minimap navigator
 */
public class MinimapNavigator implements Navigator {

    /**
     * Changes the position of the camera based on pointer location
     *
     * @param cam
     * @param x
     * @param y
     * @param minimapDimensionsProvider
     */
    @Override
    public void changePosition(OrthographicCamera cam, float x, float y, MinimapDimensionsProvider minimapDimensionsProvider) {
        float relativeX = x - minimapDimensionsProvider.getMinimapX();
        float relativeY = (Gdx.graphics.getHeight() - y) - minimapDimensionsProvider.getMinimapY();

        cam.position.x = Block.BLOCK_WIDTH * relativeX / minimapDimensionsProvider.getBlockWidth();
        cam.position.y = Block.BLOCK_HEIGHT * relativeY / minimapDimensionsProvider.getBlockHeight();
    }
}
