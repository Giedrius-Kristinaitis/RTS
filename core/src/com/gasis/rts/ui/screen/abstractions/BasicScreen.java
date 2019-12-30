package com.gasis.rts.ui.screen.abstractions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gasis.rts.resources.Resources;

/**
 * Basic screen class with no input handling
 */
public abstract class BasicScreen extends ScreenAdapter {

    // resources to be used by the screen
    protected Resources resources;

    // used to switch screens
    protected ScreenSwitcher screenSwitcher;

    // sprite batch used by the screen
    protected SpriteBatch batch;

    // used to draw texture-less shapes
    protected ShapeRenderer shapeRenderer;

    // viewport used by the screen
    protected Viewport port;

    // used in render() to check if draw() needs to be called or not
    private boolean disposed = false;

    /**
     * Performs required initialization
     */
    public void initialize() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        port.apply(true);
    }

    /**
     * Called when the screen should render itself. Clears the screen with black
     *
     * @param delta time elapsed since last render
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        // if the screen was disposed of inside update(), then do not draw.
        // of course I could call draw() before update() to avoid this issue,
        // but I think it is better for game state to update before drawing, so...
        if (!disposed) {
            port.apply();
            port.getCamera().update();
            draw(batch, (OrthographicCamera) port.getCamera(), delta);
            draw(shapeRenderer, (OrthographicCamera) port.getCamera(), delta);
        }
    }

    /**
     * Called when the screen should render itself
     *
     * @param delta time elapsed since last render
     * @param batch batch used to draw sprites to
     * @param cam   world's camera
     */
    public abstract void draw(SpriteBatch batch, OrthographicCamera cam, float delta);

    /**
     * Draws shapes
     *
     * @param shapeRenderer renderer to draw shapes to
     * @param delta         time elapsed since last render
     * @param cam           world's camera
     */
    public void draw(ShapeRenderer shapeRenderer, OrthographicCamera cam, float delta) {
    }

    /**
     * Called when the screen should update itself
     *
     * @param delta time elapsed since last update
     */
    public abstract void update(float delta);

    /**
     * Sets resources object
     *
     * @param resources resources to be used
     */
    public void setResources(Resources resources) {
        this.resources = resources;
    }

    /**
     * Sets screen switcher
     *
     * @param screenSwitcher
     */
    public void setScreenSwitcher(ScreenSwitcher screenSwitcher) {
        this.screenSwitcher = screenSwitcher;
    }

    /**
     * Sets the viewport
     *
     * @param port new viewport
     */
    public void setViewport(Viewport port) {
        this.port = port;
    }

    /**
     * Called when the window resizes
     *
     * @param width  window width
     * @param height window height
     */
    @Override
    public void resize(int width, int height) {
        port.update(width, height, true);
    }

    /**
     * Gets rid of heavy resources
     */
    @Override
    public void dispose() {
        disposed = true;

        batch.dispose();
        shapeRenderer.dispose();
    }
}
