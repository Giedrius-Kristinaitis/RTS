package com.gasis.rts.logic.player.controls;

import com.badlogic.gdx.graphics.Color;
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

    // selection rectangle's position and dimensions
    protected float selectionStartX;
    protected float selectionStartY;
    protected float selectionEndX;
    protected float selectionEndY;

    // should the selection rectangle be rendered or not
    protected boolean renderSelectionRectangle = false;

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
     * @param x x coordinate relative to the bottom left map corner
     * @param y y coordinate relative to the bottom left map corner
     * @param pointer the pointer for the event.
     * @param button  the button
     */
    public void touchDown(float x, float y, int pointer, int button) {
        selectionStartX = x;
        selectionStartY = y;
        selectionEndX = x;
        selectionEndY = y;
    }

    /**
     * Called when a finger was lifted or a mouse button was released
     *
     * @param x x coordinate relative to the bottom left map corner
     * @param y y coordinate relative to the bottom left map corner
     * @param pointer the pointer for the event.
     * @param button  the button
     */
    public void touchUp(float x, float y, int pointer, int button) {
        renderSelectionRectangle = false;
    }

    /**
     * Called when a touch drag event occurs
     *
     * @param x x coordinate relative to the bottom left map corner
     * @param y y coordinate relative to the bottom left map corner
     * @param pointer the pointer for the event
     */
    public void touchDragged(float x, float y, int pointer) {
        renderSelectionRectangle = true;

        selectionEndX = x;
        selectionEndY = y;
    }

    /**
     * Renders the object to the screen
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {
        if (renderSelectionRectangle) {
            renderSelectionRectangle();
        }
    }

    /**
     * Renders the unit selection rectangle
     */
    protected void renderSelectionRectangle() {
        shapeRenderer.setColor(Color.WHITE);

        if (selectionEndX < selectionStartX || selectionEndY < selectionStartY) {
            shapeRenderer.rect(selectionEndX, selectionEndY, selectionStartX - selectionEndX, selectionStartY - selectionEndY);
        } else {
            shapeRenderer.rect(selectionStartX, selectionStartY, selectionEndX - selectionStartX, selectionEndY - selectionStartY);
        }
    }
}
