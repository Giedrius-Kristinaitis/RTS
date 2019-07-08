package com.gasis.rts.logic.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gasis.rts.logic.Renderable;
import com.gasis.rts.logic.Updatable;
import com.gasis.rts.logic.object.combat.DefensiveSpecs;
import com.gasis.rts.resources.Resources;
import com.gasis.rts.utils.Constants;

/**
 * Represents all game objects: units, buildings
 */
public abstract class GameObject implements Updatable, Renderable, Damageable {

    // the object type identifier (e.g. All tanks in a group have code T-21)
    protected String code;

    // identifier of one specific object
    protected Long id;

    // the id of the player who owns this object
    protected Long ownerId;

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
        hp -= attack / defensiveSpecs.getDefence();
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
     * Gets the id of the object's owner
     * @return
     */
    public Long getOwnerId() {
        return ownerId;
    }

    /**
     * Sets the id of the object's owner
     *
     * @param ownerId new owner's id
     */
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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
     * Renders the object's hp bar
     *
     * @param batch sprite batch to draw to
     * @param resources game's assets
     */
    protected void renderHp(SpriteBatch batch, Resources resources) {
        if (renderHp) {
            batch.draw(resources.atlas(Constants.GENERAL_TEXTURE_ATLAS).findRegion(Constants.HP_BAR_BACKGROUND_TEXTURE),
                    x, y + height, width, height * 0.1f);

            if (hp / defensiveSpecs.getMaxHp() >= 0.66f) {
                batch.draw(resources.atlas(Constants.GENERAL_TEXTURE_ATLAS).findRegion(Constants.HP_BAR_GREEN_TEXTURE),
                        x + width * 0.025f, y + height * 1.025f, width * hp / defensiveSpecs.getMaxHp() - width * 0.05f, height * 0.05f);
            } else if (hp / defensiveSpecs.getMaxHp() >= 0.33f) {
                batch.draw(resources.atlas(Constants.GENERAL_TEXTURE_ATLAS).findRegion(Constants.HP_BAR_YELLOW_TEXTURE),
                        x + width * 0.025f, y + height * 1.025f, width * hp / defensiveSpecs.getMaxHp() - width * 0.05f, height * 0.05f);
            } else {
                batch.draw(resources.atlas(Constants.GENERAL_TEXTURE_ATLAS).findRegion(Constants.HP_BAR_RED_TEXTURE),
                        x + width * 0.025f, y + height * 1.025f, width * hp / defensiveSpecs.getMaxHp() - width * 0.05f, height * 0.05f);
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
}
