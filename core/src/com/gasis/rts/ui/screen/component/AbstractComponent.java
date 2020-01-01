package com.gasis.rts.ui.screen.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gasis.rts.logic.GameInstance;

/**
 * Abstract UI component
 */
public abstract class AbstractComponent extends Table implements InputProcessor {

    // game instance
    protected GameInstance game;

    // component's behavior
    protected ComponentBehavior behavior;

    /**
     * Sets the game instance
     *
     * @param game game instance
     */
    public void setGameInstance(GameInstance game) {
        this.game = game;
    }

    /**
     * Gets the game instance
     *
     * @return
     */
    public GameInstance getGameInstance() {
        return game;
    }

    /**
     * Called when the stage resizes
     *
     * @param width  stage width
     * @param height stage height
     */
    public void resize(int width, int height) {
    }

    /**
     * Called when a key was pressed
     *
     * @param keycode one of the constants in Input.Keys
     * @return whether the input was processed
     */
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    /**
     * Called when a key was released
     *
     * @param keycode one of the constants in Input.Keys
     * @return whether the input was processed
     */
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    /**
     * Called when a key was typed
     *
     * @param character The character
     * @return whether the input was processed
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
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
        if (!screenCoordsInBounds(screenX, screenY)) {
            return false;
        }

        return behavior != null && behavior.behaveTouchDown(this, screenX, screenY, pointer, button);
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
        if (!screenCoordsInBounds(screenX, screenY)) {
            return false;
        }

        return behavior != null && behavior.behaveTouchUp(this, screenX, screenY, pointer, button);
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
        if (!screenCoordsInBounds(screenX, screenY)) {
            return false;
        }

        return behavior != null && behavior.behaveTouchDragged(this, screenX, screenY, pointer);
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
        if (!screenCoordsInBounds(screenX, screenY)) {
            return false;
        }

        return behavior != null && behavior.behaveMouseMoved(this, screenX, screenY);
    }

    /**
     * Called when the mouse wheel was scrolled. Will not be called on iOS.
     *
     * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
     * @return whether the input was processed.
     */
    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    /**
     * Checks if the given screen coordinates are in the component's bounds
     *
     * @param screenX
     * @param screenY
     * @return
     */
    public boolean screenCoordsInBounds(int screenX, int screenY) {
        return screenX >= getX() && screenX <= getX() + getWidth() && Gdx.graphics.getHeight() - screenY >= getY() && Gdx.graphics.getHeight() - screenY <= getY() + getHeight();
    }

    /**
     * Sets the component's behavior
     *
     * @param behavior
     */
    public void setBehavior(ComponentBehavior behavior) {
        this.behavior = behavior;
    }
}
