package com.gasis.rts.ui.screen.implementations.gamescreen.components;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gasis.rts.logic.GameInstance;

/**
 * Abstract UI component
 */
public abstract class AbstractComponent extends Table {

    // game instance
    protected GameInstance game;

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
}
