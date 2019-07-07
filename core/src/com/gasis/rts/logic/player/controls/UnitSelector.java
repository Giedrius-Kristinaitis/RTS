package com.gasis.rts.logic.player.controls;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.player.Player;
import com.gasis.rts.resources.Resources;

/**
 * Unit selecting logic
 */
public class UnitSelector implements Renderable {

    // player whose units are being selected
    protected Player player;

    // used to render texture-less shapes
    protected ShapeRenderer shapeRenderer;

    /**
     * Default class constructor
     * @param player
     */
    public UnitSelector(ShapeRenderer shapeRenderer, Player player) {
        this.shapeRenderer = shapeRenderer;
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

    /**
     * Renders the object to the screen
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {

    }
}
