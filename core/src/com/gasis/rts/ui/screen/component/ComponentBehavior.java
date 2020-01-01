package com.gasis.rts.ui.screen.component;

/**
 * Abstract UI component behavior
 */
public interface ComponentBehavior {

    /**
     * Performs touch down behavior
     *
     * @param component
     * @param x
     * @param y
     * @param pointer
     * @param button
     * @return
     */
    boolean behaveTouchDown(AbstractComponent component, float x, float y, int pointer, int button);

    /**
     * Performs touch up behavior
     *
     * @param component
     * @param x
     * @param y
     * @param pointer
     * @param button
     * @return
     */
    boolean behaveTouchUp(AbstractComponent component, float x, float y, int pointer, int button);

    /**
     * Performs touch dragged behavior
     *
     * @param component
     * @param x
     * @param y
     * @param pointer
     * @return
     */
    boolean behaveTouchDragged(AbstractComponent component, float x, float y, int pointer);

    /**
     * Performs mouse moved behavior
     *
     * @param component
     * @param x
     * @param y
     * @return
     */
    boolean behaveMouseMoved(AbstractComponent component, float x, float y);
}
