package com.gasis.rts.logic.object;

import com.badlogic.gdx.files.FileHandle;
import com.gasis.rts.filehandling.FileLineReader;
import com.gasis.rts.logic.map.blockmap.BlockMap;

/**
 * Loads a game object from a file
 */
public abstract class GameObjectLoader {

    // has the object been loaded yet
    protected boolean loaded = false;

    // code of the object
    protected String code;

    // name of the texture atlas that holds the object's textures
    protected String atlas;

    // dimensions of the object
    protected float width;
    protected float height;

    // the width of the object's hp bar
    protected float hpBarWidth = 1f;

    // the offset of the hp bar upwards from the object's top
    protected float hpBarYOffset = 0;

    // the horizontal offset of the hp bar
    protected float hpBarXOffset = 0;

    // the object's control context's name
    protected String controlContextName;

    // the game's map
    protected BlockMap map;

    // the name of the object's destruction animation
    protected String destructionAnimationName;

    // the scale of the object's destruction animation
    protected float destructionAnimationScale = 1f;

    // the texture of the object's left over junk
    protected String junkTexture;

    // the scale of the object's left over junk
    protected float junkScale = 1f;

    // the name of the texture atlas that holds junk texture
    protected String junkAtlas;

    // object's healing speed (hp per second)
    protected float healingSpeed = 0.25f;

    // is the object passable or not
    protected boolean passable;

    /**
     * Default class constructor
     *
     * @param map
     */
    public GameObjectLoader(BlockMap map) {
        this.map = map;
    }

    /**
     * Loads a game object from the given file
     *
     * @param file file to load from
     * @return true if the object was loaded successfully
     */
    public final boolean load(FileHandle file) {
        try {
            FileLineReader reader = new FileLineReader(file.read(), ":");

            readMetaData(reader);
            readOtherData(reader);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return (loaded = true);
    }

    /**
     * Reads meta data of a unit
     *
     * @param reader reader to read data from
     */
    protected void readMetaData(FileLineReader reader) {
        code = reader.readLine("code");
        atlas = reader.readLine("atlas");

        width = Float.parseFloat(reader.readLine("width"));
        height = Float.parseFloat(reader.readLine("height"));

        controlContextName = reader.readLine("control context");

        try {
            passable = Boolean.parseBoolean(reader.readLine("passable"));
        } catch (Exception ex) {
        }

        try {
            destructionAnimationName = reader.readLine("destruction animation");
            destructionAnimationScale = Float.parseFloat(reader.readLine("destruction animation scale"));
        } catch (Exception ex) {
        }

        try {
            junkAtlas = reader.readLine("junk texture atlas");
            junkTexture = reader.readLine("junk texture");
            junkScale = Float.parseFloat(reader.readLine("junk scale"));
        } catch (Exception ex) {
        }

        try {
            hpBarWidth = Float.parseFloat(reader.readLine("hp bar width"));
        } catch (Exception ex) {
        }

        try {
            hpBarYOffset = Float.parseFloat(reader.readLine("hp bar height offset"));
        } catch (Exception ex) {
        }

        try {
            hpBarXOffset = Float.parseFloat(reader.readLine("hp bar horizontal offset"));
        } catch (Exception ex) {
        }

        try {
            healingSpeed = Float.parseFloat(reader.readLine("healing speed"));
        } catch (Exception ex) {
        }
    }

    /**
     * Reads other data of the object that is not meta data
     *
     * @param reader reader to read data from
     */
    protected abstract void readOtherData(FileLineReader reader);

    /**
     * Creates a new instance of the loaded object
     *
     * @return new instance of the loaded object
     */
    public abstract GameObject newInstance();

    /**
     * Gets the code of the loaded object
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets the width of the object
     *
     * @return
     */
    public float getWidth() {
        return width;
    }

    /**
     * Gets the height of the object
     *
     * @return
     */
    public float getHeight() {
        return height;
    }

    /**
     * Gets the name of the building's control context
     *
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
}
