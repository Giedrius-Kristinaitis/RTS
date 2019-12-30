package com.gasis.rts.ui.screen.implementations;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.gasis.rts.logic.GameInstance;
import com.gasis.rts.ui.screen.abstractions.StagedScreen;
import com.gasis.rts.ui.screen.implementations.gamescreen.components.Minimap;

/**
 * Game screen. Displays game world and ui
 */
public class GameScreen extends StagedScreen {

    // instance of the game-world
    private GameInstance game;

    // minimap component
    private Minimap minimap;

    /**
     * Called when the screen needs to be initialized
     */
    @Override
    public void initialize() {
        super.initialize();

        game = new GameInstance(resources);
        game.setCamera((OrthographicCamera) port.getCamera());
        game.setViewport(port);

        // create UI components
        minimap = new Minimap();
        minimap.setGameInstance(game);
    }

    /**
     * Performs ui setup
     *
     * @param stage stage to put ui widgets in
     */
    @Override
    public void setupUI(Stage stage) {
        Table layout = new Table();
        layout.setFillParent(true);

        // add ui components
        layout.align(Align.topLeft).add(minimap).padLeft(25).padTop(25).width(256).height(256);

        layout.pack();
        stage.addActor(layout);
    }

    /**
     * Draws the game
     *
     * @param delta time elapsed since last render
     * @param batch batch used to draw sprites to
     * @param cam   world's camera
     */
    @Override
    public void draw(SpriteBatch batch, OrthographicCamera cam, float delta) {
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        game.draw(batch);
        batch.end();
    }

    /**
     * Draws shapes
     *
     * @param shapeRenderer renderer to draw shapes to
     * @param cam           world's camera
     * @param delta         time elapsed since last render
     */
    @Override
    public void draw(ShapeRenderer shapeRenderer, OrthographicCamera cam, float delta) {
        shapeRenderer.setProjectionMatrix(cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        game.draw(shapeRenderer);
        shapeRenderer.end();
    }

    /**
     * Updates the game state
     *
     * @param delta time elapsed since last update
     */
    @Override
    public void update(float delta) {
        game.update(delta);
    }

    /**
     * Called when the size of the window changes
     *
     * @param width  new width of the window
     * @param height new height of the window
     */
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        port.update(width, height, true);
        game.screenSizeChanged(width, height);

        // resize ui components
        minimap.resize(width, height);
    }

    /**
     * Called when a key was pressed
     *
     * @param keycode one of the constants in Input.Keys
     * @return whether the input was processed
     */
    @Override
    public boolean keyDown(int keycode) {
        game.keyDown(keycode);
        return true;
    }

    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in Input.Keys
     * @return whether the input was processed
     */
    @Override
    public boolean keyUp(int keycode) {
        game.keyUp(keycode);
        return true;
    }

    /**
     * Called when the screen was touched or a mouse button was pressed
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        game.touchDown(screenX, screenY, pointer, button);
        return true;
    }

    /**
     * Called when a finger was lifted or a mouse button was released
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button  the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        game.touchUp(screenX, screenY, pointer, button);
        return true;
    }

    /**
     * Called when a finger or the mouse was dragged.
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @return whether the input was processed
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        game.touchDragged(screenX, screenY, pointer);
        return true;
    }

    /**
     * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
     *
     * @param screenX
     * @param screenY
     * @return whether the input was processed
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        game.mouseMoved(screenX, screenY);
        return true;
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on iOS.
     *
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed.
     */
    @Override
    public boolean scrolled(int amount) {
        game.scrolled(amount);
        return true;
    }

    /**
     * Gets rid of heavy resources like textures and so on...
     */
    @Override
    public void dispose() {
        super.dispose();
        game.unloadResources();
    }
}
