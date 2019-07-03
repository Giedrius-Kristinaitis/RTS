package com.gasis.rts.logic.map.blockmap;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Handles block map zooming logic
 */
public class BlockMapZoomer {

    // the current zoom value
    private float zoom = 1;

    // zoom limitations
    private final float maxZoomIn = 0.25f;
    private final float maxZoomOut = 1.4f;

    // how much the zoom changes per second
    private float zoomSpeed;

    /**
     * Updates the camera's zoom
     *
     * @param cam game camera
     * @param delta time elapsed since the last update
     */
    public void updateMapZoom(OrthographicCamera cam, float delta) {
        cam.zoom += zoomSpeed * delta;

        zoomSpeed *= Math.max(0, 1f - delta);

        if (cam.zoom < maxZoomIn) {
            cam.zoom = maxZoomIn;
            zoomSpeed = 0;
        } else if (cam.zoom > maxZoomOut) {
            cam.zoom = maxZoomOut;
            zoomSpeed = 0;
        }
    }

    /**
     * Updates the speed of the camera's zoom
     *
     * @param scrollAmount how much was the mouse wheel scrolled
     */
    public void updateZoomSpeed(int scrollAmount) {
        zoomSpeed += scrollAmount / 10f;
    }
}
