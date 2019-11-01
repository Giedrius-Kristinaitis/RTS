package com.gasis.rts.logic.render;

/**
 * Queues up renderable objects and renders them in the correct order
 */
public interface RenderQueueInterface extends Renderable {

    /**
     * Adds a renderable into the render queue
     *
     * @param renderable renderable to render
     * @param x object's x
     * @param y object's y
     */
    void addRenderable(Renderable renderable, float x, float y);

    /**
     * Clears the render queue
     */
    void clearQueue();
}
