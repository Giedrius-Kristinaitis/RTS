package com.gasis.rts.ui.screen.component.minimap;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Navigates the world using the minimap
 */
public interface Navigator {

    /**
     * Changes the position of the camera based on pointer location
     *
     * @param cam
     * @param x
     * @param y
     * @param minimapDimensionsProvider
     */
    void changePosition(OrthographicCamera cam, float x, float y, MinimapDimensionsProvider minimapDimensionsProvider);
}
