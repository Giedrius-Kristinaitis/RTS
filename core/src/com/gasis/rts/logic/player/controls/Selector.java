package com.gasis.rts.logic.player.controls;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.player.Player;

/**
 * Can select something the controlled player owns
 */
public abstract class Selector {

    // the game's map
    protected BlockMap map;

    // player whose units are being selected
    protected Player player;

    // used to render texture-less shapes
    protected ShapeRenderer shapeRenderer;

    /**
     * Default class constructor
     *
     * @param map
     * @param shapeRenderer
     * @param player
     */
    public Selector(BlockMap map, ShapeRenderer shapeRenderer, Player player) {
        this.map = map;
        this.shapeRenderer = shapeRenderer;
        this.player = player;
    }

    /**
     * Called when the screen was touched or a mouse button was pressed
     *
     * @param x x coordinate relative to the bottom left map corner
     * @param y y coordinate relative to the bottom left map corner
     * @param pointer the pointer for the event.
     * @param button  the button
     */
    public void touchDown(float x, float y, int pointer, int button) { }

    /**
     * Called when a finger was lifted or a mouse button was released
     *
     * @param x x coordinate relative to the bottom left map corner
     * @param y y coordinate relative to the bottom left map corner
     * @param pointer the pointer for the event.
     * @param button  the button
     */
    public void touchUp(float x, float y, int pointer, int button) { }

    /**
     * Called when a touch drag event occurs
     *
     * @param x x coordinate relative to the bottom left map corner
     * @param y y coordinate relative to the bottom left map corner
     * @param pointer the pointer for the event
     */
    public void touchDragged(float x, float y, int pointer) { }

    /**
     * Called when the mouse is moved
     *
     * @param x mouse x relative to map's bottom left
     * @param y mouse y relative to map's bottom left
     */
    public void mouseMoved(float x, float y) { }

    /**
     * Renders selection corners for the specified object
     *
     * @param object
     */
    protected void renderSelectionCorners(GameObject object) {

    }
}
