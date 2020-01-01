package com.gasis.rts.ui.screen.component;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.gasis.rts.logic.GameInstance;
import com.gasis.rts.logic.player.Player;
import com.gasis.rts.ui.screen.component.minimap.*;
import com.gasis.rts.utils.Constants;

/**
 * Minimap showing map elements
 */
public class Minimap extends AbstractComponent implements BoundsProvider, MinimapDimensionsProvider {

    // minimap's opacity
    protected final float OPACITY = 0.9f;

    // block size
    protected float blockWidth;
    protected float blockHeight;

    // border sizes
    protected float rightBorderWidth;
    protected float rightBorderHeight;
    protected float bottomBorderWidth;
    protected float bottomBorderHeight;

    // border offsets
    protected float rightBorderOffsetX;
    protected float rightBorderOffsetY;
    protected float bottomBorderOffsetX;
    protected float bottomBorderOffsetY;

    // current render bounds
    protected Bounds renderBounds;

    // minimap navigator
    protected Navigator navigator;

    // renders minimap content
    protected ContentRenderer contentRenderer;

    // the player minimap is rendered for
    protected Player player;

    /**
     * Class constructor
     */
    public Minimap() {
        super();

        renderBounds = new Bounds();
        navigator = new Navigator();
        contentRenderer = new ContentRenderer();
    }

    /**
     * Sets the player
     *
     * @param player
     */
    public void setPlayer(Player player) {
        this.player = player;

        contentRenderer.setPlayer(player);
    }

    /**
     * Sets the game instance
     *
     * @param game game instance
     */
    @Override
    public void setGameInstance(GameInstance game) {
        super.setGameInstance(game);

        contentRenderer.setBoundsProvider(this);
        contentRenderer.setDimensionsProvider(this);
        contentRenderer.setMap(game.getMap());
        contentRenderer.setExplorationData(game.getExplorationData());
    }

    /**
     * Updates the minimap
     *
     * @param delta time elapsed since the last update
     */
    @Override
    public void act(float delta) {
        super.act(delta);

        updateRenderBounds();
    }

    /**
     * Updates the current render bounds
     */
    protected void updateRenderBounds() {
        renderBounds.start.x = getX() + blockWidth * game.getRenderBoundsProvider().getRenderX();
        renderBounds.start.y = getY() + blockHeight * game.getRenderBoundsProvider().getRenderY();
        renderBounds.end.x = Math.min(renderBounds.start.x + blockWidth * game.getRenderBoundsProvider().getRenderWidth(), getX() + blockWidth * game.getMap().getWidth());
        renderBounds.end.y = Math.min(renderBounds.start.y + blockHeight * game.getRenderBoundsProvider().getRenderHeight(), getY() + blockHeight * game.getMap().getHeight());
    }

    /**
     * Draws the minimap
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        batch.setColor(1, 1, 1, OPACITY);
        contentRenderer.render(batch, game.getResources());
        batch.setColor(1, 1, 1, 1);

        renderBorders(batch);
    }

    /**
     * Renders the minimap's borders
     *
     * @param batch batch to draw to
     */
    protected void renderBorders(Batch batch) {
        batch.draw(
                game.getResources().atlas(Constants.FOLDER_ATLASES + Constants.MINIMAP_ATLAS).findRegion(Constants.MINIMAP_BORDER_RIGHT),
                getX() + rightBorderOffsetX,
                getY() + rightBorderOffsetY,
                rightBorderWidth,
                rightBorderHeight
        );

        batch.draw(
                game.getResources().atlas(Constants.FOLDER_ATLASES + Constants.MINIMAP_ATLAS).findRegion(Constants.MINIMAP_BORDER_BOTTOM),
                getX() + bottomBorderOffsetX,
                getY() + bottomBorderOffsetY,
                bottomBorderWidth,
                bottomBorderHeight
        );
    }

    /**
     * Called when the stage resizes
     *
     * @param width  stage width
     * @param height stage height
     */
    @Override
    public void resize(int width, int height) {
        blockWidth = getWidth() / game.getMap().getWidth();
        blockHeight = getHeight() / game.getMap().getHeight();

        rightBorderWidth = getWidth() * 0.15f;
        rightBorderHeight = getHeight() * 1.07f;
        bottomBorderWidth = getWidth();
        bottomBorderHeight = getHeight() * 0.08f;

        rightBorderOffsetX = getWidth();
        rightBorderOffsetY = -(rightBorderHeight - getHeight());
        bottomBorderOffsetY = -getHeight() * 0.05f;
    }

    /**
     * Gets the current render bounds in block coordinates
     *
     * @return
     */
    @Override
    public Bounds getRenderBounds() {
        return renderBounds;
    }

    /**
     * Gets the minimap's x
     *
     * @return
     */
    @Override
    public float getMinimapX() {
        return getX();
    }

    /**
     * Gets the minimap's y
     *
     * @return
     */
    @Override
    public float getMinimapY() {
        return getY();
    }

    /**
     * Gets the minimap's width
     *
     * @return
     */
    @Override
    public float getMinimapWidth() {
        return getWidth();
    }

    /**
     * Gets the minimap's height
     *
     * @return
     */
    @Override
    public float getMinimapHeight() {
        return getHeight();
    }

    /**
     * Gets the width of a block
     *
     * @return
     */
    @Override
    public float getBlockWidth() {
        return blockWidth;
    }

    /**
     * Gets the height of a block
     *
     * @return
     */
    @Override
    public float getBlockHeight() {
        return blockHeight;
    }
}
