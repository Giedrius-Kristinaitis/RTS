package com.gasis.rts.logic.player.controls;

import com.gasis.rts.logic.player.Player;

/**
 * Unit selecting logic
 */
public class UnitSelector {

    // player whose units are being selected
    protected Player player;

    /**
     * Default class constructor
     * @param player
     */
    public UnitSelector(Player player) {
        this.player = player;
    }

    /**
     * Called when the screen was touched or a mouse button was pressed
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button  the button
     */
    public void touchDown(int screenX, int screenY, int pointer, int button) {

    }

    /**
     * Called when a finger was lifted or a mouse button was released
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event.
     * @param button  the button
     */
    public void touchUp(int screenX, int screenY, int pointer, int button) {

    }

    /**
     * Called when a finger was lifted or a mouse button was released
     *
     * @param screenX
     * @param screenY
     * @param pointer the pointer for the event
     */
    public void touchDragged(int screenX, int screenY, int pointer) {

    }
}
