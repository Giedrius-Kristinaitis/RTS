package com.gasis.rts.logic.map.blockmap;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.gasis.rts.logic.map.MapRenderer;

/**
 * Handles block map scrolling logic
 */
public class BlockMapScroller {

    // the speeds at which the map is being scrolled
    private float xScrollSpeed;
    private float yScrollSpeed;

    // the maximum scroll speed per second(both positive and negative)
    private final float maxScrollSpeed = 20;

    // is one of the map scroll keys currently held down
    private boolean scrollingUp;
    private boolean scrollingDown;
    private boolean scrollingLeft;
    private boolean scrollingRight;

    // how close to the screen edge the mouse has to be in order to start
    // scrolling the map (expressed in screen width and height percentage)
    private final float mouseTriggeredScrollBounds = 0.025f;

    // the map that is being scrolled
    private BlockMap map;

    // the renderer of the scrolled map
    private MapRenderer mapRenderer;

    // half width and height of the screen (expressed in game coordinates)
    private float halfWidth;
    private float halfHeight;

    // size of the screen (expressed in screen coordinates)
    private int screenWidth;
    private int screenHeight;

    /**
     * Default class constructor
     *
     * @param map
     * @param renderer
     */
    public BlockMapScroller(BlockMap map, MapRenderer renderer) {
        this.map = map;
        this.mapRenderer = renderer;
    }

    /**
     * Sets the halves of screen dimensions
     *
     * @param halfWidth new half width
     * @param halfHeight new half height
     */
    public void setHalfScreenDimensions(float halfWidth, float halfHeight) {
        this.halfHeight = halfHeight;
        this.halfWidth = halfWidth;
    }

    /**
     * Sets the dimensions of the screen
     *
     * @param screenWidth new screen width
     * @param screenHeight new screen height
     */
    public void setScreenDimensions(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    /**
     * Updates the map's scrolling
     *
     * @param delta time elapsed since the last update
     */
    @SuppressWarnings("Duplicates") // ain't gonna write a separate method for this 'duplication'
    public void updateMapScroll(OrthographicCamera cam, float delta) {
        cam.position.x += xScrollSpeed * delta;
        cam.position.y += yScrollSpeed * delta;

        // update the vertical scrolling
        if (scrollingUp) {
            yScrollSpeed += maxScrollSpeed * delta * 2;

            if (yScrollSpeed > maxScrollSpeed) {
                yScrollSpeed = maxScrollSpeed;
            }
        } else if (scrollingDown) {
            yScrollSpeed -= maxScrollSpeed * delta * 2;

            if (yScrollSpeed < -maxScrollSpeed) {
                yScrollSpeed = -maxScrollSpeed;
            }
        } else {
            yScrollSpeed *= Math.pow(Math.max(0, 1f - delta), 4);
        }

        // update the horizontal scrolling
        if (scrollingLeft) {
            xScrollSpeed -= maxScrollSpeed * delta * 2;

            if (xScrollSpeed < -maxScrollSpeed) {
                xScrollSpeed = -maxScrollSpeed;
            }
        } else if (scrollingRight) {
            xScrollSpeed += maxScrollSpeed * delta * 2;

            if (xScrollSpeed > maxScrollSpeed) {
                xScrollSpeed = maxScrollSpeed;
            }
        } else {
            xScrollSpeed *= Math.pow(Math.max(0, 1f - delta), 4);
        }

        // make sure the camera is in the bounds of the map
        if (cam.position.x < halfWidth * cam.zoom) {
            cam.position.x = halfWidth * cam.zoom;
        } else if (cam.position.x > map.getWidth() * Block.BLOCK_WIDTH - halfWidth * cam.zoom) {
            cam.position.x = map.getWidth() * Block.BLOCK_WIDTH - halfWidth * cam.zoom;
        }

        if (cam.position.y < halfHeight * cam.zoom) {
            cam.position.y = halfHeight * cam.zoom;
        } else if (cam.position.y > map.getHeight() * Block.BLOCK_HEIGHT - halfHeight * cam.zoom) {
            cam.position.y = map.getHeight() * Block.BLOCK_HEIGHT - halfHeight * cam.zoom;
        }

        // make sure the correct portion of the map is rendered
        mapRenderer.setRenderX(cam.position.x - cam.viewportWidth / 2f - 2);
        mapRenderer.setRenderY(cam.position.y - cam.viewportHeight / 2f - 2);

        // make sure the render width and height is always up to date
        mapRenderer.setRenderWidth((cam.viewportWidth / Block.BLOCK_WIDTH + 2) * Math.max(1, cam.zoom));
        mapRenderer.setRenderHeight((cam.viewportHeight / Block.BLOCK_HEIGHT + 2) * Math.max(1, cam.zoom));
    }

    /**
     * Starts scrolling the map
     *
     * @param keycode code of the pressed key
     */
    public void startScrolling(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                if (!scrollingUp) {
                    yScrollSpeed = 0;
                }

                scrollingUp = true;
                scrollingDown = false;
                break;
            case Input.Keys.DOWN:
                if (!scrollingDown) {
                    yScrollSpeed = 0;
                }

                scrollingDown = true;
                scrollingUp = false;
                break;
            case Input.Keys.LEFT:
                if (!scrollingLeft) {
                    xScrollSpeed = 0;
                }

                scrollingLeft = true;
                scrollingRight = false;
                break;
            case Input.Keys.RIGHT:
                if (!scrollingRight) {
                    xScrollSpeed = 0;
                }

                scrollingRight = true;
                scrollingLeft = false;
                break;
        }
    }

    /**
     * Stops scrolling the map
     *
     * @param keycode code of the released key
     */
    public void stopScrolling(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                scrollingUp = false;
                break;
            case Input.Keys.DOWN:
                scrollingDown = false;
                break;
            case Input.Keys.LEFT:
                scrollingLeft = false;
                break;
            case Input.Keys.RIGHT:
                scrollingRight = false;
                break;
        }
    }

    /**
     * Starts scrolling the map with the mouse
     *
     * @param screenX mouse pointer's x coordinate on the screen
     * @param screenY mouse pointer's y coordinate on the screen
     */
    public void initiateMouseScrolling(int screenX, int screenY) {
        if (screenX <= screenWidth * mouseTriggeredScrollBounds) {
            startScrolling(Input.Keys.LEFT);
        } else if (screenX >= screenWidth - screenWidth * mouseTriggeredScrollBounds) {
            startScrolling(Input.Keys.RIGHT);
        } else {
            stopScrolling(Input.Keys.LEFT);
            stopScrolling(Input.Keys.RIGHT);
        }

        if (screenY <= screenHeight * mouseTriggeredScrollBounds) {
            startScrolling(Input.Keys.UP);
        } else if (screenY >= screenHeight - screenHeight * mouseTriggeredScrollBounds) {
            startScrolling(Input.Keys.DOWN);
        } else {
            stopScrolling(Input.Keys.DOWN);
            stopScrolling(Input.Keys.UP);
        }
    }
}
