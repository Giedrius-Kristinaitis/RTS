package com.gasis.rts.ui.component;

import com.gasis.rts.ui.event.ClickListener;

/**
 * Clickable component
 */
public interface ClickableComponentInterface {

    /**
     * Adds a click listener to the component
     *
     * @param listener listener to add
     */
    void addClickListener(ClickListener listener);

    /**
     * Sets the component's active background texture
     *
     * @param activeBackgroundTexture active background texture
     */
    void setActiveBackgroundTexture(String activeBackgroundTexture);

    /**
     * Gets the component's active background texture
     *
     * @return
     */
    String getActiveBackgroundTexture();

    /**
     * Sets the component's clicked flag
     *
     * @param clicked is the component clicked
     */
    void setClicked(boolean clicked);

    /**
     * Checks if the component is clicked
     *
     * @return
     */
    boolean isClicked();
}
