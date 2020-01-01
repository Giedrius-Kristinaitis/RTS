package com.gasis.rts.ui.screen.component.minimap.navigator;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.gasis.rts.logic.render.SimpleRenderable;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.ui.screen.component.minimap.BoundsProvider;
import com.gasis.rts.utils.Constants;

/**
 * Renders current screen bounds on the minimap
 */
public class BoundsRenderer implements SimpleRenderable {

    // provides render bounds
    protected BoundsProvider boundsProvider;

    /**
     * Renders the object
     *
     * @param batch     batch to draw to
     * @param resources game's assets
     */
    @Override
    public void render(Batch batch, Resources resources) {
        batch.draw(
                resources.atlas(Constants.FOLDER_ATLASES + Constants.MINIMAP_ATLAS).findRegion(Constants.MINIMAP_BOUNDS),
                boundsProvider.getRenderBounds().start.x,
                boundsProvider.getRenderBounds().start.y,
                boundsProvider.getRenderBounds().end.x - boundsProvider.getRenderBounds().start.x,
                boundsProvider.getRenderBounds().end.y - boundsProvider.getRenderBounds().start.y
        );
    }

    /**
     * Sets the bounds provider
     *
     * @param boundsProvider bounds provider
     */
    public void setBoundsProvider(BoundsProvider boundsProvider) {
        this.boundsProvider = boundsProvider;
    }
}
