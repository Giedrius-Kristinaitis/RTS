package com.gasis.rts.ui.screen.implementations.gamescreen.components.minimap;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.gasis.rts.logic.render.SimpleRenderable;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.ui.screen.implementations.gamescreen.components.minimap.navigator.BoundsRenderer;

/**
 * Navigates the world using the minimap
 */
public class Navigator implements SimpleRenderable {

    // renders screen bounds
    protected BoundsRenderer boundsRenderer;

    /**
     * Class constructor
     */
    public Navigator() {
        boundsRenderer = new BoundsRenderer();
    }

    /**
     * Renders the object
     *
     * @param batch     batch to draw to
     * @param resources game's assets
     */
    @Override
    public void render(Batch batch, Resources resources) {
        boundsRenderer.render(batch, resources);
    }

    /**
     * Sets the bounds provider
     *
     * @param boundsProvider bounds provider
     */
    public void setBoundsProvider(BoundsProvider boundsProvider) {
        boundsRenderer.setBoundsProvider(boundsProvider);
    }
}
