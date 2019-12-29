package com.gasis.rts.logic.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.resources.Resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Queues up renderable objects and renders them in the correct order
 */
public class RenderQueue implements RenderQueueInterface {

    // objects to render
    protected Set<Entry> renderables = new TreeSet<Entry>();

    // objects that will be rendered after all other objects ignoring their z-index
    protected Set<Entry> topLayerRenderables = new TreeSet<Entry>();

    // instances of entries (to avoid creating new instances every frame)
    protected List<Entry> entryInstances = new ArrayList<Entry>();

    // reused entry index (used to make entry instance list bigger if needed)
    protected int entryIndex;

    /**
     * Adds a renderable into the render queue
     *
     * @param renderable renderable to render
     * @param x          object's x
     * @param y          object's y
     */
    @Override
    public void addRenderable(Renderable renderable, float x, float y) {
        addRenderableToSet(renderable, x, y, renderables);
    }

    /**
     * Adds a renderable to be rendered above all other renderables ignoring their z-index
     *
     * @param renderable renderable to render
     * @param x          object's x
     * @param y          object's y
     */
    @Override
    public void addTopLayerRenderable(Renderable renderable, float x, float y) {
        addRenderableToSet(renderable, x, y, topLayerRenderables);
    }

    /**
     * Adds a renderable to the given set
     *
     * @param renderable  renderable to add
     * @param x           object's x
     * @param y           object's y
     * @param renderables set to add to
     */
    protected void addRenderableToSet(Renderable renderable, float x, float y, Set<Entry> renderables) {
        if (entryIndex == entryInstances.size()) {
            entryInstances.add(new Entry());
        }

        Entry entry = entryInstances.get(entryIndex);

        entry.zIndex = calculateZIndex(x, y);
        entry.renderable = renderable;

        renderables.add(entry);

        entryIndex++;
    }

    /**
     * Calculates z index based on x and y coordinates
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return
     */
    protected Float calculateZIndex(float x, float y) {
        return 1000000f - y;
    }

    /**
     * Renders the object to the screen
     *
     * @param batch     sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public void render(SpriteBatch batch, Resources resources, RenderQueueInterface renderQueue) {
        renderSet(renderables, batch, resources, renderQueue);
        renderSet(topLayerRenderables, batch, resources, renderQueue);
    }

    /**
     * Renders all renderables from the given set
     *
     * @param set       set with renderables
     * @param batch     sprite batch to draw to
     * @param resources game's assets
     */
    protected void renderSet(Set<Entry> set, SpriteBatch batch, Resources resources, RenderQueueInterface renderQueue) {
        for (Entry entry : set) {
            entry.renderable.render(batch, resources, renderQueue);
        }
    }

    /**
     * Clears the render queue
     */
    @Override
    public void clearQueue() {
        renderables.clear();
        topLayerRenderables.clear();
        entryIndex = 0;
    }

    /**
     * Renderable entry in the renderable data structure
     */
    protected class Entry implements Comparable<Entry> {

        protected Renderable renderable;
        protected float zIndex;

        protected Entry() {}

        protected Entry(float zIndex, Renderable renderable) {
            this.zIndex = zIndex;
            this.renderable = renderable;
        }

        @Override
        public int compareTo(Entry entry) {
            if (zIndex < entry.zIndex) {
                return -1;
            } else if (zIndex > entry.zIndex) {
                return 1;
            }

            return -1; // not returning 0 because duplicate key support is required
        }
    }
}
