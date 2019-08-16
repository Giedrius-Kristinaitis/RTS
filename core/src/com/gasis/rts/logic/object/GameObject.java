package com.gasis.rts.logic.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.Updatable;
import com.gasis.rts.logic.map.blockmap.BlockMap;
import com.gasis.rts.logic.object.combat.DefensiveSpecs;
import com.gasis.rts.logic.object.combat.DestructionListener;
import com.gasis.rts.logic.object.research.TechListener;
import com.gasis.rts.logic.player.Player;
import com.gasis.rts.logic.tech.Tech;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents all game objects: units, buildings
 */
public abstract class GameObject implements Updatable, Renderable, Damageable, TechListener {

    // the object type identifier (e.g. All tanks in a group have code T-21)
    protected String code;

    // identifier of one specific object
    protected Long id;

    // the id of the player who owns this object
    protected Player owner;

    // the name of the texture atlas to which the object's texture(s) belongs
    protected String atlas;

    // position of the object
    protected float x;
    protected float y;

    // dimensions of the object
    protected float width;
    protected float height;

    protected float hp; // current number of hit-points the object has

    // is the object passable or not
    protected boolean passable;

    // object's combat specs
    protected DefensiveSpecs defensiveSpecs;

    // should the hp bar be rendered
    protected boolean renderHp;

    // the width of the hp bar (in game units)
    protected float hpBarWidth = 1f;

    // the offset of the hp bar upwards from the object's top
    protected float hpBarYOffset = 0f;

    // the horizontal offset of the hp bar
    protected float hpBarXOffset = 0f;

    // the name of the object's control context
    protected String controlContextName;

    // the game's map
    protected BlockMap map;

    // all attached destruction listeners
    protected Set<DestructionListener> destructionListeners = new HashSet<DestructionListener>();

    // is the object destroyed or not
    protected boolean destroyed = false;

    // the name of the object's destruction animation
    protected String destructionAnimationName;

    // the scale of the object's destruction animation
    protected float destructionAnimationScale = 1f;

    // the texture of the object's left over junk
    protected String junkTexture;

    // the scale of the object's left over junk
    protected float junkScale;

    // the name of the texture atlas that holds junk texture
    protected String junkAtlas;

    // how much the object heals itself (hp per second)
    protected float healingSpeed;

    /**
     * Default class constructor
     * @param map
     */
    public GameObject(BlockMap map) {
        this.map = map;
    }

    /**
     * Sets the object's healing speed
     *
     * @param healingSpeed healing speed
     */
    public void setHealingSpeed(float healingSpeed) {
        this.healingSpeed = healingSpeed;
    }

    /**
     * Adds a destruction listener to the object
     *
     * @param listener listener to add
     */
    public void addDestructionListener(DestructionListener listener) {
        destructionListeners.add(listener);
    }

    /**
     * Removes a destruction listener from the object
     *
     * @param listener listener to remove
     */
    public void removeDestructionListener(DestructionListener listener) {
        destructionListeners.remove(listener);
    }

    /**
     * Sets the hp bar's height offset
     *
     * @param hpBarYOffset new offset
     */
    public void setHpBarYOffset(float hpBarYOffset) {
        this.hpBarYOffset = hpBarYOffset;
    }

    /**
     * Sets the horizontal offset of the hp bar
     *
     * @param hpBarXOffset x offset
     */
    public void setHpBarXOffset(float hpBarXOffset) {
        this.hpBarXOffset = hpBarXOffset;
    }

    /**
     * Sets the width of the hp bar
     *
     * @param hpBarWidth new hp bar width
     */
    public void setHpBarWidth(float hpBarWidth) {
        this.hpBarWidth = hpBarWidth;
    }

    /**
     * Sets combat specs of the object
     *
     * @param defensiveSpecs new combat specs
     */
    public void setDefensiveSpecs(DefensiveSpecs defensiveSpecs) {
        this.defensiveSpecs = defensiveSpecs;
    }

    /**
     * Gets the combat specs of the object
     * @return
     */
    public DefensiveSpecs getDefensiveSpecs() {
        return defensiveSpecs;
    }

    /**
     * Does damage to the object
     *
     * @param attack attack stat of the attacker,
     *               damage will be calculated based on the object's defence
     */
    @Override
    public void doDamage(float attack) {
        hp = Math.max(0, hp - attack / (defensiveSpecs.getDefence() + 1));

        if (hp <= 0) {
            destroyed = true;
            notifyDestructionListeners();
        }
    }

    /**
     * Notifies all destruction listeners that the object has been destroyed
     */
    protected void notifyDestructionListeners() {
        for (DestructionListener listener: destructionListeners) {
            listener.objectDestroyed(this);
        }
    }

    /**
     * Gets the code of the object
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets the id of the object
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the code of the object
     *
     * @param code new code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Sets the id of the object
     *
     * @param id new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Checks if the object is passable
     * @return
     */
    public boolean isPassable() {
        return passable;
    }

    /**
     * Sets the passable value for this object
     *
     * @param passable is the object passable or not
     */
    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    /**
     * Sets the hp of the object
     *
     * @param hp new hp
     */
    public void setHp(float hp) {
        this.hp = hp;
    }

    /**
     * Gets the current hp of the object
     * @return
     */
    public float getHp() {
        return hp;
    }

    /**
     * Gets the name of the of the texture atlas
     * @return
     */
    public String getAtlas() {
        return atlas;
    }

    /**
     * Gets the x coordinate of the object
     * @return
     */
    public float getX() {
        return x;
    }

    /**
     * Gets the y coordinate of the object
     * @return
     */
    public float getY() {
        return y;
    }

    /**
     * Gets the width of the building
     * @return
     */
    public float getWidth() {
        return width;
    }

    /**
     * Gets the height of the building
     * @return
     */
    public float getHeight() {
        return height;
    }

    /**
     * Gets the x coordinate of the object's center point
     *
     * @return
     */
    public float getCenterX() {
        return x + width / 2f;
    }

    /**
     * Gets the y coordinate of the object's center point
     *
     * @return
     */
    public float getCenterY() {
        return y + height / 2f;
    }

    /**
     * Sets the x coordinate of the object
     *
     * @param x new x coordinate
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Sets the y coordinate of the object
     *
     * @param y new y coordinate
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Sets the width of the building
     *
     * @param width new width
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * Sets the height of the building
     *
     * @param height new height
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Sets the x coordinate of the object's center point
     *
     * @param x new center x
     */
    public void setCenterX(float x) {
        this.x = x - width / 2f;
    }

    /**
     * Sets the y coordinate of the object's center point
     *
     * @param y new center y
     */
    public void setCenterY(float y) {
        this.y = y - height / 2f;
    }

    /**
     * Sets the name of the texture atlas used by this object
     *
     * @param atlas new name of the atlas
     */
    public void setAtlas(String atlas) {
        this.atlas = atlas;
    }

    /**
     * Updates the game object
     *
     * @param delta time elapsed since the last render
     */
    @Override
    public abstract void update(float delta);

    /**
     * Renders the object to the screen
     *
     * @param batch sprite batch to draw to
     * @param resources game assets
     */
    @Override
    public abstract void render(SpriteBatch batch, Resources resources);

    /**
     * Checks if the object can be safely removed from object list
     * @return
     */
    public abstract boolean canBeRemoved();

    /**
     * De-occupies the objects occupied block
     */
    public abstract void deoccupyBlocks();

    /**
     * Gets the x coordinate of it's occupied block (used for targeting the object)
     * @return
     */
    public abstract float getOccupiedBlockX();

    /**
     * Gets the y coordinate of it's occupied block (used for targeting the object)
     * @return
     */
    public abstract float getOccupiedBlockY();

    /**
     * Renders the object's hp bar
     *
     * @param batch sprite batch to draw to
     * @param resources game's assets
     */
    protected void renderHp(SpriteBatch batch, Resources resources) {
        if (renderHp) {
            batch.draw(resources.atlas(Constants.GENERAL_TEXTURE_ATLAS).findRegion(Constants.HP_BAR_BACKGROUND_TEXTURE),
                    hpBarXOffset + getCenterX() - hpBarWidth / 2f, y + height + hpBarYOffset, hpBarWidth, 0.1f);

            if (hp / defensiveSpecs.getMaxHp() >= 0.66f) {
                batch.draw(resources.atlas(Constants.GENERAL_TEXTURE_ATLAS).findRegion(Constants.HP_BAR_GREEN_TEXTURE),
                        hpBarXOffset + getCenterX() - hpBarWidth / 2f + 0.025f, y + height + 0.025f + hpBarYOffset, hpBarWidth * hp / defensiveSpecs.getMaxHp() - 0.05f, 0.05f);
            } else if (hp / defensiveSpecs.getMaxHp() >= 0.33f) {
                batch.draw(resources.atlas(Constants.GENERAL_TEXTURE_ATLAS).findRegion(Constants.HP_BAR_YELLOW_TEXTURE),
                        hpBarXOffset + getCenterX() - hpBarWidth / 2f + 0.025f, y + height + 0.025f + hpBarYOffset, hpBarWidth * hp / defensiveSpecs.getMaxHp() - 0.05f, 0.05f);
            } else {
                batch.draw(resources.atlas(Constants.GENERAL_TEXTURE_ATLAS).findRegion(Constants.HP_BAR_RED_TEXTURE),
                        hpBarXOffset + getCenterX() - hpBarWidth / 2f + 0.025f, y + height + 0.025f + hpBarYOffset, hpBarWidth * hp / defensiveSpecs.getMaxHp() - 0.05f, 0.05f);
            }
        }
    }

    /**
     * Updates the object's healing
     *
     * @param delta time since the last update
     */
    protected void updateHealing(float delta) {
        if (hp < defensiveSpecs.getMaxHp()) {
            hp += healingSpeed * delta;

            if (hp > defensiveSpecs.getMaxHp()) {
                hp = defensiveSpecs.getMaxHp();
            }
        }
    }

    /**
     * Toggles hp bar's rendering
     *
     * @param renderHp should the hp bar be rendered or not
     */
    public void setRenderHp(boolean renderHp) {
        this.renderHp = renderHp;
    }

    /**
     * Gets the name of the building's control context
     * @return
     */
    public String getControlContextName() {
        return controlContextName;
    }

    /**
     * Sets the name of the building's control context
     *
     * @param controlContextName new name
     */
    public void setControlContextName(String controlContextName) {
        this.controlContextName = controlContextName;
    }

    /**
     * Sets the owner of the object
     *
     * @param owner new owner
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * Gets the owner of the object
     * @return
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Sets the name of the name of the destruction animation
     *
     * @param destructionAnimationName new animation name
     */
    public void setDestructionAnimationName(String destructionAnimationName) {
        this.destructionAnimationName = destructionAnimationName;
    }

    /**
     * Gets the name of the destruction animation
     * @return
     */
    public String getDestructionAnimationName() {
        return destructionAnimationName;
    }

    /**
     * Sets the scale of the destruction animation
     *
     * @param destructionAnimationScale new scale
     */
    public void setDestructionAnimationScale(float destructionAnimationScale) {
        this.destructionAnimationScale = destructionAnimationScale;
    }

    /**
     * Gets the scale of the destruction animation
     * @return
     */
    public float getDestructionAnimationScale() {
        return destructionAnimationScale;
    }

    /**
     * Checks if the object is destroyed
     * @return
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Gets the junk texture
     * @return
     */
    public String getJunkTexture() {
        return junkTexture;
    }

    /**
     * Sets the junk texture
     *
     * @param junkTexture new junk texture
     */
    public void setJunkTexture(String junkTexture) {
        this.junkTexture = junkTexture;
    }

    /**
     * Gets the junk scale
     * @return
     */
    public float getJunkScale() {
        return junkScale;
    }

    /**
     * Sets the junk scale
     *
     * @param junkScale new junk scale
     */
    public void setJunkScale(float junkScale) {
        this.junkScale = junkScale;
    }

    /**
     * Gets the name of the junk texture atlas
     * @return
     */
    public String getJunkAtlas() {
        return junkAtlas;
    }

    /**
     * Sets the name of the junk atlas
     *
     * @param junkAtlas new atlas name
     */
    public void setJunkAtlas(String junkAtlas) {
        this.junkAtlas = junkAtlas;
    }

    /**
     * Called when a tech gets researched
     *
     * @param player the player the tech was applied to
     * @param tech   the researched tech
     */
    @Override
    public void techResearched(Player player, String tech) {
        // default implementation does nothing
    }
}
