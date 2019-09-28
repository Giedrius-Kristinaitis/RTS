package com.gasis.rts.ui.component;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.ui.event.ClickListener;
import com.gasis.rts.utils.Constants;

import java.util.HashSet;
import java.util.Set;

/**
 * Clickable component
 */
public class ClickableComponent extends Component implements ClickableComponentInterface {

    // looks of the component
    protected String activeBackgroundTexture;

    // click listeners
    protected Set<ClickListener> clickListeners = new HashSet<ClickListener>();

    // is the component currently clicked on
    protected boolean clicked;

    /**
     * Adds a click listener to the component
     *
     * @param listener listener to add
     */
    @Override
    public void addClickListener(ClickListener listener) {
        clickListeners.add(listener);
    }

    /**
     * Sets the component's active background texture
     *
     * @param activeBackgroundTexture active background texture
     */
    @Override
    public void setActiveBackgroundTexture(String activeBackgroundTexture) {
        this.activeBackgroundTexture = activeBackgroundTexture;
    }

    /**
     * Gets the component's active background texture
     *
     * @return
     */
    @Override
    public String getActiveBackgroundTexture() {
        return activeBackgroundTexture;
    }

    /**
     * Sets the component's clicked flag
     *
     * @param clicked is the component clicked
     */
    @Override
    public void setClicked(boolean clicked) {
        this.clicked = clicked;

        if (clicked) {
            notifyClickListeners();
        }
    }

    /**
     * Checks if the component is clicked
     *
     * @return
     */
    @Override
    public boolean isClicked() {
        return clicked;
    }

    /**
     * Notifies all listeners that the component has been clicked
     */
    protected void notifyClickListeners() {
        for (ClickListener listener: clickListeners) {
            listener.clicked(this);
        }
    }

    /**
     * Renders the object to the screen
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources) {
        if (clicked && activeBackgroundTexture != null) {
            batch.draw(
                    resources.atlas(Constants.FOLDER_ATLASES + textureAtlas).findRegion(activeBackgroundTexture),
                    x,
                    y,
                    width,
                    height
            );

            return;
        }

        super.render(batch, resources);
    }
}
