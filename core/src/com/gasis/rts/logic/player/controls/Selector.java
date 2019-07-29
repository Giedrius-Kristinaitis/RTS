package com.gasis.rts.logic.player.controls;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.map.blockmap.Block;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.GameObject;
import com.gasis.rts.logic.player.Player;
import com.gasis.rts.resources.Resources;

/**
 * Can select something the controlled player owns
 */
public abstract class Selector implements Renderable {

    // the game's map
    protected BlockMap map;

    // player whose units are being selected
    protected Player player;

    // the object which should have selection corners drawn
    protected GameObject objectToRenderCornersFor;

    /**
     * Default class constructor
     *
     * @param map
     * @param player
     */
    public Selector(BlockMap map, Player player) {
        this.map = map;
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
    public void mouseMoved(float x, float y) {
        objectToRenderCornersFor = map.getOccupyingObject((short) (x / Block.BLOCK_WIDTH), (short) (y / Block.BLOCK_HEIGHT));
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

    /**
     * Renders texture-less shapes
     *
     * @param shapeRenderer renderer to draw shapes to
     */
    public void render(ShapeRenderer shapeRenderer) {
        if (objectToRenderCornersFor != null) {
            renderSelectionCorners(shapeRenderer, objectToRenderCornersFor);
        }
    }

    /**
     * Renders selection corners for the specified object
     *
     * @param object object to render corners for
     * @param shapeRenderer renderer to draw shapes to
     */
    protected void renderSelectionCorners(ShapeRenderer shapeRenderer, GameObject object) {
        if (object.isDestroyed()) {
            return;
        }

        // top left corner
        renderCorner(shapeRenderer, object.getX(), object.getY() + object.getHeight() - 0.2f, object.getX(), object.getY() + object.getHeight(), object.getX() + 0.2f, object.getY() + object.getHeight());

        // top right corner
        renderCorner(shapeRenderer, object.getX() + object.getWidth(), object.getY() + object.getHeight() - 0.2f, object.getX() + object.getWidth(), object.getY() + object.getHeight(), object.getX() + object.getWidth() - 0.2f, object.getY() + object.getHeight());

        // bottom right corner
        renderCorner(shapeRenderer, object.getX() + object.getWidth(), object.getY() + 0.2f, object.getX() + object.getWidth(), object.getY(), object.getX() + object.getWidth() - 0.2f, object.getY());

        // bottom left corner
        renderCorner(shapeRenderer, object.getX(), object.getY() + 0.2f, object.getX(), object.getY(), object.getX() + 0.2f, object.getY());
    }

    /**
     * Renders a single selection corner
     *
     * @param shapeRenderer renderer to draw shapes to
     * @param x1 x of the first point
     * @param y1 y of the first point
     * @param x2 x of the second point
     * @param y2 y of the second point
     * @param x3 x of the third point
     * @param y3 y of the third point
     */
    protected void renderCorner(ShapeRenderer shapeRenderer, float x1, float y1, float x2, float y2, float x3, float y3) {
        shapeRenderer.line(x1, y1, x2, y2);
        shapeRenderer.line(x2, y2, x3, y3);
    }
}
