package com.gasis.rts.ui.screen.abstractions;

/**
 * Switches screens
 */
public interface ScreenSwitcher {

    /**
     * Sets the current screen to the specified one
     * @param screen screen to be shown
     */
    void showScreen(BasicScreen screen);
}
