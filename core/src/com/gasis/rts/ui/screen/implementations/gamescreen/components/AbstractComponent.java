package com.gasis.rts.ui.screen.implementations.gamescreen.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gasis.rts.logic.GameInstance;

/**
 * Abstract UI component
 */
public class AbstractComponent extends Actor {

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
}
