package com.gasis.rts.ui.screen.component;

/**
 * Abstract component behavior
 */
public class ComponentBehaviorAdapter implements ComponentBehavior {

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
    @Override
    public boolean behaveTouchDown(AbstractComponent component, float x, float y, int pointer, int button) {
        return false;
    }

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
    @Override
    public boolean behaveTouchUp(AbstractComponent component, float x, float y, int pointer, int button) {
        return false;
    }

    /**
     * Performs touch dragged behavior
     *
     * @param component
     * @param x
     * @param y
     * @param pointer
     * @return
     */
    @Override
    public boolean behaveTouchDragged(AbstractComponent component, float x, float y, int pointer) {
        return false;
    }

    /**
     * Performs mouse moved behavior
     *
     * @param component
     * @param x
     * @param y
     * @return
     */
    @Override
    public boolean behaveMouseMoved(AbstractComponent component, float x, float y) {
        return false;
    }
}
